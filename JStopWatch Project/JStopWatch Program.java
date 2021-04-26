import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Jp{

	public static void main(String[] args){

		new StopWatch();

	}

}

class StopWatch{

	JFrame frame = new JFrame("JStopWatch");
	JLabel display;
	JButton start_button, reset_button, lap_button;
	JTextArea lap_display;
	long milli_seconds = 0, seconds = 0, minutes = 0, hours = 0;
	int lap_count = 0;
	String time = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milli_seconds).replace(" ", "");
	boolean is_started = false;
	String lap_str = "";

	Timer timer = new Timer(0, e -> {

		try {
			Thread.sleep(1);
		}catch (InterruptedException ex){
			ex.printStackTrace();
		}
		milli_seconds += 1;
		seconds = (milli_seconds/1000) % 60;
		minutes = (milli_seconds/60000) % 60;
		hours = (milli_seconds/3600000);
		time = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milli_seconds%1000);
		display.setText(time);

	});

	StopWatch(){

		try {
			// Nimbus Theme in Swing
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		}catch (Exception ex){
			JOptionPane.showMessageDialog(null, "Theme Error", "Error", JOptionPane.ERROR_MESSAGE);
		}

		// Label
		display = new JLabel();
		display.setFont(new Font("Times New Roman", Font.PLAIN, 76));
		display.setBorder(BorderFactory.createBevelBorder(1));
		display.setHorizontalAlignment(JTextField.CENTER);
		display.setOpaque(true);
		display.setText(time);

		// Buttons
		start_button = new JButton("Start");
		start_button.setFont(new Font("Harlow Solid Italic", Font.PLAIN, 25));

		reset_button = new JButton("Reset");
		reset_button.setFont(new Font("Harlow Solid Italic", Font.PLAIN, 25));

		lap_button = new JButton("Lap");
		lap_button.setFont(new Font("Harlow Solid Italic", Font.PLAIN, 25));

		// Action Listeners
		start_button.addActionListener(e -> start());
		reset_button.addActionListener(e -> reset());
		lap_button.addActionListener(e -> lap());

		// Text Area
		lap_display = new JTextArea();
		lap_display.setColumns(37);
		lap_display.setRows(6);
		lap_display.setWrapStyleWord(true);
		lap_display.setFocusable(false);
		lap_display.setBackground(new Color(215, 218, 223));
		lap_display.setFont(new Font("Times New Roman", Font.BOLD, 13));

		// Adding widgets to frame
		frame.add(display);
		frame.add(start_button);
		frame.add(reset_button);
		frame.add(lap_button);
		frame.add(lap_display);
		frame.add(new JScrollPane(lap_display), BorderLayout.CENTER);

		// Window settings
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(425, 300);
		frame.setLayout(new FlowLayout());
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	private void start(){

		timer.start();
		if(! is_started){
			is_started = true;
			start_button.setText("Stop");
		}else{
			stop();
		}

	}

	private void stop(){

		timer.stop();
		is_started = false;
		start_button.setText("Start");

	}

	private void reset(){

		stop();
		milli_seconds = seconds = minutes = hours = 0;
		time = String.format("%02d:%02d:%02d:%03d", hours, minutes, seconds, milli_seconds);
		display.setText(time);

	}

	private void lap(){

		lap_count += 1;
		lap_str += lap_count + ".  " + time + "\n";
		lap_display.setText(lap_str);

	}

}
