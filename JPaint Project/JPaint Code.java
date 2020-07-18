import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Jp {

    public static void main(String[] args){

        new JPaint();

    }

}

class JPaint extends JFrame {

    Draw draws;
    JButton line, rect, oval, c_rect, c_oval, c_arc, rect_3d, a_switch, background, colors, clear;
    JPanel controls;
    static JLabel status_bar;

    public JPaint(){

        try {

            // Nimbus Theme in Swing
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

        }catch (Exception ex){

            JOptionPane.showMessageDialog(null, "Theme Error", "Error", JOptionPane.ERROR_MESSAGE);

        }

        // Components

        draws = new Draw();
        controls = new JPanel();
        controls.setLayout(new GridLayout(0, 1));
        status_bar = new JLabel("   X : 0, Y : 0");

        // Adding control buttons

        line = new JButton("|");
        line.setToolTipText("Line");
        line.addActionListener(e -> draws.setElement(1));
        controls.add(line);

        rect = new JButton("▭");
        rect.setToolTipText("Rectangle");
        rect.addActionListener(e -> draws.setElement(3));
        controls.add(rect);

        c_rect = new JButton("▬");
        c_rect.setToolTipText("Colored Rectangle");
        c_rect.addActionListener(e -> draws.setElement(4));
        controls.add(c_rect);

        rect_3d = new JButton("❏");
        rect_3d.setToolTipText("3D Rectangle");
        rect_3d.addActionListener(e -> draws.setElement(6));
        controls.add(rect_3d);

        oval = new JButton("O");
        oval.setToolTipText("Oval");
        oval.addActionListener(e -> draws.setElement(2));
        controls.add(oval);

        c_oval = new JButton("●");
        c_oval.setToolTipText("Colored Oval");
        c_oval.addActionListener(e -> draws.setElement(5));
        controls.add(c_oval);

        c_arc = new JButton("⌒");
        c_arc.setToolTipText("Colored Arc");
        c_arc.addActionListener(e -> draws.setElement(7));
        controls.add(c_arc);

        a_switch = new JButton("All");
        a_switch.setToolTipText("Toggle Between All And One Drawing");
        a_switch.addActionListener(e -> {
            if(draws.d == 1) {
                a_switch.setText("One");
                draws.d = 0;
            }else {
                a_switch.setText(" All ");
                draws.d = 1;
            }
        });
        controls.add(a_switch);

        background = new JButton("⚛");
        background.setToolTipText("Change Background");
        background.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Select A Background Color", null);
            draws.changeBackground(c);
        });
        controls.add(background);

        colors = new JButton("⚜");
        colors.setToolTipText("Change Inside Colors");
        colors.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Select An Inside Color", null);
            draws.changeColor(c);
        });
        controls.add(colors);

        clear = new JButton("Clear");
        clear.setToolTipText("Clear The Screen");
        clear.addActionListener(e -> draws.clear_screen());
        controls.add(clear);

        // Window components alignment

        add(new JScrollPane(draws), BorderLayout.CENTER);
        add(controls, BorderLayout.WEST);
        add(status_bar, BorderLayout.SOUTH);

        // Add all the window settings in the bottom

        setTitle("JPaint");
        setVisible(true);
        setSize(1000,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void change_status_bar(String str){

        status_bar.setText(str);

    }


}

class Draw extends JPanel {

    int element=1;
    int x1=0, y1=0, x2=0, y2=0, width=0, height=0, d=1;
    Color c;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(c);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                x1 = e.getX();
                y1 = e.getY();
                JPaint.change_status_bar(String.format("   X : %s, Y : %s", e.getX(), e.getY()));

            }

            @Override
            public void mouseReleased(MouseEvent e) {

                x2 = e.getX();
                y2 = e.getY();
                width = x2 - x1;
                height = y2 - y1;

                JPaint.change_status_bar(String.format("   X : %s, Y : %s", e.getX(), e.getY()));

                if(d==1)
                    repaint(x1, y1, width+1, height+1);
                else
                    repaint();
            }

        });

        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {

                JPaint.change_status_bar(String.format("   X : %s, Y : %s", e.getX(), e.getY()));

            }

            @Override
            public void mouseDragged(MouseEvent e) {

                JPaint.change_status_bar(String.format("   X : %s, Y : %s", e.getX(), e.getY()));

            }

        });

        if(element == 1){
            g.drawLine(x1, y1, x2, y2);
        }else if(element == 2){
            g.drawOval(x1, y1, width, height);
        }else if(element == 3){
            g.drawRect(x1, y1, width, height);
        }else if(element == 4){
            g.fillRect(x1, y1, width, height);
        }else if(element == 5){
            g.fillOval(x1, y1, width, height);
        }else if(element == 6){
            g.fill3DRect(x1, y1, width, height, true);
        }else if(element == 7){
            g.fillArc(x1, y1, width, height, x1, y1);
        }

    }

    public void clear_screen(){

        x1 = x2 = y1 = y2 = width = height = 0;
        repaint();

    }

    public void changeBackground(Color color){

        setBackground(color);

    }

    public void changeColor(Color color){

        c = color;

    }

    public void setElement(int num){

        element = num;

    }

}