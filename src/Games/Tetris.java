package Games;

import Audio.Sound;
import Game.Color;
import Game.ColorChar;
import Game.Frame;
import Game.GameInterface;
import In_Out.Out;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.util.ArrayList;
import java.util.Collections;


public class Tetris implements GameInterface {
    private final int width = 20;
    private final int height = 20;

    private int frameTime = 1000;
    ColorChar[][] field = new ColorChar[width][height];
    private boolean dead = false;
    private int score = 0;
    private int currentPiece = 0;
    int rotation = 0;
    private final int[] pieceOrigin = new int[2];
    private final int[] nextpieceOrigin = {2, 5};
    private final ArrayList<Integer> nextPieces = new ArrayList<>();
    Sound sound = Sound.LETSGO;


    ColorChar[][][] pieces = {
            //I-Piece
            {
                {new ColorChar(' '),new ColorChar(' '),new ColorChar(' '),new ColorChar(' ')},
                {new ColorChar('O'),new ColorChar('O'),new ColorChar('O'),new ColorChar('O')}
            },
            // J-Piece
            {
                    {new ColorChar('O'),new ColorChar('O'),new ColorChar('O'),new ColorChar(' ')},
                    {new ColorChar('O'),new ColorChar(' '),new ColorChar(' '),new ColorChar(' ')},

            },
            //L-Piece
            {
                    {new ColorChar('O'),new ColorChar(' '),new ColorChar(' '),new ColorChar(' ')},
                    {new ColorChar('O'),new ColorChar('O'),new ColorChar('O'),new ColorChar(' ')}
            },
            //O-Piece
            {
                    {new ColorChar('O'),new ColorChar('O'),new ColorChar(' '),new ColorChar(' ')},
                    {new ColorChar('O'),new ColorChar('O'),new ColorChar(' '),new ColorChar(' ')}
            },
            //S-Piece
            {
                    {new ColorChar(' '),new ColorChar('O'),new ColorChar('O'),new ColorChar(' ')},
                    {new ColorChar('O'),new ColorChar('O'),new ColorChar(' '),new ColorChar(' ')}
            },
            //T-Piece
            {
                    {new ColorChar(' '),new ColorChar('O'),new ColorChar(' '),new ColorChar(' ')},
                    {new ColorChar('O'),new ColorChar('O'),new ColorChar('O'),new ColorChar(' ')}
            },
            //Z-Piece
            {
                    {new ColorChar('O'),new ColorChar('O'),new ColorChar(' '),new ColorChar(' ')},
                    {new ColorChar(' '),new ColorChar('O'),new ColorChar('O'),new ColorChar(' ')}
            }
    };

    @Override
    public void inputPressed(int keyCode) {
        switch(keyCode){
            case NativeKeyEvent.VC_LEFT:
                move(-1);
                sound = Sound.CLACK;
                break;
            case NativeKeyEvent.VC_RIGHT:
                move(1);
                sound = Sound.CLACK;
                break;
            case NativeKeyEvent.VC_UP:
                rotate(-1);
                sound = Sound.CLACK;
                break;
            case NativeKeyEvent.VC_DOWN:
                rotate(1);
                sound = Sound.CLACK;
                break;
            default:
                break;
        }
    }

    private void rotate(int i) {    //deac until move fix
        int newRotation = (rotation + i) %4;
        if(newRotation == 0){
            rotation = 1;
        }
        if(!collidesAt(pieceOrigin[0], pieceOrigin[1])){
            rotation = newRotation;
        }
        draw();
    }

    private void draw() {
        if(collidesAt(nextpieceOrigin[1], nextpieceOrigin[0])){
            if(pieceOrigin[1] <= 2){
                dead = true;
            }else {
                newPiece();
            }
        }else {
            //clear old pos
            for (int i = 0; i < pieces[currentPiece].length; i++) {
                for (int j = 0; j < pieces[currentPiece][0].length; j++) {
                    field[(i + pieceOrigin[0]) % width][(j + pieceOrigin[1]) % height] = new ColorChar(' ');
                }
            }
            //draw new
            for (int i = 0; i < pieces[currentPiece].length; i++) {
                for (int j = 0; j < pieces[currentPiece][0].length; j++) {
                    field[(i + nextpieceOrigin[1]) % height][(j + nextpieceOrigin[0]) % width] = pieces[currentPiece][i % pieces[currentPiece].length][j % pieces[currentPiece][i % pieces[currentPiece].length].length];
                }
            }
            pieceOrigin[0] = nextpieceOrigin[0];
            pieceOrigin[1] = nextpieceOrigin[1];
        }
    }

    private void move(int i) {
        if(nextpieceOrigin[1] < 1){
            nextpieceOrigin[1] = 2;
        }
        if(nextpieceOrigin[1] > width-1){
            nextpieceOrigin[1] = width -2;
        }
        if(!collidesAt(nextpieceOrigin[1], nextpieceOrigin[0])){
            nextpieceOrigin[1] = pieceOrigin[1] + i;
        }
        draw();
    }
    private boolean collidesAt(int posx, int posy){
        if(posx == height -1){
            return true;
        }
        for(int i = posx; i < pieces[currentPiece].length -1; i++){
            for(int j = posy; j < pieces[currentPiece][i].length; j++){
                if(field[i][j].aChar == 'O'){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void startGame() {
        sound = null;
        newPiece();
    }


    private void newPiece() {
        pieceOrigin[0] = 2;
        pieceOrigin[1] = 5;
        nextpieceOrigin[0] = 2;
        nextpieceOrigin[1] = 5;
        rotation = 0;
        if(collidesAt(pieceOrigin[0], pieceOrigin[1])){
            dead = true;
        }else {
            //random generator
            if (nextPieces.isEmpty()) {
                Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
                Collections.shuffle(nextPieces);
            }
            currentPiece = nextPieces.get(0);
            nextPieces.remove(0);
            draw();
        }
    }

    @Override
    public Frame nextFrame() {
        Sound sound = getSound();
        downMove();
        Frame frame = new Frame(field,sound,this);
        return frame;
    }

    private void downMove() {
        nextpieceOrigin[0] ++;
        if(collidesAt(pieceOrigin[1],pieceOrigin[0])){
            boolean rowFill = true;
            for(int i = 1; i < field[0].length; i++){
                if(field[pieceOrigin[i]][0].aChar != 'O'){
                    rowFill  = false;
                }
            }
            if(rowFill){
                clearRow(pieceOrigin[0]);
            }
        }
        if(pieceOrigin[0] >= width || pieceOrigin[1] >= height){
            newPiece();
        }else {
            draw();
        }
    }
    private void clearRow(int row){
        for(int i = 1; i < field[0].length; i++){
            field[row][i] = null;
        }
        score++;
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
        return frameTime;
    }


    @Override
    public boolean hasEnded() {
        return dead;
    }

    @Override
    public int getScore() {
        return score;
    }

}
