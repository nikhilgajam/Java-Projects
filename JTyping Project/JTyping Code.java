import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.DefaultCaret;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.*;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Random;
import java.util.Date;


public class Jp{

    public static void main(String[] args){

        new JTyping();

    }

}


class JTyping{

    // Widgets
    JFrame window;
    JLabel title_lbl, time_display_lbl;
    JTextArea display;
    JScrollPane display_scroll_pane;
    JComboBox<String> text_name_box, time_box;
    JMenuBar menu_bar;
    JMenu about_menu;
    JMenuItem about_menu_item;
    Timer timer;

    // Variables
    String all_chars = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789\n`~!@#$%^&*()-_=+\\|{}[]:;\"',<>./?'";
    String[] text_names = {"Astronauts", "Babbage", "Baseball", "Cast", "Credits", "DNA", "Fables", "Franklin", "Girl", "Hill", "Hubble", "Insects", "Jane", "Lincoln", "Netiquette", "Pangrams", "Photo", "Rabbits", "Strebel", "Yosemite", "ZNumbers1", "ZNumbers2", "Custom Text"};
    String[] time_names = {"46 Seconds", "1 Minute", "2 Minutes", "3 Minutes", "5 Minutes", "10 Minutes", "15 Minutes", "30 Minutes", "46 Minutes", "1 Hours", "2 Hours", "3 Hours", "Custom Time"};
    int char_pointer = 0, char_correct = 0, char_error = 0, best_wpm = 0, time_in_seconds = 0, time_counter = 0;
    double program_used_time = 0;
    String text_data = "";
    String data_store_file_name = "Store.data";
    String date_time_stored = "";
    boolean typing_started = false, stop_typing = true, sound_var = true, is_ready = false;
    HighlightPainter greenHighlighter = new DefaultHighlighter.DefaultHighlightPainter(new Color(60, 128, 78));
    HighlightPainter redHighlighter = new DefaultHighlighter.DefaultHighlightPainter(new Color(220, 4, 4));

    // Constructor
    public JTyping(){

        try{
            // Nimbus Theme in Swing
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Theme Error", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Window
        window = new JFrame("JTyping");
        window.setLayout(new BorderLayout());

        // Title label
        title_lbl = new JLabel("JTyping");
        title_lbl.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        title_lbl.setHorizontalAlignment(JLabel.CENTER);
        window.add(title_lbl, BorderLayout.NORTH);

        // Display textarea
        display = new JTextArea();
        display.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        display.setWrapStyleWord(true);
        display.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));   // Changing cursor to arrow
        display.getCaret().setBlinkRate(0);  // Making the cursor to not blink
        DefaultCaret caret = (DefaultCaret) display.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        display.setCaret(caret);
        display.setLineWrap(true);
        display.setBackground(new Color(53, 54, 58));
        display.setForeground(new Color(218, 220, 224));
        display.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "none");  // Making backspace to not get triggered
        display.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "none");  // Making enter to not get triggered
        display.getInputMap().put(KeyStroke.getKeyStroke("TAB"), "none");  // Making tab to not get triggered
        display.getInputMap().put(KeyStroke.getKeyStroke("UP"), "none");  // Making up arrow to not get triggered
        display.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "none");  // Making down arrow to not get triggered
        display.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "none");  // Making left arrow to not get triggered
        display.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "none");  // Making right arrow to not get triggered
        display.getInputMap().put(KeyStroke.getKeyStroke("INSERT"), "none");  // Making insert to not get triggered
        display.getInputMap().put(KeyStroke.getKeyStroke("HOME"), "none");  // Making home to not get triggered
        display.getInputMap().put(KeyStroke.getKeyStroke("END"), "none");  // Making end to not get triggered
        display.getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "none");  // Making delete to not get triggered
        display.getInputMap().put(KeyStroke.getKeyStroke("PAGE_UP"), "none");  // Making delete to not get triggered
        display.getInputMap().put(KeyStroke.getKeyStroke("PAGE_DOWN"), "none");  // Making delete to not get triggered
        display.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                display.setCaretPosition(char_pointer);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mousePressed(e);
                display.setCaretPosition(char_pointer);
            }
        });

        // Adding scrollbar to the text display
        display_scroll_pane = new JScrollPane(display);
        display_scroll_pane.setEnabled(false);
        window.add(display_scroll_pane, BorderLayout.CENTER);

        // Display key listener
        display.addKeyListener(new KeyAdapter(){
            @Override
            public void keyTyped(KeyEvent e){
                e.setKeyChar('\0');  // Making the editor to display null character or nothing on screen
//              25 Redo, 26 Undo, 8 Backspace, 9 Tab, 27 ESC, 10 Enter
//              System.out.println(e.getKeyChar() + " " + (int)e.getKeyChar());
            }

            @Override
            public void keyPressed(KeyEvent e){
                keyOperations(e);
            }

        });

        // Utility panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        window.add(panel, BorderLayout.SOUTH);

        // Time combo box
        time_box = new JComboBox<>(time_names);
        time_box.setSelectedIndex(1);
        time_box.setToolTipText("Select a time interval");
        time_box.setFocusable(false);
        time_box.addActionListener(e -> textOrTimeSelected());
        time_box.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                playSounds("sounds/click.wav");   // Playing the click sound when this box is clicked
            }
        });
        panel.add(time_box, BorderLayout.WEST);

        // Text name combo box
        text_name_box = new JComboBox<>(text_names);
        text_name_box.setToolTipText("Select a typing text");
        text_name_box.setFocusable(false);
        text_name_box.addActionListener(e -> textOrTimeSelected());
        text_name_box.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                playSounds("sounds/click.wav");   // Playing the click sound when this box is clicked
            }
        });
        panel.add(text_name_box, BorderLayout.CENTER);

        // Time display label
        time_display_lbl = new JLabel("  0 Seconds ");
        time_display_lbl.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        time_display_lbl.setToolTipText("Displays the time when you start typing");
        panel.add(time_display_lbl, BorderLayout.EAST);

        // Reading and storing the dictionary.data document
        start();

        // Changing the functionality of close button
        window.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                exit();
            }
        });

        // Window settings
        window.setSize(1000, 670);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.setVisible(true);

    }


    // Methods


    private void start(){
        try{
            // Playing the intro sound
            playSounds("sounds/intro.wav");

            // Setting the random text
            int rand_text_number = new Random().nextInt(text_names.length - 1);
            text_name_box.setSelectedIndex(rand_text_number);
            display.setCaretPosition(display.getDocument().getLength());

            // Setting the variables to their default values
            typing_started = false;
            stop_typing = true;
            is_ready = false;

            // Store.data handling
            String data = "";
            File file = new File(data_store_file_name);

            // Write the file if file doesn't exist
            if (!file.exists()) {
                date_time_stored = new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(new Date().getTime());
                writeOperation(data_store_file_name, best_wpm + "," + program_used_time + "," + date_time_stored);
            }

            data = readOperation(data_store_file_name);
            String[] arr = data.split(",");
            best_wpm = Integer.parseInt(arr[0]);
            program_used_time = Double.parseDouble(arr[1]);
            date_time_stored = arr[2];

            // Setting the initial text to the screen
            display.setText("Welcome...\n\n" +
                    "JTyping Is A Typing Practising And Analysing Program.\n" +
                    "You Can Toggle The Sound On/Off By Pressing \"F6\" Button.\n" +
                    "You Can Press \"ESC\" Key To Stop The Timer And Show Analysis.\n" +
                    "You Can Select A Specific Time And Text Given In The Provided Boxes Below.\n" +
                    "You Can Select Custom Time And Text By Using Custom Option Provided In Below Boxes.\n\n" +
                    "Press \"F5\" To Start Typing The Randomly Selected Text.\n\n" +
                    "Your Best WPM: " + best_wpm + " Words Per Minute\n" +
                    "You Spent " + program_used_time + " Minutes Of Time Using JTyping Since " + date_time_stored + "\n\n\n" +
                    "JTyping\nVersion 1.0\nDeveloped By Nikhil");

            char_pointer = 10;
            display.setCaretPosition(char_pointer);

        }catch(Exception e){
            // Showing the error message
            JOptionPane.showMessageDialog(null, e, "Error (Start)", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String readFromJAR(String text_path){
        StringBuilder data = new StringBuilder();

        try{
            InputStream data_path = getClass().getResourceAsStream(text_path);  // To get the data inside JAR
            assert data_path != null;
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(data_path, StandardCharsets.UTF_8)   // To read utf-8 text
            );

            // Reading
            int c;
            while((c = br.read()) != -1){
                data.append((char)c);
            }

            br.close();
            data_path.close();
        }catch(Exception e){
            // Showing the error message
            JOptionPane.showMessageDialog(null, e, "Error (Read From JAR)", JOptionPane.ERROR_MESSAGE);
        }

        return data.toString();

    }

    private String readOperation(String url){
        StringBuilder data = new StringBuilder();
        try{
            FileInputStream read = new FileInputStream(url);

            int ch;
            while((ch = read.read()) != -1){
                data.append((char)ch);
            }

            read.close();
        }catch(Exception e){
            // Showing the error message
            JOptionPane.showMessageDialog(null, e, "Error (Read Operation)", JOptionPane.ERROR_MESSAGE);
        }

        return data.toString();

    }

    private void writeOperation(String url, String data){
        try{
            FileOutputStream write = new FileOutputStream(url);
            for(int i=0; i < data.length(); i++){
                write.write(data.charAt(i));
            }
            write.close();
        }catch(Exception e){
            // Showing the error message
            JOptionPane.showMessageDialog(null, e, "Error (Write Operation)", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void keyOperations(KeyEvent e){
        try{
            int code = e.getKeyCode();
            // If typing is stopped then we can use F5 to start typing with selected text
            if(code == KeyEvent.VK_F6){
                sound_var = !sound_var;
            }

            if(stop_typing){
                if(code == KeyEvent.VK_F5){
                    textOrTimeSelected();
                }
                return;
            }

            if(code == KeyEvent.VK_ESCAPE){   // If ESC key is pressed then stop typing and display analysis
                typing_started = false;
                // If you press ESC when you are typing it shows this message and displays analysis
                if(!is_ready){  // If is_ready is false then display the message
                    display.append("\n\n\nYou Pressed ESC Key.");
                    char_pointer = display.getDocument().getLength();
                    display.setCaretPosition(char_pointer);
                }
            }

            char ch = e.getKeyChar();

            // Key to be considered if the size doesn't exceed, and it is present in all_chars array
            if(char_pointer <= text_data.length()-1 && all_chars.indexOf(ch) != -1){

                // Updating the display to show good amount of characters on display when \n is encountered in text_data
                if(text_data.charAt(char_pointer) == '\n'){
                    display_scroll_pane.getViewport().scrollRectToVisible(display.getVisibleRect());
                    display_scroll_pane.getViewport().scrollRectToVisible(display.getVisibleRect());
                    display_scroll_pane.getViewport().scrollRectToVisible(display.getVisibleRect());
                    display_scroll_pane.getViewport().scrollRectToVisible(display.getVisibleRect());
                }

                // Timer is started only if is_ready is true
                if(is_ready){
                    typing_started = true;
                    timer = new Timer();
                    timer.schedule(new Count(), 0, 1000);  // Starting the timer
                    is_ready = false;
                }
                if(ch == text_data.charAt(char_pointer)){
                    display.getHighlighter().addHighlight(char_pointer, char_pointer+1, greenHighlighter);
                    char_correct++;
                }else{
                    display.getHighlighter().addHighlight(char_pointer, char_pointer+1, redHighlighter);
                    playSounds("sounds/error.wav");  // Playing a sound when a character typed incorrectly
                    char_error++;
                }

                // When we reach end of the text then we need to stop the execution and show analysis
                if(char_pointer == text_data.length()-1){
                    typing_started = false;
                }

                char_pointer++;
                display.setCaretPosition(char_pointer);


            }
        }catch(Exception ex){
            // Showing the error message
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex, "Error (Key Operations)", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void textOrTimeSelected(){
        try{
            // Setting the variables to its default values
            typing_started = false;
            is_ready = false;
            stop_typing = false;
            char_pointer = char_correct = char_error = time_in_seconds = time_counter = 0;

            // Setting the selected time
            String time_selected = time_names[time_box.getSelectedIndex()];
            if(time_selected.equals(time_names[0])){
                time_in_seconds = 46;  // 46 Seconds
            }else if(time_selected.equals(time_names[1])){
                time_in_seconds = 60;  // 1 Minute
            }else if(time_selected.equals(time_names[2])){
                time_in_seconds = 120;  // 2 Minutes
            }else if(time_selected.equals(time_names[3])){
                time_in_seconds = 180;  // 3 Minutes
            }else if(time_selected.equals(time_names[4])){
                time_in_seconds = 300;  // 5 Minutes
            }else if(time_selected.equals(time_names[5])){
                time_in_seconds = 600;  // 10 Minutes
            }else if(time_selected.equals(time_names[6])){
                time_in_seconds = 900;  // 15 Minutes
            }else if(time_selected.equals(time_names[7])){
                time_in_seconds = 1800;  // 30 Minutes
            }else if(time_selected.equals(time_names[8])){
                time_in_seconds = 2760;  // 46 Minutes
            }else if(time_selected.equals(time_names[9])){
                time_in_seconds = 3600;  // 1 Hour
            }else if(time_selected.equals(time_names[10])){
                time_in_seconds = 7200;  // 2 Hours
            }else if(time_selected.equals(time_names[11])){
                time_in_seconds = 10800;  // 3 Hours
            }else if(time_selected.equals(time_names[12])){
                time_in_seconds = customTime();  // Custom Time Selection
            }

            time_counter = time_in_seconds;
            time_counter++;  // Adding extra one second to add up one second

            time_display_lbl.setText("  " + time_in_seconds + " Seconds ");

            StringBuilder data = new StringBuilder();

            // Setting the selected text
            String text_name_selected = text_names[text_name_box.getSelectedIndex()];

            String text_path = "texts/" + text_name_selected.toLowerCase() + ".exm";

            if(text_name_selected.equals("Custom Text")){
                text_path = customText();  // Custom Text Selection
                if(text_path.equals("texts/pangrams.exm")){
                    text_data = readFromJAR(text_path);
                }else{
                    text_data = readOperation(text_path);
                }
            }else{
                text_data = readFromJAR(text_path);
            }

            text_data = text_data.replace("\r", "");  // Replacing the carriage return to enter and storing
            text_data = text_data.replace(" \n", "\n");  // Replacing space and enter with just enter
            text_data = text_data.trim();  // Removing unwanted spaces and newline at start and end
            display.setText(text_data);  // Displaying the text on the screen
            display.setCaretPosition(0);  // Moving the cursor to starting position

            is_ready = true;

        }catch(Exception e){
            // Showing the error message
            JOptionPane.showMessageDialog(null, e, "Error (Time Or Text Selection)", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayAnalysis(){
        // Stop typing until you press a key indicating with this boolean variable
        stop_typing = true;

        double time_taken = (time_in_seconds-time_counter);  // Total Time - Time Spent In Typing

        // If you type very less time or if you type more errors than correct characters then analysis will not be shown
        if((time_taken < 40) || ((char_correct) <= char_error)){
            JOptionPane.showMessageDialog(null, "Try to type the text for atleast 40 seconds.\n" +
                            "And try to type more characters correctly than wrong.\nTry to type accurately and eventually your typing " +
                            "speed increases.\n\nNow reloading the text.\n\n" +
                            "Best WPM: " + best_wpm + " Words Per Minute\nYou Spent " + program_used_time + " Minutes Of Time Using JTyping Since " + date_time_stored,
                    "Analysis Warning", JOptionPane.WARNING_MESSAGE);
            textOrTimeSelected();  // Loading the same text
            return;
        }

        // WPM Information: https://www.speedtypingonline.com/typing-equations
        double chars_typed = char_pointer;
        double chars_typed_by_5 = chars_typed/5.0;
        double errors = char_error;
        double time_taken_in_minutes = time_taken/60.0;  // Divide by 60 to convert seconds to minutes

        // Gross WPM(Words Per Minute) = (No. of chars typed/5)/Time taken (in minutes)
        double gross_wpm = (chars_typed_by_5)/(time_taken_in_minutes);
        // Net WPM(Words Per Minute) = ((No. of chars typed/5)-Errors)/Time taken (in minutes) (OR) Gross WPM - (Errors/Time taken (in minutes))
        double net_wpm = (chars_typed_by_5 - errors)/(time_taken_in_minutes);
        // Accuracy = (Correct characters/All Characters)*100
        double accuracy = (char_correct/chars_typed)*100.0;
        // Gross strokes = All Characters Typed
        int gross_strokes = (int)chars_typed;
        // Error hits = no of errors typed
        int error_hits = char_error;
        // Net strokes = gross - error hits
        int net_strokes = gross_strokes - error_hits;

        // Best WPM and program used time calculation and storing
        if(net_wpm > best_wpm){
            best_wpm = Math.abs((int)net_wpm);
        }
        program_used_time += Math.round(Math.abs(time_taken_in_minutes)*100.0)/100.0;  // Rounding the time to two digits
        writeOperation(data_store_file_name, best_wpm + "," + program_used_time + "," + date_time_stored);  // Writing WPM And Program Use Time

        // Displaying the analysis
        String display_str = "Analysis:\n\n";
        display_str += "Time taken:    \t" + time_taken + " Seconds of total " + time_in_seconds + " Seconds\n";
        display_str += "Gross Speed:   \t" + (int)gross_wpm + " Words Per Minute (WPM)\n";
        display_str += "*Net Speed:    \t" + (int)net_wpm + " Words Per Minute (WPM)\n";
        display_str += "Accuracy:      \t" + (int)accuracy + "%\n";
        display_str += "Gross Strokes: \t" + gross_strokes + "\n";
        display_str += "Error Hits:    \t" + error_hits + "\n";
        display_str += "*Net Strokes:  \t" + net_strokes + "\n\n";
        display_str += "Your Best WPM:   " + best_wpm + " Words Per Minute\n";
        display_str += "You Spent " + program_used_time + " Minutes Of Time Using JTyping Since " + date_time_stored + "\n\n";
        display_str += "If You Want To Type The Same Text Again Press \"F5\" Key On Your Keyboard\n\n";
        display_str += "Text You Typed:\n";

        try{
            // Inorder to place the text in the start we need to remove the first highlighted character otherwise that
            // will highlight will be given to inserted text to avoid that we are removing that highlight and adding
            // it after adding
            Highlighter.Highlight[] highlights = display.getHighlighter().getHighlights();
            Highlighter.Highlight highlight_deleting = highlights[0];
            display.getHighlighter().removeHighlight(highlights[0]);  // Removing the highlight of first character
            display.insert(display_str, 0);
            char_pointer = 9;
            display.setCaretPosition(char_pointer);  // Setting the cursor position to start
            // Highlighting the removed character
            if(highlight_deleting.getPainter().equals(greenHighlighter)) {
                display.getHighlighter().addHighlight(display_str.length(), display_str.length()+1, greenHighlighter);
            }else{
                display.getHighlighter().addHighlight(display_str.length(), display_str.length()+1, redHighlighter);
            }
        }catch(Exception e){
            // Showing the error message
            JOptionPane.showMessageDialog(null, e, "Error (Display Analysis)", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int customTime(){
        int time = 60;  // If error occurs then we set timer to 60 seconds

        try{
            String inp = JOptionPane.showInputDialog(null, "Enter Time In Minutes.", "JTyping (Custom Time)", JOptionPane.PLAIN_MESSAGE);
            time =  Math.abs(Integer.parseInt(inp)) * 60;  // Converting Minutes To Seconds And Returning The Value
        }catch(Exception e){
            // Showing the error message
            JOptionPane.showMessageDialog(null, "Enter A Valid Number Without Spaces\n(" + e + ")", "Error (Custom Time)", JOptionPane.ERROR_MESSAGE);
        }

        return time;
    }

    private String customText(){
        String path = "texts/pangrams.exm";  // If error occurs then we set text to pangrams

        try{
            JOptionPane.showMessageDialog(null, "There are few rules to select a text in JTyping.\n\n" +
                    "Rules:\n1. Store The Needed Text In A .txt File And Select That File.\n2. Try To Give A Text File Which Takes More Than 40 Seconds To Type.\n" +
                    "3. JTyping Only Supports ASCII Characters.", "Custom Text Rules", JOptionPane.INFORMATION_MESSAGE);  // Custom Text Rules Message
            JFileChooser jc = new JFileChooser();
            jc.setMultiSelectionEnabled(false);
            jc.showOpenDialog(window);
            path = jc.getSelectedFile().getAbsolutePath();
        }catch(Exception e){
            // Showing the error message
            JOptionPane.showMessageDialog(null, "Select A Valid Document Location.\n(" + e + ")", "Error (Custom Text)", JOptionPane.ERROR_MESSAGE);
            // Showing the displayed text name when error occurs
            JOptionPane.showMessageDialog(null, "You Encountered With An Error Selecting The Text Document\nSo, Loading The Pangrams Text Document.", "JTyping (Custom Text)", JOptionPane.PLAIN_MESSAGE);
            text_name_box.setSelectedIndex(15);  // Pangrams text index is 15
        }

        return path;
    }

    private void playSounds(String url){
        // Plays the sound
        if(sound_var){
            try{
                // Objects... will fetch the files from jar as well. If you're not using jar try to keep those files in the same folder in which you have class
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(url)));
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            }catch(Exception e){
                // Showing the error message
                JOptionPane.showMessageDialog(null, e, "Error (Playing Sound)", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exit(){
        // Stores data before closing
        writeOperation(data_store_file_name, best_wpm + "," + program_used_time + "," + date_time_stored);  // Writing WPM And Program Use Time
        if(timer != null)
            timer.cancel();  // Stopping the timer
        window.dispose();  // Closing the window
    }

    // Inner class

    class Count extends TimerTask{
        // This inner class will update the timer

        public synchronized void run(){

            try{
                time_counter--;
                time_display_lbl.setText("  " + time_counter + " Seconds ");
                if(time_counter == 0 || !typing_started){
                    timer.cancel();  // Stops the timer
                    typing_started = false;
                    time_display_lbl.setText("  " + time_in_seconds + " Seconds ");
                    if(!is_ready){   // If is_ready is false then display analysis
                        playSounds("sounds/completed.wav");
                        displayAnalysis();
                    }
                }
            }catch(Exception e){
                // Showing the error message
                JOptionPane.showMessageDialog(null, e, "Error (Thread)", JOptionPane.ERROR_MESSAGE);
            }

        }

    }

}
