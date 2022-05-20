package Game;

public class ColorChar {
    public final char aChar;
    public final Color color;

    public ColorChar(char c, Color color) {
        this.aChar = c;
        this.color = color;
    }
    public ColorChar(char c) {
        this.aChar = c;
        color = null;
    }
}
