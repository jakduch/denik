package diary;

import diary.storage.FileStorage;
import diary.ui.MainWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            String name = FileStorage.loadUserName();

            if (name == null || name.isEmpty()) {
                name = JOptionPane.showInputDialog("Zadej své jméno:");
                FileStorage.saveUserName(name);
            }



            new MainWindow(name);
        });
    }
}