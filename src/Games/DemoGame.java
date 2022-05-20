package Games;

import Audio.Sound;
import Game.Color;
import Game.ColorChar;
import Game.Frame;
import Game.GameInterface;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class DemoGame implements GameInterface {
    private int width=10;
    private int height=10;
    private int[][] playfield;
    private Point pos;
    private boolean finished=false;
    private boolean onStartframe =true;
    @Override
    public void startGame() {
        playfield = new int[width][height];
        pos = new Point();
        pos.x = 5;
        pos.y = 5;
    }

    @Override
    public Frame nextFrame() {
        ColorChar[][] chars = new ColorChar[width][height];
        chars[pos.x][pos.y] = new ColorChar('O', Color.BLUE);
        Sound sound =getSound();
        Frame frame = new Frame(chars,sound,this);
        return frame;
    }

    private Sound getSound() {
        if(onStartframe) {
            onStartframe =false;
            return Sound.BEAR;
        }
        else return null;
    }

    @Override
    public int getScale() {
        return 2;
    }

    @Override
    public String getInstructions() {

        StringBuilder sb = new StringBuilder();
        sb.append("Use the Arrow keys to move the dot\n");
        sb.append("The game ends if you touch the border\n");
        return sb.toString();
    }

    @Override
    public String getName() {
        return "Demo";
    }

    @Override
    public int milisBetweenFrames() {
        return 100;
    }

    @Override
    public void inputPressed(int keyCode) {
        switch (keyCode){
            case NativeKeyEvent.VC_LEFT:
                move(0,-1);
                break;
            case NativeKeyEvent.VC_RIGHT:
                move(0,1);
                break;
            case NativeKeyEvent.VC_UP:
                move(-1,0);
                break;
            case NativeKeyEvent.VC_DOWN:
                move(1,0);
                break;
            default:
                break;
        }
    }
    private void move(int up, int right){
        pos.x+=right;
        if(pos.x<0||pos.x>=height) {
            pos.x -=right;
            finished =true;
        }

        pos.y+= up;
        if( pos.y<0||pos.y>=width){
            pos.y-= up;
            finished =true;
        }

    }

    @Override
    public boolean hasEnded() {
        return finished;
    }

    private static class Point{
        int x;
        int y;
    }
}
