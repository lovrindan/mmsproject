package Audio;

import java.io.File;
import java.io.IOException;

public enum Sound {
    GAMEOVER("src/Audio/SoundFiles/GameOver.wav"),
    CLACK("src/Audio/SoundFiles/Clack.wav"),
    AMAZINGGAMEPLAY("src/Audio/SoundFiles/AmazingGamePlay.wav"),
    LETSGO("src/Audio/SoundFiles/LetsGo.wav"),
    KLATSCHEN("src/Audio/SoundFiles/Klatschen.wav"),
    BEAR("src/Audio/SoundFiles/bear_growl_y.wav"),
    PING("src/Audio/SoundFiles/ping.wav"),
    PING2("src/Audio/SoundFiles/ping2.wav"),
    PONGPADDLE("src/Audio/SoundFiles/pong_paddle.wav"),
    PONGSCORE("src/Audio/SoundFiles/pong_score.wav"),
    PONGWALL("src/Audio/SoundFiles/pong_wall.wav"),
    SCHMAZER("src/Audio/SoundFiles/Schmazer.wav"),
    SCHNIPS("src/Audio/SoundFiles/Schnips.wav"),
    TSSS("src/Audio/SoundFiles/Tsss.wav"),
    WELLDONE("src/Audio/SoundFiles/WellDone.wav");


    //TODO: Add your custom sound files here
    private final String filePath;

    Sound(String path) {
        this.filePath = path;
    }
    public File getFile(){
        return adjustForWorkingDirectory();
    }
    private File adjustForWorkingDirectory(){
        //This bit of code is necessary because the working Directory depends
        //on whether the jar file was started from inside the IDE or the file explorer
        if(workingDirectoryIsJarFile()){
            return new File("../../../"+ filePath);
        }
        else {
            return new File( filePath);
        }
    }
    private boolean workingDirectoryIsJarFile(){
        try {
            String currentPath = new File(".").getCanonicalPath();
            return (currentPath.endsWith("MultimediaProjekt_jar"));
        } catch (IOException e) {
            return false;
        }
    }
}
