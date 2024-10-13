package SML;



import javax.swing.*;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

public class MusicClientUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private MusicClient musicClient;
    private JComboBox<String> songList;
    private JButton playButton;
    private JButton pauseButton;
    private JButton stopButton;
    private Player audioPlayer;
    private Thread playerThread;

    public MusicClientUI() {
        // Configure the frame
        setTitle("Music Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialize the MusicClient
        try {
            musicClient = new MusicClient("localhost", 12345);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Unable to connect to server", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Set up the UI components
        songList = new JComboBox<>();
        playButton = new JButton("Play");
        pauseButton = new JButton("Pause");
        stopButton = new JButton("Stop");

        // Populate the song list
        try {
            updateSongList(); // Update the song list when creating the UI
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add action listeners
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSong = (String) songList.getSelectedItem();
                if (selectedSong != null) {
                    try {
                        if (audioPlayer != null) {
                            audioPlayer.close();
                        }
                        InputStream musicStream = musicClient.getMusicStream(selectedSong);
                        playMusicStream(musicStream);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pause functionality is not supported by JLayer directly
                // You might need to implement your own player to support pause/resume
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (audioPlayer != null) {
                    audioPlayer.close();
                }
            }
        });

        // Layout the UI components
        setLayout(new BorderLayout());
        add(songList, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);

        add(controlPanel, BorderLayout.SOUTH);
    }

    // Method to update the list of music tracks
    private void updateSongList() throws IOException {
        String[] songs = musicClient.getMusicList();
        songList.removeAllItems(); // Remove existing items
        for (String song : songs) {
            songList.addItem(song); // Add new music tracks
        }
    }

    private void playMusicStream(InputStream musicStream) {
        playerThread = new Thread(() -> {
            try {
                audioPlayer = new Player(musicStream);
                audioPlayer.play();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        });
        playerThread.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            
            public void run() {
                new MusicClientUI().setVisible(true);
            }
        });
    }
}