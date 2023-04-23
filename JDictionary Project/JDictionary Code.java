import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashSet;


public class Jp{

	public static void main(String[] args){

		new JDictionary();

	}

}


class JDictionary{

	JLabel title_lbl;
	JTextArea display;
	JTextField search_box;
	JButton search_btn;
	LinkedHashMap<String, String> dictionary = new LinkedHashMap<>();
	ArrayList<String> keywords = new ArrayList<>();
	int keyword_pointer = 0;
	String onscreen_keywords = "";

	// Constructor
	public JDictionary(){

		try {
			// Nimbus Theme in Swing
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		}catch (Exception ex){
			JOptionPane.showMessageDialog(null, "Theme Error", "Error", JOptionPane.ERROR_MESSAGE);
		}

		// Window
		JFrame window = new JFrame("JDictionary");
		window.setLayout(new BorderLayout());

		// Title label
		title_lbl = new JLabel("JDictionary");
		title_lbl.setFont(new Font("Times New Roman", Font.PLAIN, 40));
		title_lbl.setHorizontalAlignment(JLabel.CENTER);
		window.add(title_lbl, BorderLayout.NORTH);

		// Display textarea
		display = new JTextArea("Welcome...♪♫\n\n\n" +
				"Type Any Word And Use Up/Down Arrow Keys To Navigate.\n" +
				"You Can Press Enter Key Or Search Button To Get The Meaning Of Your Query.\n\n\n\n\n\n\n\n\n\n" +
				"\t\t\tOffline Dictionary Created By Nikhil");
		display.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		display.setWrapStyleWord(true);
		display.setLineWrap(true);
		display.setBackground(new Color(53, 54, 58));
		display.setForeground(new Color(218, 220, 224));
		display.setEditable(false);
		display.setFocusable(false);
		window.add(new JScrollPane(display), BorderLayout.CENTER);

		// Search box and button in a panel
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		window.add(panel, BorderLayout.SOUTH);

		search_box = new JTextField();
		search_box.setToolTipText("Enter The Keyword And Press Enter");
		search_box.setColumns(4);
		panel.add(search_box, BorderLayout.CENTER);
		search_box.addKeyListener(new KeyAdapter(){
			@Override
			public void keyTyped(KeyEvent e){
				if(e.getKeyChar() == 10){
					getMeaning();
					return;
				}

				new UpdateList().start();
//				25 Redo, 26 Undo, 8 Backspace, 10 Enter
//              System.out.println(e.getKeyChar() + " " + (int)e.getKeyChar());
			}

			@Override
			public void keyPressed(KeyEvent e){
				if(KeyEvent.VK_UP == e.getKeyCode()){
					arrowSelector("up");
				}else if(KeyEvent.VK_DOWN == e.getKeyCode()){
					arrowSelector("down");
				}
			}
		});

		search_btn = new JButton("Search");
		search_btn.setFocusable(false);
		search_btn.addActionListener(e -> getMeaning());
		panel.add(search_btn, BorderLayout.EAST);

		// Window settings
		window.setVisible(true);
		window.setSize(910, 620);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Reading and storing the dictionary.data
		start();

	}

	// Methods

	private String toTitleCase(String str){
		String[] x = str.split(" ");
		String ans = "";

		for(int i=0; i<x.length; i++){
			if(!x[i].isEmpty()){
				x[i] = x[i].substring(0, 1).toUpperCase() + x[i].substring(1);
			}
		}

		for(String i : x){
			if(!i.isEmpty())
				ans += i + " ";
		}

		return ans.substring(0, ans.length()-1);
	}


