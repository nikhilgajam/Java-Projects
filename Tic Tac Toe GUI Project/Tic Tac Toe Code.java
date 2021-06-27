import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.Random;
import javax.sound.sampled.*;

public class Jp{

	public static void main(String[] args) {

		new TicTacToe();

	}

}



class TicTacToe{


	// Window
	JFrame window = new JFrame("Tic Tac Toe");

	// Widgets
	JLabel display;
	JPanel panel = new JPanel();
	JButton[] buttons = new JButton[9];
	JMenuBar menu_bar;
	JMenu game_menu, help_menu;
	JCheckBoxMenuItem comp_m_item, human_m_item, sound_m_item;
	JMenuItem help_m_item, about_us_m_item;

	// Random
	Random rand = new Random();

	// Variables
	char[] boxes = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
	char get;  // Stores the character in buttons array at specified index
	int turn = rand.nextInt(2) + 1;   // turn == 1 => X & turn == 2 => O
	int checks = 0;  // Stores the value returned by check method
	int comp;  // Stores the
	int play_count = 0, x_score = 0, o_score = 0, tie_count = 0;
	boolean computer_plays = true;
	boolean sound_on = true;


	TicTacToe(){

		try {
			// Nimbus Theme in Swing
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		}catch (Exception ex){
			playSounds("sounds/Windows XP Critical Stop.wav");
			JOptionPane.showMessageDialog(null, "Theme Error", "Error", JOptionPane.ERROR_MESSAGE);
		}

		// Binding number keys
		window.addKeyListener(new KeyAdapter(){
			@Override
			public void keyTyped(KeyEvent e){
				// Number 49 is 1 number in keyboard and 50 is 2, 51 is 3 ...
				if(e.getKeyChar() == 49){ clicked(0);
				}else if(e.getKeyChar() == 50){ clicked(1);
				}else if(e.getKeyChar() == 51){ clicked(2);
				}else if(e.getKeyChar() == 52){ clicked(3);
				}else if(e.getKeyChar() == 53){ clicked(4);
				}else if(e.getKeyChar() == 54){ clicked(5);
				}else if(e.getKeyChar() == 55){ clicked(6);
				}else if(e.getKeyChar() == 56){ clicked(7);
				}else if(e.getKeyChar() == 57){ clicked(8);
				}
			}
		});

		// Display label
		display = new JLabel("TIC TAC TOE");
		display.setOpaque(true);
		display.setBackground(Color.BLACK);
		display.setForeground(Color.lightGray);
		display.setHorizontalAlignment(JLabel.CENTER);
		display.setFont(new Font("Stencil", Font.PLAIN, 65));

		// Button panel
		panel.setLayout(new GridLayout(3, 3));

		// Buttons
		for(int i=0; i<9; i++){
			buttons[i] = new JButton(" ");
			buttons[i].setFont(new Font("MV Boli", Font.BOLD, 110));
			buttons[i].setFocusable(false);
			panel.add(buttons[i]);
		}
		// Adding action listeners
		buttons[0].addActionListener(e -> clicked(0));
		buttons[1].addActionListener(e -> clicked(1));
		buttons[2].addActionListener(e -> clicked(2));
		buttons[3].addActionListener(e -> clicked(3));
		buttons[4].addActionListener(e -> clicked(4));
		buttons[5].addActionListener(e -> clicked(5));
		buttons[6].addActionListener(e -> clicked(6));
		buttons[7].addActionListener(e -> clicked(7));
		buttons[8].addActionListener(e -> clicked(8));

		// Menu
		menu_bar = new JMenuBar();
		game_menu = new JMenu("Game");
		help_menu = new JMenu("Help");

		comp_m_item = new JCheckBoxMenuItem("Play With Computer");
		comp_m_item.setSelected(true);
		comp_m_item.addActionListener(e -> toggleMode());

		human_m_item = new JCheckBoxMenuItem("Play With Human");
		human_m_item.addActionListener(e -> toggleMode());

		help_m_item = new JMenuItem("Help");
		help_m_item.addActionListener(e -> JOptionPane.showMessageDialog(null,
				"1. You can play this game in two modes play with computer mode / play with human mode\n" +
						"click on the game menu to access those settings.\n\n" +
						"2. This game randomly selects the starting move.\n\n" +
						"3. You can turn sounds on/off by clicking on the sounds check box in game menu.\n\n" +
						"4. You can press keyboard numbers (1 to 9) to select any particular box without using mouse.\n\n",
				"Help", JOptionPane.PLAIN_MESSAGE));

		about_us_m_item = new JMenuItem("About Us");
		about_us_m_item.addActionListener(e -> JOptionPane.showMessageDialog(null,
				"Tic Tac Toe\nVersion 1.0\nDeveloped By Nikhil",
				"About Us", JOptionPane.PLAIN_MESSAGE));

		sound_m_item = new JCheckBoxMenuItem("Sounds");
		sound_m_item.setSelected(true);
		sound_m_item.addActionListener(e -> toggleSound());

		// Adding to menu
		menu_bar.add(game_menu);
		menu_bar.add(help_menu);

		game_menu.add(comp_m_item);
		game_menu.add(human_m_item);

		help_menu.add(help_m_item);

		help_menu.addSeparator();

		help_menu.add(about_us_m_item);

		game_menu.addSeparator();

		game_menu.add(sound_m_item);

		// Adding to window
		window.setJMenuBar(menu_bar);
		window.add(display, BorderLayout.NORTH);
		window.add(panel, BorderLayout.CENTER);

		// Window Settings
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon img = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/icon.png"))); // Icon
		window.setIconImage(img.getImage());
		window.setSize(456, 496);
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		// If computer starts the game then that move will be displayed with this code
		if(computer_plays && turn == 2)
			validates(-1);

	}

