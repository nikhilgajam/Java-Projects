import javax.swing.*;
import java.awt.GridLayout;

public class Jp {

    public static void main(String[] args){

        new SimpleCal();

    }

}

class SimpleCal extends JFrame{

    JButton btn;
    JLabel label, display;
    ButtonGroup group = new ButtonGroup();
    JTextField t1, t2;
    JRadioButton add, sub, mult, div;

    public SimpleCal(){

        label = new JLabel("Simple Calculations");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);

        t1 = new JTextField(25);
        t2 = new JTextField(25);
        t1.setToolTipText("Enter a number");
        t2.setToolTipText("Enter a number");
        add(t1);
        add(t2);

        add = new JRadioButton("Addition");
        sub = new JRadioButton("Subtraction");
        mult = new JRadioButton("Multiplication");
        div = new JRadioButton("Division");
        // Button group will mark all as the same components
        group.add(add);
        group.add(sub);
        group.add(mult);
        group.add(div);
        // Adding the things in sequence will arrange same on the screen
        add(add);
        add(sub);
        add(mult);
        add(div);
        add.setSelected(true);   // To select a radio button by default

        btn = new JButton("Process");
        add(btn);

        // ActionListener will occur when a button or other is clicked
        btn.addActionListener(e -> btn_clicked());

        display = new JLabel("Display");
        display.setToolTipText("Displays Answer");
        display.setHorizontalAlignment(SwingConstants.CENTER);
        float size = 15;
        display.setFont(display.getFont().deriveFont(size));
        add(display);


        // Action Listeners which will respond when clicked enter
        t2.addActionListener(e -> btn_clicked());
        add.addActionListener(e -> btn_clicked());
        sub.addActionListener(e -> btn_clicked());
        mult.addActionListener(e -> btn_clicked());
        div.addActionListener(e -> btn_clicked());


        // Add all the windows settings after the widgets

        setTitle("Simple Calculations");
        setVisible(true);     // Important to set true
        setLayout(new GridLayout(0, 1));
        setSize(350, 350);
        setLocationRelativeTo(null);   // Opens the window in middle of the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void btn_clicked(){

        String num1 = t1.getText();
        String num2 = t2.getText();

        double data;

        try {
            if (add.isSelected()) {
                data = Double.parseDouble(num1) + Double.parseDouble(num2);
            } else if (sub.isSelected()) {
                data = Double.parseDouble(num1) - Double.parseDouble(num2);
            } else if (mult.isSelected()) {
                data = Double.parseDouble(num1) * Double.parseDouble(num2);
            } else {
                data = Double.parseDouble(num1) / Double.parseDouble(num2);
            }

            display.setText(data + "");

        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Enter Only Numbers", "Error", JOptionPane.PLAIN_MESSAGE);
        }

    }

}
