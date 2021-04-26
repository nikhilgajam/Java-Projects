import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Jp{

	public static void main(String[] args){

		new Clock();

	}

}

class Clock{

	// Creating a frame
	JFrame frame = new JFrame("Clock");

	// Widgets
	SimpleDateFormat time_format, day_format, date_format;
	JLabel time_label, day_label, date_label;
	String time, day, date;

	Clock(){

		// Labels
		time_label = new JLabel();
		time_label.setFont(new Font("Bell MT", Font.PLAIN, 80));
		time_label.setForeground(new Color(218, 220, 224));
		time_label.setBackground(new Color(53, 54, 58));
		time_label.setOpaque(true);

		day_label = new JLabel();
		day_label.setFont(new Font("Bell MT", Font.PLAIN, 50));
		day_label.setForeground(Color.BLACK);

		date_label = new JLabel();
		date_label.setFont(new Font("Bell MT", Font.PLAIN, 50));
		date_label.setForeground(Color.BLACK);

		// Adding the labels to frame
		frame.add(time_label);
		frame.add(day_label);
		frame.add(date_label);

		// Time formats
		time_format = new SimpleDateFormat("hh:mm:ss a");
		day_format = new SimpleDateFormat("EEEE");
		date_format = new SimpleDateFormat("MMMMM dd, yyyy");

		// Window Settings
		frame.setSize(430, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLayout(new FlowLayout());
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		// update() will update the time after every second i.e 1000 millis
		update();

	}

	private void update(){

		while(true){

			time = time_format.format(Calendar.getInstance().getTime());
			time_label.setText(time);

			day = day_format.format(Calendar.getInstance().getTime());
			day_label.setText(day);

			date = date_format.format(Calendar.getInstance().getTime());
			date_label.setText(date);

			try {
				Thread.sleep(1000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}

		}

	}

}
