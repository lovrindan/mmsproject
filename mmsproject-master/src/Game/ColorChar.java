package Game;


// for future work could be used for
// changing text color
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
