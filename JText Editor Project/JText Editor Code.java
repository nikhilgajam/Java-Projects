import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.undo.UndoManager;
import java.util.Date;

public class Jp {

    public static void main(String[] args){

        new JTextEditor();

    }

}

class JTextEditor extends JFrame{

    Date date;
    JTextArea editor;
    JLabel status_bar;
    JMenuBar menu_bar;
    JPopupMenu popup_menu;
    JMenu file_menu, edit_menu, settings_menu,about_menu;
    JMenuItem new_j, open, save, save_as, quit, cut, copy, paste, select, undo, redo, count, search, replace, time, size, style, text, b_color, t_color, about, undo_p, redo_p, cut_p, copy_p, paste_p, save_p;
    Font font = new Font("Calibri", Font.PLAIN, 18);
    String path = "";
    int opened = 0;
    int saved = 0;

    public JTextEditor(){

        try {
            // Nimbus Theme in Swing
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Theme Error", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // JTextArea
        editor = new JTextArea();
        editor.setMargin(new Insets(3, 3, 3, 3));
        editor.setFont(font);
        add(new JScrollPane(editor), BorderLayout.CENTER);

        // Status Bar
        status_bar = new JLabel(" Welcome To JText Editor ");
        add(status_bar, BorderLayout.SOUTH);

        UndoManager ur_manager = new UndoManager();
        editor.getDocument().addUndoableEditListener(ur_manager);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quit();    // Changing the functionality of close button
            }
        });

        // Menu Bar
        menu_bar = new JMenuBar();

        file_menu = new JMenu("File");
        edit_menu = new JMenu("Edit");
        settings_menu = new JMenu("Settings");
        about_menu = new JMenu("About");

        // Menu Declarations

        new_j = new JMenuItem("New");
        new_j.addActionListener(e -> new_j());
        new_j.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        file_menu.add(new_j);

        file_menu.addSeparator();

        open = new JMenuItem("Open");
        open.addActionListener(e -> open());
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        file_menu.add(open);

        save = new JMenuItem("Save");
        save.addActionListener(e -> save());
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        file_menu.add(save);

        save_as = new JMenuItem("Save As");
        save_as.addActionListener(e -> save_as());
        file_menu.add(save_as);

        file_menu.addSeparator();

        quit = new JMenuItem("Quit");
        quit.addActionListener(e -> quit());
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        file_menu.add(quit);

        cut = new JMenuItem("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        cut.addActionListener(e -> editor.cut());
        edit_menu.add(cut);

        copy = new JMenuItem("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        copy.addActionListener(e -> editor.copy());
        edit_menu.add(copy);

        paste = new JMenuItem("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        paste.addActionListener(e -> editor.paste());
        edit_menu.add(paste);

        select = new JMenuItem("Select All");
        select.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        select.addActionListener(e -> editor.selectAll());
        edit_menu.add(select);

        edit_menu.addSeparator();

        undo = new JMenuItem("Undo");
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        undo.addActionListener(e -> ur_manager.undo());
        edit_menu.add(undo);

        redo = new JMenuItem("Redo");
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
        redo.addActionListener(e -> ur_manager.redo());
        edit_menu.add(redo);

        edit_menu.addSeparator();

        count = new JMenuItem("Count");
        count.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK));
        count.addActionListener(e -> count());
        edit_menu.add(count);

        search = new JMenuItem("Search");
        search.addActionListener(e -> search());
        edit_menu.add(search);

        replace = new JMenuItem("Replace All");
        replace.addActionListener(e -> replace());
        replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        edit_menu.add(replace);

        date = new Date();
        time = new JMenuItem("Date And Time");
        time.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
        time.addActionListener(e -> editor.append(date.toString()));
        edit_menu.add(time);

        size = new JMenuItem("Text Size");
        size.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
        size.addActionListener(e -> text_size());
        settings_menu.add(size);

        style = new JMenuItem("Text Style");
        style.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
        style.addActionListener(e -> text_style());
        settings_menu.add(style);

        text = new JMenuItem("Text Font");
        text.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK));
        text.addActionListener(e -> text_font());
        settings_menu.add(text);

        settings_menu.addSeparator();

        t_color = new JMenuItem("Text Color");
        t_color.addActionListener(e -> text_color());
        t_color.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
        settings_menu.add(t_color);

        b_color = new JMenuItem("Background Color");
        b_color.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK));
        b_color.addActionListener(e -> background_color());
        settings_menu.add(b_color);

        about = new JMenuItem("About");
        about.addActionListener(e -> help());
        about_menu.add(about);

        // Menu adding
        menu_bar.add(file_menu);
        menu_bar.add(edit_menu);
        menu_bar.add(settings_menu);
        menu_bar.add(about_menu);
        setJMenuBar(menu_bar);

        // Popup Menu
        popup_menu = new JPopupMenu();

        undo_p = new JMenuItem("Undo             ");
        undo_p.addActionListener(e -> ur_manager.undo());
        popup_menu.add(undo_p);

        redo_p = new JMenuItem("Redo");
        redo_p.addActionListener(e -> ur_manager.redo());
        popup_menu.add(redo_p);

        popup_menu.addSeparator();

        cut_p = new JMenuItem("Cut");
        cut_p.addActionListener(e -> editor.cut());
        popup_menu.add(cut_p);

        copy_p = new JMenuItem("Copy");
        copy_p.addActionListener(e -> editor.copy());
        popup_menu.add(copy_p);

        paste_p = new JMenuItem("Paste");
        paste_p.addActionListener(e -> editor.paste());
        popup_menu.add(paste_p);

        popup_menu.addSeparator();

        save_p = new JMenuItem("Save");
        save_p.addActionListener(e -> save());
        popup_menu.add(save_p);

        editor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                show_popup_menu(e);
            }
        });


        // Add all the window settings after the widgets

        setTitle("JText Editor");
        setVisible(true);
        setSize(747, 497);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    // Actions of the menu button clicks


    private void show_popup_menu(MouseEvent m_event){

        if(m_event.isPopupTrigger())
            popup_menu.show(m_event.getComponent(), m_event.getX(), m_event.getY());

    }

    public void new_j(){

        String data = editor.getText();

        if(saved == 1){
            editor.setText("");
        }else{
            if(data.equals("")){
                editor.setText("");
            }else{
                int yn = JOptionPane.showConfirmDialog(null, "Do You Want To Save The Data Before Clearing?", "JText Editor", JOptionPane.YES_NO_CANCEL_OPTION);
                if(yn == JOptionPane.YES_OPTION){
                    int get = save_as();
                    if(get == 1){
                        editor.setText("");
                        status_bar.setText(" New ");
                    }
                }else if(yn == JOptionPane.NO_OPTION){
                    editor.setText("");
                    status_bar.setText(" New ");
                }
            }
        }

    }

    public void open(){

        try {
            JFileChooser jc = new JFileChooser();
            jc.showOpenDialog(null);
            path = jc.getSelectedFile().getAbsoluteFile().toString();
            File read = new File(path);
            FileReader r = new FileReader(read);
            StringBuilder data = new StringBuilder();
            int i = r.read();
            while(i != -1){
                data.append((char) i);
                i = r.read();
            }
            editor.setText(data.toString());
            status_bar.setText(" " + path + "   Opened");
            opened = 1;
            saved = 0;

        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Error in opening", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void save(){

        if(opened == 1) {
            try {
                File write = new File(path);
                FileWriter w = new FileWriter(write);
                String data = editor.getText();
                w.write(data);
                w.close();
                status_bar.setText(" " + path + "   Saved");
                saved = 1;

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error in Saving", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }else {
            save_as();
        }

    }

    public int save_as(){

        try{
            JFileChooser jc = new JFileChooser();
            jc.showSaveDialog(null);
            String save_as = jc.getSelectedFile().getAbsolutePath();
            File check = new File(save_as);
            if(check.exists()) {
                int yn = JOptionPane.showConfirmDialog(null, "This file already exists do you want to replace?", "JText Editor", JOptionPane.YES_NO_CANCEL_OPTION);
                if(yn == JOptionPane.YES_OPTION){
                    File write = new File(save_as);
                    FileWriter w = new FileWriter(write);
                    String data = editor.getText();
                    w.write(data);
                    w.close();
                    status_bar.setText(" " + save_as + "   Replaced Successfully");
                    saved = 1;
                }else if(yn == JOptionPane.NO_OPTION) {
                    status_bar.setText(" " + save_as + "   Not Saved");
                }
            }else{
                File write = new File(save_as);
                FileWriter w = new FileWriter(write);
                String data = editor.getText();
                w.write(data);
                w.close();
                status_bar.setText(" " + save_as + "   Saved Successfully");
                saved = 1;
            }
            return 1;

        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Error in Saving", "Error", JOptionPane.ERROR_MESSAGE);
            return 0;
        }

    }

    public void quit(){

        String data = editor.getText();
        if(saved == 1){
            dispose();
        }else{

            if(data.equals("")){
                dispose();  // Destroys the present window
            }else{
                int yn = JOptionPane.showConfirmDialog(null, "Do You Want To Save The Data Before Quitting?", "JText Editor", JOptionPane.YES_NO_CANCEL_OPTION);
                if(yn == JOptionPane.YES_OPTION){
                    int get = save_as();
                    if(get == 1){
                        dispose();
                    }
                }else if(yn == JOptionPane.NO_OPTION){
                    dispose();
                }

            }
        }

    }

    public void count(){

        String data = editor.getText();

        int space_count = 0, tab_count = 0, enter_count = 0, char_count = 0, word_count=0;
        for(int i=0; i<data.length(); i++){
            if(data.charAt(i) == ' ')
                space_count++;
            else if(data.charAt(i) == '\t')
                tab_count++;
            else if(data.charAt(i) == '\n')
                enter_count++;
            else
                char_count++;
        }

        long total_count = space_count + tab_count + enter_count + char_count;
        String[] words = data.split(" ");

        if(!(data.equals(""))) {
            word_count = words.length;
        }

        JOptionPane.showMessageDialog(null, "\nLine Count = " + enter_count + "\nSpace Count = " + space_count +
                "\nTab Count = " + tab_count + "\nCharacter Count = " + char_count + "\nTotal Count = " + total_count +
                "\nWord Count = " + word_count, "Count", JOptionPane.PLAIN_MESSAGE);

    }

    public void search(){

        try{
            String get = JOptionPane.showInputDialog(null, "Enter Old Word To Replace", "JText Editor", JOptionPane.PLAIN_MESSAGE);
            String data = editor.getText();
            String[] arr = data.split(" ");
            int count = 0;
            for (String s: arr){
                if(get.equals(s)){
                    count++;
                }
            }

            JOptionPane.showMessageDialog(null, get + " Repeated " + count + " Time(s)", "Search", JOptionPane.PLAIN_MESSAGE);

        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Error in Searching", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void replace(){

        try{
            String get_old = JOptionPane.showInputDialog(null, "Enter Existing Word To Replace", "JText Editor", JOptionPane.PLAIN_MESSAGE);
            String get_new = JOptionPane.showInputDialog(null, "Enter New Word To Replace Existing Word", "JText Editor", JOptionPane.PLAIN_MESSAGE);
            String data = editor.getText();
            data = data.replaceAll(get_old, get_new);
            editor.setText(data);

        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Error in Replacing", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void text_size(){

        String get = JOptionPane.showInputDialog(null, "Enter the size of text (1-100)\nDefault is 12", "JText Editor", JOptionPane.PLAIN_MESSAGE);
        try {
            int text_size = Integer.parseInt(get);
            font = new Font(font.getFamily(), font.getStyle(), text_size);
            editor.setFont(font);

            status_bar.setText(" Text Size = " + text_size);

        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Enter Numbers Only", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void text_style(){

        String get = JOptionPane.showInputDialog(null, "Enter 0 - PLAIN\nEnter 1 - BOLD\nEnter 2 - ITALIC\nEnter 3 - BOLD + ITALIC\nDefault is PLAIN", "JText Editor", JOptionPane.PLAIN_MESSAGE);
        try {
            int text_style = Integer.parseInt(get);
            if(text_style == 0) {
                font = new Font(font.getFamily(), Font.PLAIN, font.getSize());
                status_bar.setText(" Text Style = PLAIN");
            }else if(text_style == 1) {
                font = new Font(font.getFamily(), Font.BOLD, font.getSize());
                status_bar.setText(" Text Style = BOLD");
            }else if(text_style == 2) {
                font = new Font(font.getFamily(), Font.ITALIC, font.getSize());
                status_bar.setText(" Text Style = ITALIC");
            }else {
                font = new Font(font.getFamily(), Font.ITALIC + Font.BOLD, font.getSize());
                status_bar.setText(" Text Style = BOLD + ITALIC");
            }
            editor.setFont(font);

        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Enter Numbers Only", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void text_font(){

        String get = JOptionPane.showInputDialog(null, "Enter the name of the font\nEx:Pristina, Calibri, Rockwell etc\nDefault is EMPTY String", "JText Editor", JOptionPane.PLAIN_MESSAGE);
        try {

            font = new Font(get, font.getStyle(), font.getSize());
            editor.setFont(font);

            if(get.equals("")){
                // Default font = Dialog
                status_bar.setText(" Text Font = DEFAULT");
            }else{
                status_bar.setText(" Text Font = " + font.getFamily());
            }

        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Enter valid font", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void text_color(){

        try{
            Color color = JColorChooser.showDialog(this,"Select A Text Color", null);

            if(color == null){
                color = Color.BLACK;
                editor.setForeground(color);
                status_bar.setText("Text Color Changed To DEFAULT");
            }else {
                editor.setForeground(color);
                status_bar.setText("Text Color Changed");
            }

        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Select A Correct Color", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void background_color(){

        try{
            Color color = JColorChooser.showDialog(this,"Select A Background Color", null);

            if(color == null){
                color = Color.WHITE;
                editor.setBackground(color);
                status_bar.setText("Background Color Changed To DEFAULT");
            }else {
                editor.setBackground(color);
                status_bar.setText("Background Color Changed");
            }

        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Select A Correct Color", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void help(){

        status_bar.setText(" JText Editor Is Developed By Nikhil");
        JOptionPane.showMessageDialog(null, "JText Editor Code Is Written By Nikhil", "About", JOptionPane.PLAIN_MESSAGE);

    }


}