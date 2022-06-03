package App;

import Audio.Sound;
import Game.*;
import Games.*;
import In_Out.*;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;

// main class calls the games
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

    // runs the console never ends
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


    // runs the game checks if game has ended
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

    // if we would have more game classes we could enable them here
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
                    case NativeKeyEvent.VC_5:
                        game = new TetrisFix();
                    default:
                        break;
                }

            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
    }

    // draws frame
    private static void drawFrame(Frame frame) {
        clearScreen();
        Out.print(frame);
    }


    // handles audio play
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

    // here we handle the fps with a simple thread.sleep
    private static void waitForNextFrame(){
        try {
            Thread.sleep(game.millisBetweenFrames());
        } catch (InterruptedException e) {}
    }

    // sends the inputs to the game
    // some need specific handling for key released (is also handled)
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