	private void start(){
		// Opening the dictionary.data, reading and storing the data in a dictionary
		StringBuilder data = new StringBuilder();
		try{
			InputStream data_path = getClass().getResourceAsStream("dictionary.data");
			BufferedReader br = new BufferedReader(
					new InputStreamReader(data_path, StandardCharsets.UTF_8)   // To read utf-8 text
			);

			// Reading
			int c;
			while((c = br.read()) != -1){
				data.append((char)c);
			}

			br.close();

			String text = data.toString();

			String[] data_split = text.split("\n");

			for(String i : data_split){
				if(!i.isEmpty()){
					String[] x = i.split(" == ");
					dictionary.put(x[0], x[1]);
				}
			}

			dictionary.remove("_Keyword_");

		}catch(Exception e){
			// Showing the error message
			JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void getMeaning(){
		// This method will show you meaning when you click search or hit enter key
		String inp = search_box.getText();

		// Input should not to be empty
		if(inp.isEmpty()){
			return;
		}

		int count = 0;  // If this counter value is four then it means answer not found
		HashSet<String> ans = new HashSet<>();  // Using the set datatype to avoid duplicate data

		// Search all types of answer for the given keyword
		String[] list = {inp, inp.toLowerCase(), inp.toUpperCase(), toTitleCase(inp)};
		for(String i : list){
			String x = dictionary.get(i);

			if(x == null){
				count++;  // Increasing the count if answer not found
			}else{
				ans.add("*|░▒▓ " + i + " ▓▒░|* : ♯\n» " + x + "\n\n");  //adding to the set if answer found
			}

		}

		if(count == 4){
			// Clearing the screen and showing not found message
			display.setText("");
			display.setText("Requested word not found.");
		}else{
			// Formatting the displaying answer with new line and arrow
			String ans_str = "";
			for(String i : ans){
				ans_str += i;
			}
			ans_str = ans_str.replace("||", "\n» ").trim();
			display.setText("");
			display.setText(ans_str);
			display.setCaretPosition(0);

		}

	}

	private void arrowSelector(String arrow){
		// This method is going to point arrow to the selected word on the display when up and down arrows are used

		// If input is a null string then do nothing
		if(search_box.getText().isEmpty()){
			return;
		}

		if(arrow.equals("up")){  // Up arrow button pressed
			if(keyword_pointer > 0){
				keyword_pointer--;
			}
		}else{  // Down arrow button pressed
			if(keyword_pointer < keywords.size()-1){
				keyword_pointer++;
			}
		}

		// If keyword_pointer == -1 then we need to do nothing otherwise it throws negative line error
		if(keyword_pointer == -1){
			return;
		}

		// Making the pointer to point at the selected keyword when arrow up/down arrow keys are used
		try{
			display.setText(onscreen_keywords);  // Re-displaying the content on the screen
			int pos = display.getLineStartOffset(keyword_pointer);  // Getting the keyword position
			display.setCaretPosition(pos);  // Pointing to the selected word
			display.insert("→  ", pos);  // Displaying arrow in front of selected word
			search_box.setText(keywords.get(keyword_pointer));  // Placing the selected word in search_box
		}catch(Exception e){
			// Showing the error message
			JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	// Inner class

	class UpdateList extends Thread{
		// This inner class will update the contents on the display when you type in the input_box

		public void run() {

			String inp = search_box.getText();  // Getting the input data

			// input should not to be null string
			if(!inp.isEmpty()) {
				display.setText("");
				keywords.clear();

				for (String i : dictionary.keySet()) {
					// Searching all types of the given input
					if (i.startsWith(inp) || i.startsWith(inp.toLowerCase()) || i.startsWith(toTitleCase(inp)) || i.startsWith(inp.toUpperCase())) {
						keywords.add(i);
					}

				}

				StringBuilder sb = new StringBuilder();
				for (String i : keywords) {
					sb.append(i).append("\n");
				}

				onscreen_keywords = sb.toString().trim();  // Converting string builder to string and removing extra white spaces and newlines at front and back
				display.setText(onscreen_keywords);
				keyword_pointer = -1;  // Setting the keyword_pointer to -1 when list gets updated
			}else{
				// Clearing the screen and keywords list
				display.setText("Keep Going...♪♫");
				keywords.clear();
			}

		}

	}


}
