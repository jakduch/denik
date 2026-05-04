package diary.ui;

import diary.Navigation;
import diary.storage.FileStorage;
import diary.model.Entry;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

public class CalendarWindow extends JFrame {

    private LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);

    public CalendarWindow() {
        setTitle("Kalendář");
        setSize(700, 500);
        setLocationRelativeTo(null);
        render();
    }

    private void render() {

        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // 🔝 TOP PANEL
        JPanel top = new JPanel();
        top.setBackground(Color.GRAY);

        JButton prev = new JButton("<");
        JButton next = new JButton(">");
        JButton back = new JButton("Menu");
        JButton exit = new JButton("Exit");

        JLabel label = new JLabel(currentMonth.getMonth() + " " + currentMonth.getYear());
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));


        prev.setFocusPainted(false);
        prev.setBorderPainted(false);
        prev.setOpaque(true);
        prev.setBackground(Color.GRAY);
        prev.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        prev.setPreferredSize(new Dimension(80, 60));
        next.setFocusPainted(false);
        next.setBorderPainted(false);
        next.setOpaque(true);
        next.setBackground(Color.GRAY);
        next.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        next.setPreferredSize(new Dimension(80, 60));
        back.setFocusPainted(false);
        back.setBorderPainted(false);
        back.setOpaque(true);
        back.setBackground(Color.GRAY);
        back.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        back.setPreferredSize(new Dimension(80, 60));
        exit.setFocusPainted(false);
        exit.setBorderPainted(false);
        exit.setOpaque(true);
        exit.setBackground(Color.GRAY);
        exit.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        exit.setPreferredSize(new Dimension(80, 60));
        exit.setBounds(800, 0, 80, 60);

        prev.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            render();
        });

        next.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            render();
        });

        back.addActionListener(e ->
                Navigation.go(this, new MainWindow(FileStorage.loadUserName()))
        );

        exit.addActionListener(e -> System.exit(0));

        top.add(prev);
        top.add(label);
        top.add(next);
        top.add(back);
        top.add(exit);

        add(top, BorderLayout.NORTH);


        // 🗓️ GRID PANEL
        JPanel grid = new JPanel(new GridLayout(0, 7, 5, 5));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        grid.setBackground(Color.WHITE);

        // dny v týdnu
        String[] days = {"Po", "Út", "St", "Čt", "Pá", "So", "Ne"};
        for (String d : days) {
            JLabel l = new JLabel(d, SwingConstants.CENTER);
            l.setFont(new Font("Segoe UI", Font.BOLD, 14));
            grid.add(l);
        }

        LocalDate first = currentMonth.withDayOfMonth(1);
        int offset = first.getDayOfWeek().getValue();

        for (int i = 1; i < offset; i++) {
            grid.add(new JLabel());
        }

        int daysInMonth = currentMonth.lengthOfMonth();

        for (int i = 1; i <= daysInMonth; i++) {

            LocalDate date = currentMonth.withDayOfMonth(i);
            JButton btn = new JButton(String.valueOf(i));

            // styl tlačítka
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setOpaque(true);
            btn.setBackground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            btn.setPreferredSize(new Dimension(80, 60));

            // dnešní den
            if (date.equals(LocalDate.now())) {
                btn.setBackground(new Color(100, 149, 237));
                btn.setForeground(Color.WHITE);
            }

            // hover efekt
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    if (!date.equals(LocalDate.now())) {
                        btn.setBackground(new Color(220, 220, 220));
                    }
                }

                public void mouseExited(MouseEvent evt) {
                    if (!date.equals(LocalDate.now())) {
                        btn.setBackground(Color.WHITE);
                    }
                }
            });

            btn.addActionListener(e -> showEntries(date));

            grid.add(btn);
        }

        add(grid, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
    private void showEntries(LocalDate date) {
        List<Entry> list = FileStorage.loadEntries().stream()
                .filter(e -> e.getDateTime().toLocalDate().equals(date))
                .toList();
        JButton addBtn = new JButton("Přidat zápis");
        JButton okBtn = new JButton("OK");

        addBtn.addActionListener(e ->
                Navigation.go(this, new EntryWindow(date.atStartOfDay()))
        );
        okBtn.addActionListener(e -> {
            Navigation.go(this, new CalendarWindow());
        });

        Object[] options = {addBtn, okBtn};

        JPanel panel = null;

        panel = new JPanel(new BorderLayout());

        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(addBtn, BorderLayout.SOUTH);

        JLabel title = new JLabel("Zápisy pro " + date);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        panel.add(title, BorderLayout.NORTH);

        if (list.isEmpty()) {
            JLabel empty = new JLabel("Žádné zápisy");
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(empty, BorderLayout.CENTER);
        } else {

            JPanel entriesPanel = new JPanel();
            entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));

            for (Entry e : list) {

                JPanel entryPanel = new JPanel();
                entryPanel.setLayout(new BorderLayout());
                entryPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
                entryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

                JLabel entryTitle = new JLabel(e.getTitle());
                entryTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));

                JTextArea content = new JTextArea(e.getContent());
                content.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                content.setLineWrap(true);
                content.setWrapStyleWord(true);
                content.setEditable(false);
                content.setOpaque(false);

                entryPanel.add(entryTitle, BorderLayout.NORTH);
                entryPanel.add(content, BorderLayout.CENTER);

                entriesPanel.add(entryPanel);
                entriesPanel.add(Box.createVerticalStrut(8));
            }
            JScrollPane scrollPane = new JScrollPane(entriesPanel);
            scrollPane.setBorder(null);

            panel.add(scrollPane, BorderLayout.CENTER);
        }
        JOptionPane.showOptionDialog(
                this,
                panel,
                "Zápisy",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                okBtn
        );
    }
}
