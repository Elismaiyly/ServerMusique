package SML;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private MusicLibrary musicLibrary;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(Socket clientSocket, MusicLibrary musicLibrary) throws IOException {
        this.clientSocket = clientSocket;
        this.musicLibrary = musicLibrary;
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                String request = in.readUTF();
                if (request.equals("LIST")) {
                    sendMusicList();
                } else if (request.startsWith("PLAY")) {
                    String songName = request.substring(5);
                    playMusic(songName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMusicList() throws IOException {
        String[] musicList = musicLibrary.getMusicList();
        out.writeInt(musicList.length);
        for (String song : musicList) {
            out.writeUTF(song);
        }
    }

    private void playMusic(String songName) throws IOException {
        File songFile = musicLibrary.getMusicFile(songName);
        FileInputStream fileIn = new FileInputStream(songFile);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fileIn.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        fileIn.close();
    }
}
