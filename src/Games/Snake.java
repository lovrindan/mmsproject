package Games;
import Audio.Sound;
import Game.Color;
import Game.ColorChar;
import Game.Frame;
import Game.GameInterface;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.util.ArrayList;
import java.util.List;


public class Snake implements GameInterface {
    //playground measurement and score
    private int width = 25;
    private int height = 25;
    private int[][] playField;
    private int score = 0;

    //coordinates for spawning apples
    int xApple;
    int yApple;

    //coordinates for moving snake
    private int dirY = 0;
    private int dirX = 0;
    private Point animal;

    //ArrayList for enlarging the snake, if an Apple got eaten
    List<Point> bodyParts =new ArrayList<>();

    //flags
    private boolean startingSound = true;
    private boolean keyPressed = false;
    private boolean gameOver = false;
    private boolean firstFrame = true;
    private boolean applePlaced = false;
    private boolean appleConsumed = false;

    //Code for moving animal which can be controlled by player
    private static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public void startGame() {
        playField = new int[width][height];
        animal = new Point(5,5);
        bodyParts.add(0,animal);

    }

    @Override
    public void inputPressed(int keyCode) {
            switch (keyCode) {
                case NativeKeyEvent.VC_UP:
                    if(dirY == 1) break;
                    dirX=0;
                    dirY=-1;
                    keyPressed = true;
                    break;
                case NativeKeyEvent.VC_DOWN:
                    if(dirY == -1) break;
                    dirX = 0;
                    dirY = 1;
                    keyPressed = true;
                    break;
                case NativeKeyEvent.VC_LEFT:
                    if(dirX == 1) break;
                    dirX = -1;
                    dirY = 0;
                    keyPressed = true;
                    break;
                case NativeKeyEvent.VC_RIGHT:
                    if(dirX == -1) break;
                    dirX = 1;
                    dirY = 0;
                    keyPressed = true;
                    break;
                default:
                    break;
            }
    }

    @Override
    public Frame nextFrame() {
        ColorChar[][] chars = new ColorChar[width][height];
        placeApple(chars);
        move(chars);
        snakeBitHerself(chars);
        Sound sound = getSound();
        Frame frame = new Frame(chars,sound,this);
        return frame;

    }
    private void placeApple(ColorChar[][] chars){
        if(!applePlaced) {
            do {
                xApple = (int) (Math.random() * height);
                yApple = (int) (Math.random() * width);
            }while((xApple <= 0 || xApple >= playField.length || yApple <= 0 || yApple >= playField[0].length));
            applePlaced = true;
        }
        chars[xApple][yApple] = new ColorChar('o',Color.BLUE);
    }

    private void move(ColorChar[][] chars) {

        Point head = bodyParts.get(0);
        int newx= head.x + dirX;
        int newy= head.y + dirY;
        Point newHead = new Point(newx,newy);
        bodyParts.add(0,newHead);
        bodyParts.remove(bodyParts.size()-1);
        printAnimal(chars);

    }
    private void printAnimal(ColorChar[][] chars){
        for(int i = 0; i < bodyParts.size();i++){
            if(hasEnded()) return;
            chars[bodyParts.get(i).x][bodyParts.get(i).y] = new ColorChar('S', Color.BLUE);
        }
    }
    private boolean snakeBitHerself(ColorChar[][] chars){
        if(!hasEnded() && !checkCollision(chars)){
            for(int i = 1; i < bodyParts.size();i++){
                if(chars[bodyParts.get(0).x][bodyParts.get(0).y] == chars[bodyParts.get(i).x][bodyParts.get(i).y]){
                    gameOver = true;
                }
            }
        }
        return gameOver;
    }

    private boolean checkCollision(ColorChar[][] chars){
        if(hasEnded()) return false;
        if(chars[xApple][yApple] == chars[bodyParts.get(0).x][bodyParts.get(0).y]){
            applePlaced = false;
            appleConsumed = true;
            score++;
            Point temp = new Point(xApple,yApple);
            bodyParts.add(0,temp);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasEnded() {
        if(firstFrame) {
            firstFrame = false;
            return gameOver;
        }
        if(bodyParts.get(0).x == -1 || bodyParts.get(0).x == height){
            gameOver = true;
        }
        if(bodyParts.get(0).y == -1 || bodyParts.get(0).y == width){
            gameOver = true;
        }

        return gameOver;
    }


    private Sound getSound(){
        /*if(startingSound){
            startingSound = false;
            return Sound.OKAYLETSGO;
        }
        if(keyPressed) {
            keyPressed = false;
            return Sound.SCHREI;
        }
        if(appleConsumed){
            appleConsumed = false;
            return Sound.ZUNGENSCHNALZER;
        }
        if(hasEnded()){
            return Sound.GAMEOVERNOOB;
        }*/
        return null;
    }

    @Override
    public String getName() {
        return "Snake";
    }

    @Override
    public int millisBetweenFrames() {
        if(score < 3){
            return 200;
        }else if(score < 7){
            return 150;
        }else if(score < 15){
            return 100;
        }else{
            return 50;
        }
    }

    @Override
    public String getInstructions() {
        StringBuilder b = new StringBuilder();
        b.append("This game is called Snake\n");
        b.append("Goal is to eat the spawning apples\n");
        b.append("Move by using the arrow-keys\n");
        b.append("Have fun and enjoy!\n");
        return b.toString();
    }
    @Override
    public int getScore(){
        return score;
    }

}
