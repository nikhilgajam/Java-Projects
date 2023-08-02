import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Utilities;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import java.util.TimerTask;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.Date;


public class Jp{

    public static void main(String[] args){

        new JTyping();  // Anonymous object

    }

}


class JTyping{

    // Widgets
    private final JFrame window;
    private final JLabel title_lbl, time_display_lbl;
    private final JTextArea display;
    private final JScrollPane display_scroll_pane;
    private final JComboBox<String> text_name_box, time_box;
    private Timer timer;

    // Variables
    private final String all_chars = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789\n`~!@#$%^&*()-_=+\\|{}[]:;\"',<>./?'";
    private final String[] text_names = {"Random", "Astronauts", "Babbage", "Baseball", "Cast", "Credits", "DNA", "Fables", "Franklin", "Girl", "Hill", "Hubble", "Insects", "Jane", "Lincoln", "Netiquette", "Pangrams", "Photo", "Rabbits", "Strebel", "Yosemite", "ZNumbers1", "ZNumbers2", "Custom Text"};
    private final String[] time_names = {"46 Seconds", "1 Minute", "2 Minutes", "3 Minutes", "5 Minutes", "10 Minutes", "15 Minutes", "30 Minutes", "46 Minutes", "1 Hours", "2 Hours", "3 Hours", "Custom Time"};
    private int char_pointer = 0, correct_chars = 0, error_chars = 0, best_wpm = 0, time_in_seconds = 0, time_counter = 0, display_font_size = 25, time_box_selected_index = 0, line_no = 0, win_w = 1041, win_h = 670;
    private double program_used_time = 0;
    private String text_data = "", data_store_file_name = "store.data", date_time_stored = "", display_font_style = "Times New Roman";
    private boolean typing_started = false, stop_typing = true, sound_var = true, is_ready = false, is_maximized = false;
    private final HighlightPainter greenHighlighter = new DefaultHighlighter.DefaultHighlightPainter(new Color(60, 128, 78));
    private final HighlightPainter redHighlighter = new DefaultHighlighter.DefaultHighlightPainter(new Color(220, 4, 4));

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
        display.setFont(new Font(display_font_style, Font.PLAIN, display_font_size));
        display.setWrapStyleWord(true);
        display.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));   // Changing cursor to arrow
        display.getCaret().setBlinkRate(0);  // Making the cursor to not blink
        DefaultCaret caret = (DefaultCaret) display.getCaret();   // These three lines are important to get the changes get reflected without typing on the display area
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
        display.getInputMap().put(KeyStroke.getKeyStroke("PAGE_UP"), "none");  // Making page up to not get triggered
        display.getInputMap().put(KeyStroke.getKeyStroke("PAGE_DOWN"), "none");  // Making page down to not get triggered
        display.addMouseListener(new MouseAdapter(){
            // When you click any mouse button then the caret is pointed to current position
            @Override
            public void mousePressed(MouseEvent e){
                super.mousePressed(e);
                display.setCaretPosition(char_pointer);
            }

            @Override
            public void mouseReleased(MouseEvent e){
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
        time_box.setToolTipText("Select a time interval");
        time_box.setFocusable(false);
        time_box.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        time_box.addActionListener(e -> textOrTimeSelected());
        time_box.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                super.mouseClicked(e);
                playSounds("sounds/click.wav");   // Playing the click sound when this box is clicked
            }
        });
        panel.add(time_box, BorderLayout.WEST);

        // Text name combo box
        text_name_box = new JComboBox<>(text_names);
        text_name_box.setToolTipText("Select a typing text");
        text_name_box.setFocusable(false);
        text_name_box.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        text_name_box.addActionListener(e -> textOrTimeSelected());
        text_name_box.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                super.mouseClicked(e);
                playSounds("sounds/click.wav");   // Playing the click sound when this box is clicked
            }
        });
        panel.add(text_name_box, BorderLayout.CENTER);

        // Time display label
        time_display_lbl = new JLabel("  0 Seconds ");
        time_display_lbl.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        panel.add(time_display_lbl, BorderLayout.EAST);

        // Reading and storing the dictionary.data document and presenting start screen
        initVariables();
        startScreen();

        // Changing the functionality of close button
        window.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                exit();
            }
        });

        // To detect if the screen is maximized or restored
        window.addWindowStateListener(new WindowAdapter(){
            @Override
            public void windowStateChanged(WindowEvent e){
                // Minimized
//                if((e.getNewState() & JFrame.ICONIFIED) == JFrame.ICONIFIED){
//                    System.out.println("Minimized");
//                }

                // Maximized
                if((e.getNewState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH){
                    is_maximized = true;
                }

                // Restored
                if(e.getNewState() == JFrame.NORMAL){
                    is_maximized = false;
                }
            }
        });

        // Window settings
        window.setSize(win_w, win_h);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ImageIcon img = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/icon.png"))); // Icon
        window.setIconImage(img.getImage());
        window.setVisible(true);

    }


    // Methods


    private void initVariables(){
        try{
            // store.data handling
            String data;
            File file = new File(data_store_file_name);

            // Write the file if file doesn't exist
            if(!file.exists()){
                date_time_stored = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss a").format(new Date().getTime());

                // File Data Sequence: window_width, window_height, font_style, font_size, best_wpm, program_used_time, time_box_selected_index, date_time_stored, sound_state, screen_maximized_state
                writeOperation(data_store_file_name, win_w + "," + win_h + "," + display_font_style +"," + display_font_size + "," + best_wpm + "," + program_used_time + "," + time_box.getSelectedIndex() + "," + date_time_stored + "," + sound_var + "," + is_maximized, false);
            }

            data = readOperation(data_store_file_name);  // Reading store.data
            String[] arr = data.split(",");  // Separating the read string

            // Storing the read data in the variables
            win_w = Integer.parseInt(arr[0]);
            win_h = Integer.parseInt(arr[1]);
            display_font_style = arr[2];
            display_font_size = Integer.parseInt(arr[3]);
            best_wpm = Integer.parseInt(arr[4]);
            program_used_time = Double.parseDouble(arr[5]);
            time_box_selected_index = Integer.parseInt(arr[6]);
            date_time_stored = arr[7];
            sound_var = Boolean.parseBoolean(arr[8]);
            is_maximized = Boolean.parseBoolean(arr[9]);

            // If the screen is set to maximized then maximize when the window opens
            if(is_maximized){
                window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }

            // Playing the intro sound
            playSounds("sounds/intro.wav");

            // Setting the saved display properties
            display.setFont(new Font(display_font_style, Font.PLAIN, display_font_size));

            // Setting the random text
            int rand_text_number = new Random().nextInt(text_names.length - 1);  // All texts without custom option
            text_name_box.setSelectedIndex(rand_text_number);
            display.setCaretPosition(display.getDocument().getLength());

            // Setting the time to time_box to selected index
            time_box.setSelectedIndex(time_box_selected_index);
        }catch(Exception e){
            // Showing the error message
            JOptionPane.showMessageDialog(null, e, "Error (Init Variables)", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void startScreen(){
        // Help Or Start Screen
        try{
            // Setting the variables to their default values
            typing_started = false;
            stop_typing = true;
            is_ready = false;

            // Setting the initial text to the screen
            text_data = "Welcome...\n\n" +
                    "JTyping Is A Typing Improvement And Analysis Program.\n" +
                    "You Can Toggle The Sound On/Off By Pressing The \"F6\" Key.\n" +
                    "You Can Press \"ESC\" Key To Stop The Timer And Show Analysis.\n" +
                    "You Can See Your Text Typing History By Pressing The \"F12\" Key.\n" +
                    "You Can Select Any Time And Text Options Provided In The Picklists Given Below.\n" +
                    "You Can Select Custom Time And Text By Choosing Custom Option Provided In The Picklist.\n" +
                    "You Can Decrease/Increase Text Size Using \"F7\"/\"F8\" Keys And Change Style Using \"F11\" Key.\n\n" +
                    "Press \"F5\" To Start Typing The Randomly Selected Text.\n\n" +
                    "Your Best WPM : " + best_wpm + " Words Per Minute (" + getTypingLevel(best_wpm) + " Level)\n" +
                    "You Spent " + program_used_time + " Minutes Of Time Using JTyping Since " + date_time_stored + "\n\n" +
                    "JTyping\nVersion 1.0\nDeveloped By Nikhil";
            display.setText(text_data);
            char_pointer = 10;
            display.setCaretPosition(char_pointer);

            // If the sound_var is false then show sound off message
            if(!sound_var){
                time_display_lbl.setText("  Sound: Off  ");
            }
        }catch(Exception e){
            // Showing the error message
            JOptionPane.showMessageDialog(null, e, "Error (Start)", JOptionPane.ERROR_MESSAGE);
        }
    }


    private String readFromJAR(String text_path){
        // Reading from the JAR
        StringBuilder data = new StringBuilder();

        try{
            InputStream data_path = getClass().getResourceAsStream(text_path);  // To get the data inside JAR
            assert data_path != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(data_path, StandardCharsets.UTF_8));   // To read utf-8 text

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
        // Reading from the disk
        StringBuilder data = new StringBuilder();
        try{
            FileInputStream read = new FileInputStream(url);

            // Reading
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


    private void writeOperation(String url, String data, boolean append){
        // Write to the disk
        try{
            FileOutputStream write = new FileOutputStream(url, append);
            for(int i=0; i < data.length(); i++){
                write.write(data.charAt(i));  // Writing data
            }
            write.close();
        }catch(Exception e){
            // Showing the error message
            JOptionPane.showMessageDialog(null, e, "Error (Write Operation)", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void keyOperations(KeyEvent e){
        // Invoked when you press any key on your keyboard
        try{
            int code = e.getKeyCode();

            if(code == KeyEvent.VK_F1){  // Start screen if presented, if F1 key is pressed
                if(stop_typing){  // If typing is halted then start screen will be displayed
                    startScreen();
                }
            }else if(code == KeyEvent.VK_F6){  // If F6 is pressed we can toggle sound on/off
                sound_var = !sound_var;
                if(sound_var){
                    time_display_lbl.setText("  Sound: On  ");
                }else{
                    time_display_lbl.setText("  Sound: Off  ");
                }
            }else if(code == KeyEvent.VK_F7){  // If F7 is pressed then font size decreases
                fontSettings("decrease");
                time_display_lbl.setText("  Font Size: " + display_font_size + "  ");
            }else if(code == KeyEvent.VK_F8){  // If F8 is pressed then font size increases
                fontSettings("increase");
                time_display_lbl.setText("  Font Size: " + display_font_size + "  ");
            }else if(code == KeyEvent.VK_F11){  // If F11 is pressed then we can change font style
                fontSettings("style");
                time_display_lbl.setText("  Font Style: " + display.getFont().getFontName() + "  ");
            }else if(code == KeyEvent.VK_F12){  // If F12 is pressed then history is displayed
                displayHistory();
            }

            // If typing is stopped then we can use F5 to start typing with selected text
            if(stop_typing){
                if(code == KeyEvent.VK_F5){
                    textOrTimeSelected();
                }
                return;   // If stop_typing is true then no below statements should be executed but above statements can
            }

            // When backspace key is pressed
            if(code == KeyEvent.VK_BACK_SPACE){
                if(char_pointer-1 >= 0 && (text_data.charAt(char_pointer-1) != ' ' && text_data.charAt(char_pointer - 1) != '\n')){
                    Highlighter.Highlight[] highlights = display.getHighlighter().getHighlights();
                    if(highlights.length > 0){  // If some letter got highlighted then only below statements should be executed
                        Highlighter.Highlight highlight_deleting = highlights[highlights.length - 1];
                        display.getHighlighter().removeHighlight(highlight_deleting);  // Removing the highlight of first character

                        // Decrement the number by one depending on the before highlighted color
                        if(highlight_deleting.getPainter().equals(greenHighlighter)){
                            correct_chars--;
                        }else{
                            error_chars--;
                        }

                        // Decrementing the char_pointer
                        char_pointer--;
                        display.setCaretPosition(char_pointer);  // Setting the cursor position to char_pointer location
                    }
                }else{
                    playSounds("sounds/error2.wav");  // When we press backspace if there is a space or \n then play error sound
                }
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

                // Updating the display to show good amount of characters on display when last letter of line is encountered in display
                int new_line_no = getDisplayLineNumber(char_pointer + 1);  // Getting the line number changes that's 1 is added
                if(text_data.charAt(char_pointer) == '\n' || line_no != new_line_no){
                    display_scroll_pane.getViewport().scrollRectToVisible(display.getVisibleRect());  // We are using same method many times to get the display the present line at top
                    display_scroll_pane.getViewport().scrollRectToVisible(display.getVisibleRect());
                    display_scroll_pane.getViewport().scrollRectToVisible(display.getVisibleRect());
                    display_scroll_pane.getViewport().scrollRectToVisible(display.getVisibleRect());
                    line_no = new_line_no;  // Setting the line_no to new one
                }

                // Timer is started only if is_ready is true
                if(is_ready){
                    if(timer != null){
                        timer.purge();  // Clears timer scheduled list
                    }
                    typing_started = true;
                    timer = new Timer();
                    timer.schedule(new Count(), 0, 1000);  // Starting the timer which will run every 1 second
                    is_ready = false;
                }

                // When \n is there we can press space also
                if((ch == ' ' && text_data.charAt(char_pointer) == '\n')){
                    display.getHighlighter().addHighlight(char_pointer, char_pointer+1, greenHighlighter);
                    correct_chars++;
                }else{
                    if(ch == text_data.charAt(char_pointer)){
                        display.getHighlighter().addHighlight(char_pointer, char_pointer+1, greenHighlighter);
                        correct_chars++;
                    }else{
                        display.getHighlighter().addHighlight(char_pointer, char_pointer+1, redHighlighter);
                        playSounds("sounds/error.wav");  // Playing a sound when a character typed incorrectly
                        error_chars++;
                    }
                }

                // When we reach the end of the text then we need to stop the execution and show analysis
                if(char_pointer == text_data.length() - 1){
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
        // Invoked when text or time picklist item is selected

        // This will remove unwanted before highlights, and it is important to not get inserted text highlighted
        display.getHighlighter().removeAllHighlights();

        try{
            // Setting the variables to its default values
            typing_started = false;
            is_ready = false;
            stop_typing = false;
            char_pointer = correct_chars = error_chars = time_in_seconds = time_counter = line_no = 0;

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
            }else if(text_name_selected.equals("Random")){
                text_data = randomTextLoader(text_path);
            }else{
                text_data = readFromJAR(text_path);
            }

            text_data = text_data.replace("\r", "");  // Replacing the carriage return to enter and storing
            text_data = text_data.replace("  ", " ");  // Replacing two space with single space
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
        // Displays the typed text and its analysis

        // Stop typing until you press a key indicating with this boolean variable
        stop_typing = true;

        // WPM Information: https://www.speedtypingonline.com/typing-equations
        double time_taken = Math.abs(time_in_seconds - time_counter);  // Duration = Time Selected - Time Spent In Typing
        double time_taken_in_minutes = time_taken / 60.0;  // Divide by 60 to convert seconds to minutes
        double chars_typed = (correct_chars + error_chars);
        double chars_typed_divided_by_5 = chars_typed / 5.0;
        double errors = error_chars;

        // Gross WPM(Words Per Minute) = (No. of chars typed/5)/Time taken (in minutes)
        double gross_wpm = Math.round((chars_typed_divided_by_5) / (time_taken_in_minutes));
        // Net WPM(Words Per Minute) = ((No. of chars typed/5)-Errors)/Time taken (in minutes) (OR) Gross WPM - (Errors/Time taken (in minutes))
        double net_wpm = Math.round((chars_typed_divided_by_5 - errors) / (time_taken_in_minutes));
        // Accuracy = (Correct characters/All Characters)*100
        double accuracy = ((double)correct_chars / chars_typed) * 100.0;
        // Gross strokes = All Characters Typed
        int gross_strokes = (int)chars_typed;
        // Error hits = no of errors typed
        int error_hits = error_chars;
        // Net strokes = gross - error hits
        int net_strokes = gross_strokes - error_hits;

        // Adding the new typed time to old typed time
        program_used_time += time_taken_in_minutes;  // Converting minutes to hours
        program_used_time = Math.round(program_used_time * 100.0) / 100.0;  // Rounding the time to two digits

        // Best WPM and program used time calculation and storing
        if(net_wpm > best_wpm){
            best_wpm = (int)net_wpm;
        }

        // Writing data to store.data
        writeOperation(data_store_file_name, win_w + "," + win_h + "," + display_font_style +"," + display_font_size + "," + best_wpm + "," + program_used_time + "," + time_box.getSelectedIndex() + "," + date_time_stored + "," + sound_var + "," + is_maximized, false);

        // Writing the score to score.data
        if(net_wpm >= 0){
            String time_str = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss a").format(new Date().getTime());
            writeOperation("history.data", (int)net_wpm + ", " + (int)accuracy + "%\t" + (int)time_taken + "\t" + getTypingLevel((int)net_wpm) + "\t" + text_names[text_name_box.getSelectedIndex()] + "\t" + time_str + "\n", true);
        }

        // Displaying the analysis
        text_data = "Analysis:\n\n";
        text_data += "Duration\t:    " + (int)time_taken + " Seconds Of Total " + time_in_seconds + " Seconds\n";
        text_data += "Gross Speed\t:    " + (int)gross_wpm + " Words Per Minute (WPM)\n";
        if(net_wpm < 0){
            text_data += "*Net Speed\t:    Cannot Be Calculated [Try To Type For More Time With Less Mistakes]\n";
        }else{
            text_data += "*Net Speed\t:    " + (int)net_wpm + " Words Per Minute (WPM)      [Typing Level :  " + getTypingLevel((int)net_wpm) + "]\n";
        }
        text_data += "Error Hits\t:    " + error_hits + "\n";
        text_data += "Accuracy\t:    " + (int)accuracy + " %\n";
        text_data += "Gross Strokes\t:    " + gross_strokes + "\n";
        text_data += "*Net Strokes\t:    " + net_strokes + "\n\n";
        text_data += "Typing Speed Levels (In WPM):   [Press \"F12\" Key To View Typing History]\n";
        text_data += "\n\n";
        text_data += "Your Best WPM : " + best_wpm + " Words Per Minute (" + getTypingLevel(best_wpm) + " Level)\n";
        text_data += "You Spent " + program_used_time + " Minutes Of Time Using JTyping Since " + date_time_stored + "\n\n";
        text_data += "If You Want To Type The Same Text Again Press \"F5\" Key On Your Keyboard\n\n";
        text_data += "Text You Typed:\n";

        // Dealing with highlights and showing the data on screen
        try{
            // Inorder to place the text in the start we need to remove the first highlighted character otherwise that
            // will highlight will be given to inserted text to avoid that we are removing that highlight and adding
            // it after adding
            Highlighter.Highlight[] highlights = display.getHighlighter().getHighlights();
            if(highlights.length > 0){  // If highlight is present then length will be greater than zero
                Highlighter.Highlight highlight_deleting = highlights[0];
                display.getHighlighter().removeHighlight(highlight_deleting);  // Removing the highlight of first character

                display.insert(text_data, 0);
                char_pointer = 9;
                display.setCaretPosition(char_pointer);  // Setting the cursor position to start

                // Highlighting the removed character
                if(highlight_deleting.getPainter().equals(greenHighlighter)) {
                    display.getHighlighter().addHighlight(text_data.length(), text_data.length() + 1, greenHighlighter);
                }else{
                    display.getHighlighter().addHighlight(text_data.length(), text_data.length() + 1, redHighlighter);
                }
            }
        }catch(Exception e){
            // Showing the error message
            JOptionPane.showMessageDialog(null, e, "Error (Display Analysis)", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void updateRealtimeAnalysis(int state){
        // Updates the window title with realtime analysis
        if(state == 0){  // Off state
            window.setTitle("JTyping");
        }else{  // On state
            double time_taken = Math.abs(time_in_seconds - time_counter);  // Duration = Time Selected - Time Spent In Typing
            double time_taken_in_minutes = time_taken / 60.0;  // Divide by 60 to convert seconds to minutes
            double chars_typed = (correct_chars + error_chars);
            double chars_typed_divided_by_5 = chars_typed / 5.0;

            // Gross WPM(Words Per Minute) = (No. of chars typed/5)/Time taken (in minutes)
            int gross_wpm = (int)Math.round((chars_typed_divided_by_5) / (time_taken_in_minutes));
            // Accuracy = (Correct characters/All Characters)*100
            int accuracy = (int)(((double)correct_chars / chars_typed) * 100.0);

            // Updating the window title
            window.setTitle("JTyping | " + "WPM: " + gross_wpm + " | Accuracy: " + accuracy + "%");
        }
    }


    private String getTypingLevel(int speed){
        // Returns the typing level
        // (0-25 = Slow)      (26-45 = Average)      (46-65 = Fluent)      (66-80 = Fast)      (81-∞ = Insane)
        if(speed >= 0 && speed <= 25){
            return "Slow";
        }else if(speed >= 26 && speed <= 45){
            return "Average";
        }else if(speed >= 46 && speed <= 65){
            return "Fluent";
        }else if(speed >= 66 && speed <= 80){
            return "Fast";
        }else{
            return "Insane";
        }
    }


    private void fontSettings(String operation){
        // Text settings
        if(operation.equals("increase")  && display_font_size <= 115){  // Increasing font size
            display_font_size++;
            display.setFont(new Font(display_font_style, Font.PLAIN, display_font_size));
        }else if(operation.equals("decrease") && display_font_size >= 16){  // Decreasing font size
            display_font_size--;
            display.setFont(new Font(display_font_style, Font.PLAIN, display_font_size));
        }else if(operation.equals("style")){  // Changing font size
            display_font_style = JOptionPane.showInputDialog(null, "Enter A Valid Style. (Default Style: Times New Roman)", "JTyping (Font Settings)", JOptionPane.PLAIN_MESSAGE);
            display.setFont(new Font(display_font_style, Font.PLAIN, display_font_size));
            display_font_style = display.getFont().getFontName();
        }
    }


    private String randomTextLoader(String path){
        // Select a random paragraph from random.exm and returns
        String data = readFromJAR(path);

        data = data.replace("\r", "");  // Replacing the carriage return to enter and storing
        data = data.replace("  ", " ");  // Replacing two space with single space
        data = data.replace(" \n", "\n");  // Replacing space and enter with just enter
        data = data.trim();  // Removing unwanted spaces and newline at start and end

        // Splitting the text into paragraphs and selecting a random paragraph
        String[] str_arr = data.split("\n\n");
        int rand_num = new Random().nextInt(str_arr.length);

        return str_arr[rand_num];
    }


    private int customTime(){
        // Enables user to select custom time
        int time = 60;  // If error occurs then we set timer to 60 seconds

        if(window.isVisible()){  // Show custom time box only if window is visible
            try{
                String inp = JOptionPane.showInputDialog(null, "Enter Time (In Minutes).", "JTyping (Custom Time)", JOptionPane.PLAIN_MESSAGE);
                time =  Math.abs(Integer.parseInt(inp)) * 60;  // Converting Minutes To Seconds And Returning The Value
            }catch(Exception e){
                // Showing the error message
                JOptionPane.showMessageDialog(null, "Enter A Valid Number Without Spaces\n(" + e + ")", "Error (Custom Time)", JOptionPane.ERROR_MESSAGE);
            }
        }

        return time;
    }


    private String customText(){
        // Enables user to select custom text
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


    private int getDisplayLineNumber(int caretPos){
        // Returns the display line number
        if(caretPos > text_data.length()){  // If caretPos > text length then return 0
            return 0;
        }

        int rowNum = (caretPos == 0) ? 1 : 0;
        try{
            for(int offset = caretPos; offset > 0; ){
                offset = Utilities.getRowStart(display, offset) - 1;
                rowNum++;
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "Error (Display Line Number)", JOptionPane.ERROR_MESSAGE);
        }

        return rowNum;
    }


    private void displayHistory(){
        // The history can only be displayed if typing is not started
        if(stop_typing){
            File file = new File("history.data");
            display.setText("History:\n\n");

            if(!file.exists()){
                // If history.data is not created then it shows this message
                display.append("There's no history to show.\n[Try to type some text and check here]");
            }else{
                // Displaying history
                display.append("Speed, Acc\tDuration\tSpeed Level\tText Name\tDate And Time\n");
                display.append("(WPM, %)\t(Seconds)\n");
                display.append(readOperation("history.data"));  // Reading history.data and appending to the display
                display.append("\n[Number of entries :  " + (display.getLineCount() - 5) + "]");  // Only counting the data lines
            }

            char_pointer = 8;
            display.setCaretPosition(char_pointer);
        }
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
        // Storing the window size if the screen is not maximized
        if(!is_maximized){
            win_w = window.getWidth();
            win_h = window.getHeight();
        }

        // Stores data before closing
        writeOperation(data_store_file_name, win_w + "," + win_h + "," + display_font_style +"," + display_font_size + "," + best_wpm + "," + program_used_time + "," + time_box.getSelectedIndex() + "," + date_time_stored + "," + sound_var + "," + is_maximized, false);  // Writing WPM And Program Use Time
        if(timer != null){
            timer.cancel();  // Stopping the timer
            timer.purge();  // Clears the scheduled list
        }
        window.dispose();  // Closing the window
        System.exit(0);  // Closing all running threads and statements
    }


    // Inner class


    class Count extends TimerTask{
        // This inner class will update the timer

        public synchronized void run(){
            try{
                time_counter--;
                time_display_lbl.setText("  " + time_counter + " Seconds ");
                updateRealtimeAnalysis(1);  // Starting the realtime analysis
                if(time_counter == 0 || !typing_started){
                    typing_started = false;
                    timer.cancel();  // Stops the timer
                    if(!is_ready){   // If is_ready is false then display analysis
                        playSounds("sounds/completed.wav");
                        displayAnalysis();
                        time_counter = -1;  // Setting the counter to -1 which indicates timer is not running
                    }
                    timer.purge();  // Clears timer scheduled list
                    updateRealtimeAnalysis(0);  // Halting the realtime analysis
                }
            }catch(Exception e){
                // Showing the error message
                JOptionPane.showMessageDialog(null, e, "Error (Thread)", JOptionPane.ERROR_MESSAGE);;
            }
        }

    }

}
