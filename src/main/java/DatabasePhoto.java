import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.io.IOException;

import java.io.BufferedWriter;

public class DatabasePhoto {
    private ArrayList<String> photosPath;

    public DatabasePhoto() {
        photosPath = new ArrayList<String>();
    }

    public boolean loadPhotos() {
        try {
            File f = new File("UsersPhotos.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            while (true) {
                if (line == null) {
                    break;
                }
                photosPath.add(line);
                line = bfr.readLine();
            }
            bfr.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean savePhotos() {
        try {
            File f = new File("UsersPhotos.txt");
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (Exception e) {
                    return false;
                }
            }
            FileWriter fw = new FileWriter(f);
            BufferedWriter bfw = new BufferedWriter(fw);
            for (String path : photosPath) {
                bfw.write(path + "\n");
            }
            bfw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addPhotos(String path) {
        photosPath.add(path);
    }

    public void displayPhotos(String path) {
        // Create a JFrame Window
        JFrame frame = new JFrame(path);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        // Load image using ImageIcon
        ImageIcon imageIcon = new ImageIcon(path);

        // Add image to the window
        JLabel label = new JLabel(imageIcon);

        // add label to frame(window)
        frame.add(label);

        // make visible
        frame.setVisible(true);
    }
}
