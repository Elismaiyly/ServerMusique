package SML;

import javax.sound.sampled.*;
import java.io.*;

public class MusicPlayer {
    private final AudioInputStream audioStream;

    public MusicPlayer(InputStream musicStream) throws UnsupportedAudioFileException, IOException {
        audioStream = AudioSystem.getAudioInputStream(musicStream);
    }

    public void play() throws LineUnavailableException, IOException {
        AudioFormat format = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);
        audioLine.open(format);
        audioLine.start();

        byte[] br = new byte[4096];
        int bytesRead;
        while ((bytesRead = audioStream.read(br)) != -1) {
            audioLine.write(br, 0, bytesRead);
        }

        audioLine.drain();
        audioLine.close();
    }
}
