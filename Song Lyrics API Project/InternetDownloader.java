import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;

public class InternetDownloader{

    public String getData(String link){

        try{

            // Used in loop to make a string
            StringBuilder str = new StringBuilder();

            // Establishing a connection
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // Creating an object to the data
            InputStream stream = connection.getInputStream();

            int ch;
            // Reading the data from the connection through inputstream object
            while((ch = stream.read()) != -1){
                str.append((char)ch);
            }

            String raw_data = str.toString();
            return raw_data;

        }catch(java.net.UnknownHostException e){

            return "Check Your Internet Connection";

        }catch(Exception e){

            return e.toString();

        }

    }

}
