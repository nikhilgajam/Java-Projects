import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import java.io.File;
import java.util.Objects;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import javax.swing.undo.UndoManager;

public class Jp {

    public static void main(String[] args){

        new JSQLite();

    }

}


class JSQLite{

    // Variables
    JFrame window;
    JLabel connection_display;
    JEditorPane output_display;
    JTextArea query_box;
    JButton execute_btn;
    JMenuBar menu_bar;
    JMenu help_menu, db_menu, about_menu;
    JMenuItem help, open_db_settings, create_db_settings, about_us;
    HTMLDocument doc;
    HTMLEditorKit output_kit;
    Connection connection = null;
    Statement stmt;
    ResultSet resultset;
    String db_name = "Data.db";
    String path = "";

    // Constructor
    public JSQLite(){

        // Window
        window = new JFrame("JSQLite");
        window.setLayout(new BorderLayout());

        connect(db_name);

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
        JLabel title = new JLabel("JSQLite");
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

        // Connection Display
        connection_display = new JLabel("Connected To: " + db_name);
        connection_display.setAlignmentX(Component.CENTER_ALIGNMENT);
        connection_display.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        connection_display.setOpaque(true);
        connection_display.setHorizontalAlignment(JLabel.CENTER);
        connection_display.setForeground(Color.decode("#e6e8eb"));
        connection_display.setBackground(Color.decode("#333333"));
        display_panel.add(connection_display);
        display_panel.add(Box.createRigidArea(new Dimension(5, 10)));  // Space between the components

        // Output Display
        output_display = new JEditorPane();
        output_display.setContentType("text/html");
        output_display.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        output_display.setPreferredSize(new Dimension(260, 260));
        // Used to insert text into output_display
        doc = (HTMLDocument) output_display.getDocument();
        output_kit = (HTMLEditorKit) output_display.getEditorKit();
        output_display.setSize(600, 600);
        // Adding styling to output_display
        Style style = output_kit.getStyleSheet().getStyle("body");
        StyleConstants.setBackground(style, Color.decode("#333333"));
        output_display.setForeground(Color.decode("#e6e8eb"));
        // Replacing the white border with grey
        Border border = new LineBorder(Color.decode("#333333"), 4);
        output_display.setBorder(border);
        // Inserting the text to output_display
        try {
            output_kit.insertHTML(doc, doc.getLength(), "<b>Welcome...</b>", 0, 0, null);
        }catch(Exception e){
            e.printStackTrace();
        }

        // If any key is pressed with output_display selected then query_box focus will be requested
        output_display.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e){
                query_box.requestFocusInWindow();
            }
        });

        // Adding shortcut to output_display
        Action action = new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                execute();
            }
        };

        String keyStrokeAndKey = "control ENTER";
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyStrokeAndKey);
        output_display.getInputMap().put(keyStroke, keyStrokeAndKey);
        output_display.getActionMap().put(keyStrokeAndKey, action);

        // output_display settings
        output_display.setEditable(false);
        display_panel.add(new JScrollPane(output_display));
        display_panel.add(Box.createRigidArea(new Dimension(5, 10)));  // Space between the components

        // Query Box
        query_box = new JTextArea("SELECT name FROM sqlite_master WHERE type = 'table';");
        query_box.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        query_box.setForeground(Color.decode("#e6e8eb"));
        query_box.setBackground(Color.decode("#333333"));
        query_box.setWrapStyleWord(true);
        query_box.setLineWrap(true);
        query_box.setRows(3);

        // Adding undo redo to query_box
        UndoManager undo_manager = new UndoManager();
        query_box.getDocument().addUndoableEditListener(undo_manager);
        query_box.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e){
                try{
                    // CTRL+Z == 26
                    if (e.getKeyChar() == 26){
                    undo_manager.undo();
                    }
                    // CTRL+Y == 25
                    if (e.getKeyChar() == 25){
                        undo_manager.redo();
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        display_panel.add(new JScrollPane(query_box));

        // Adding display panel to the window in the center position
        window.add(display_panel, BorderLayout.CENTER);

        // Adding shortcut to query_box
        query_box.getInputMap().put(keyStroke, keyStrokeAndKey);
        query_box.getActionMap().put(keyStrokeAndKey, action);

        // Button panel
        JPanel btn_panel = new JPanel();
        btn_panel.setBackground(Color.decode("#333333"));

        execute_btn = new JButton("Execute Query");
        execute_btn.setForeground(Color.decode("#e6e8eb"));
        execute_btn.setBackground(Color.decode("#333333"));
        execute_btn.addActionListener(e -> execute());
        execute_btn.setFocusable(false);
        btn_panel.add(execute_btn);

        window.add(btn_panel, BorderLayout.SOUTH);

        // Menu
        menu_bar = new JMenuBar();

        // Help menu
        help_menu = new JMenu("Help");

        help = new JMenuItem("Help");
        help.addActionListener(e -> showHelp());
        help_menu.add(help);

        menu_bar.add(help_menu);

        // Databases menu
        db_menu = new JMenu("Databases");

        create_db_settings = new JMenuItem("Create A New SQLite Database");
        create_db_settings.addActionListener(e -> create_new_db());
        db_menu.add(create_db_settings);

        open_db_settings = new JMenuItem("Open Existing SQLite Database");
        open_db_settings.addActionListener(e -> open_existing_db());
        db_menu.add(open_db_settings);

        menu_bar.add(db_menu);

        // About menu
        about_menu = new JMenu("About");

        about_us = new JMenuItem("About Us");
        about_us.addActionListener(e -> aboutUs());
        about_menu.add(about_us);

        menu_bar.add(about_menu);

        // Adding menu bar to window
        window.setJMenuBar(menu_bar);

        // Closing the database before exit
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // Closing the database connection
                try{
                    if(connection != null){
                        connection.close();
                    }
                }catch(SQLException ex){
                    ex.printStackTrace();
                }

                // Closing the window
                window.dispose();
            }
        });

        // Window settings
        window.setVisible(true);
        window.setSize(700, 600);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Query box settings
        window.addWindowFocusListener(new WindowAdapter(){
            public void windowGainedFocus(WindowEvent e){
                query_box.requestFocusInWindow();
            }
        });

        query_box.setCaretPosition(query_box.getText().length());

    }

    // Action Methods

    private void connect(String path){

        try{

            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
            stmt = connection.createStatement();

        }catch(SQLException e){

            e.printStackTrace();

        }

    }

    private void execute(){

        String input = query_box.getText().toLowerCase();

        String[] query_arr = input.split(";");
        for(int i=0; i<query_arr.length; i++){
            input = query_arr[i].trim();

            if(input.equals("")){
                continue;    // Skips the query if that is an empty string
            }

            if(input.startsWith("select")){
                printTable(input, i+1);
            }else{
                executeStmt(input, i+1);
            }
        }

    }

    private void printTable(String query, int line){

        try{
            resultset = stmt.executeQuery(query);

            long count = 0;

            StringBuilder table_str = new StringBuilder();
            table_str.append("<br>Line " + line + ": \"" + query + ";\"<br><br><table style='border: 1px dashed white'><tr>");

            ResultSetMetaData metadata = resultset.getMetaData();
            int columnCount = metadata.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                table_str.append("<th><u>" + metadata.getColumnName(i) + "</u></th>");
            }

            table_str.append("</tr>");

            while (resultset.next()){
                table_str.append("<tr>");

                for (int i = 1; i <= columnCount; i++){
                    table_str.append("<td>" + resultset.getString(i) + "</td>");
                }

                table_str.append("</tr>");
                count++;

            }

            table_str.append("</table>");
            output_kit.insertHTML(doc, doc.getLength(), table_str.toString(), 0, 0, null);
            output_kit.insertHTML(doc, doc.getLength(), count + " rows selected.", 0, 0, null);

        }catch(Exception e){
            try{
                output_kit.insertHTML(doc, doc.getLength(), "<p style='color:red;'>Error At Line " + line + ": \"" + query + ";\"<br>" + e.toString() + "</p>", 0, 0, null);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        // Auto scrolling the output_display
        output_display.setCaretPosition(output_display.getDocument().getLength());

    }

    private void executeStmt(String query, int line){

        try{
            int n = stmt.executeUpdate(query);
            if(n == 1){
                output_kit.insertHTML(doc, doc.getLength(), "<br>Line " + line + ": \"" + query +  ";\"<br>Query executed successfully\n" + n + " row got affected." , 0, 0, null);
            }else{
                output_kit.insertHTML(doc, doc.getLength(), "<br>Line " + line + ": \"" + query +  ";\"<br>Query executed successfully\n" + n + " rows got affected." , 0, 0, null);
            }
        }catch(Exception e){
            try{
                output_kit.insertHTML(doc, doc.getLength(), "<p style='color:red;'>Error At Line " + line + ": \"" + query + ";\"<br>" + e.toString() + "</p>", 0, 0, null);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        // Auto scrolling the output_display
        output_display.setCaretPosition(output_display.getDocument().getLength());

    }

    private void create_new_db(){

        try{
            JFileChooser jc = new JFileChooser();
            jc.showDialog(null, "Create Database");
            path = jc.getSelectedFile().getAbsolutePath().toString();
            db_name = new File(path).getName();
            connection_display.setText("Connected To: " + db_name);
            connect(path);
        }catch(Exception e){
            db_name = "Data.db";
            connection_display.setText("Connected To: " + db_name);
            connect(db_name);
            try {
                output_kit.insertHTML(doc, doc.getLength(), "<p style='color:red;'>Select A Valid Save Path</p>", 0, 0, null);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        
        // Auto scrolling the output_display
        output_display.setCaretPosition(output_display.getDocument().getLength());

    }

    private void open_existing_db(){

        try{
            JFileChooser jc = new JFileChooser();
            jc.showDialog(null, "Open Database");
            path = jc.getSelectedFile().getAbsolutePath().toString();
            db_name = new File(path).getName();
            connection_display.setText("Connected To: " + db_name);
            connect(path);
        }catch(Exception e){
            db_name = "Data.db";
            connection_display.setText("Connected To: " + db_name);
            connect(db_name);
            try {
                output_kit.insertHTML(doc, doc.getLength(), "<p style='color:red;'>Select A Valid DB Path</p>", 0, 0, null);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        // Auto scrolling the output_display
        output_display.setCaretPosition(output_display.getDocument().getLength());

    }

    private void showHelp(){

        JOptionPane.showMessageDialog(window, "JSQLite is a SQLite Engine With Java Swing GUI\n\nHelp:\nYou Can Create And Open Databases Using Database Menu\nYou Can Use CTRL+ENTER Keyboard Shortcut (OR) Execute Button To Execute SQL Queries\n" +
        "You Can Copy The Text In Output Display By Selecting The Text And Using CTRL+C Keyboard Shortcut\nYou Can Use CTRL+Z(UNDO) And CTRL+Y(REDO) Keyboard Shortcuts" , "Help", JOptionPane.PLAIN_MESSAGE);

    }

    private void aboutUs(){

        JOptionPane.showMessageDialog(window, "JSQLite Version 1.0\nDeveloped By Nikhil", "About Us", JOptionPane.PLAIN_MESSAGE);

    }


}
