import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.net.HttpURLConnection;
import javax.sound.sampled.Clip;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.File;
import java.net.URL;

public class Jp{

    public static void main(String[] args){

        JTextToSpeech jts = new JTextToSpeech("Hello World");

        try{

            jts.save("hello");
            jts.play();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}


class JTextToSpeech{

    String text;
    String lang = "en";
    String path = "";

    public JTextToSpeech(String text){

        text = text.replace(" ", "%20");
        this.text = text;

    }

    public JTextToSpeech(String text, String lang){

        text = text.replace(" ", "%20");
        this.text = text;
        this.lang = lang;

    }

    public void save(String path) throws Exception{

        try{

            this.path = path + ".wav";  // It takes extension by default as .wav

            // Establishing a connection
            String link = "https://translate.google.com/translate_tts?ie=UTF-8&q=" + text + "&tl=" + lang + "&tk=995126.592330&client=t";
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // Creating an object to the data
            InputStream stream = connection.getInputStream();
            FileOutputStream w = new FileOutputStream(this.path);   // Creates a new document in the location present in pwd

            int ch;
            // Reading the data from the connection through inputstream object
            while((ch = stream.read()) != -1){
                w.write((byte) ch);    // Writes the data to document or file in pwd
            }

            w.close();
        }catch(Exception e){
            throw new Exception(e.toString());
        }

    }

    public void play() throws Exception{

        try{
            System.out.println(path);
            System.out.println(new File(path).exists());
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        }catch(Exception e){
            throw new Exception(e.toString());
        }

    }

}