	private void clicked(int index){
		// When ever you click a button this method will be executed
		playSounds("sounds/Windows XP Start.wav");
		get = boxes[index] = buttons[index].getText().charAt(0);  // Specified button array text will be stored in both get variable and boxes array
		if(get == ' '){
			// Shows the turn if you enable play with human mode
			if(!computer_plays)
				showTurn('O', 'X');

			// Validates the move
			validates(index);

			// Checks that who won
			checks = check();
			if(checks == 1)
				wantToContinue();

		}else{
			playSounds("sounds/Windows XP Exclamation.wav");
			JOptionPane.showMessageDialog(null, "This Box Is Already Occupied Try Another One", "Tic Tac Toe", JOptionPane.WARNING_MESSAGE);
		}

		if(computer_plays && turn == 2)
			validates(-1);
	}

	private void putX(int index){
		// X will be placed in boxes array and buttons
		boxes[index] = 'X';
		buttons[index].setForeground(Color.RED);
		buttons[index].setText("X");
		turn = 2;
	}

	private void putO(int index){
		// O will be placed in boxes array and buttons
		boxes[index] = 'O';
		buttons[index].setForeground(Color.BLUE);
		buttons[index].setText("O");
		turn = 1;
	}

	private void showTurn(char p, char q){
		// If you select human mode then it shows the who's turn and printing it with which ever way we want
		if(turn == 1)
			display.setText(p + " Turn");
		else
			display.setText(q + " Turn");
	}

	private void validates(int index){
		// Validates and executes the code according to turn and mode
		if(turn == 1){

			if(boxes[index] == ' '){
				boxes[index] = 'X';
				putX(index);
			}

		}else{

			if(computer_plays){
				computer_moves();
			}else{
				if(boxes[index] == ' '){
					boxes[index] = 'O';
					putO(index);
				}
			}

		}
		// Keeps a count of how many times this method got executed
		play_count++;
	}

