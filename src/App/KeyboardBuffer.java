package App;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.*;

import java.util.ArrayList;
import java.util.List;


class KeyboardBuffer implements NativeKeyListener {

    private List<Integer> keyCodeBuffer = new ArrayList<Integer>();
    public static KeyboardBuffer GetKeyboardBuffer() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {}
        KeyboardBuffer buffer = new KeyboardBuffer();
        GlobalScreen.addNativeKeyListener(buffer);
        return buffer;

    }
    public List<Integer> ClearBuffer(){
        List<Integer> buffer = new ArrayList<Integer>(keyCodeBuffer);
        keyCodeBuffer.clear();
        return buffer;
    }
    public synchronized void waitForEnter(){
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public synchronized void wakeUp(){
        notify();
        keyCodeBuffer.clear();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode==NativeKeyEvent.VC_ENTER) wakeUp();
        keyCodeBuffer.add(keyCode);
    }



    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {
        int keyCode = -nke.getKeyCode();
        keyCodeBuffer.add(keyCode);
    }

}
