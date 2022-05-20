package Game;

public interface GameInterface {
    void startGame();
    default void inputReleased(int keyCode){};
    void inputPressed(int keyCode);
    Frame nextFrame();
    String getInstructions(); // Anleitung, bzw Controls. Pro Zeile maximal 38 Zeichen, wegen Formatierung
    String getName();
    default int getScale(){return 1;}
    int milisBetweenFrames();
    boolean hasEnded();
}
