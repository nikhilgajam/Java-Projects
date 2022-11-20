import java.util.*;

public class Main{

    public static void main(String[] args){

        System.out.println("Song Lyrics API Command Line Interface(CLI)");
        SongLyricsAPI api = new SongLyricsAPI();
        // System.out.println(api.get("baby"));
        Scanner scan = new Scanner(System.in);
        System.out.print("\nEnter song name: ");
        String song = scan.nextLine();
        LinkedList<String> artists = api.getArtists(song);

        System.out.println("\n");

        if(!artists.get(0).equals("Entered song is unavailable")){

            for(int i=0; i<artists.size(); i++){
                System.out.print((i+1) + " " + artists.get(i));
            }
            
            System.out.print("\n\nEnter song or artist number: ");
            int artist_number = scan.nextInt();
            
            if((artist_number < 1) || (artist_number > artists.size())){

                System.out.println("Enter correct number shown on the screen");

            }else{

                System.out.println();
                System.out.println(artists.get(artist_number-1) + "\n");
                String lyrics = api.getLyrics(artists.get(artist_number-1));
                System.out.println(lyrics);
            }
        }else{
            System.out.println("Entered song is unavailable");
        }

    }

}
