package Game;

import Audio.Sound;

public class Frame {
    public final ColorChar[][] colorChars;
    public final Sound sound;
    public final GameInterface parent;

    public Frame(ColorChar[][] colorChars, Sound sound, GameInterface parent) {
        this.colorChars = colorChars;
        this.sound = sound;
        this.parent = parent;
    }
    @Override
    public String toString(){
        ColorChar[][] scaledFrame = scaleFrame();
        ColorChar[][] borderedFrame  = addBorder(scaledFrame);
        return frameToSting(borderedFrame);
    }
    private ColorChar[][] scaleFrame() {
        int scale = parent.getScale();
        int height = colorChars[0].length*scale;
        int width = colorChars.length*scale;
        ColorChar[][] scaled = new ColorChar[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                scaled[x][y] = colorChars[x/scale][y/scale];
            }
        }
        return  scaled;
    }

    private ColorChar[][] addBorder(ColorChar[][] frame) {
        int height = frame[0].length+2;
        int width = frame.length+2;
        ColorChar[][] border = new ColorChar[width][height];
        drawBorder(border);
        addGameName(border);
        copyFrameIntoBorder(frame,border);
        return border;
    }

    private void drawBorder(ColorChar[][] border) {
        int height = border[0].length;
        int width = border.length;
        ColorChar left_up = new ColorChar('╔');
        ColorChar left_down = new ColorChar('╚');
        ColorChar right_up = new ColorChar('╗');
        ColorChar right_down = new ColorChar('╝');
        ColorChar vertical = new ColorChar('═');
        ColorChar horizontal = new ColorChar('║');
        for (int y = 0; y < height; y++) {
            border[0][y]=horizontal;
            border[width-1][y]=horizontal;
        }
        for (int x = 0; x < width; x++) {
            border[x][0] = vertical;
            border[x][height-1] = vertical;
        }
        border[0][0]= left_up;
        border[width-1][0]= right_up;
        border[0][height-1]= left_down;
        border[width-1][height-1]= right_down;
    }

    private void addGameName(ColorChar[][] border) {
        int score = parent.getScore();
        char[] gameName;
        if(score>0)
            gameName = (parent.getName()+": " +parent.getScore()).toCharArray();
        else
            gameName = (parent.getName()).toCharArray();
        int startIndex = border.length/2-gameName.length/2;
        for (int i = 0; i < gameName.length; i++) {
            border[i+startIndex][0]= new ColorChar(gameName[i]);
        }
    }

    private void copyFrameIntoBorder(ColorChar[][] frame, ColorChar[][] border) {
        for (int i = 0; i < frame.length; i++) {
            for (int j = 0; j < frame[0].length; j++) {
                border[i+1][j+1]= frame[i][j];
            }
        }
    }

    private String frameToSting(ColorChar[][] borderedFrame) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < borderedFrame[0].length; y++) {
            for (int x = 0; x < borderedFrame.length; x++) {
                sb.append(getChar(borderedFrame[x][y]));
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private char getChar(ColorChar c){
        if(c == null) return ' ';
        return c.aChar;
    }
}
