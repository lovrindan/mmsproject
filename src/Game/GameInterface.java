package Game;

public interface GameInterface {
    void startGame();
    default void inputReleased(int keyCode){};
    void inputPressed(int keyCode);
    Frame nextFrame();
    String getInstructions();
    String getName();
    default int getScale(){return 1;}
    int millisBetweenFrames();
    boolean hasEnded();
    default int getScore(){return -1;};
}
