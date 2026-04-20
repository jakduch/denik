import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;

public class FlipPanel extends JPanel {

    private Image page1;
    private Image page2;
    private double t = 0; // 0–1 animace
    private Timer timer;

    public FlipPanel() {
        page1 = new ImageIcon("page1.png").getImage();
        page2 = new ImageIcon("page2.png").getImage();

        timer = new Timer(16, (ActionEvent e) -> {
            t += 0.02;
            if (t > 1) t = 1;
            repaint();
        });
    }

    public void startFlip() {
        t = 0;
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();

        // vykresli spodní stránku (ta, která se objeví po otočení)
        g2.drawImage(page2, 0, 0, w, h, null);

        // výpočet šířky viditelné části otáčené stránky
        int visibleWidth = (int) (w * (1 - t));

        if (visibleWidth > 0) {
            // ořízni oblast
            Shape oldClip = g2.getClip();
            g2.setClip(0, 0, visibleWidth, h);

            // transformace pro efekt ohnutí
            AffineTransform at = new AffineTransform();
            double shear = 0.5 * t; // ohnutí
            at.shear(shear, 0);
            g2.setTransform(at);

            // vykresli horní stránku
            g2.drawImage(page1, 0, 0, w, h, null);

            // obnov klip a transformaci
            g2.setClip(oldClip);
            g2.setTransform(new AffineTransform());
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Flipbook");
        FlipPanel panel = new FlipPanel();

        f.add(panel);
        f.setSize(600, 400);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        panel.startFlip();
    }
}
