import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Random;
import javax.sound.sampled.*;

public class Jp{

	public static void main(String[] args){

		new NumberGame();

	}

}


class NumberGame{


	// Window
	JFrame frame = new JFrame("Number Game");

	// Widgets
	JLabel title_label, score_label, question_label, range_label, message_label;
	JButton check_button;
	JTextField ans_box, range_box;
	JPanel button_panel = new JPanel();
	JPanel range_panel = new JPanel();

	// Random object
	Random rand = new Random();

	// Variables
	char[] operators = {'+', '-', '*', '/', '%', '^'};
	int correct = 0, mistake = 0;
	String ques_str;
	double range_num = 0.0, num1 = 0.0, num2 = 0.0, ans = 0.0;
	char op = ' ';
	boolean boolean_value;

	NumberGame(){

		try {
			// Nimbus Theme in Swing
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		}catch (Exception ex){
			JOptionPane.showMessageDialog(null, "Theme Error", "Error", JOptionPane.ERROR_MESSAGE);
		}

		// Title
		title_label = new JLabel("Number Game");
		title_label.setFont(new Font("Algerian", Font.PLAIN, 60));

		// Score
		score_label = new JLabel("Correct: 0   ________________   Mistake: 0");
		score_label.setFont(new Font("Bell MT", Font.PLAIN, 25));

		// Question label and ans box
		question_label = new JLabel();
		question_label.setFont(new Font("Verdana", Font.PLAIN, 50));
		ans_box = new JTextField();
		ans_box.setFont(new Font("Verdana", Font.PLAIN, 50));
		ans_box.addActionListener(e -> answer());
		ans_box.setColumns(3);

		// Message label and check button
		message_label = new JLabel("Start Answering");
		message_label.setFont(new Font("Bell MT", Font.BOLD, 60));
		check_button = new JButton("Check");
		check_button.addActionListener(e -> answer());
		button_panel.setLayout(new GridLayout(2, 1));
		button_panel.add(message_label);
		button_panel.add(check_button);
		button_panel.setSize(50, 20);

		// Range label and range box
		range_label = new JLabel("Range: ");
		range_label.setFont(new Font("Arial", Font.PLAIN, 20));
		range_box = new JTextField("100");
		range_box.setFont(new Font("Arial", Font.PLAIN, 20));
		range_box.addActionListener(e -> evaluate());
		range_box.setColumns(8);
		range_panel.add(range_label);
		range_panel.add(range_box);
		range_panel.setLayout(new GridLayout(1, 2, 0, 0));

		// Adding widgets to frame
		frame.add(title_label);
		frame.add(score_label);
		frame.add(question_label);
		frame.add(ans_box);
		frame.add(button_panel);
		frame.add(range_panel);

		// Window settings
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(521, 418);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setLayout(new FlowLayout());
		frame.setVisible(true);

		// Displaying question and populating variables
		evaluate();

	}

	private void evaluate(){

		try{
			range_num = Integer.parseInt(range_box.getText());
			if(range_num < 9 || range_num >100){
				throw new Exception("Value Error");
			}
		}catch(Exception ex){
			JOptionPane.showMessageDialog(null, "Enter The Numbers In Range (9 to 100) Only", "Value Error", JOptionPane.ERROR_MESSAGE);
			range_box.setText("100");
			evaluate();
		}

		num1 = rand.nextInt((int)range_num);       // Numerator
		num2 = rand.nextInt((int)range_num) + 1;   // Denominator, We don't want denominator as 0 that's why we added 1
		// 6 operators are there in operators array that's why we use this, rand.nextInt(6)
		op = operators[rand.nextInt(6)];
		ans = 0;

		ques_str = String.format("%d %c %d = ", (int)num1, op, (int)num2);
		question_label.setText(ques_str);

		if(op == '+'){
			ans = num1 + num2;
		}else if(op == '-'){
			ans = num1 - num2;
		}else if(op == '*'){
			ans = num1 * num2;
		}else if(op == '/'){
			// Rounding to two decimals
			ans = Math.round((num1 / num2)*100.0)/100.0;
		}else if(op == '%'){
			ans = num1 % num2;
		}else if(op == '^'){
			// Using small power values because large values will be complex
			num1 = rand.nextInt(9);
			num2 = rand.nextInt(3) ;
			ques_str = String.format("%d %c %d = ", (int)num1, op, (int)num2);
			question_label.setText(ques_str);
			ans = Math.pow(num1, num2);
		}
		ans_box.requestFocus();   // Selecting the answer box

	}

	private void answer(){

		try{
			boolean_value = (Double.parseDouble(ans_box.getText())) == ans;
			if(boolean_value){
				correct++;
				sound(1);
			}else{
				mistake++;
				sound(0);
			}

			message_label.setText(ques_str + ans);
			score_label.setText(String.format("Correct: %d   ________________   Mistake: %d", correct, mistake));
			ans_box.setText("");
			ans_box.requestFocus();
			evaluate();   // To display new question
		}catch (Exception e){
			JOptionPane.showMessageDialog(null, "Enter Only Numbers", "Value Error", JOptionPane.ERROR_MESSAGE);
			ans_box.setText("");
		}

	}

	private void sound(int track){

		String str = "";

		if(track == 1)
			str = "sounds/correct.wav";
		else if(track == 0)
			str = "sounds/mistake.wav";

		try{
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(str)));
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.start();
		}catch(Exception e){
			e.printStackTrace();
		}

	}


}
