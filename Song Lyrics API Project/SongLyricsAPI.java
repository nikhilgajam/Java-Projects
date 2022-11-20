import java.util.LinkedList;
import java.util.HashMap;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.StringReader;

public class SongLyricsAPI{

    HashMap<String, String> title_url_values;
    LinkedList<String> artist_names;

    public SongLyricsAPI(){
        title_url_values = new HashMap<>();
        artist_names = new LinkedList<>();
    }

    public String get(String song_name){
        // This method will get the first song and returns it
        LinkedList<String> artists = getArtists(song_name);

        if(artists.get(0).equals("Entered song is unavailable")){
            return "Entered song is unavailable";
        }else{
            String lyrics_string = getLyrics(artists.get(0));
            return lyrics_string;
        }

    }

    public LinkedList<String> getArtists(String song_name){
        // Converting spaces to html equivalent
        song_name = song_name.replace(" ", "+");

        // Retrieving the lyricsfreak search page
        InternetDownloader id = new InternetDownloader();
        String raw = id.getData("https://search.yahoo.com/search?p=" + song_name + "+site%3Aazlyrics.com");

        // Declaring variables
        int start = 0, end = 0;

        // Song data storing list
        LinkedList<String> data = new LinkedList<>();

        // Storing the actual url and artist in the list
        while(true){
            start = raw.indexOf("\" href=\"https://www.azlyrics.com/lyrics/", end+1);
            end = raw.indexOf("</a></h3>", start);

            if(start == -1 || end == -1){   // If string is not found then break the loop
                break;
            }

            data.add(raw.substring(start+8, end));
        }

        // Storing the title and url in hashmap
        for(String i: data){
            start = i.indexOf("</span></span>");
            if(i.contains("| AZLyrics.com")){
                end = i.indexOf("| AZLyrics.com");
            }else{
                end = i.length();
            }
            String title = i.substring(start+14, end);

            // Unescaping the html entities
            try{
                HTMLDocument doc = new HTMLDocument();
                new HTMLEditorKit().read( new StringReader( "<html><body>" + title), doc, 0 );
                title = doc.getText( 1, doc.getLength());
            } catch( Exception ex ) {
                title = title;
            }

            start = 0;
            end = i.indexOf("\"", start+1);
            String url = i.substring(start, end);

            title_url_values.put(title, url);
        }

        // Handling unavailabe songs
        if(title_url_values.size() == 0){
            artist_names.add("Entered song is unavailable");
            return artist_names;
        }

        // Returning artist or keys of the hashmap as a linkedlist
        for(String i: title_url_values.keySet()){
            artist_names.add(i);
        }

        // Returning artist or keys of the dictionary as a list
        return artist_names;

    }

    public String getLyrics(String artist_name){
        // Returns a message if this method is executed first
        if(title_url_values.size() == 0){
            return "Use the getArtist method first and getLyrics method next";
        }

        if(artist_names.get(0) == "Entered song is unavailable"){
            return "Enter any available song please";
        }

        // Taking url by using artist
        String artist_song_url = title_url_values.get(artist_name);

        // Retriving the lyricsfreak song page
        InternetDownloader id = new InternetDownloader();
        String raw = id.getData(artist_song_url);

        // Taking the song starting line by eliminating unwanted html script
        int start = raw.indexOf("that. -->");
        int end = raw.indexOf("</div>", start+1);

        String lyrics = raw.substring(start, end);

        // Removing the unwanted div, removing whitespaces from first and last
        lyrics = lyrics.substring(lyrics.indexOf(">")+2).trim();
        // Unescaping the html entities
        try{
            HTMLDocument doc = new HTMLDocument();
            new HTMLEditorKit().read( new StringReader( "<html><body>" + raw), doc, 0 );
            raw = doc.getText( 1, doc.getLength());
        } catch( Exception ex ) {
            raw = raw;
        }
        
        // Replacing the break tags with null string
        lyrics = lyrics.replace("<br>", "");
        lyrics = lyrics.replace("<i>", "");
        lyrics = lyrics.replace("</i>", "");

        // Returning the lyrics
        return lyrics;
    }

}
