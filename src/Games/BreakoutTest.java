package Games;

import Game.GameInterface;
import In_Out.Out;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class BreakoutTest {
    public static void main(String[] args) {
        GameInterface game = new Breakout();
        for (int i = 0; i < 1000; i++) {
            game.inputPressed(NativeKeyEvent.VC_RIGHT);
            game.nextFrame();
        }
        Out.println("dfgsg");
    }
}
