package App;

import Audio.Sound;
import Game.*;
import Games.*;
import In_Out.*;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;


public class MainApp {
    static GameInterface game;
    static KeyboardBuffer keyboardBuffer;
    public static void main(String[] args) throws IOException {
        Console console = System.console();
        if(console == null){
            String filename = MainApp.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar \"" + filename + "\""});
        }else{
            runApp();
        }
    }

    public static void runApp() {
        keyboardBuffer = KeyboardBuffer.GetKeyboardBuffer();
        Out.println(Screens.titleScreen());
        waitForEnter();
        clearScreen();
        while(true){
            runGame();
            clearScreen();
            Out.println(Screens.gameFinishedScreen());
            waitForEnter();
            clearScreen();
        }
    }

    private static void waitForEnter() {
        keyboardBuffer.waitForEnter();
    }

    private static void runGame()  {
        selectGame();
        showInstructions();
        game.startGame();
        while(!game.hasEnded()){
            Frame frame = game.nextFrame();
            drawFrame(frame);
            playAudio(frame.sound);
            waitForNextFrame();
            sendInputs();
        }
    }

    private static void showInstructions() {
        String inst = game.getInstructions();
        Out.println(Screens.getInstructionsScreen(inst));
        keyboardBuffer.ClearBuffer();
        waitForEnter();
    }

    private static void selectGame() {
        Out.println(Screens.gameSelectScreen());
        trySelectGame();
        clearScreen();
    }
    private static void trySelectGame(){
        while(true){
            for (int keyCode:keyboardBuffer.ClearBuffer()) {
                switch (keyCode){
                    case NativeKeyEvent.VC_1:
                        game = new DemoGame();
                        return;
                    case NativeKeyEvent.VC_2:
                        game = new Breakout();
                        return;
                    case NativeKeyEvent.VC_3:
                        game = new Snake();
                        return;
                    case NativeKeyEvent.VC_4:
                        game = new Pong();
                        return;
                    default:
                        break;
                }

            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
    }
    private static void drawFrame(Frame frame) {
        clearScreen();
        Out.print(frame);
    }


    private static void playAudio(Sound sound){
        if(sound==null)return;
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound.getFile()));
            clip.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void waitForNextFrame(){
        try {
            Thread.sleep(game.millisBetweenFrames());
        } catch (InterruptedException e) {}
    }
    private static void sendInputs() {
        var inputs = keyboardBuffer.ClearBuffer();
        for (int i:inputs) {
            if(i>0)
                game.inputPressed(i);
            else
                game.inputReleased(-i);
        }
    }
    public static void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {

        }
    }

}
//ProjectAntrag 1Stunde
//Erstes Treffen 29.04.2022 2Stunden ~(1,5)
//Zweites Treffen 13.05.2022 0l,5 Stunden