package Games;

import Game.Frame;
import Game.GameInterface;

public class Pong implements GameInterface {
    @Override
    public int millisBetweenFrames() {
        return 0;
    }

    @Override
    public void inputPressed(int keyCode) {

    }

    @Override
    public void startGame() {

    }

    @Override
    public Frame nextFrame() {
        return null;
    }

    @Override
    public String getInstructions() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }


    @Override
    public boolean hasEnded() {
        return false;
    }

}
