package Games;

import Audio.Sound;
import Game.ColorChar;
import Game.Frame;
import Game.GameInterface;

import java.util.ArrayList;

public class TetrisFix implements GameInterface {
    ColorChar[][] fieldOut;
    ColorChar[][] fieldInter;
    Sound sound;
    boolean hasEnded;
    final ArrayList<Position> moving = new ArrayList<>();
    final ArrayList<Position> cur = new ArrayList<>();
    Position[][] pieces;
    int width = 20;
    int height = 20;
    int gameCode = 0;


    @Override
    public void startGame() {
        fieldInter = new ColorChar[width][height];
        fieldOut = new ColorChar[width][height];
        hasEnded = false;
        sound = null;
        pieces = setPieces();
        getRandomPiece();

    }



    @Override
    public void inputPressed(int keyCode) {
        return;
    }

    @Override
    public Frame nextFrame() {
        switch (gameCode){
            default:
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                fieldOut = fieldInter;
                for(int i = 0; i < moving.size(); i++){
                    Position pos = new Position(moving.get(i).getX(), (moving.get(i).getY()+1));
                    if(moving.get(i).getY() < 20 && fieldOut[pos.getX()][pos.getY()] == null){
                        fieldOut[pos.getX()][pos.getY()] = new ColorChar('O', null);
                        cur.add(pos);
                    } else break;
                    if(i == moving.size() - 1 ){
                        moving.clear();
                        for(Position c: cur){
                            moving.add(c);
                        }
                        cur.clear();
                        return new Frame(fieldOut, null, this);
                    }
                }


                return new Frame(fieldInter, null, this);
        }
    }

    private void getRandomPiece() {
        Position[] nextPiece = pieces[(int) (Math.random() * 3)];
        int offset = ((int) (Math.random() * 10))+1;
        for(Position pos: nextPiece){
            pos.moveLR(offset);
            moving.add(pos);
        }
        for(int i = 0; i < moving.size(); i++){
            if(fieldInter[moving.get(i).getX()][moving.get(i).getY()] != null){
                hasEnded = true;
                gameCode = 2;
                return;
            }
        }
    }

    public Position[][] setPieces(){
        Position[][] p = {
                {new Position(0, 0), new Position(0, 1), new Position(0, 2)}, // I
                {new Position(0, 0), new Position(0, 1), new Position(0, 2), new Position(1, 2)},   // L
                {new Position(1, 0), new Position(1, 1), new Position(1, 2), new Position(1, 2), new Position(0, 2)},// T
                {new Position(0,0), new Position(0,1), new Position(0,2), new Position(1,0), new Position(1,1), new Position(1,2)}
        };

        return p;

    }

    private Sound getSound() {
        return sound;
    }

    @Override
    public String getInstructions() {
        StringBuilder sb = new StringBuilder();
        sb.append("Use the left / right keys to move\n");
        sb.append("Use up / down to rotate //WIP\n");
        sb.append("The game ends if you touch the border\n");
        return sb.toString();
    }

    @Override
    public String getName() {
        return "Tetris";
    }

    @Override
    public int millisBetweenFrames() {
        return 0;
    }

    @Override
    public boolean hasEnded() {
        return hasEnded;
    }

    class Position{
        int x;
        int y;

        Position(int x, int y){
            this.x = x;
            this.y = y;
        }

        int getX(){
            return x;
        }

        int getY(){
            return y;
        }

        void moveLR(int i){
            x += i;
        }
    }
}


