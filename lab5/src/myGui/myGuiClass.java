package myGui;

import javax.swing.*;
import java.awt.*;

public class myGuiClass extends JFrame {
    private JTextPane jinput;
    private JTextPane joutput;
    public myGuiClass(String title){
        super("NOPE");
        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout());
        Container cont = getContentPane();
        setSize(500, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // setup left panel:
        JPanel leftPane = new JPanel();
        leftPane.setPreferredSize(new Dimension(250, 1000));
        cont.add(leftPane, BorderLayout.WEST);

        jinput = new JTextPane();
        joutput = new JTextPane();

        // setup text-pane:
        jinput.setPreferredSize(new Dimension(250,500));
        jinput.setBackground(Color.DARK_GRAY);

        joutput.setPreferredSize(new Dimension(250, 500));
        joutput.setBackground(Color.BLACK);

        leftPane.add(jinput, BorderLayout.NORTH);
        leftPane.add(joutput, BorderLayout.SOUTH);

        setVisible(true);
    }
}
