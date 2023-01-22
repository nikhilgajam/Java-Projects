import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class Jp{

    public static void main(String[] args){

        new BhagavadGita();

    }

}


class BhagavadGita{

    // Variables
    JFrame window;
    JLabel title, display_image;
    JEditorPane display;
    JButton p_chp_btn, n_chp_btn, p_ver_btn, n_ver_btn, tp_btn, tm_btn;
    JComboBox<String> chapter_box, verse_box, language_box;
    HTMLDocument doc;
    HTMLEditorKit output_kit;
    int chapter_selected = 0, verse_selected = 0, text_size = 22;
    String language_selected = "English";
    String display_image_path = "/Data/Pictures/Chapter 11.jpg";
    // Consider index numbers as chapter numbers to get number of verses in a chapter
    int[] verse_range = {0, 47, 72, 43, 42, 29, 47, 30, 28, 34, 42, 55, 20, 35, 27, 20, 24, 28, 78};

    // Constructor
    BhagavadGita(){

        // Window
        window = new JFrame("Bhagavad Gita");
        window.setLayout(new BorderLayout());

        try {
            // Nimbus Theme in Swing
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Theme Error", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Display image
        display_image = new JLabel();
        display_image.setOpaque(true);
        display_image.setBackground((Color.decode("#333333")));
        window.add(display_image, BorderLayout.WEST);

        changeDisplayImage(display_image_path);

        // Title label
        title = new JLabel("Bhagavad Gita");
        title.setFont(new Font("Times New Roman", Font.BOLD, 40));
        title.setOpaque(true);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setForeground(Color.decode("#e6e8eb"));
        title.setBackground(Color.decode("#333333"));

        // Adding title label to the window in the north(top) position
        window.add(title, BorderLayout.NORTH);

        // Output display
        display = new JEditorPane("text/html;charset=UTF-8", "");
//        display.setContentType("text/html");
        display.setFont(new Font("Times New Roman", Font.PLAIN, text_size));
        display.setPreferredSize(new Dimension(260, 260));
        display.setEditable(false);
        display.setFocusable(false);
        // Used to insert text into output_display
        doc = (HTMLDocument) display.getDocument();
        output_kit = (HTMLEditorKit) display.getEditorKit();
        display.setSize(600, 600);
        // Adding styling to output_display
        Style style = output_kit.getStyleSheet().getStyle("body");
        StyleConstants.setBackground(style, Color.decode("#333333"));
        display.setForeground(Color.decode("#e6e8eb"));
        // Replacing the white border with grey
        Border border = new LineBorder(Color.decode("#333333"), 4);
        display.setBorder(border);
        // Inserting the text to output_display

        // Adding display to the window
        window.add(new JScrollPane(display), BorderLayout.CENTER);

        // Button panel
        JPanel btn_panel = new JPanel();
        btn_panel.setBackground(Color.decode("#333333"));
        btn_panel.setLayout(new FlowLayout());
        window.add(btn_panel, BorderLayout.SOUTH);

        // Buttons

        // Text size decrease button
        tm_btn = new JButton("-");
        tm_btn.setToolTipText("Decreases Text Size");
        tm_btn.setForeground(Color.decode("#e6e8eb"));
        tm_btn.setBackground(Color.decode("#333333"));
        tm_btn.addActionListener(e -> {
            if(text_size - 1 >= 6){
                text_size--;
                display.setFont(new Font("Times New Roman", Font.PLAIN, text_size));
            }
        });
        tm_btn.setFocusable(false);
        btn_panel.add(tm_btn);

        // Previous chapter button
        p_chp_btn = new JButton("<<");
        p_chp_btn.setToolTipText("Previous Chapter");
        p_chp_btn.setForeground(Color.decode("#e6e8eb"));
        p_chp_btn.setBackground(Color.decode("#333333"));
        p_chp_btn.addActionListener(e -> prevChapterClicked());
        p_chp_btn.setFocusable(false);
        btn_panel.add(p_chp_btn);

        // Previous verse button
        p_ver_btn = new JButton("<");
        p_ver_btn.setToolTipText("Previous Verse");
        p_ver_btn.setForeground(Color.decode("#e6e8eb"));
        p_ver_btn.setBackground(Color.decode("#333333"));
        p_ver_btn.addActionListener(e -> prevVerseClicked());
        p_ver_btn.setFocusable(false);
        btn_panel.add(p_ver_btn);

        // Chapter combo box
        chapter_box = new JComboBox<>();
        for(int i=0; i<=18; i++){
            chapter_box.addItem("Chapter " + i);
        }
        chapter_box.setToolTipText("Chapters");
        chapter_box.setForeground(Color.decode("#e6e8eb"));
        chapter_box.setBackground(Color.decode("#333333"));
        chapter_box.addActionListener(e -> chapterControlSelected());
        chapter_box.setFocusable(false);
        btn_panel.add(chapter_box);

        // Verse combo box
        verse_box = new JComboBox<>();
        verse_box.addItem("Verse 0");
        verse_box.setToolTipText("Verses");
        verse_box.setForeground(Color.decode("#e6e8eb"));
        verse_box.setBackground(Color.decode("#333333"));
        verse_box.addActionListener(e -> otherControlSelected());
        verse_box.setFocusable(false);
        btn_panel.add(verse_box);

        // Language combo box
        language_box = new JComboBox<>();
        language_box.addItem("English");
        language_box.addItem("Hindi");
        language_box.addItem("Telugu");
        language_box.setToolTipText("Language");
        language_box.setForeground(Color.decode("#e6e8eb"));
        language_box.setBackground(Color.decode("#333333"));
        language_box.addActionListener(e -> otherControlSelected());
        language_box.setFocusable(false);
        btn_panel.add(language_box);

        // Next verse button
        n_ver_btn = new JButton(">");
        n_ver_btn.setToolTipText("Next Verse");
        n_ver_btn.setForeground(Color.decode("#e6e8eb"));
        n_ver_btn.setBackground(Color.decode("#333333"));
        n_ver_btn.addActionListener(e -> nextVerseClicked());
        n_ver_btn.setFocusable(false);
        btn_panel.add(n_ver_btn);

        // Next chapter button
        n_chp_btn = new JButton(">>");
        n_chp_btn.setToolTipText("Next Chapter");
        n_chp_btn.setForeground(Color.decode("#e6e8eb"));
        n_chp_btn.setBackground(Color.decode("#333333"));
        n_chp_btn.addActionListener(e -> nextChapterClicked());
        n_chp_btn.setFocusable(false);
        btn_panel.add(n_chp_btn);

        // Text size increase button
        tp_btn = new JButton("+");
        tp_btn.setToolTipText("Increases Text Size");
        tp_btn.setForeground(Color.decode("#e6e8eb"));
        tp_btn.setBackground(Color.decode("#333333"));
        tp_btn.addActionListener(e -> {
            if(text_size + 1 <= 100){
                text_size++;
                display.setFont(new Font("Times New Roman", Font.PLAIN, text_size));
            }
        });
        tp_btn.setFocusable(false);
        btn_panel.add(tp_btn);

        // Window settings
        window.setVisible(true);
        window.setSize(1064, 641);
        window.setLocationRelativeTo(null);
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        loadData();

    }

    // Methods

    private void changeDisplayImage(String path){
        // Image display
        try{
            // Reading data from jar
            InputStream temp_path = getClass().getResourceAsStream(path);
            BufferedImage pic = ImageIO.read(temp_path);
            Image scaledImage = pic.getScaledInstance(400, 520, Image.SCALE_SMOOTH);
            display_image.setIcon(new ImageIcon(scaledImage));
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chapterControlSelected(){
        chapter_selected = chapter_box.getSelectedIndex();
        int verse_range_temp = verse_range[chapter_selected];
        verse_selected = 0;
        verse_box.removeAllItems();
        for(int i=0; i<=verse_range_temp; i++){
            verse_box.addItem("Verse " + i);
        }
        loadData();
        changeDisplayImage("/Data/Pictures/Chapter " + chapter_selected + ".jpg");  // Changing the picture
    }

    private void otherControlSelected(){
        chapter_selected = chapter_box.getSelectedIndex();
        verse_selected = verse_box.getSelectedIndex();

        if(verse_selected == -1)  // if verse is -1 then the operation is not performed
            return;

        language_selected = (String)language_box.getSelectedItem();
        loadData();
    }

    private void prevChapterClicked(){
        if(chapter_selected - 1 >= 0){
            chapter_selected--;
            chapter_box.setSelectedIndex(chapter_selected);
        }
    }

    private void nextChapterClicked(){
        if(chapter_selected + 1 <= 18){
            chapter_selected++;
            chapter_box.setSelectedIndex(chapter_selected);
        }
    }

    private void prevVerseClicked(){
        if(verse_selected - 1 >= 0){
            verse_selected--;
            verse_box.setSelectedIndex(verse_selected);
        }else{
            if(chapter_selected - 1 >= 0) {
                chapter_selected--;
                chapter_box.setSelectedIndex(chapter_selected);
                verse_selected = verse_range[chapter_selected];
                verse_box.setSelectedIndex(verse_selected);
            }
        }
    }

    private void nextVerseClicked(){
        if(verse_selected + 1 <= verse_range[chapter_selected]){
            verse_selected++;
            verse_box.setSelectedIndex(verse_selected);
        }else{
            nextChapterClicked();
        }
    }

    private void loadData(){
        StringBuilder data = new StringBuilder();
        try{
            // Reading data from jar
            String url = "/Data/Bhagavad_Gita/" + language_selected + "/Chapter_" + chapter_selected + "/Verse_" + verse_selected + ".txt";
            InputStream temp_path = getClass().getResourceAsStream(url);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(temp_path, StandardCharsets.UTF_8)   // To read utf-8 text
            );

            // Reading
            int c;
            while((c = br.read()) != -1){
                data.append((char)c);
            }

            br.close();

            String text = data.toString();
            text = text.replace("\n", "<br>");
            text = "<center>" + text + "</center>";
            display.setText(text);
            display.setCaretPosition(0);  // Pointing to page start
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

}
