import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Objects;


public class Jp{

    public static void main(String[] args){

        new JImageCompressor();


    }

}


class JImageCompressor{

    // Variables
    JFrame window;
    JRadioButton single_img_rb, multi_img_rb, low_rb, normal_rb, high_rb;
    JTextField quality_box;
    JMenuBar menu_bar;
    JMenu help_menu;
    JMenuItem help, about_JIC, about_dev;
    JLabel status_bar;
    JButton compress_btn, sd_btn;
    JLabel sd_lbl, quality_lbl, compress_lbl;
    ButtonGroup quality_rb_group, mode_rb_group;
    ImageIcon compress_img, stop_img;
    String src_path = null, dest_path = null;
    long compression_counter = 0, total_no_of_compressions = 0;
    boolean stop_compression = true;

    public JImageCompressor(){

        // Window
        window = new JFrame("JImage Compressor");
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

        // Menu
        menu_bar = new JMenuBar();

        // Help menu
        help_menu = new JMenu("Help");

        help = new JMenuItem("Help");
        help.addActionListener(e -> showHelp());
        help_menu.add(help);

        about_dev = new JMenuItem("About Developer");
        about_dev.addActionListener(e -> aboutDev());
        help_menu.add(about_dev);

        about_JIC = new JMenuItem("About JImage Compressor");
        about_JIC.addActionListener(e -> aboutJIC());
        help_menu.add(about_JIC);

        menu_bar.add(help_menu);

        // Title Label
        JLabel title = new JLabel("JImage Compressor");
        title.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        title.setOpaque(true);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setForeground(Color.decode("#e6e8eb"));
        title.setBackground(Color.decode("#333333"));
        window.add(title, BorderLayout.NORTH);

        // Button panel
        JPanel btn_panel = new JPanel();
        btn_panel.setBackground(Color.decode("#333333"));
        btn_panel.setLayout(new BoxLayout(btn_panel, BoxLayout.Y_AXIS));
        btn_panel.setBorder(new javax.swing.border.EmptyBorder(1, 25, 10, 25));  // Giving padding to the btn_panel

        // Source and destination button with image and label
        ImageIcon sd_img = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/source.png")));  // Button Image
        sd_btn = new JButton(sd_img);
        sd_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        sd_btn.setForeground(Color.decode("#e6e8eb"));
        sd_btn.setBackground(Color.decode("#333333"));
        sd_btn.addActionListener(e -> imgSrcDestButtonClicked());
        btn_panel.add(sd_btn);

        sd_lbl = new JLabel("Image Source And Destination");
        sd_lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        sd_lbl.setForeground(Color.decode("#e6e8eb"));
        sd_lbl.setBackground(Color.decode("#333333"));
        btn_panel.add(sd_lbl);

        btn_panel.add(Box.createRigidArea(new Dimension(5, 16)));  // Space between the components

        // Mode label
        JLabel mode_lbl = new JLabel("MODE");
        mode_lbl.setFont(new Font("Calibri", Font.BOLD, 16));
        mode_lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        mode_lbl.setForeground(Color.decode("#e6e8eb"));
        mode_lbl.setBackground(Color.decode("#333333"));
        btn_panel.add(mode_lbl);

        // Mode radio buttons
        mode_rb_group = new ButtonGroup();
        single_img_rb = new JRadioButton("Single Image Compression Mode", true);
        single_img_rb.setAlignmentX(Component.CENTER_ALIGNMENT);
        single_img_rb.setForeground(Color.decode("#e6e8eb"));
        single_img_rb.setBackground(Color.decode("#993e1c"));
        single_img_rb.addActionListener(e -> {src_path = null; status_bar.setText("Single Image Compression Mode Selected");});
        mode_rb_group.add(single_img_rb);
        btn_panel.add(single_img_rb);

        multi_img_rb = new JRadioButton("Multi Image Compression Mode");
        multi_img_rb.setAlignmentX(Component.CENTER_ALIGNMENT);
        multi_img_rb.setForeground(Color.decode("#e6e8eb"));
        multi_img_rb.setBackground(Color.decode("#993e1c"));
        multi_img_rb.addActionListener(e -> {src_path = null; status_bar.setText("Multi Image Compression Mode Selected");});
        mode_rb_group.add(multi_img_rb);
        btn_panel.add(multi_img_rb);

        btn_panel.add(Box.createRigidArea(new Dimension(5, 10)));  // Space between the components

        // Quality label
        quality_lbl = new JLabel("QUALITY");
        quality_lbl.setFont(new Font("Calibri", Font.BOLD, 16));
        quality_lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        quality_lbl.setForeground(Color.decode("#e6e8eb"));
        quality_lbl.setBackground(Color.decode("#333333"));
        btn_panel.add(quality_lbl);

        // Quality box
        quality_box = new JTextField("1920");
        quality_box.setToolTipText("Enter Image Compression Width");
        quality_box.setHorizontalAlignment(JTextField.CENTER);
        quality_box.setAlignmentX(Component.CENTER_ALIGNMENT);
        quality_box.setColumns(40);
        quality_box.setMinimumSize(quality_box.getPreferredSize());
        quality_box.setMaximumSize(quality_box.getPreferredSize());
        quality_box.setForeground(Color.decode("#e6e8eb"));
        quality_box.setBackground(Color.decode("#993e1c"));
        quality_box.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                quality_rb_group.clearSelection();
                status_bar.setText("Changing Compression Quality Box Value");
            }
        });
        btn_panel.add(quality_box);

        // Quality radio buttons
        quality_rb_group = new ButtonGroup();
        low_rb = new JRadioButton("Low Quality Compression");
        low_rb.setAlignmentX(Component.CENTER_ALIGNMENT);
        low_rb.setForeground(Color.decode("#e6e8eb"));
        low_rb.setBackground(Color.decode("#993e1c"));
        low_rb.addActionListener(e -> qualityRBClicked());
        quality_rb_group.add(low_rb);
        btn_panel.add(low_rb);

        normal_rb = new JRadioButton("Normal Quality Compression");
        normal_rb.setAlignmentX(Component.CENTER_ALIGNMENT);
        normal_rb.setForeground(Color.decode("#e6e8eb"));
        normal_rb.setBackground(Color.decode("#993e1c"));
        normal_rb.addActionListener(e -> qualityRBClicked());
        quality_rb_group.add(normal_rb);
        btn_panel.add(normal_rb);

        high_rb = new JRadioButton("High Quality Compression", true);
        high_rb.setAlignmentX(Component.CENTER_ALIGNMENT);
        high_rb.setForeground(Color.decode("#e6e8eb"));
        high_rb.setBackground(Color.decode("#993e1c"));
        high_rb.addActionListener(e -> qualityRBClicked());
        quality_rb_group.add(high_rb);
        btn_panel.add(high_rb);

        btn_panel.add(Box.createRigidArea(new Dimension(5, 10)));  // Space between the components

        // Compress label
        compress_lbl = new JLabel("COMPRESS");
        compress_lbl.setFont(new Font("Calibri", Font.BOLD, 16));
        compress_lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        compress_lbl.setForeground(Color.decode("#e6e8eb"));
        compress_lbl.setBackground(Color.decode("#333333"));
        btn_panel.add(compress_lbl);

        // Source and destination button with image and label
        compress_img = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/compress.png")));  // Button Image
        stop_img = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/stop.png")));  // Button Image
        compress_btn = new JButton(compress_img);
        compress_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        compress_btn.setForeground(Color.decode("#e6e8eb"));
        compress_btn.setBackground(Color.decode("#333333"));
        compress_btn.addActionListener(e -> compressButtonClicked());
        btn_panel.add(compress_btn);

        window.add(btn_panel, BorderLayout.CENTER);

        // Status bar with border
        Border border = BorderFactory.createBevelBorder(1);

        status_bar = new JLabel("Welcome To JIC");
        status_bar.setHorizontalAlignment(JLabel.CENTER);
        status_bar.setOpaque(true);
        status_bar.setForeground(Color.decode("#e6e8eb"));
        status_bar.setBackground(Color.DARK_GRAY);
        status_bar.setBorder(border);

        window.add(status_bar, BorderLayout.SOUTH);

        // Adding menu bar to window
        window.setJMenuBar(menu_bar);

        // Window settings
        window.setVisible(true);
        window.setSize(411, 521);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    // Button action methods

    private void imgSrcDestButtonClicked(){

        new ImageSrcDest();

    }

    private void qualityRBClicked(){

        // Quality radio button options
        if(low_rb.isSelected()){
            quality_box.setText("800");
            status_bar.setText("Low Compression Quality Selected");
        }else if(normal_rb.isSelected()){
            quality_box.setText("1080");
            status_bar.setText("Normal Compression Quality Selected");
        }else if(high_rb.isSelected()){
            quality_box.setText("1920");
            status_bar.setText("High Compression Quality Selected");
        }

    }

    private void compressButtonClicked(){

        if(stop_compression){
            new CompressThreadClass().start();  // Creating thread by creating a new class
            stop_compression = false;
        }else{
            stop_compression = true;
        }


    }

    private void showHelp(){

        // Displays help message box
        JOptionPane.showMessageDialog(null, "1. Select Single Or Multi Image Mode Radio Button\n" +
                        "2. Select Source And Destination Directories By Clicking On The Image Source And Destination Button\n" +
                        "3. Select A Quality Radio Button Or Enter Desired Compression Width In The Quality Box\n" +
                        "4. Click Compress Button To Start Compression\n\n" +
                        "All The Compression Details Will Be Displayed On The Status Bar\n\n" +
                        "                     Thanks for Using JImage Compressor", "JImage Compressor Help",
                JOptionPane.INFORMATION_MESSAGE);
        status_bar.setText("Thanks for using JIC");   // Displaying on status bar

    }

    private void aboutDev(){

        // Displays about us message box
        JOptionPane.showMessageDialog(window, "This Program Is Written By Nikhil Gajam", "About Developer", JOptionPane.PLAIN_MESSAGE);
        status_bar.setText("JIC Developed By Nikhil Gajam");   // Displaying on status bar

    }

    private void aboutJIC(){

        // Displays about us message box
        JOptionPane.showMessageDialog(window, "JImage Compressor \nVersion: 1.0 \nDeveloper: Nikhil \nOpen Source Java Image Compressor", "About Developer", JOptionPane.PLAIN_MESSAGE);
        status_bar.setText("JIC Code Written By Nikhil");   // Displaying on status bar

    }

    // Inner classes

    class ImageSrcDest{

        JDialog w;
        JButton src_button, dest_button;
        JTextField src_box, dest_box;

        ImageSrcDest(){

            // Window
            w = new JDialog(window, "JImage Compressor (Image Source & Destination Selection Window)");
            w.setLayout(new BorderLayout());
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/icon.png")));
            w.setIconImage(icon.getImage());

            String src_btn_text = null;

            // Stops parent window
            window.setEnabled(false);

            // Window close action
            w.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e){
                    window.setEnabled(true);
                }
            });

            if(single_img_rb.isSelected())
                src_btn_text = "Select Source Image Path";
            else
                src_btn_text = "Select Source Directory Path";

            JPanel panel = new JPanel();
            panel.setBackground(Color.decode("#333333"));
            panel.setLayout(new GridLayout(2,2));
            panel.setBorder(new javax.swing.border.EmptyBorder(10, 1, 10, 1));  // Giving padding to the btn_panel

            src_box = new JTextField();
            src_box.setForeground(Color.decode("#e6e8eb"));
            src_box.setBackground(Color.DARK_GRAY);
            src_box.setToolTipText("Click Source Button Or Paste Source Path In This Box");
            src_box.addActionListener(e -> okBtnClicked());
            panel.add(src_box);

            src_button = new JButton(src_btn_text);
            src_button.setForeground(Color.BLACK);
            src_button.setBackground(Color.GRAY);
            src_button.addActionListener(e -> srcBtnClicked());
            panel.add(src_button);

            dest_box = new JTextField();
            dest_box.setForeground(Color.decode("#e6e8eb"));
            dest_box.setBackground(Color.DARK_GRAY);
            dest_box.setToolTipText("Click Destination Button Or Paste Destination Path In This Box");
            dest_box.addActionListener(e -> okBtnClicked());
            panel.add(dest_box);

            dest_button = new JButton("Select Destination Directory Path");
            dest_button.setForeground(Color.BLACK);
            dest_button.setBackground(Color.GRAY);
            dest_button.addActionListener(e -> destBtnClicked());
            panel.add(dest_button);

            w.add(panel, BorderLayout.CENTER);

            JButton ok_btn = new JButton("OK");
            ok_btn.setForeground(Color.decode("#e6e8eb"));
            ok_btn.setBackground(Color.decode("#993e1c"));
            ok_btn.addActionListener(e -> okBtnClicked());
            w.add(ok_btn, BorderLayout.SOUTH);

            // Window settings
            w.setSize(600, 160);
            w.setLocationRelativeTo(window);
            w.setResizable(false);
            w.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            w.setVisible(true);

            // Logic for showing selected path in the respective boxes
            if(src_path != null)
                src_box.setText(src_path);
            if(dest_path != null)
                dest_box.setText(dest_path);

            ok_btn.requestFocus();  // Setting the focus to ok_btn

        }

        // Button action methods

        private void srcBtnClicked(){

            try{
                JFileChooser jc = new JFileChooser();
                if(single_img_rb.isSelected()){
                    jc.showDialog(w, "Select File");
                }else if(multi_img_rb.isSelected()){
                    jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    jc.showDialog(w, "Select Folder");
                }
                src_path = jc.getSelectedFile().getAbsolutePath().toString().toLowerCase();

                // Checking whether select file is image or not when single image is selected
                if(single_img_rb.isSelected()){
                    String ext_temp = src_path.substring(src_path.lastIndexOf(".")+1).toLowerCase();
                    if(ext_temp.equals("jpeg") || ext_temp.equals("jpg") || ext_temp.equals("png") || ext_temp.equals("gif")){
                        src_path = src_path;
                    }else{
                        JOptionPane.showMessageDialog(w, "Select A Valid Image With Extension (.jpeg) or (.jpg) or (.png) or (.gif)", "Warning", JOptionPane.WARNING_MESSAGE);
                        src_path = null;
                        return;
                    }
                }

                src_box.setText(src_path);

            }catch(Exception e){
                src_path = null;
                JOptionPane.showMessageDialog(w, "Select A Valid Source Path", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        }

        private void destBtnClicked(){

            try{
                JFileChooser jc = new JFileChooser();
                jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jc.showDialog(w, "Select Folder");
                dest_path = jc.getSelectedFile().getAbsolutePath().toString().toLowerCase();
                dest_box.setText(dest_path);
            }catch(Exception e){
                dest_path = null;
                JOptionPane.showMessageDialog(w, "Select A Valid Destination Path", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        }

        private void okBtnClicked(){

            src_path = src_box.getText().toLowerCase();
            dest_path = dest_box.getText().toLowerCase();

            // Checking whether select file is image or not when single image is selected
            if(single_img_rb.isSelected()){

                String ext_temp = src_path.substring(src_path.lastIndexOf(".")+1).toLowerCase();
                if(ext_temp.equals("jpeg") || ext_temp.equals("jpg") || ext_temp.equals("png") || ext_temp.equals("gif")){
                    src_path = src_path;  // Placeholder operation
                }else{
                    JOptionPane.showMessageDialog(w, "Select A Valid Image With Extension (.jpeg) or (.jpg) or (.png) or (.gif)", "Warning", JOptionPane.WARNING_MESSAGE);
                    src_path = null;
                    return;
                }

            }

            if(multi_img_rb.isSelected() && !new File(src_path).isDirectory()){
                JOptionPane.showMessageDialog(w, "Select A Valid Source Directory Path", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if(src_path == null || !new File(src_path).exists()){
                JOptionPane.showMessageDialog(w, "Select A Valid Source Path", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }else if(dest_path == null || !new File(dest_path).exists()){
                JOptionPane.showMessageDialog(w, "Select A Valid Destination Path", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            window.setEnabled(true);  // Parent window can be used
            w.dispose();  // Closes this window
            status_bar.setText("Source Path And Destination Path Acquired");

        }

    }


    class CompressThreadClass extends Thread{

        public void run(){

            // Needed classes and variables
            Compressor compressor = new Compressor();
            String quality_width = quality_box.getText();
            int qw_int;

            // Checking whether src & dest path is valid
            if(src_path == null || !new File(src_path).exists()){
                JOptionPane.showMessageDialog(window, "Select A Source Path By Clicking On Image Source And Destination Button", "Warning", JOptionPane.WARNING_MESSAGE);
                stop_compression = true;
                return;
            }else if(dest_path == null || !new File(dest_path).exists()){
                JOptionPane.showMessageDialog(window, "Select A Destination Path By Clicking On Image Source And Destination Button", "Warning", JOptionPane.WARNING_MESSAGE);
                stop_compression = true;
                return;
            }

            // Quality box number validation
            if(!quality_width.matches("[0-9]+")){
                JOptionPane.showMessageDialog(window, "Enter Valid Image Compression Width In Quality Box", "Error", JOptionPane.ERROR_MESSAGE);
                stop_compression = true;
                return;
            }

            // Converting quality_width into integer
            try{
                qw_int = Integer.parseInt(quality_width);
            }catch(Exception e){
                JOptionPane.showMessageDialog(window, "Enter Valid Image Compression Width In Quality Box", "Error", JOptionPane.ERROR_MESSAGE);
                stop_compression = true;
                return;
            }

            // Disabling all the unwanted components during compression
            sd_btn.setEnabled(false);
            single_img_rb.setEnabled(false);
            multi_img_rb.setEnabled(false);
            quality_box.setEnabled(false);
            low_rb.setEnabled(false);
            normal_rb.setEnabled(false);
            high_rb.setEnabled(false);

            // Changing compress label and button
            compress_lbl.setText("STOP COMPRESSION");
            compress_btn.setIcon(stop_img);

            // Initializing the counter
            compression_counter = 0;

            // Single and multi compression logic
            if(single_img_rb.isSelected()){

                total_no_of_compressions = 1;  // Assigning no of images to total_no_of_compressions
                compressor.compress(src_path, dest_path, qw_int);  // Calling compress method

            }else if(multi_img_rb.isSelected()){

                File[] file_list = new File(src_path).listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name){
                        name = name.toLowerCase();
                        return name.endsWith(".jpeg") || name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif");
                    }
                });
                assert file_list != null;  // Telling the compiler that list is not empty

                total_no_of_compressions = file_list.length;  // Assigning no of images to total_no_of_compressions

                if(total_no_of_compressions == 0){
                    JOptionPane.showMessageDialog(window, "Selected Directory Is Empty", "Warning", JOptionPane.WARNING_MESSAGE);
                }

                if(total_no_of_compressions > 100){
                    int temp = JOptionPane.showConfirmDialog(window, total_no_of_compressions + " Images Are There In The Source Directory\n" +
                            "Do You Want To Compress?", "Do You Want To Compress?", JOptionPane.YES_NO_OPTION);
                    if(temp == 1){
                        stop_compression = true;
                    }
                }

                for(File x: file_list){
                    if(stop_compression)
                        break;  // loop breaks when stop_compression==true

                    compressor.compress(x.getAbsolutePath().toLowerCase(), dest_path, qw_int);  // Calling compress method
                }

                // Showing stop on status bar
                if(stop_compression)
                    status_bar.setText("[" + compression_counter + "/" + total_no_of_compressions + "] Image(s) Compressed & (Compression Stopped)");
            }

            // Enabling the disabled components
            sd_btn.setEnabled(true);
            single_img_rb.setEnabled(true);
            multi_img_rb.setEnabled(true);
            quality_box.setEnabled(true);
            low_rb.setEnabled(true);
            normal_rb.setEnabled(true);
            high_rb.setEnabled(true);

            // Changing compress label and button back to normal
            compress_lbl.setText("COMPRESS");
            compress_btn.setIcon(compress_img);

            stop_compression = true;  // Setting variable to true

        }

    }


    class Compressor{

        public void compress(String in_img_path, String out_img_path, int compression_width){

            try{

                // Reading input image
                File in_path = new File(in_img_path);
                BufferedImage in_img = ImageIO.read(in_path);

                // Image compression calculation
                float w = (compression_width/(float)in_img.getWidth());
                float h = ((float)in_img.getHeight()*w);
                int height = (int)h;

                // Output image
                BufferedImage out_img = new BufferedImage(compression_width, height, in_img.getType());

                // Resizing the image
                Graphics2D graphics = out_img.createGraphics();
                graphics.drawImage(in_img, 0, 0, compression_width, height, null);
                graphics.dispose();

                // Extension extraction
                String ext = in_img_path.substring(in_img_path.lastIndexOf(".")+1);

                // Adding (Compressed) string to image name
                String path = in_path.getName();
                path = path.substring(0, path.lastIndexOf("."));
                out_img_path = out_img_path + "/" + path + " (Compressed With JIC)" + in_path.getAbsolutePath().substring(in_path.getAbsolutePath().lastIndexOf("."));
                out_img_path = out_img_path.replace("\\", "/");

                // Writing compressed image to disk
                ImageIO.write(out_img, ext, new File(out_img_path));

                compression_counter++;  // Incrementing the compression_counter
                status_bar.setText("[" + compression_counter + "/" + total_no_of_compressions + "] Image(s) Compressed Successfully");

                if(compression_counter == total_no_of_compressions){
                    JOptionPane.showMessageDialog(window, compression_counter + " Image(s) Compressed Successfully", "Successfully Compressed", JOptionPane.INFORMATION_MESSAGE);
                    dest_path = null;
                }

            }catch(Exception e){
                status_bar.setText("[" + compression_counter + "/" + total_no_of_compressions + "] Image(s) Compressed & (Compression Stopped)");
                JOptionPane.showMessageDialog(null, in_img_path + " is causing \n" + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                stop_compression = true;
            }

        }

    }


}
