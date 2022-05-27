package Games;

import Audio.Sound;
import Game.Color;
import Game.ColorChar;
import Game.Frame;
import Game.GameInterface;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class Pong implements GameInterface {
    boolean hasEnded = false;
    int gameCodes;
    int height = 15;
    int width = 50;
    final int pongLength = 4;
    int pongBPos;
    int pongAPos;
    ColorChar[][] field;
    Ball ball;
    int scoreA;
    int scoreB;
    int soundCode;




    public int milisBetweenFrames() {
        return 50;
    }

    @Override
    public void inputPressed(int keyCode) {
        switch (keyCode){
            case NativeKeyEvent.VC_W:
                if(pongBPos > 0 ) pongBPos--;
                return;
            case NativeKeyEvent.VC_S:
                if(pongBPos < height - pongLength) pongBPos++;
                return;
            case NativeKeyEvent.VC_DOWN:
                if(pongAPos < height - pongLength) pongAPos++;
                return;
            case NativeKeyEvent.VC_UP:
                if(pongAPos > 0 ) pongAPos--;
                return;
            case NativeKeyEvent.VC_E:
                ball.setDiff('e');
                startGame();
                return;
            case NativeKeyEvent.VC_M:
                ball.setDiff('M');
                startGame();
                return;
            case NativeKeyEvent.VC_D:
                ball.setDiff('d');
                startGame();
                return;
            case NativeKeyEvent.VC_Q:
                hasEnded = true;
            default:
                return;
        }

    }



    @Override
    public void startGame() {
        pongBPos = height/2;
        pongAPos = height/2;
        field = new ColorChar[width][height];
        hasEnded = false;
        scoreA = 0;
        scoreB = 0;
        soundCode = 0;
        ball = new Ball();
    }

    public void resumeGame(){
        pongBPos = height/2;
        pongAPos = height/2;
        ball = new Ball();
    }

    public void moveBall(){
        if(ball.nextY() < 0 || ball.nextY() > height - 0.5){
            soundCode = 1;
            ball.hitBorder();
        }
        if(ball.nextX() < 6 || ball.nextX() > width - 5){
            if(hitPlayer()){
                soundCode = 0;
                ball.hitPlayer();
            } else {
                if(ball.nextX() < 5){
                    scoreA++;
                    soundCode = 2;
                    gameCodes = 2;
                } else {
                    soundCode = 2;
                    scoreB++;
                    gameCodes = 2;

                }
                resumeGame();
                return;
            }
        }
        ball.move();
    }

    public boolean hitPlayer(){
        return (ball.nextY() > pongBPos && ball.nextY() < pongBPos + pongLength) || (ball.nextY() > pongAPos && ball.nextY() < pongLength + pongAPos);
    }

    @Override
    public Frame nextFrame() {
        if(gameCodes == 2){
            field[20][7] = new ColorChar(Integer.toString(scoreB).charAt(0), null);
            field[21][7] = new ColorChar( '-', null);
            field[22][7] = new ColorChar(Integer.toString(scoreA).charAt(0), null);
            gameCodes--;
            return new Frame(field, null, this);
        }
        if(gameCodes == 1){
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gameCodes--;
            Frame frame = new Frame(field, null, this);
            field = new ColorChar[width][height];
            return frame;
        }
        moveBall();
        for(int i = 0; i < pongLength; i++){
            field[width - 5][pongAPos + i] = new ColorChar('|', null);
            field[5][pongBPos + i] = new ColorChar('|', null);

        }
        field[(int) ball.px][(int) ball.py] = new ColorChar('*', null);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Frame frame = new Frame(field, getSound(), this);

        soundCode = -1;
        field = new ColorChar[width][height];
        return frame;
    }

    public Sound getSound(){
        switch (soundCode) {
            case 1:
                return Sound.PONGWALL;
            case 2:
                return Sound.PONGSCORE;
            case 0:
                return Sound.PONGPADDLE;
            default:
                return null;
        }
    }

    @Override
    public String getInstructions() {
        return "A: Up and Down, B: W and S\nDiff: Easy: e, Med: m, Hard:h";
    }

    @Override
    public String getName() {
        return "Pong";
    }



    @Override
    public int millisBetweenFrames() {
        return 0;
    }


    @Override
    public boolean hasEnded() {
        return hasEnded;
    }



    private class Ball{
        double px;
        double py;
        double vx;
        double vy;

        double diff = 0.6;

        double max = 1;
        double min = 0.5;

        public Ball(){
            px = width/2;
            py = height/2;

            vy = (Math.random()*(max-min+1)+min)*diff;
            vx = diff;

        }

        public void hitPlayer(){
            vy = (Math.random()*(max-min+1)+min)*diff;
            if(Math.random() < 0.5) vy = -vy;
            vx = -vx;
        }

        public void hitBorder(){
            vy = -vy;
        }

        public void move(){
            px += vx;
            py += vy;
        }

        public double nextX(){
            return px + vx;
        }

        public double nextY(){
            return py + vy;
        }

        public void setDiff(char c){
            if(c == 'e'){
                diff = 0.5;
            } else if(c == 'm'){
                diff = 1;
            } else {
                diff = 1.5;
            }
        }
    }

    // 1,5h plan how to implement
    // 4h implementation
    // searching for audio files
    // adding features
}
