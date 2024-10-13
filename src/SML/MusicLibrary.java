package SML;
import java.io.File;

public class MusicLibrary {
    private final File musicDir;

    public MusicLibrary() {
        musicDir = new File("music");
        if (!musicDir.exists()) {
            musicDir.mkdirs();
        }
    }

    public String[] getMusicList() {
        return musicDir.list((dir, name) -> name.endsWith(".mp3"));
    }

    public File getMusicFile(String songName) {
        return new File(musicDir, songName);
    }
}
