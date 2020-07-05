import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.FileReader;
import java.util.ArrayList;

public class Jp{

    public static void main(String[] args){

        new SimpleBrowser();

    }

}

class SimpleBrowser extends JFrame{

    JEditorPane browser;
    JTextField url_box;
    JPanel panel;
    JButton front_btn, back_btn, reload_btn, home_btn, history_btn;
    ArrayList<String> history = new ArrayList<>();
    int nav_count = 0;

    SimpleBrowser(){

        try {
            // Nimbus Theme in Swing
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Theme Error", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // URL Box
        url_box = new JTextField();
        url_box.setToolTipText("Type URL And Hit Enter Key");
        url_box.addActionListener(e -> {
            load_pages();
            if(history.size()-1 >= 0)
                back_btn.setEnabled(true);
            front_btn.setEnabled(false);
            if(url_box.getText().startsWith("http")){
                history.add(url_box.getText());
            }else{
                history.add("https://" + url_box.getText());
            }
            history_btn.setEnabled(true);
            nav_count = history.size()-1;
        });

        // Browser View Settings
        browser = new JEditorPane();
        browser.setEditable(false);
        browser.setContentType("text/html");
        browser.setText(home_page());
        browser.addHyperlinkListener(e -> {
            if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
                try {
                    browser.setPage(e.getURL());
                    url_box.setText(e.getURL().toString());
                    history.add(e.getURL().toString());
                    nav_count = history.size()-1;
                    if(history.size()-1 >= 1)
                        back_btn.setEnabled(true);
                    reload_btn.setEnabled(true);
                    front_btn.setEnabled(false);
                    history_btn.setEnabled(true);
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Cannot Load The URL", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Bottom Panel
        panel = new JPanel();

        home_btn = new JButton("⌂");
        home_btn.setToolTipText("Home");
        home_btn.addActionListener(e -> {
            browser.setText("<!DOCTYPE html><html><head><title>OK</title><style>body{background:white;font-size:9.2px;}a{color:blue;text-decoration:underline;}</style><body></body></head></html>");
            url_box.setText("");
            browser.setText(home_page());
        });

        reload_btn = new JButton("↻");
        reload_btn.setToolTipText("Reload");
        reload_btn.addActionListener(e -> reload());
        reload_btn.setEnabled(false);

        history_btn = new JButton("☰");
        history_btn.setToolTipText("History");
        history_btn.addActionListener(e -> history_display());
        history_btn.setEnabled(false);

        back_btn = new JButton("←");
        back_btn.setToolTipText("Click to go back");
        back_btn.setEnabled(false);
        back_btn.addActionListener(e -> load_back());

        front_btn = new JButton("→");
        front_btn.setToolTipText("Click to go forward");
        front_btn.setEnabled(false);
        front_btn.addActionListener(e -> load_front());

        // Adding the buttons to bottom panel
        panel.add(home_btn);
        panel.add(reload_btn);
        panel.add(history_btn);
        panel.add(back_btn);
        panel.add(front_btn);

        // Adding the elements to window
        add(url_box, BorderLayout.NORTH);
        add(new JScrollPane(browser), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        // Keep all the window setting after the widgets

        setTitle("Simple Browser");
        setVisible(true);
        setSize(830, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    // Methods that add navigation to the browser

    private void load_pages(){

        try{
            String entered = url_box.getText();
            if(!entered.startsWith("http"))
                entered = "https://" + entered;
            else
                entered = entered + "";
            browser.setPage(entered);
            reload_btn.setEnabled(true);

        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Enter A Valid URL", "Warning", JOptionPane.WARNING_MESSAGE);
        }

    }

    private void load_front(){

        if(nav_count <= history.size()-1)
            nav_count++;
        if(history.size()-1 == nav_count){
            // We need to disable one and enable another
            front_btn.setEnabled(false);
            back_btn.setEnabled(true);
        }
        try{
            url_box.setText(history.get(nav_count));
            browser.setPage(history.get(nav_count));

        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Enter A Valid URL", "Warning", JOptionPane.WARNING_MESSAGE);
        }

    }

    private void load_back() {
        // Button enable
        if (nav_count > 0) {
            back_btn.setEnabled(true);
            nav_count = nav_count - 1;
        }else{
            // We need to disable one and enable another
            back_btn.setEnabled(false);
            front_btn.setEnabled(true);
        }
        // Button disable
        if (nav_count == 0)
            back_btn.setEnabled(false);

        try{
            front_btn.setEnabled(true);
            url_box.setText(history.get(nav_count));
            browser.setPage(history.get(nav_count));

        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Enter A Valid URL", "Warning", JOptionPane.WARNING_MESSAGE);
        }

    }

    private void reload(){

        try{
            url_box.setText(history.get(nav_count));
            browser.setPage(history.get(nav_count));

        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Enter A Valid URL", "Warning", JOptionPane.WARNING_MESSAGE);
        }

    }

    private void history_display(){

        url_box.setText("");
        browser.setText("<!DOCTYPE html><html><head><title>OK</title><style>body{background:white;font-size:9.2px;}a{color:blue;text-decoration:underline;}</style><body></body></head></html>");

        StringBuilder str = new StringBuilder();
        str.append("<html><h1>History</h1>");

        int count = 1;

        for(String x : history){
            str.append(count).append("- <a href='").append(x).append("'>").append(x).append("</a><br><br>");
            count++;
        }

        str.append("</html>");
        browser.setText(str.toString());

    }

    private String home_page(){

        StringBuilder page = new StringBuilder();

        try {
            FileReader r = new FileReader("home/index.html");

            int i = r.read();

            while (i != -1){
                page.append((char)i);
                i = r.read();
            }

        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Error", "Error", JOptionPane.PLAIN_MESSAGE);
        }
        return page.toString();

    }


}