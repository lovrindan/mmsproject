package Audio;

import java.io.File;
import java.io.IOException;

public enum Sound {
    BEAR("src/Audio/SoundFiles/bear_growl_y.wav"),
    PING("src/Audio/SoundFiles/ping.wav"),
    PING2("src/Audio/SoundFiles/ping2.wav");
    //TODO: Add your custom sound files here
    private final String filePath;

    private Sound(String path) {
        this.filePath = path;;
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
