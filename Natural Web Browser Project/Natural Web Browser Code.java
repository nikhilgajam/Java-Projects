import javafx.application.Application;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Jp extends Application {

    WebView view;
    WebEngine web;
    Button back_btn, forward_btn, home_btn, reload_btn, history_btn, n_btn;
    TextField url_box;
    ProgressBar progressBar;

    public static void main(String[] args) {

        launch(args);

    }

    @Override
    public void start(Stage stage) {

        // Components on window

        url_box = new TextField();
        url_box.setPromptText("Search Anything");
        url_box.setOnAction(e -> load_pages());

        view = new WebView();
        web = view.getEngine();

        HBox controls = new HBox();
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(11);

        progressBar = new ProgressBar();

        back_btn = new Button(" ❮ ");   // ◀
        back_btn.setTooltip(new Tooltip("Click to go back"));
        back_btn.setOnAction(e -> load_back());
        HBox.setMargin(back_btn, new Insets(3, 3, 3, 3));

        forward_btn = new Button(" ❯ ");    // ▶
        forward_btn.setTooltip(new Tooltip("Click to go forward"));
        forward_btn.setOnAction(e -> load_forward());

        reload_btn = new Button(" \uD83D\uDD03 ");
        reload_btn.setTooltip(new Tooltip("Reload"));
        reload_btn.setOnAction(e -> reload());

        n_btn = new Button(" N ");
        n_btn.setTooltip(new Tooltip("Nikhil"));
        n_btn.setOnAction(e -> n_page());

        home_btn = new Button("\uD83C\uDFE0");
        home_btn.setTooltip(new Tooltip("Home"));
        home_btn.setOnAction(e -> home_page());

        history_btn = new Button("☰");
        history_btn.setTooltip(new Tooltip("History"));
        history_btn.setOnAction(e -> history_display());

        controls.getChildren().addAll(back_btn, forward_btn, reload_btn, progressBar, n_btn, home_btn, history_btn);

        BorderPane layout = new BorderPane();
        layout.setTop(url_box);
        layout.setCenter(view);
        layout.setBottom(controls);

        // Window settings

        stage.setTitle("Natural Web Browser");
        Scene scene = new Scene(layout, 1000, 600);
        stage.setScene(scene);
        Image img = new Image(getClass().getResourceAsStream("res/natural.png"));
        stage.getIcons().add(img);
        stage.show();

        // Loading the home page
        home_page();

    }

    // Methods that add controls to buttons

    private void load_pages(){

        progressBar.progressProperty().bind(view.getEngine().getLoadWorker().progressProperty());

        String url = url_box.getText();

        if(!url.contains(".")){
            url = "https://www.google.com/search?q=" + url;
        }

        if(url.contains(".")){

            if(!url.startsWith("http"))
                url = "https://www." + url;

        }

        web.load(url);
        url_box.setText(web.getLocation());

    }

    private void load_back(){

        web.executeScript("history.back()");
        url_box.setText("");

    }

    private void load_forward(){

        web.executeScript("history.forward()");
        url_box.setText("");

    }

    private void reload(){

        web.reload();
        url_box.setText(web.getLocation());

    }

    private void n_page(){

        web.load("https://nikhilphotography.weebly.com/");
        // Setting the progress bar to load mode
        progressBar.progressProperty().bind(view.getEngine().getLoadWorker().progressProperty());
        url_box.setText(web.getLocation());


    }

    private void history_display(){

        StringBuilder str = new StringBuilder();
        str.append("<html><h1>History</h1> <style>body{color: rgb(218, 220, 224);background-color:rgb(53, 54, 58);font-family: monospace;} a{color:orange;}</style>");

        int count = 1;

        String history = web.getHistory().getEntries().toString();

        String[] sep = history.split(",");

        for(int i=0; i<sep.length; i++){
            if(sep[i].contains("url")) {

                if(i==0)
                    sep[i] = sep[i].replace("[[url:", "");
                else
                    sep[i] = sep[i].replace("[url:", "");

                str.append(count).append(" - <a href='").append(sep[i]).append("'>").append(sep[i]).append("</a><br><br>");
                count++;
            }
        }

        str.append("</html>");

        web.loadContent(str.toString());

    }

    private void home_page(){

        web.load(getClass().getResource("res/index.html").toString());
        url_box.setText("");

    }


}