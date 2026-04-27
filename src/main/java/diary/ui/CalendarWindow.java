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

        JButton prev = new JButton("<");
        JButton next = new JButton(">");
        JButton back = new JButton("Menu");
        JButton exit = new JButton("Exit");

        JLabel label = new JLabel(currentMonth.getMonth() + " " + currentMonth.getYear());
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));

        prev.setFocusPainted(false);
        next.setFocusPainted(false);
        back.setFocusPainted(false);
        exit.setFocusPainted(false);

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

        JTextArea area = new JTextArea();
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setEditable(false);

        if (list.isEmpty()) {
            area.setText("Žádné zápisy");
        } else {
            StringBuilder sb = new StringBuilder();

            for (Entry e : list) {
                sb.append(e.getTitle()).append("\n");
                sb.append(e.getContent()).append("\n\n");
            }

            area.setText(sb.toString());
        }

        JOptionPane.showMessageDialog(this, new JScrollPane(area));
    }
}