	private void computer_moves(){
		// Computer makes a move with this code
		if(boxes[0] == 'X' && boxes[1] == 'X' && boxes[2] == ' '){
			putO(2);
		}else if(boxes[0] == ' ' && boxes[1] == 'X' && boxes[2] == 'X'){
			putO(0);
		}else if(boxes[0] == 'X' && boxes[1] == ' ' && boxes[2] == 'X'){
			putO(1);
		}else if(boxes[3] == 'X' && boxes[4] == 'X' && boxes[5] == ' '){
			putO(5);
		}else if(boxes[3] == ' ' && boxes[4] == 'X' && boxes[5] == 'X'){
			putO(3);
		}else if(boxes[3] == 'X' && boxes[4] == ' ' && boxes[5] == 'X'){
			putO(4);
		}else if(boxes[6] == 'X' && boxes[7] == 'X' && boxes[8] == ' '){
			putO(8);
		}else if(boxes[6] == ' ' && boxes[7] == 'X' && boxes[8] == 'X'){
			putO(6);
		}else if(boxes[6] == 'X' && boxes[7] == ' ' && boxes[8] == 'X'){
			putO(7);
		}else if(boxes[0] == 'X' && boxes[4] == 'X' && boxes[8] == ' '){
			putO(8);
		}else if(boxes[0] == ' ' && boxes[4] == 'X' && boxes[8] == 'X'){
			putO(0);
		}else if(boxes[0] == 'X' && boxes[4] == ' ' && boxes[8] == 'X'){
			putO(4);
		}else if(boxes[2] == 'X' && boxes[4] == 'X' && boxes[6] == ' '){
			putO(6);
		}else if(boxes[2] == ' ' && boxes[4] == 'X' && boxes[6] == 'X'){
			putO(2);
		}else if(boxes[2] == 'X' && boxes[4] == ' ' && boxes[6] == 'X'){
			putO(4);
		}else if(boxes[0] == 'X' && boxes[3] == 'X' && boxes[6] == ' '){
			putO(6);
		}else if(boxes[0] == ' ' && boxes[3] == 'X' && boxes[6] == 'X'){
			putO(0);
		}else if(boxes[0] == 'X' && boxes[3] == ' ' && boxes[6] == 'X'){
			putO(3);
		}else if(boxes[1] == 'X' && boxes[4] == 'X' && boxes[7] == ' '){
			putO(7);
		}else if(boxes[1] == ' ' && boxes[4] == 'X' && boxes[7] == 'X'){
			putO(1);
		}else if(boxes[1] == 'X' && boxes[4] == ' ' && boxes[7] == 'X'){
			putO(4);
		}else if(boxes[2] == 'X' && boxes[5] == 'X' && boxes[8] == ' '){
			putO(8);
		}else if(boxes[2] == ' ' && boxes[5] == 'X' && boxes[8] == 'X'){
			putO(2);
		}else if(boxes[2] == 'X' && boxes[5] == ' ' && boxes[8] == 'X'){
			putO(5);
		}else{
			comp = rand.nextInt(9);
			while(boxes[comp] != ' '){
				comp = rand.nextInt(9);
			}
			putO(comp);
		}
		// If play_count is 9 and computer mode is on then it executes this code to get the data that who won
		if(computer_plays && play_count == 9)
			clicked(-1);
	}

	private int check(){
		// This code will be executed to know that who won the game

		// X wins
		if(boxes[0] == 'X' && boxes[1] == 'X' && boxes[2] == 'X'){
			winner_settings(0, 1, 2, 'X');
			return 1;
		}else if(boxes[3] == 'X' && boxes[4] == 'X' && boxes[5] == 'X'){
			winner_settings(3, 4, 5, 'X');
			return 1;
		}else if(boxes[6] == 'X' && boxes[7] == 'X' && boxes[8] == 'X'){
			winner_settings(6, 7, 8, 'X');
			return 1;
		}else if(boxes[0] == 'X' && boxes[3] == 'X' && boxes[6] == 'X'){
			winner_settings(0, 3, 6, 'X');
			return 1;
		}else if(boxes[1] == 'X' && boxes[4] == 'X' && boxes[7] == 'X'){
			winner_settings(1, 4, 7, 'X');
			return 1;
		}else if(boxes[2] == 'X' && boxes[5] == 'X' && boxes[8] == 'X'){
			winner_settings(2, 5, 8, 'X');
			return 1;
		}else if(boxes[0] == 'X' && boxes[4] == 'X' && boxes[8] == 'X'){
			winner_settings(0, 4, 8, 'X');
			return 1;
		}else if(boxes[2] == 'X' && boxes[4] == 'X' && boxes[6] == 'X'){
			winner_settings(2, 4, 6, 'X');
			return 1;
		}

		// O wins
		if(boxes[0] == 'O' && boxes[1] == 'O' && boxes[2] == 'O'){
			winner_settings(0, 1, 2, 'O');
			return 1;
		}else if(boxes[3] == 'O' && boxes[4] == 'O' && boxes[5] == 'O'){
			winner_settings(3, 4, 5, 'O');
			return 1;
		}else if(boxes[6] == 'O' && boxes[7] == 'O' && boxes[8] == 'O'){
			winner_settings(6, 7, 8, 'O');
			return 1;
		}else if(boxes[0] == 'O' && boxes[3] == 'O' && boxes[6] == 'O'){
			winner_settings(0, 3, 6, 'O');
			return 1;
		}else if(boxes[1] == 'O' && boxes[4] == 'O' && boxes[7] == 'O'){
			winner_settings(1, 4, 7, 'O');
			return 1;
		}else if(boxes[2] == 'O' && boxes[5] == 'O' && boxes[8] == 'O'){
			winner_settings(2, 5, 8, 'O');
			return 1;
		}else if(boxes[0] == 'O' && boxes[4] == 'O' && boxes[8] == 'O'){
			winner_settings(0, 4, 8, 'O');
			return 1;
		}else if(boxes[2] == 'O' && boxes[4] == 'O' && boxes[6] == 'O'){
			winner_settings(2, 4, 6, 'O');
			return 1;
		}
		// If play_count is 9 then its a tie and we cannot make any move
		if(play_count == 9){
			tie_count++;
			display.setText("TIE");
			wantToContinue();
		}
		// If computer mode is on and it's the computer turn then it's make sure that
		if(computer_plays && turn == 2){
			validates(-1);
			checks = check();
			if(checks == 1){
				wantToContinue();
			}
		}

		return 0;
	}

