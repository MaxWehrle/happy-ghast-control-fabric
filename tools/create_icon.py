"""Generate the original pixel-style project icon from simple shapes."""

from pathlib import Path

from PIL import Image, ImageDraw


def main() -> None:
    canvas = Image.new("RGBA", (512, 512), "#1c3451")
    draw = ImageDraw.Draw(canvas)

    # Soft sky bands and an abstract white flying creature.
    draw.rectangle((0, 305, 511, 511), fill="#244868")
    draw.rectangle((0, 407, 511, 511), fill="#2e5d75")
    draw.rectangle((70, 98, 343, 329), fill="#d8e6ed")
    draw.rectangle((86, 82, 327, 313), fill="#ffffff")
    draw.rectangle((102, 300, 142, 364), fill="#e8f4f6")
    draw.rectangle((165, 300, 205, 347), fill="#e8f4f6")
    draw.rectangle((244, 300, 284, 364), fill="#e8f4f6")
    draw.rectangle((300, 300, 327, 343), fill="#e8f4f6")
    draw.rectangle((129, 177, 166, 214), fill="#203847")
    draw.rectangle((252, 177, 289, 214), fill="#203847")
    draw.rectangle((187, 245, 229, 258), fill="#203847")
    draw.rectangle((177, 232, 189, 246), fill="#203847")
    draw.rectangle((229, 232, 241, 246), fill="#203847")

    # Down arrow, visually separate from the Ghast silhouette.
    draw.rectangle((389, 125, 439, 302), fill="#43dfc2")
    draw.polygon([(353, 281), (475, 281), (414, 358)], fill="#43dfc2")
    draw.rectangle((394, 130, 405, 274), fill="#a5fff1")

    destination = Path(__file__).resolve().parents[1] / (
        "src/main/resources/assets/happyghastcontrol/icon.png"
    )
    destination.parent.mkdir(parents=True, exist_ok=True)
    canvas.save(destination)


if __name__ == "__main__":
    main()
