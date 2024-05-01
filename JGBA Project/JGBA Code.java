import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;

public class Jp {

    public static void main(String[] args) {

        new JGBA();

    }

}

class JGBA {

    private final JMenuItem how_to, open_settings, about_us;
    private final TreeMap<String, String> all_games_list = new TreeMap<>();
    private final TreeMap<String, String> downloaded_games_list = new TreeMap<>();
    private final String all_games_location = "metadata/AllGamesData";
    private final String downloaded_games_save_location = "Data/DownloadedGamesData";
    private final String emulator_settings_path = "Emulator/EmulatorSettings.txt";
    private final String game_load_path = "Data/DownloadedGames/";
    // Variables
    private final JFrame window;
    private final JTextField search_box;
    private final JMenuBar menu_bar;
    private final JMenu help_menu;
    private final JMenu settings_menu;
    private final JMenu about_menu;
    private final JProgressBar progress_bar;
    private final JButton all_games_btn;
    private final JButton downloaded_games_btn;
    private final JButton download_btn;
    private final JButton load_btn;
    private final JButton delete_btn;
    private final JList<String> listbox;
    private final String save_location = "Data/DownloadedGames/";
    private boolean displaying_all_games_list, is_downloading = false;
    private String emulator_path = "\"Emulator/mGBA-0.10.3-win32/mGBA.exe\"";
    private String just_downloaded_game_name = "";