	private void winner_settings(int i, int j, int k, char c){
		// Updating scores of x and o
		if(c == 'X'){
			x_score++;
		}else{
			o_score++;
		}

		// If someone's won then it will mark those boxes with green color and prints who won
		buttons[i].setBackground(Color.GREEN);
		buttons[j].setBackground(Color.GREEN);
		buttons[k].setBackground(Color.GREEN);
		display.setText(c + " WON");
	}

	private void reset(){
		// Resets the count, boxes array and buttons
		play_count = 0;

		for(int i=0; i<9; i++){
			boxes[i] = ' ';
		}

		for(int i=0; i<9; i++){
			buttons[i].setText(" ");
			buttons[i].setBackground(null);
		}

		display.setText("Tic Tac Toe");
	}

	private void wantToContinue(){
		// Continues and resets if yes selected, closes if no is selected
		int yn = JOptionPane.showConfirmDialog(null, "Do You Want To Continue Playing?\n\n" +
						"_____Score_____\n\n" + "X Won: " + x_score + " time(s)\n" + "O Won: " + o_score + " time(s)\n" + "Tie: " + tie_count + " time(s)",
				"Tic Tac Toe", JOptionPane.YES_NO_OPTION);
		if(yn == 1){
			window.dispose();
			playSounds("sounds/Windows XP Shutdown.wav");
		}else{
			reset();
			playSounds("sounds/Windows XP Startup.wav");
			// It will show the current turn at start
			if(!computer_plays)
				showTurn('X', 'O');
		}
	}

	private void toggleMode(){
		// Menu mode toggle method
		reset();
		x_score = o_score = tie_count = 0;

		if(computer_plays){
			comp_m_item.setSelected(false);
			human_m_item.setSelected(true);
			computer_plays = false;
		}else{
			comp_m_item.setSelected(true);
			human_m_item.setSelected(false);
			computer_plays = true;
		}

		// It will show the current turn at start
		if(!computer_plays)
			showTurn('X', 'O');

		// If computer starts the game then that move will be displayed with this code
		if(computer_plays && turn == 2)
			validates(-1);
	}

	private void toggleSound(){
		// Toggles the sound button on/off
		if(sound_on){
			sound_on = false;
			sound_m_item.setSelected(false);
		}else{
			sound_on = true;
			sound_m_item.setSelected(true);
		}
	}

	void playSounds(String url){
		// Plays the sound
		if(sound_on){
			try{
				// Objects... will fetch the files from jar as well. If you're not using jar try to keep those files in the same folder in which you have class
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(url)));
				Clip clip = AudioSystem.getClip();
				clip.open(audioStream);
				clip.start();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}


}
