import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Jp {

	public static void main(String[] args){

		new KnowledgeCheck();

	}

}

class KnowledgeCheck{

	JFrame window;
	JLabel score_lbl;
	JTextArea question_display;
	JTextArea prev_ans_display;
	JButton btn1, btn2, btn3, btn4;
	JMenuBar menu_bar;
	JMenu help_menu, settings_menu, about_menu;
	JMenuItem help, open_settings, about_us;
	String data = "", question = "", correct_option = "", answer_check = "";
	int ques_category = 9, correct_option_index = -1;
	long correct_score = 0, wrong_score = 0;
	String ques_difficulty = "";  // easy or medium or hard
	String question_type = "multiple";  //  multiple or boolean
	int ques_category_selected = 0, ques_difficulty_selected = 0, ques_type_selected = 0;
	ArrayList<String> options = new ArrayList<>();
	boolean sound_var = true;

	// Constructor
	public KnowledgeCheck(){

		// Window
		window = new JFrame("Knowledge Check");
		window.setLayout(new BorderLayout());

		// Icon
		ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/icon.png"))); // Icon to be placed near *.class or inside jar
		window.setIconImage(icon.getImage());

		try {
			// Nimbus Theme in Swing
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		}catch (Exception ex){
			JOptionPane.showMessageDialog(null, "Theme Error", "Error", JOptionPane.ERROR_MESSAGE);
		}

		// Title Label
		JLabel title = new JLabel("Knowledge Check");
		title.setFont(new Font("Times New Roman", Font.BOLD, 40));
		title.setOpaque(true);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setForeground(Color.decode("#e6e8eb"));
		title.setBackground(Color.decode("#333333"));

		// Adding title label to the window in the north(top) position
		window.add(title, BorderLayout.NORTH);

		// Display Panel
		JPanel display_panel = new JPanel();
		display_panel.setBackground(Color.decode("#333333"));
		display_panel.setLayout(new BoxLayout(display_panel, BoxLayout.Y_AXIS));
		display_panel.setBorder(new javax.swing.border.EmptyBorder(6, 10, 6, 10));  // Giving padding to the btn_panel

		// Score Display
		score_lbl = new JLabel("Correct: " + correct_score + "                                               Wrong: " + wrong_score);
		score_lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		score_lbl.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		score_lbl.setOpaque(true);
		score_lbl.setHorizontalAlignment(JLabel.CENTER);
		score_lbl.setForeground(Color.decode("#e6e8eb"));
		score_lbl.setBackground(Color.decode("#333333"));
		display_panel.add(score_lbl);
		display_panel.add(Box.createRigidArea(new Dimension(5, 10)));  // Space between the components

		// Question Display
		question_display = new JTextArea("Welcome...");
		question_display.setRows(6);
		question_display.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		question_display.setOpaque(true);
		question_display.setForeground(Color.decode("#e6e8eb"));
		question_display.setBackground(Color.decode("#333333"));
		question_display.setFocusable(false);
		question_display.setLineWrap(true);
		question_display.setWrapStyleWord(true);
		question_display.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));   // Changing cursor to arrow
		display_panel.add(new JScrollPane(question_display));
		display_panel.add(Box.createRigidArea(new Dimension(5, 10)));  // Space between the components

		// Previous Answer Display
		prev_ans_display = new JTextArea("Previous Question With Answer Will Be Displayed Here\n\nStart Answering...");
		prev_ans_display.setRows(2);
		prev_ans_display.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		prev_ans_display.setForeground(Color.decode("#e6e8eb"));
		prev_ans_display.setBackground(Color.decode("#333333"));
		prev_ans_display.setFocusable(false);
		prev_ans_display.setWrapStyleWord(true);
		prev_ans_display.setLineWrap(true);
		prev_ans_display.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));   // Changing cursor to arrow
		display_panel.add(new JScrollPane(prev_ans_display));

		// Adding display panel to the window in the center position
		window.add(display_panel, BorderLayout.CENTER);

		// Button panel
		JPanel btn_panel = new JPanel();
		btn_panel.setBackground(Color.decode("#333333"));

		btn1 = new JButton("Option A");
		btn1.setForeground(Color.decode("#e6e8eb"));
		btn1.setBackground(Color.decode("#333333"));
		btn1.addActionListener(e -> validate(0));
		btn1.setFocusable(false);
		btn_panel.add(btn1);

		btn2 = new JButton("Option B");
		btn2.setForeground(Color.decode("#e6e8eb"));
		btn2.setBackground(Color.decode("#333333"));
		btn2.addActionListener(e -> validate(1));
		btn2.setFocusable(false);
		btn_panel.add(btn2);

		btn3 = new JButton("Option C");
		btn3.setForeground(Color.decode("#e6e8eb"));
		btn3.setBackground(Color.decode("#333333"));
		btn3.addActionListener(e -> validate(2));
		btn3.setFocusable(false);
		btn_panel.add(btn3);

		btn4 = new JButton("Option D");
		btn4.setForeground(Color.decode("#e6e8eb"));
		btn4.setBackground(Color.decode("#333333"));
		btn4.addActionListener(e -> validate(3));
		btn4.setFocusable(false);
		btn_panel.add(btn4);

		window.add(btn_panel, BorderLayout.SOUTH);

		// Menu
		menu_bar = new JMenuBar();

		// Help menu
		help_menu = new JMenu("Help");

		help = new JMenuItem("Help");
		help.addActionListener(e -> showHelp());
		help_menu.add(help);

		menu_bar.add(help_menu);

		// Settings menu
		settings_menu = new JMenu("Settings");

		open_settings = new JMenuItem("Open Settings");
		open_settings.addActionListener(e -> openSettings());
		settings_menu.add(open_settings);

		menu_bar.add(settings_menu);

		// About menu
		about_menu = new JMenu("About");

		about_us = new JMenuItem("About Us");
		about_us.addActionListener(e -> aboutUs());
		about_menu.add(about_us);

		menu_bar.add(about_menu);

		// Adding menu bar to window
		window.setJMenuBar(menu_bar);

		// Set all components not focusable to make this window KeyListener work
		window.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int key = e.getKeyChar();

				// Binding to 1, 2, 3, 4, A. B, C, D, a, b, c, d keys
				if(key == '1' || key == 'A' || key == 'a'){
					validate(0);
				}else if(key == '2' || key == 'B' || key == 'b'){
					validate(1);
				}else if(key == '3' || key == 'C' || key == 'c'){
					if(question_type.equals("multiple")){
						validate(2);
					}else{
						JOptionPane.showMessageDialog(window, "You Need To Use 1, 2 (OR) A, B Buttons When " +
								"True/False Type Is Selected", "Warning", JOptionPane.WARNING_MESSAGE);
					}
				}else if(key == '4' || key == 'D' || key == 'd'){
					if(question_type.equals("multiple")) {
						validate(3);
					}else{
						JOptionPane.showMessageDialog(window, "You Need To Use 1, 2 (OR) A, B Buttons When " +
								"True/False Type Is Selected", "Warning", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});

		// Window settings
		window.setVisible(true);
		window.setSize(620, 530);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Loads a question and displays the question and loads next question, argument 0 means no and 1 means display content
		new LoadQuestion(1).start();

	}

	// KnowledgeCheck class methods

	private void displayAndLoadNextQuestion(){

		correct_option_index = -1;  // Correct option index is going to be -1 when the question is not loaded completely

		// Previous answer display
		if(!question.equals("")){
			answer_check = question + "\n(Ans: " + correct_option + ")";
			prev_ans_display.setText("");
			prev_ans_display.setText(answer_check);
		}

		int start, end;

		// Question substring
		start = data.indexOf("question\":") + 11;
		end = data.indexOf("\",\"correct_answer", start);
		question = data.substring(start, end);
		question_display.setText(question + "\n\n");  // Displays question

		// Correct option
		start = data.indexOf("\"correct_answer\":") + 18;
		end = data.indexOf("\",", start);
		correct_option = data.substring(start, end);

		// Other options
		start = data.indexOf("\"incorrect_answers\":[\"") + 22;
		end = data.indexOf("\"]", start);
		String other_options = data.substring(start, end);

		options.clear();  // Clears the list every time
		options.add(correct_option);
		for(String str: other_options.split("\",\"")){
			options.add(str);
		}

		Collections.shuffle(options);
		char c = 65;  // c variable has value 65 which is 'A' ASCII value, and it gets incremented to show 'B', 'C', 'D'
		int len = options.size();
		int last_index = len-1;
		for(int i=0; i<len; i++){

			if(options.get(i).equals(correct_option))
				correct_option_index = i;

			if(i == last_index){
				question_display.append(c + ".) " + options.get(i));
			}else{
				question_display.append(c + ".) " + options.get(i) + "\n");
			}

			c++;

		}

		question_display.setCaretPosition(0);  // Shows the top portion of the question_display

		// Loads the next question and 0 means don't display loaded question on the question_display
		new LoadQuestion(0).start();

	}

	private void validate(int num){

		if(correct_option_index != -1){
			if(num == correct_option_index){
				correct_score++;
				if(sound_var){
					playAudio("sounds/correct.wav");
				}
			}else{
				wrong_score++;
				if(sound_var){
					playAudio("sounds/wrong.wav");
				}
			}

			score_lbl.setText("Correct: " + correct_score + "                                               Wrong: " + wrong_score);
			displayAndLoadNextQuestion();
		}else{
			JOptionPane.showMessageDialog(window, "Question is not yet loaded, wait till it loads",
					"Warning", JOptionPane.WARNING_MESSAGE);
		}

	}

	private void playAudio(String url){

		try{
			// Objects... will fetch the files from jar as well. If you're not using jar try to keep those files in the same folder in which you have class
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(url)));
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.start();
		}catch(Exception e){
			sound_var = false;
			JOptionPane.showMessageDialog(window, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void openSettings(){

		new Settings();

	}

	private void showHelp(){

		JOptionPane.showMessageDialog(window, "Knowledge Check Is A Program Which Will Display Questions " +
				"Related To Various Categories.\n" +
				"Settings : You Can Turn Sound On Or Off, Change Text Size And You Can Select (Category, Difficulty," +
				"Type) Of The Questions By Clicking On The Settings Menu.\n" +
				"Selecting Options : You Can Either Click On The Option Buttons (OR) 1, 2, 3, 4 or " +
				"A, B, C, D Keys To Select An Option.\n" +
				"Knowledge Check Needs Internet Connection.\n", "Help", JOptionPane.PLAIN_MESSAGE);

	}

	private void aboutUs(){

		JOptionPane.showMessageDialog(window, "Knowledge Check Version 1.0\nDeveloped By Nikhil", "About Us", JOptionPane.PLAIN_MESSAGE);

	}


	// Inner Classes


	// LoadQuestion inner class and it extends Thread class
	class LoadQuestion extends Thread{
		// This class gets the data from internet it takes some time that's why we are using thread

		String link;
		int var;

		public LoadQuestion(int var){

			this.var = var;

			if(ques_difficulty.equals("")){

				if(ques_category_selected == 0)
					link = "https://opentdb.com/api.php?amount=1" + "&type=" + question_type;  // Getting the data from Open Trivia Database
				else
					link = "https://opentdb.com/api.php?amount=1&category=" + ques_category + "&type=" + question_type;  // Getting the data from Open Trivia Database

			}else{
				link = "https://opentdb.com/api.php?amount=1&category=" + ques_category + "&difficulty=" + ques_difficulty + "&type=" + question_type;  // Getting the data from Open Trivia Database
			}

		}

		// LoadQuestion class methods

		public void run(){

			try{
				// Used in loop to make a string
				StringBuilder str = new StringBuilder();

				// Establishing a connection
				URL url = new URL(link);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();

				// Creating an object to the data
				InputStream stream = connection.getInputStream();

				int ch;
				// Reading the data from the connection through inputstream object
				while((ch = stream.read()) != -1){
					str.append((char)ch);
				}


				String raw_data = str.toString();

				// Converts non-unicode characters to unicode like \\u2013 to \u2013
				try{
					Pattern unicode = Pattern.compile("\\\\u(.{4})");
					Matcher matcher = unicode.matcher(raw_data);
					StringBuffer sb = new StringBuffer();
					while (matcher.find()) {
						int code = Integer.parseInt(matcher.group(1), 16);
						matcher.appendReplacement(sb, new String(Character.toChars(code)));
					}
					matcher.appendTail(sb);
					raw_data = sb.toString();
				}catch(Exception e){
					JOptionPane.showMessageDialog(window, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				}

				// Unescaping the HTML entities
				try {
					HTMLDocument doc = new HTMLDocument();
					new HTMLEditorKit().read( new StringReader( "<html><body>" + raw_data), doc, 0 );
					raw_data = doc.getText( 1, doc.getLength());
				} catch( Exception ex ) {
					raw_data = raw_data;
				}

				// This replaces \/ to /
				raw_data = raw_data.replace("\\/", "/");

				data = raw_data;

				if(var == 1){
					displayAndLoadNextQuestion();
				}

			}catch (java.net.UnknownHostException e){
				question_display.setText("Check Your Internet Connection And Restart The Program");
				correct_option_index = -1;  // Correct option index is going to be -1 when the question is not loaded completely
			}catch(Exception e){
				question_display.setText("Check Your Internet Connection And Restart The Program");
				JOptionPane.showMessageDialog(window, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				correct_option_index = -1;  // Correct option index is going to be -1 when the question is not loaded completely
			}

		}

	}


	// Settings inner class and openSettings methods creates object of this class
	class Settings{

		JFrame w;
		JComboBox<String> categories_cb, difficulty_cb, type_cb;
		JCheckBox sound_checkbox;
		JButton change_btn, text_size_btn;

		Settings(){

			// Window
			w = new JFrame("Knowledge Check Settings");
			w.setLayout(new BorderLayout());
			ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/icon.png")));
			w.setIconImage(icon.getImage());

			// Program manager title
			JLabel title = new JLabel("Knowledge Check Settings");
			title.setFont(new Font("Times New Roman", Font.PLAIN, 29));
			title.setOpaque(true);
			title.setHorizontalAlignment(JLabel.CENTER);
			title.setForeground(Color.decode("#e6e8eb"));
			title.setBackground(Color.decode("#333333"));

			// Adding title label to the settings window in the north(top) position
			w.add(title, BorderLayout.NORTH);

			// Buttons panel
			JPanel components_panel = new JPanel();
			components_panel.setBackground(Color.decode("#333333"));
			components_panel.setLayout(new BoxLayout(components_panel, BoxLayout.Y_AXIS));
			components_panel.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));  // Giving padding to the btn_panel

			// Categories combo box with its configurations and categories_array
			String[] categories_array = {"Categories", "General Knowledge", "Entertainment: Books",
					"Entertainment: Film", "Entertainment: Music", "Entertainment: Musicals & Theatres",
					"Entertainment: Television", "Entertainment: Video Games", "Entertainment: Board Games",
					"Science & Nature", "Science: Computers", "Science: Mathematics", "Mythology", "Sports",
					"Geography", "History", "Politics", "Art", "Celebrities", "Animals", "Vehicles",
					"Entertainment: Comics", "Science: Gadgets", "Entertainment: Japanese Anime & Manga",
					"Entertainment: Cartoon & Animations"};
			categories_cb = new JComboBox<>(categories_array);
			categories_cb.setAlignmentX(Component.CENTER_ALIGNMENT);
			categories_cb.setForeground(Color.decode("#e6e8eb"));
			categories_cb.setBackground(Color.decode("#333333"));
			categories_cb.setSelectedIndex(ques_category_selected);
			components_panel.add(categories_cb);
			components_panel.add(Box.createRigidArea(new Dimension(5, 10)));  // Space between the components

			// Difficulty combo box with its configurations and difficulty_array
			String[] difficulty_array = {"Difficulty", "Easy", "Medium", "Hard", "Any Difficulty"};
			difficulty_cb = new JComboBox<>(difficulty_array);
			difficulty_cb.setAlignmentX(Component.CENTER_ALIGNMENT);
			difficulty_cb.setForeground(Color.decode("#e6e8eb"));
			difficulty_cb.setBackground(Color.decode("#333333"));
			difficulty_cb.setSelectedIndex(ques_difficulty_selected);
			components_panel.add(difficulty_cb);
			components_panel.add(Box.createRigidArea(new Dimension(5, 10)));  // Space between the components

			// Type combo box with its configurations and type_array
			String[] type_array = {"Type", "Multiple Choice", "True/False"};
			type_cb = new JComboBox<>(type_array);
			type_cb.setAlignmentX(Component.CENTER_ALIGNMENT);
			type_cb.setForeground(Color.decode("#e6e8eb"));
			type_cb.setBackground(Color.decode("#333333"));
			type_cb.setSelectedIndex(ques_type_selected);
			components_panel.add(type_cb);
			components_panel.add(Box.createRigidArea(new Dimension(5, 10)));  // Space between the components

			// Change display text size button and its configurations
			text_size_btn = new JButton("Change Display Text Size");
			text_size_btn.setAlignmentX(JButton.CENTER_ALIGNMENT);
			text_size_btn.setForeground(Color.decode("#e6e8eb"));
			text_size_btn.setBackground(Color.decode("#333333"));
			text_size_btn.addActionListener(e -> changeTextSize());
			components_panel.add(text_size_btn);
			components_panel.add(Box.createRigidArea(new Dimension(5, 16)));  // Space between the components

			// Sound check box and its configurations
			sound_checkbox = new JCheckBox("Sound");
			sound_checkbox.setAlignmentX(Component.CENTER_ALIGNMENT);
			sound_checkbox.setForeground(Color.decode("#e6e8eb"));
			sound_checkbox.setBackground(Color.decode("#333333"));
			sound_checkbox.addActionListener(e -> soundToggle());
			components_panel.add(sound_checkbox);
			components_panel.add(Box.createRigidArea(new Dimension(5, 10)));  // Space between the components

			// Sound checkbox will be selected upon the sound_var
			if(sound_var)
				sound_checkbox.setSelected(true);
			else
				sound_checkbox.setSelected(false);

			// Change button and its color, configurations
			change_btn = new JButton("Apply Settings");
			change_btn.setAlignmentX(JButton.CENTER_ALIGNMENT);
			change_btn.setForeground(Color.decode("#e6e8eb"));
			change_btn.setBackground(Color.decode("#993e1c"));
			change_btn.addActionListener(e -> change());
			components_panel.add(change_btn);

			// Adding component panel to settings window in the center position
			w.add(components_panel, BorderLayout.CENTER);

			// Window settings
			w.setVisible(true);
			w.setSize(400, 350);
			w.setLocationRelativeTo(null);
			w.setResizable(false);
			w.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		}

		// Settings class methods

		private void change(){

			// If question is not loaded then we cannot make any changes
			if(correct_option_index == -1){
				JOptionPane.showMessageDialog(w, "Cannot apply changes until present question is loaded",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Count variable will keep a count that settings are changed or not
			int selected_count = 0, index;

			// index variable will contain the selected JComboBox value

			index = categories_cb.getSelectedIndex();
			if(index != 0 && index != ques_category_selected){
				// Categories start with index 9 and categories in the category_list start with index 1 as 1st one is ComboBox placeholder
				ques_category = index + 8;
				ques_category_selected = index;
				selected_count++;
			}

			index = difficulty_cb.getSelectedIndex();
			if(index != 0 && index != ques_difficulty_selected){
				if(difficulty_cb.getItemAt(index).equals("Any Difficulty"))
					ques_difficulty = "";
				else
					ques_difficulty = difficulty_cb.getItemAt(index).toLowerCase();

				ques_difficulty_selected = index;
				selected_count++;
			}

			index = type_cb.getSelectedIndex();
			if(index != 0 && index != ques_type_selected){
				if(type_cb.getItemAt(index).equals("Multiple Choice")){
					question_type = "multiple";
					btn3.setEnabled(true);  // Multiple and boolean type button settings
					btn4.setEnabled(true);  // Multiple and boolean type button settings
				}else{
					question_type = "boolean";
					btn3.setEnabled(false);  // Multiple and boolean type button settings
					btn4.setEnabled(false);  // Multiple and boolean type button settings
				}
				ques_type_selected = index;
				selected_count++;
			}

			if(selected_count > 0){
				// Loads a question
				new LoadQuestion(1).start();
				question_display.setText("Wait a moment...");
				correct_option_index = -1;  // Correct option index is going to be -1 when the question is not loaded completely
			}

			w.dispose();  // Closes the settings window

		}

		private void changeTextSize(){

			// This method will change the size of the question_display and prev_ans_display
			String size_str = JOptionPane.showInputDialog(w, "Enter Text Size (22-50): ",
					"Knowledge Check", JOptionPane.PLAIN_MESSAGE);

			// If cancel is pressed JOptionPane will be closed and this method will be exited
			if(size_str == null)
				return;

			// If nothing is entered and pressed ok or non digit are there in the string then this if condition will be executed
			if(size_str.equals("") || size_str.matches("\\D+")){
				JOptionPane.showMessageDialog(w, "Enter a valid number",
						"Error", JOptionPane.ERROR_MESSAGE);
			}else{
				int size = Integer.parseInt(size_str);

				if(size<22 || size>50) {
					JOptionPane.showMessageDialog(w, "Enter text sizes only in this range (22-50)",
							"Error", JOptionPane.ERROR_MESSAGE);
				}else{
					question_display.setFont(new Font("Times New Roman", Font.PLAIN, size));
					prev_ans_display.setFont(new Font("Times New Roman", Font.PLAIN, size-4));
				}

			}

		}

		private void soundToggle(){

			if(sound_var){
				sound_var = false;
				sound_checkbox.setSelected(false);
			}else{
				sound_var = true;
				sound_checkbox.setSelected(true);
			}

		}


	}


}
