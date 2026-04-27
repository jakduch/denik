package diary.ui;

import diary.model.Entry;
import diary.storage.FileStorage;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import diary.utils.DateUtils;

public class EntryWindow extends JFrame {

    public EntryWindow(LocalDateTime date) {

        setTitle("Zápis");
        setSize(500, 400);
        setLocationRelativeTo(null);

        JTextField title = new JTextField();
        JTextArea text = new JTextArea();

        String[] img = {null};

        JButton save = new JButton("Uložit");
        JButton delete = new JButton("Smazat");
        JButton image = new JButton("Obrázek");
        JButton exit = new JButton("Exit");



        image.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                img[0] = fc.getSelectedFile().getAbsolutePath();
            }
        });

        save.addActionListener(e -> {

            LocalDateTime safeDateTime =
                    (date != null)
                            ? DateUtils.fromDate(LocalDate.from(date))
                            : DateUtils.now();

            Entry entry = new Entry(
                    title.getText(),
                    text.getText(),
                    safeDateTime,
                    img[0]
            );

            FileStorage.saveEntry(entry);


            JOptionPane.showMessageDialog(this, "Uloženo!");
        });


        delete.addActionListener(e -> {

            int res = JOptionPane.showConfirmDialog(
                    this,
                    "Opravdu smazat?",
                    "Potvrzení",
                    JOptionPane.YES_NO_OPTION
            );

            if (res == JOptionPane.YES_OPTION) {
                FileStorage.deleteEntry(new Entry(title.getText(), "", date, null));
                JOptionPane.showMessageDialog(this, "Smazáno");
            }
        });

        exit.addActionListener(e -> System.exit(0));

        JPanel bottom = new JPanel();

        bottom.add(save);
        bottom.add(delete);
        bottom.add(image);
        bottom.add(exit);

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(text), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }
}