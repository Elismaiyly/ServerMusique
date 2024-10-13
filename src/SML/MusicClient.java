package SML;

import java.io.*;
import java.net.*;

public class MusicClient {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public MusicClient(String serverAddress, int port) throws IOException {
        socket = new Socket(serverAddress, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        System.out.println("Connected to server at " + serverAddress + ":" + port);
    }

    public String[] getMusicList() throws IOException {
        out.writeUTF("LIST");
        System.out.println("Requesting music list from server");
        int length = in.readInt();
        System.out.println("Number of songs received: " + length);
        String[] musicList = new String[length];
        for (int i = 0; i < length; i++) {
            musicList[i] = in.readUTF();
            System.out.println("Received song: " + musicList[i]);
        }
        return musicList;
    }

    public InputStream getMusicStream(String songName) throws IOException {
        out.writeUTF("PLAY " + songName);
        return new BufferedInputStream(socket.getInputStream());
    }

    public void close() throws IOException {
        socket.close();
    }
}