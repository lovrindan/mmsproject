package Games;

import Audio.Sound;
import Game.ColorChar;
import Game.Frame;
import Game.GameInterface;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.util.ArrayList;
import java.util.List;

public class Breakout implements GameInterface {
    int height = 20;
    int width = 30;
    boolean gameFinished = false;
    int pedalDir = 0;
    boolean leftKeyPressed = false;
    boolean rightKeyPressed = false;
    Sound curSound;
    Box leftWall= new Box(-1,0,height,1);
    Box rightWall= new Box(width,0,height,1);
    Box celling= new Box(0,-1,1,width);
    Box floor= new Box(0,height,1,width);
    Box pedal= new Box(8,height-1,1,6);
    Ball ball = new Ball(width/2,height-2);
    List<Box> breakableBoxes = new ArrayList<>();

    @Override
    public String getName() {
        return "Breakout";
    }

    @Override
    public int millisBetweenFrames() {
        return 50;
    }

    @Override
    public boolean hasEnded() {
        return gameFinished;
    }

    @Override
    public String getInstructions() {
        return """
                Use the Arrow keys to move the panel
                Clear all boxes to win
                The game ends if you the Ball touches
                the floor
                """;
    }

    @Override
    public void startGame() {
        //Fills the first n rows with BreakableBoxes
        int nRows =3;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < nRows; y++) {
                breakableBoxes.add(new Box(x,y,1,1));
            }
        }
    }

    @Override
    public void inputPressed(int keyCode) {
        setPressedState(keyCode, true);
    }

    @Override
    public void inputReleased(int keyCode) {
        setPressedState(keyCode, false);
    }

    private void setPressedState(int keyCode, boolean b) {
        switch (keyCode) {
            case NativeKeyEvent.VC_LEFT -> rightKeyPressed = b;
            case NativeKeyEvent.VC_RIGHT -> leftKeyPressed = b;
            default -> {
            }
        }
    }

    @Override
    public Frame nextFrame() {
        determinePedalDir();
        int nMicroFrames = 10;
        for (int i = 0; i <nMicroFrames ; i++) {
            nextMicroFrame();
        }
        ColorChar[][] chars = new ColorChar[width][height];
        drawBoxes(chars);
        drawPedal(chars);
        drawBall(chars);
        if(breakableBoxes.isEmpty()) gameFinished =true;
        Frame frame = new Frame(chars,curSound, this);
        curSound =null;
        return frame;
    }
    private void determinePedalDir(){
        if(leftKeyPressed &&!rightKeyPressed){
            pedalDir =1;
        }
        else if(rightKeyPressed &&!leftKeyPressed) {
            pedalDir =-1;
        }
        else pedalDir =0;
    }

    private void movePedal() {
        double speed =0.2;
        pedal.pos.x+=pedalDir*speed;
        pedal.pos.x = clamp(pedal.pos.x,0,width-pedal.width);
    }

    private void drawBoxes(ColorChar[][] chars) {
        for (Box b:breakableBoxes) {
            chars[(int)b.pos.x][(int)b.pos.y]=new ColorChar('O');
        }
    }

    private void drawPedal(ColorChar[][] chars) {
        for (int i = 0; i < pedal.width; i++) {
            chars[(int)pedal.pos.x+i][height-1]=new ColorChar('=');
        }
    }

    private void drawBall(ColorChar[][] chars) {
        chars[(int)ball.pos.x][(int)ball.pos.y] = new ColorChar('O');
    }

    private void nextMicroFrame() {
        ball.Move();
        movePedal();
        checkForCollision();
    }

    private void checkForCollision() {
        //Detects Collisions, see Documentation
        if(ball.boxCollide(floor)) gameFinished = true;
        ball.boxCollide(celling);
        ball.boxCollide(leftWall);
        ball.boxCollide(rightWall);
        if (ball.boxCollide(pedal)){
            adjustBallForPedalSpeed(pedalDir);
            curSound=Sound.PING;
        }
        breakableBoxes.removeIf(b -> {
            if(ball.boxCollide(b)){
                curSound = Sound.PING2;
                return true;
            }
            return false;
        });
    }

    private void adjustBallForPedalSpeed(int pedaldir) {
        //If the pedal is moving when the ball hits it.
        //The Ball is accelerated in that direction.
        Vector2D pedalDirVec = new Vector2D(pedaldir*0.1,0);
        Vector2D newBallDir = ball.dir.add(pedalDirVec).toLength(ball.speed);
        ball.dir =newBallDir;
    }

    private static double clamp(double val, double min, double max) {
        if (val < min) return min;
        else if (val > max) return max;
        else return val;
    }
    class Ball{
        double radius = 0.5;
        double speed;
        Vector2D pos;
        Vector2D dir = new Vector2D(0.06,-0.13);

        public Ball(int x, int y) {
            this.pos = new Vector2D(x,y);
            speed=dir.length();
        }
        void Move(){
            pos.x+=dir.x;
            pos.y+=dir.y;
        }

        boolean boxCollide(Box box){
            double x = clamp(pos.x,box.pos.x,box.pos.x+box.width);
            double y = clamp(pos.y,box.pos.y,box.pos.y+box.height);
            Vector2D nearestPoint = new Vector2D(x,y);
            Vector2D dist = nearestPoint.distVec(ball.pos);
            if(collisionDetected(dist)){
                deflect(dist);
                return true;
            }
            return false;
        }
        boolean collisionDetected(Vector2D dist){  return dist.lengthSquared()<radius*radius;}

        private void deflect(Vector2D deflectVec) {
            //Deflects the Ball direction. see Documentation
            if(Vector2D.isAngleAcute(deflectVec,dir)) return;
            Vector2D proj = deflectVec.project(dir);
            Vector2D proj2 = proj.scalarMult(2);
            dir =dir.sub(proj2);
        }
    }
    static class Box{
        Vector2D pos;
        int height;
        int width;
        public Box(int x, int y, int height, int width) {
            pos = new Vector2D(x,y);
            this.height = height;
            this.width = width;
        }
    }

    static class Vector2D {
        public double x;
        public double y;
        public Vector2D(double x, double y) {
            this.x = x;
            this.y = y;
        }
        public Vector2D scalarMult(double f){
            return new Vector2D(x*f,y*f);
        }
        public Vector2D sub(Vector2D other){
            return new Vector2D(x-other.x,y-other.y);
        }
        public Vector2D add(Vector2D other){
            return new Vector2D(x+other.x,y+other.y);
        }
        public Vector2D distVec (Vector2D other){
            return new Vector2D(other.x-x,other.y-y);
        }
        public Vector2D project(Vector2D other){
            double v = dotProd(this, other) / dotProd(this, this);
            return scalarMult(v);
        }
        private double lengthSquared(){
            return x*x+y*y;
        }
        private double length(){
            return Math.sqrt(lengthSquared());
        }
        static public double dotProd(Vector2D a, Vector2D b){
            return a.x*b.x+a.y*b.y;
        }
        static public boolean isAngleAcute(Vector2D a, Vector2D b){
            return dotProd(a,b)>0;
        }
        public Vector2D toLength(double len){
            return normalize().scalarMult(len);
        }
        public Vector2D normalize(){
            double len = Math.sqrt(lengthSquared());
            return scalarMult(1/len);
        }
    }
}
