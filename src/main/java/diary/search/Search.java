package diary.search;

import javax.swing.*;

public class Search extends JFrame {
    final JTextField text = new JTextField();

    public Search() {
    JFrame frame = new JFrame();
    this.setSize(300, 200);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(null);

    this.add(text);
    this.setVisible(true);
}

    public String getSearchValue() {
        return text.getText();
    }

    }