    public JGBA() {

        // Window
        window = new JFrame("JGBA");
        window.setLayout(new BorderLayout());

        // Icon
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/icon.png"))); // Icon to be placed near *.class or inside jar
        window.setIconImage(icon.getImage());

        try {
            // Nimbus Theme in Swing
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Theme Error", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Title Label
        JLabel title = new JLabel("JGameBoyAdvance");
        title.setFont(new Font("Times New Roman", Font.PLAIN, 65));
        title.setOpaque(true);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setForeground(Color.decode("#e6e8eb"));
        title.setBackground(Color.decode("#333333"));
        window.add(title, BorderLayout.NORTH);

        // Listbox
        listbox = new JList<>(); // listbox = new JList<>(list.keySet().toArray(new String[0]));
        listbox.setForeground(Color.decode("#e6e8eb"));
        listbox.setBackground(Color.decode("#333333"));
        listbox.setSelectionForeground(Color.decode("#e6e8eb"));
        listbox.setSelectionBackground(Color.decode("#993e1c"));
        listbox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // Only one item should be selected
        listbox.setFont(new Font("Times New Roman", Font.PLAIN, 28));
        listbox.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();
                if (ch == 10) {  // Using enter key to perform download or load operation
                    // Enter = 10
                    if (displaying_all_games_list) {
                        downloadGame();
                    } else {
                        loadGame();
                    }
                }else if(ch == 47){  // Using / key the search box will be selected
                    // /(Slash) = 47
                    search_box.grabFocus();
                }
            }
        });
        window.add(new JScrollPane(listbox), BorderLayout.CENTER);

        // Button panel
        JPanel btn_panel = new JPanel();
        btn_panel.setBackground(Color.decode("#333333"));
        btn_panel.setLayout(new BoxLayout(btn_panel, BoxLayout.Y_AXIS));
        btn_panel.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));  // Giving padding to the btn_panel

        // Progress Bar
        progress_bar = new JProgressBar();
        btn_panel.add(progress_bar);

        // Search Box
        search_box = new JTextField("");
        search_box.setToolTipText("Search Box - Type Something And Press Enter To Search");
        search_box.setAlignmentX(Component.CENTER_ALIGNMENT);
        search_box.setHorizontalAlignment(JTextField.CENTER);
        search_box.setColumns(40);
        search_box.setMinimumSize(search_box.getPreferredSize());
        search_box.setMaximumSize(search_box.getPreferredSize());
        search_box.setForeground(Color.decode("#e6e8eb"));
        search_box.setBackground(Color.decode("#993e1c"));
        search_box.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {  // Using enter key to perform download or load operation
                if (e.getKeyChar() == 10)
                    searchList();
            }
        });
        btn_panel.add(search_box);

        // Main Buttons Panel
        JPanel main_btn_panel = new JPanel();
        main_btn_panel.setBackground(Color.decode("#333333"));
        btn_panel.add(main_btn_panel);

        all_games_btn = new JButton("All Games List");
        all_games_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        all_games_btn.setForeground(Color.decode("#e6e8eb"));
        all_games_btn.setBackground(Color.decode("#333333"));
        all_games_btn.addActionListener(e -> gameLoader(true));
        main_btn_panel.add(all_games_btn);

        downloaded_games_btn = new JButton("Downloaded Games List");
        downloaded_games_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        downloaded_games_btn.setForeground(Color.decode("#e6e8eb"));
        downloaded_games_btn.setBackground(Color.decode("#333333"));
        downloaded_games_btn.addActionListener(e -> gameLoader(false));
        main_btn_panel.add(downloaded_games_btn);

        // Sub Buttons Panel
        JPanel sub_btn_panel = new JPanel();
        sub_btn_panel.setBackground(Color.decode("#333333"));
        btn_panel.add(sub_btn_panel);

        download_btn = new JButton("Download");
        download_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        download_btn.setForeground(Color.decode("#e6e8eb"));
        download_btn.setBackground(Color.decode("#333333"));
        download_btn.addActionListener(e -> downloadGame());
        sub_btn_panel.add(download_btn);

        load_btn = new JButton("Load");
        load_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        load_btn.setForeground(Color.decode("#e6e8eb"));
        load_btn.setBackground(Color.decode("#333333"));
        load_btn.addActionListener(e -> loadGame());
        sub_btn_panel.add(load_btn);

        delete_btn = new JButton("Delete");
        delete_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        delete_btn.setForeground(Color.decode("#e6e8eb"));
        delete_btn.setBackground(Color.decode("#333333"));
        delete_btn.addActionListener(e -> deleteGame());
        sub_btn_panel.add(delete_btn);

        window.add(btn_panel, BorderLayout.SOUTH);

        // Menu
        menu_bar = new JMenuBar();

        // Help menu
        help_menu = new JMenu("Help");

        how_to = new JMenuItem("How To Use JGBA");
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

        // Initialization
        init();

        // Window settings
        window.setVisible(true);
        window.setSize(800, 600);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Methods

    private void init() {
        // Contains all initializations to start the program

        // Creating Data directory and DownloadGamesData file if not exists
        if (!new File("Data").exists()) {
            new File(save_location).mkdirs();
            writeOperation(downloaded_games_save_location, "", false);
        }

        // Creating Emulator directory and EmulatorSettings.txt file if not exists
        if (!new File("Emulator").exists()) {
            new File("Emulator").mkdir();
            writeOperation(emulator_settings_path, emulator_path, false);
        } else {
            // If file exists then read and store that value in emulator_path variable
            emulator_path = readOperation(emulator_settings_path);
        }

        loadingAllAndDownloadedGameLists();

        // Displaying all_games_list
        gameLoader(true);
    }

    private void loadingAllAndDownloadedGameLists() {
        // Reading and adding all games data to the list

        String data = readFromJAR(all_games_location);
        if (!data.isEmpty()) {
            String[] all_data_arr = data.split("\\n");

            String d_data = readOperation(downloaded_games_save_location).trim();
            String[] d_data_arr = d_data.split("\\n");

            // Adding downloaded games to the downloaded_games_list TreeMap
            if (!d_data.isEmpty()) {
                for (String s : d_data_arr) {
                    String[] temp_arr = s.split(" : ");
                    downloaded_games_list.put(temp_arr[0], temp_arr[1]);
                }
            }

            // Adding all games to the all_games_list TreeMap which are not present in downloaded_games_list
            for (String s : all_data_arr) {
                String[] temp_arr = s.split(" : ");
                if (!downloaded_games_list.containsKey(temp_arr[0]))
                    all_games_list.put(temp_arr[0], temp_arr[1]);
            }
        }
    }

    private void gameLoader(boolean all_games) {
        // Loads the game list depending on the boolean all_games variable if game is not downloading

        if (is_downloading) {
            JOptionPane.showMessageDialog(null, "Wait Until Game Gets Downloaded", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (all_games) {
            displaying_all_games_list = true;  // This variable is used for Enter key mapping and search
            // Changing the selected button to orange color
            all_games_btn.setBackground(Color.decode("#993e1c"));
            downloaded_games_btn.setBackground(Color.decode("#333333"));

            // Showing and hiding the buttons based on list selected
            download_btn.setVisible(true);
            load_btn.setVisible(false);
            delete_btn.setVisible(false);

            displayAllGames();
        } else {
            displaying_all_games_list = false;  // This variable is used for Enter key mapping and search
            // Changing the selected button to orange color
            downloaded_games_btn.setBackground(Color.decode("#993e1c"));
            all_games_btn.setBackground(Color.decode("#333333"));

            // Showing and hiding the buttons based on list selected
            download_btn.setVisible(false);
            load_btn.setVisible(true);
            delete_btn.setVisible(true);

            displayDownloadedGames();
        }
    }

    private void setGamesCount(int all_games_count, int downloaded_games_count) {
        // Displays the all games and downloaded games count inside buttons

        all_games_btn.setText("All Games List (" + all_games_count + ")");
        downloaded_games_btn.setText("Downloaded Games List (" + downloaded_games_count + ")");
    }

    private void setAllGamesSearchedGamesCount(int all_games_count, int downloaded_games_count) {
        // Displays the all games searched list and downloaded games count inside buttons

        all_games_btn.setText("All Games Searched List (" + all_games_count + ")");
        downloaded_games_btn.setText("Downloaded Games List (" + downloaded_games_count + ")");
    }

    private void setDownloadedGamesSearchedGamesCount(int all_games_count, int downloaded_games_count) {
        // Displays the all games and downloaded games searched count inside buttons

        all_games_btn.setText("All Games List (" + all_games_count + ")");
        downloaded_games_btn.setText("Downloaded Games Searched List (" + downloaded_games_count + ")");
    }

    private void displayAllGames() {
        // Loads all_games_list in the listbox and sets games count

        try {
            setGamesCount(all_games_list.size(), downloaded_games_list.size());

            listbox.setListData(all_games_list.keySet().toArray(new String[0]));  // Updating the listbox(JList)
            listbox.grabFocus();
            listbox.setSelectedIndex(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayDownloadedGames() {
        // Loads downloaded_games_list in the listbox and sets games count

        try {
            setGamesCount(all_games_list.size(), downloaded_games_list.size());

            String[] arr = downloaded_games_list.keySet().toArray(new String[0]);
            listbox.setListData(arr);  // Updating the listbox(JList)
            listbox.grabFocus();

            // Points the just_downloaded_game_name in the list when the game name is not Empty by searching the name in list
            if (just_downloaded_game_name.isEmpty())
                listbox.setSelectedIndex(0);
            else
                listbox.setSelectedIndex(Arrays.binarySearch(arr, just_downloaded_game_name));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchList() {
        // Searches the opened list when no game is getting downloaded

        if (is_downloading) {
            JOptionPane.showMessageDialog(null, "Wait Until Game Gets Downloaded", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TreeSet<String> search_list = new TreeSet<>();
        TreeMap<String, String> data_list;

        String input = search_box.getText().toLowerCase();

        // Pointing data_list to all_games_list or downloaded_games_list
        if (displaying_all_games_list) {
            data_list = all_games_list;
        } else {
            data_list = downloaded_games_list;
        }

        // Searching the list if the input is not empty
        if (!input.isEmpty()) {
            for (String i : data_list.keySet()) {
                if (i.toLowerCase().contains(input)) {
                    search_list.add(i);
                }
            }

            // Displaying search list size
            if (displaying_all_games_list) {
                setAllGamesSearchedGamesCount(search_list.size(), downloaded_games_list.size());
            } else {
                setDownloadedGamesSearchedGamesCount(all_games_list.size(), search_list.size());
            }

            listbox.setListData(search_list.toArray(new String[0]));  // Updating the listbox(JList)
            listbox.grabFocus();
            if (search_list.size() > 0) {
                listbox.setSelectedIndex(0);
                listbox.scrollRectToVisible(listbox.getCellBounds(0, 1));  // Scrolling to the top
            }
        }

    }

    private void downloadGame() {
        // This method will initialize the game download process if any game is selected

        String selected_val = listbox.getSelectedValue();

        if (selected_val == null) {
            JOptionPane.showMessageDialog(null, "Select A Title To Download", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            if (is_downloading) {
                is_downloading = false; // Stopping the download process
            } else {
                String url = all_games_list.get(selected_val);
                just_downloaded_game_name = selected_val;
                new DownloadGame("https://" + url, save_location + selected_val + ".zip").start();  // Downloading
            }
        }

    }

    private void loadGame() {
        // This method will start the process to load the game

        String selected_val = listbox.getSelectedValue();
        if (selected_val == null || selected_val.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Download A Game By Navigating To All Games Then Clicking On Download Button", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            runGame(game_load_path + selected_val + ".zip");
        }
    }

    private void runGame(String path) {
        // This method will invoke the emulator to run the game

        try {
            File file = new File(path);
            String command = emulator_path + " \"" + file.getAbsolutePath() + "\"";
            Runtime.getRuntime().exec(command);  // Executing the command to start the game
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error (runGame)", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteGame() {
        // This method will delete any downloaded game

        try {
            String selected_val = listbox.getSelectedValue();
            if (selected_val == null || selected_val.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Select A Title To Delete.\n" +
                        "If The List Is Empty Then Download Any Game.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int value = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this " + selected_val + " game?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
            if (value == JOptionPane.NO_OPTION || value == -1) {
                return;
            }

            String path = "Data/DownloadedGames/" + selected_val + ".zip";
            File file = new File(path);

            if (file.delete()) {
                JOptionPane.showMessageDialog(null, selected_val + " Game Deleted Successfully", "Game Deleted", JOptionPane.PLAIN_MESSAGE);
                all_games_list.put(selected_val, downloaded_games_list.remove(selected_val));  // Adding deleted game to the all games list and deleting from downloaded list

                writeOperation(downloaded_games_save_location, "", false);  // Clearing the previous content
                for (String title : downloaded_games_list.keySet()) {
                    writeOperation(downloaded_games_save_location, title + " : " + all_games_list.get(title) + "\n", true);
                }

                if (selected_val.equals(just_downloaded_game_name)) {
                    just_downloaded_game_name = "";
                }

                displayDownloadedGames();
            } else {
                JOptionPane.showMessageDialog(null, "Cannot delete " + selected_val + " game.\n" +
                        "If you are playing this game then close the game and try again.", "Error (deleteGame)", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error (runGame)", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String readFromJAR(String text_path) {
        // This method will read files present in the JAR

        StringBuilder data = new StringBuilder();

        try {
            InputStream data_path = getClass().getResourceAsStream(text_path);  // To get the data inside JAR
            assert data_path != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(data_path, StandardCharsets.UTF_8));   // To read utf-8 text

            // Reading
            int c;
            while ((c = br.read()) != -1) {
                data.append((char) c);
            }

            br.close();
            data_path.close();
        } catch (Exception e) {
            // Showing the error message
            JOptionPane.showMessageDialog(null, e, "Error (Read From JAR)", JOptionPane.ERROR_MESSAGE);
        }

        return data.toString();
    }


    private String readOperation(String url) {
        // This method will read files present on the disk

        StringBuilder data = new StringBuilder();
        try {
            FileInputStream read = new FileInputStream(url);

            // Reading
            int ch;
            while ((ch = read.read()) != -1) {
                data.append((char) ch);
            }

            read.close();
        } catch (Exception e) {
            // Showing the error message
            JOptionPane.showMessageDialog(null, e, "Error (Read Operation)", JOptionPane.ERROR_MESSAGE);
        }

        return data.toString();
    }


    private void writeOperation(String url, String data, boolean append) {
        // This method will write files to the disk

        try {
            FileOutputStream write = new FileOutputStream(url, append);
            for (int i = 0; i < data.length(); i++) {
                write.write(data.charAt(i));  // Writing data
            }
            write.close();
        } catch (Exception e) {
            // Showing the error message
            JOptionPane.showMessageDialog(null, e, "Error (Write Operation)", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void howToHelp() {
        // Displays how to help message box

        JOptionPane.showMessageDialog(null, "You Need To Select Any Game In All Games List " +
                "Or You Can Search Any Game.\nClick On Download Button Then After The Game Gets Downloaded Click Load Button To Play.\n" +
                "You Can Click On Delete Button To Delete Any Game And You Can Click (Slash Key) / To Select Search Box.", "How To Use JGBA", JOptionPane.PLAIN_MESSAGE);
    }

    private void aboutUs() {
        // Displays about us message box

        JOptionPane.showMessageDialog(null, "JGBA Version 1.0\nDeveloped By Nikhil", "About Us", JOptionPane.PLAIN_MESSAGE);
    }

    private void openSettings() {
        // This method will create an anonymous object of Settings

        new Settings();
    }


    // Inner Classes

    class Settings {
        private final JFrame settings_window;
        private final JLabel title, emulator_path_box_label;
        private final JTextField emulator_path_box;
        private final JButton save_path_btn;

        Settings() {

            // Window
            settings_window = new JFrame("JGBA Settings");
            settings_window.setLayout(new BorderLayout());
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("images/icon.png")));
            settings_window.setIconImage(icon.getImage());

            // Settings title
            title = new JLabel("JGBA Settings");
            title.setFont(new Font("Times New Roman", Font.PLAIN, 35));
            title.setOpaque(true);
            title.setHorizontalAlignment(JLabel.CENTER);
            title.setForeground(Color.decode("#e6e8eb"));
            title.setBackground(Color.decode("#333333"));
            settings_window.add(title, BorderLayout.NORTH);

            // Buttons panel
            JPanel btn_panel = new JPanel();
            btn_panel.setBackground(Color.decode("#333333"));
            btn_panel.setLayout(new BoxLayout(btn_panel, BoxLayout.Y_AXIS));
            btn_panel.setBorder(new javax.swing.border.EmptyBorder(25, 0, 25, 0));

            // Emulator path box label
            emulator_path_box_label = new JLabel("Emulator Path:");
            emulator_path_box_label.setFont(new Font("Times New Roman", Font.PLAIN, 22));
            emulator_path_box_label.setOpaque(true);
            emulator_path_box_label.setAlignmentX(Component.CENTER_ALIGNMENT);
            emulator_path_box_label.setForeground(Color.decode("#e6e8eb"));
            emulator_path_box_label.setBackground(Color.decode("#333333"));
            btn_panel.add(emulator_path_box_label);

            // Emulator path box
            emulator_path_box = new JTextField(emulator_path.replaceAll("\"", ""));
            emulator_path_box.setAlignmentX(Component.CENTER_ALIGNMENT);
            emulator_path_box.setHorizontalAlignment(JTextField.CENTER);
            emulator_path_box.setColumns(26);
            emulator_path_box.setMinimumSize(emulator_path_box.getPreferredSize());
            emulator_path_box.setMaximumSize(emulator_path_box.getPreferredSize());
            emulator_path_box.setForeground(Color.decode("#e6e8eb"));
            emulator_path_box.setBackground(Color.decode("#993e1c"));
            btn_panel.add(emulator_path_box);

            // Save path button
            save_path_btn = new JButton("Save Path");
            save_path_btn.setForeground(Color.decode("#e6e8eb"));
            save_path_btn.setBackground(Color.decode("#333333"));
            save_path_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            save_path_btn.addActionListener(e -> savePath());
            btn_panel.add(save_path_btn);

            settings_window.add(btn_panel, BorderLayout.CENTER);

            // Window settings
            settings_window.setVisible(true);
            settings_window.setSize(400, 220);
            settings_window.setLocationRelativeTo(null);
            settings_window.setResizable(false);
            settings_window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        }

        // Button action method

        private void savePath() {
            String new_emulator_path = emulator_path_box.getText();

            String old_emulator_path = emulator_path.replaceAll("\"", "");

            if (!old_emulator_path.equals(new_emulator_path)) {

                emulator_path_box.setText(new_emulator_path);

                if (!new File(new_emulator_path).exists()) {
                    JOptionPane.showMessageDialog(null, "Please Enter A Valid Path", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Emulator Path Updated Successfully", "Emulator Path", JOptionPane.PLAIN_MESSAGE);
                    new_emulator_path = '"' + new_emulator_path + '"';
                    writeOperation(emulator_settings_path, new_emulator_path, false);
                    emulator_path = new_emulator_path;
                    settings_window.dispose();
                }
            }
        }
    }


    class DownloadGame extends Thread {
        // This class gets the data from internet it takes some time that's why we are using thread

        String link, save_location;

        public DownloadGame(String url, String save_location) {
            // link variable holds the link to request
            this.link = url;
            this.save_location = save_location;
        }

        public void run() {
            try {
                // Initializing the progress bar
                progress_bar.setValue(1);
                is_downloading = true;
                download_btn.setText("Stop Downloading");

                // Establishing a connection
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                long length = connection.getContentLengthLong();

                // Creating an object to the data
                InputStream in = connection.getInputStream();
                FileOutputStream out = new FileOutputStream(save_location);   // Creates a new document in the location present in pwd

                int ch;
                long count = 0;
                // Reading the data from the connection through inputstream object
                while ((ch = in.read()) != -1 && is_downloading) {
                    out.write(ch);    // Writes the data to document or file in pwd

                    // Calculates the value to display in progress bar as 100% ((downloaded_bits/total_size) * 100)
                    final int currentProgress = (int) ((((double) count) / ((double) length)) * 100);
                    progress_bar.setValue(currentProgress);   // Updating progress bar value

                    count++;
                }

                out.close();
                in.close();

                if (is_downloading) {
                    // Setting the progress bar value to 100
                    progress_bar.setValue(100);
                    writeOperation(downloaded_games_save_location, just_downloaded_game_name + " : " + all_games_list.get(just_downloaded_game_name) + "\n", true);  // Writing downloaded game to the disk
                    downloaded_games_list.put(just_downloaded_game_name, all_games_list.remove(just_downloaded_game_name));
                    JOptionPane.showMessageDialog(null, "Downloaded Successfully ", "Download Completed", JOptionPane.PLAIN_MESSAGE);
                    download_btn.setText("Download");

                    is_downloading = false;
                    gameLoader(false);

                } else {

                    if (new File(save_location).delete()) {
                        JOptionPane.showMessageDialog(null, "Game Download Stopped", "Download Stopped", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Game Download Stopped But Partially Downloaded File Exists", "Download Stopped", JOptionPane.WARNING_MESSAGE);
                    }

                    // Setting the progress bar value to 0
                    progress_bar.setValue(0);
                    download_btn.setText("Download");
                    just_downloaded_game_name = "";
                    is_downloading = false;

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                is_downloading = false;
                download_btn.setEnabled(true);
                progress_bar.setValue(0);
            }
        }
    }


}
