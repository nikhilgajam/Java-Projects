import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Jp {

	public static void main(String[] args){

		new JEasyDOS();

	}

}

class JEasyDOS{

	// Variables
	JFrame window;
	JRadioButton yes_fullscreen_rb, no_fullscreen_rb;
	JTextField dir_box;
	JMenuBar menu_bar;
	JMenu help_menu, settings_menu, about_menu;
	JMenuItem help, dos_help, commands, where_to, how_to, open_settings, about_us;

	public JEasyDOS(){

		// Window
		window = new JFrame("JEasyDOS");
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
		JLabel title = new JLabel("JEasyDOS");
		title.setFont(new Font("Times New Roman", Font.PLAIN, 65));
		title.setOpaque(true);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setForeground(Color.decode("#e6e8eb"));
		title.setBackground(Color.decode("#333333"));
		window.add(title, BorderLayout.NORTH);

		// Button panel
		JPanel btn_panel = new JPanel();
		btn_panel.setBackground(Color.decode("#333333"));
		btn_panel.setLayout(new BoxLayout(btn_panel, BoxLayout.Y_AXIS));
		btn_panel.setBorder(new javax.swing.border.EmptyBorder(25, 10, 25, 10));  // Giving padding to the btn_panel

		// Program manager button
		JButton program_btn = new JButton("Program Manager");
		program_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		program_btn.setForeground(Color.decode("#e6e8eb"));
		program_btn.setBackground(Color.decode("#333333"));
		program_btn.addActionListener(e -> openProgramManager());
		btn_panel.add(program_btn);
		btn_panel.add(Box.createRigidArea(new Dimension(5, 10)));  // Space between the components

		// Load DOS Program Button with image and label
		ImageIcon img = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/load.png")));  // Button Image
		JButton load_dos_btn = new JButton(img);
		load_dos_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		load_dos_btn.setForeground(Color.decode("#e6e8eb"));
		load_dos_btn.setBackground(Color.decode("#333333"));
		load_dos_btn.addActionListener(e -> openDOSProgram());
		btn_panel.add(load_dos_btn);

		JLabel load_dos_lbl = new JLabel("Load DOS Program");
		load_dos_lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		load_dos_lbl.setForeground(Color.decode("#e6e8eb"));
		load_dos_lbl.setBackground(Color.decode("#333333"));
		btn_panel.add(load_dos_lbl);

		btn_panel.add(Box.createRigidArea(new Dimension(5, 10)));  // Space between the components

		// Default DOS Window button
		JButton dos_win_btn = new JButton("Default DOS Window");
		dos_win_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		dos_win_btn.setForeground(Color.decode("#e6e8eb"));
		dos_win_btn.setBackground(Color.decode("#333333"));
		dos_win_btn.addActionListener(e -> openDOS("DOSBox\\DOSBox.exe -noconsole"));
		btn_panel.add(dos_win_btn);
		btn_panel.add(Box.createRigidArea(new Dimension(5, 20)));  // Space between the components

		// Directory box
		dir_box = new JTextField("To Mount A Drive, Paste The Path Here And Press Enter Key.");
		dir_box.setToolTipText("To Mount A Drive, Paste The Path Here And Press Enter Key.");
		dir_box.setAlignmentX(Component.CENTER_ALIGNMENT);
		dir_box.setForeground(Color.decode("#e6e8eb"));
		dir_box.setBackground(Color.decode("#993e1c"));
		dir_box.addMouseListener(new MouseAdapter() {   // Clearing the dir_box when mouse is clicked on it
			public void mouseClicked(MouseEvent e) {
				// Whenever you click on the directory box string inside the box will be replaced by empty string("")
				dir_box.setText("");
			}
		});
		dir_box.addActionListener(e -> openDirBoxPath());
		btn_panel.add(dir_box);
		btn_panel.add(Box.createRigidArea(new Dimension(5, 20)));  // Space between the components

		// DOSBox fullscreen radio buttons
		ButtonGroup group = new ButtonGroup();
		yes_fullscreen_rb = new JRadioButton("Fullscreen Mode");
		yes_fullscreen_rb.setAlignmentX(Component.CENTER_ALIGNMENT);
		yes_fullscreen_rb.setForeground(Color.decode("#e6e8eb"));
		yes_fullscreen_rb.setBackground(Color.decode("#993e1c"));
		group.add(yes_fullscreen_rb);
		btn_panel.add(yes_fullscreen_rb);
		btn_panel.add(Box.createRigidArea(new Dimension(5, 10)));  // Space between the components

		no_fullscreen_rb = new JRadioButton("Windowed Mode", true);
		no_fullscreen_rb.setAlignmentX(Component.CENTER_ALIGNMENT);
		no_fullscreen_rb.setForeground(Color.decode("#e6e8eb"));
		no_fullscreen_rb.setBackground(Color.decode("#993e1c"));
		group.add(no_fullscreen_rb);
		btn_panel.add(no_fullscreen_rb);

		window.add(btn_panel, BorderLayout.CENTER);

		// Menu
		menu_bar = new JMenuBar();

		// Help menu
		help_menu = new JMenu("Help");

		help = new JMenuItem("Help");
		help.addActionListener(e -> showHelp());
		help_menu.add(help);

		dos_help = new JMenuItem("DOS Help");
		dos_help.addActionListener(e -> dosHelp());
		help_menu.add(dos_help);

		commands = new JMenuItem("Commands");
		commands.addActionListener(e -> commandsHelp());
		help_menu.add(commands);

		where_to = new JMenuItem("Where To Download DOS Programs");
		where_to.addActionListener(e -> whereToHelp());
		help_menu.add(where_to);

		how_to = new JMenuItem("How To Run Script");
		how_to.addActionListener(e -> howToHelp());
		help_menu.add(how_to);

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

		// Window settings
		window.setVisible(true);
		window.setSize(450, 450);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	// Button action methods

	private void openDOS(String command){

		// If yes_fullscreen radio button is selected then adding the fullscreen tag to the command
		if(yes_fullscreen_rb.isSelected())
			command = command + " -fullscreen";

		try {
			Runtime.getRuntime().exec(command);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void openDOSProgram(){

		String path = "";
		try{
			// Open file box
			JFileChooser jc = new JFileChooser();
			jc.showOpenDialog(window);
			path = jc.getSelectedFile().getAbsolutePath().toString().toLowerCase();
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Select A Correct DOS Executable With Extension (.exe) or (.com) or (.bat)", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if(path.equals(""))
			JOptionPane.showMessageDialog(null, "Select A Correct DOS Executable With Extension (.exe) or (.com) or (.bat)", "Error", JOptionPane.ERROR_MESSAGE);

		if(path.contains(".exe") || path.contains(".com") || path.contains(".bat")){
			openDOS("DOSBox\\DOSBox.exe \"" + path + "\" -exit -noconsole");
		}else{
			JOptionPane.showMessageDialog(null, "Select A Correct DOS Executable With Extension (.exe) or (.com) or (.bat)", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void openProgramManager(){

		// Creating an object of ProgramManager inner class
		new ProgramManager();

	}

	private void openSettings(){

		// Creating an object of Settings inner class
		new Settings();

	}

	private void openDirBoxPath(){

		String path = dir_box.getText();
		// Checking whether path is valid or not and opening the specified path in dosbox
		if(new File(path).exists()){
			openDOS("DOSBox\\DOSBox.exe \"" + path + "\" -exit -noconsole");
		}else{
			JOptionPane.showMessageDialog(null, "Enter A Valid Path", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	// Help methods
	private void showHelp(){

		// Displays help message box
		JOptionPane.showMessageDialog(null, "Program Manager Button : You Can Add Programs To Browse Box To Access Them Later Quickly.\n" +
				"Load DOS Program Button: Will Open A Select Box To Run A DOS Program And That DOS Will Be Closed As Soon As You Quit The Program.\n" +
				"Default DOS Window Button : Will Open DOS With Default Directory.\n" +
				"Drive Mount Box : When You Paste A Path(CTRL + V) And Press Enter Key Then DOS Will Get Opened In That Directory, Mounted As C: Drive.\n" +
				"Fullscreen And Windowed Mode Radio Buttons : Used To Run DOS In Fullscreen Or In A Window.\n" +
				"Settings Menu Button : With This You Can Change DOSBox Settings.", "JEasyDOS Help", JOptionPane.PLAIN_MESSAGE);

	}

	private void dosHelp(){

		// Displays dos help message box
		try {
			Runtime.getRuntime().exec("cmd /K \".\\DOSBox\\Manual\\Manual.html && exit\"");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void commandsHelp(){

		// Displays comands help message box
		try {
			Runtime.getRuntime().exec("cmd /K \"start https://www.dosbox.com/wiki/Commands && exit\"");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void whereToHelp(){

		// Displays where to help message box
		JOptionPane.showMessageDialog(null, "Games: https://www.dosgames.com/\n" +
				"Programs: https://archive.org/details/softwarelibrary_msdos\n" +
				"Use google to get more programs and games.", "Where To Download DOS Programs", JOptionPane.PLAIN_MESSAGE);

	}

	private void howToHelp(){

		// Displays how to help message box
		JOptionPane.showMessageDialog(null, "To run many lines of DOS code then,\n" +
				"You can create a .BAT file to execute many DOS commands at once.", "How To Run Script", JOptionPane.PLAIN_MESSAGE);

	}

	private void aboutUs(){

		// Displays about us message box
		JOptionPane.showMessageDialog(null, "JEasyDOS Version 1.0\nDeveloped By Nikhil", "About Us", JOptionPane.PLAIN_MESSAGE);

	}

	// Inner classes

	// Program manager window class
	class ProgramManager{

		JFrame w;
		JList<String> listbox;
		TreeMap<String, String> list;
		File file = new File("Data.csv");  // file that stores the program manager data

		ProgramManager(){

			// Window
			w = new JFrame("JEasyDOS Program Manager");
			w.setLayout(new BorderLayout());
			ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/icon.png")));
			w.setIconImage(icon.getImage());

			// Program manager title
			JLabel title = new JLabel("Program Manager");
			title.setFont(new Font("Times New Roman", Font.PLAIN, 35));
			title.setOpaque(true);
			title.setHorizontalAlignment(JLabel.CENTER);
			title.setForeground(Color.decode("#e6e8eb"));
			title.setBackground(Color.decode("#333333"));
			w.add(title, BorderLayout.NORTH);

			// Listbox
			list = new TreeMap<>();
			listbox = new JList<>(); // listbox = new JList<>(list.keySet().toArray(new String[0]));
			listbox.setForeground(Color.decode("#e6e8eb"));
			listbox.setBackground(Color.decode("#333333"));
			listbox.setSelectionForeground(Color.decode("#e6e8eb"));
			listbox.setSelectionBackground(Color.decode("#993e1c"));
			listbox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // Only one item should be selected
			listbox.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if(e.getKeyChar() == 10)
						load();  // Enter = 10
					else if(e.getKeyChar() == 127)
						del();  // Delete = 127
				}
			});
			w.add(new JScrollPane(listbox), BorderLayout.CENTER);

			// Buttons panel
			JPanel btn_panel = new JPanel();
			btn_panel.setBackground(Color.decode("#333333"));

			JButton load_btn = new JButton("LOAD");
			load_btn.setForeground(Color.decode("#e6e8eb"));
			load_btn.setBackground(Color.decode("#333333"));
			load_btn.addActionListener(e -> load());
			btn_panel.add(load_btn);

			JButton add_btn = new JButton("ADD");
			add_btn.setForeground(Color.decode("#e6e8eb"));
			add_btn.setBackground(Color.decode("#333333"));
			add_btn.addActionListener(e -> add());
			btn_panel.add(add_btn);

			JButton del_btn = new JButton("DEL");
			del_btn.setForeground(Color.decode("#e6e8eb"));
			del_btn.setBackground(Color.decode("#333333"));
			del_btn.addActionListener(e -> del());
			btn_panel.add(del_btn);

			w.add(btn_panel, BorderLayout.SOUTH);

			// Window settings
			w.setVisible(true);
			w.setSize(400, 400);
			w.setLocationRelativeTo(null);
			w.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			update_box();

		}

		// Update box method to load the data from csv to TreeMap
		private void update_box(){

			try{
				// If data.csv file is not found then ends the execution
				if(!file.exists()){
					return;
				}

				int check = 0;

				FileReader read = new FileReader(file);
				BufferedReader br = new BufferedReader(read);
				String temp;

				temp = br.readLine();             // Reading a line from data.csv
				String[] arr = new String[2];     // Creating a string array with size two
				while(temp != null){
					arr = temp.split(", "); // Splitting the string a ,(comma)
					list.put(arr[0], arr[1]);     // Inserting data into list(TreeMap)
					temp = br.readLine();         // Reading a line from data.csv
					check = 1;
				}

				// If loop executes then this block should be executed
				if(check == 1){
					listbox.setListData(list.keySet().toArray(new String[0]));  // Updating the listbox(JList)
					listbox.grabFocus();
					listbox.setSelectedIndex(0);
				}

			}catch(Exception e){
				e.printStackTrace();
			}

		}

		// Button action methods

		private void load(){

			String selected_val = listbox.getSelectedValue();

			if(selected_val == null){
				JOptionPane.showMessageDialog(null, "Select A Title To Load", "Warning", JOptionPane.WARNING_MESSAGE);
			}else{
				String path = list.get(selected_val);
				openDOS("DOSBox\\DOSBox.exe \"" + path + "\" -noconsole");
				// Closes the program manager after loading the program
				w.dispose();
			}

		}

		private void add(){

			try{

				// Path and name strings
				String name = "", s_path = "";

				name = JOptionPane.showInputDialog(null, "Enter A Program Title:", "JEasyDOS", JOptionPane.PLAIN_MESSAGE);

				// Title entered by the user is already in TreeMap should not be added
				if(list.get(name) != null){
					JOptionPane.showMessageDialog(null, "Don't enter existing title name", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}

				// Commas are not accepted in the title name
				if(name.contains(",")){
					JOptionPane.showMessageDialog(null, "Don't use ,(comma) in the title", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}

				// If the entered title or name is null or a null string then it throws exception and message in the except block is displayed
				if(name.equals("null") || name.equals(""))
					throw new Exception();

				JFileChooser jc = new JFileChooser();
				jc.showOpenDialog(null);
				s_path = jc.getSelectedFile().getAbsolutePath().toString().toLowerCase();

				if(s_path.contains(".exe") || s_path.contains(".com") || s_path.contains(".bat")){
					FileWriter write = new FileWriter(file, true);
					write.append(name + ", " + s_path + "\n");
					write.close();
				}else{
					JOptionPane.showMessageDialog(null, "Select A Correct DOS Executable With Extension (.exe) or (.com) or (.bat)", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				list.put(name, s_path); // Adding to the treemap
				listbox.setListData(list.keySet().toArray(new String[0]));  // Updating the listbox(JList)

			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "Enter A Correct Title And Select A Correct DOS Executable With Extension (.exe) or (.com) or (.bat)", "Error", JOptionPane.ERROR_MESSAGE);
			}

		}

		private void del(){

			try{

				String name = listbox.getSelectedValue();

				list.remove(name);  // Removing from treemap

				FileWriter writer = new FileWriter(file);

				for(Map.Entry<String, String> obj: list.entrySet()){
					writer.append(obj.getKey() + ", " + obj.getValue() + "\n");
				}

				writer.close();
				listbox.setListData(list.keySet().toArray(new String[0]));  // Updating the listbox(JList)

			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "Select A Title To Delete", "Warning", JOptionPane.WARNING_MESSAGE);
			}

		}


	}


	// Settings window class
	class Settings{

		Settings(){

			// Window
			JFrame w = new JFrame("JEasyDOS Settings");
			w.setLayout(new BorderLayout());
			ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/icon.png")));
			w.setIconImage(icon.getImage());

			// Program manager title
			JLabel title = new JLabel("JEasyDOS Settings");
			title.setFont(new Font("Times New Roman", Font.PLAIN, 35));
			title.setOpaque(true);
			title.setHorizontalAlignment(JLabel.CENTER);
			title.setForeground(Color.decode("#e6e8eb"));
			title.setBackground(Color.decode("#333333"));
			w.add(title, BorderLayout.NORTH);

			// Buttons panel
			JPanel btn_panel = new JPanel();
			btn_panel.setBackground(Color.decode("#333333"));
			btn_panel.setLayout(new BoxLayout(btn_panel, BoxLayout.Y_AXIS));
			btn_panel.setBorder(new javax.swing.border.EmptyBorder(25, 0, 25, 0));

			JButton opt_btn = new JButton("Options");
			opt_btn.setForeground(Color.decode("#e6e8eb"));
			opt_btn.setBackground(Color.decode("#333333"));
			opt_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			opt_btn.addActionListener(e -> optionsOpen());
			btn_panel.add(opt_btn);

			JButton ropt_btn = new JButton("Reset Options");
			ropt_btn.setForeground(Color.decode("#e6e8eb"));
			ropt_btn.setBackground(Color.decode("#333333"));
			ropt_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			ropt_btn.addActionListener(e -> resetOptions());
			btn_panel.add(ropt_btn);

			JButton rkm_btn = new JButton("Reset KeyMapper");
			rkm_btn.setForeground(Color.decode("#e6e8eb"));
			rkm_btn.setBackground(Color.decode("#333333"));
			rkm_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			rkm_btn.addActionListener(e -> resetKeyMapper());
			btn_panel.add(rkm_btn);

			JButton sr_btn = new JButton("Screenshots & Recordings");
			sr_btn.setForeground(Color.decode("#e6e8eb"));
			sr_btn.setBackground(Color.decode("#333333"));
			sr_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			sr_btn.addActionListener(e -> screenshotsRecs());
			btn_panel.add(sr_btn);


			w.add(btn_panel, BorderLayout.CENTER);

			w.setVisible(true);
			w.setSize(400, 220);
			w.setLocationRelativeTo(null);
			w.setResizable(false);
			w.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		}

		// Button action methods

		private void optionsOpen(){

			// This method opens the dosbox options in a notepad
			try {
				Runtime.getRuntime().exec("cmd /K \"cd DOSBox && \"DOSBox 0.74-3 Options\" \"");
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			}

		}

		private void resetOptions(){

			// This method resets the dosbox options
			try {
				Runtime.getRuntime().exec("cmd /K \" cd DOSBox && \"Reset Options\" \"");
				JOptionPane.showMessageDialog(null, "Options Reset Completed", "JEasyDOS", JOptionPane.ERROR_MESSAGE);
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			}

		}

		private void resetKeyMapper(){

			// This method resets the dosbox key mapper
			try {
				Runtime.getRuntime().exec("cmd /K \" cd DOSBox && \"Reset KeyMapper\" \"");
				JOptionPane.showMessageDialog(null, "KeyMapper Reset Completed", "JEasyDOS", JOptionPane.ERROR_MESSAGE);
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			}

		}

		private void screenshotsRecs(){

			try {
				Runtime.getRuntime().exec("cmd /K \" cd DOSBox && \"Screenshots & Recordings\" \"");
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			}

		}


	}


}
