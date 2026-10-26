"""Check and package the built JAR with a source archive for release."""

import json
import shutil
import zipfile
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
VERSION = next(
    line.split("=", 1)[1].strip()
    for line in (ROOT / "gradle.properties").read_text(encoding="utf-8").splitlines()
    if line.startswith("mod_version=")
)
BASE_NAME = f"happy-ghast-control-fabric-26.3-{VERSION}"
JAR = ROOT / "build" / "libs" / f"{BASE_NAME}.jar"
DIST = ROOT / "dist"


def verify_jar() -> None:
    with zipfile.ZipFile(JAR) as archive:
        assert archive.testzip() is None, "The mod JAR is corrupt"
        names = set(archive.namelist())
        required = {
            "de/maxwehrle/happyghastcontrol/HappyGhastControl.class",
            "de/maxwehrle/happyghastcontrol/client/HappyGhastControlClient.class",
            "de/maxwehrle/happyghastcontrol/mixin/HappyGhastMixin.class",
            "de/maxwehrle/happyghastcontrol/ParkingState.class",
            "de/maxwehrle/happyghastcontrol/ParkingRequestPayload.class",
            "de/maxwehrle/happyghastcontrol/ParkingStatusPayload.class",
            "de/maxwehrle/happyghastcontrol/client/GhastControlScreen.class",
            "assets/happyghastcontrol/icon.png",
            "assets/happyghastcontrol/lang/en_us.json",
            "assets/happyghastcontrol/lang/de_de.json",
            "happyghastcontrol.mixins.json",
            "LICENSE_happy-ghast-control",
        }
        assert required <= names, f"Missing JAR files: {sorted(required - names)}"
        metadata = json.loads(archive.read("fabric.mod.json"))
        assert metadata["version"] == VERSION
        assert metadata["license"] == "MIT"
        assert metadata["depends"]["minecraft"] == "~26.3"
        assert "Copyright (c) 2026 Max Wehrle" in archive.read(
            "LICENSE_happy-ghast-control"
        ).decode("utf-8")


def main() -> None:
    verify_jar()
    DIST.mkdir(exist_ok=True)
    shutil.copy2(JAR, DIST / JAR.name)
    shutil.copy2(
        ROOT / "modrinth/icon.png",
        DIST / "happy-ghast-control-icon.png",
    )

    source_zip = DIST / f"{BASE_NAME}-source.zip"
    source_files = [
        ROOT / ".gitignore",
        ROOT / "LICENSE",
        ROOT / "README.md",
        ROOT / "build.gradle",
        ROOT / "gradle.properties",
        ROOT / "gradlew",
        ROOT / "gradlew.bat",
        ROOT / "settings.gradle",
        *(ROOT / ".github").rglob("*"),
        *(ROOT / "gradle").rglob("*"),
        *(ROOT / "modrinth").rglob("*"),
        *(ROOT / "src").rglob("*"),
        *(ROOT / "tools").rglob("*"),
    ]
    with zipfile.ZipFile(source_zip, "w", zipfile.ZIP_DEFLATED) as archive:
        for path in source_files:
            if path.is_file() and "__pycache__" not in path.parts:
                archive.write(path, path.relative_to(ROOT).as_posix())

    print(f"Verified {JAR.name}")
    print(f"Created {source_zip.name}")


if __name__ == "__main__":
    main()
