package SML;
import java.io.*;
import java.net.*;
import java.util.*;

public class MusicServer {
    private ServerSocket serverSocket;
    private List<Socket> clients;
    private MusicLibrary musicLibrary;

    public MusicServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<>();
        musicLibrary = new MusicLibrary();
    }

    public void start() {
        System.out.println("Music server started...");
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                clients.add(clientSocket);
                new Thread(new ClientHandler(clientSocket, musicLibrary)).start();
                System.out.println("New client connected...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            MusicServer server = new MusicServer(12345);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
