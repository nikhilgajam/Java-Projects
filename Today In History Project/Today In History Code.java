import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Jp{

	public static void main(String[] args){

		new SearchAnything();

	}

}


class SearchAnything{

	JTextArea display;

	public SearchAnything(){

		try {
			// Nimbus Theme in Swing
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		}catch (Exception ex){
			JOptionPane.showMessageDialog(null, "Theme Error", "Error", JOptionPane.ERROR_MESSAGE);
		}

		// Window
		JFrame window = new JFrame("Today In History");
		window.setLayout(new BorderLayout());

		// Display textarea
		display = new JTextArea("Welcome");
		display.setFont(new Font("Rockwell", Font.PLAIN, 25));
		display.setWrapStyleWord(true);
		display.setLineWrap(true);
		display.setBackground(Color.BLACK);
		display.setForeground(Color.ORANGE);
		window.add(new JScrollPane(display), BorderLayout.CENTER);

		// Window settings
		window.setVisible(true);
		window.setSize(600, 500);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		startSearch();

	}

	private void startSearch(){

		// Replaces the space with %20 which is space equivalent in url
		String search = new SimpleDateFormat("MMMMM%20dd").format(Calendar.getInstance().getTime());
		GetData get = new GetData("https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&explaintext&titles=" + search + "&redirects=");
		get.start();

	}

	class GetData extends Thread{
		// This class gets the data from internet it takes some time that's why we are using thread

		String link;

		public GetData(String link){
			// link variable holds the link to request
			this.link = link;
		}

		public void run(){

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
				// System.out.println(raw_data);

				// Converts non-unicode characters to unicode like \\u2013 to \u2013
				try{
					Pattern unicode = Pattern.compile("\\\\u(.{4})");
					Matcher matcher = unicode.matcher(raw_data);
					StringBuffer sb = new StringBuffer();
					while (matcher.find()) {
						int code = Integer.parseInt(matcher.group(1), 16);
						matcher.appendReplacement(sb, new String(Character.toChars(code)));
					}
					matcher.appendTail(sb);
					raw_data = sb.toString();
				}catch(Exception e){
					System.out.println(e.toString());
				}

				String title, data;
				int start, end;

				// Title
				start = raw_data.indexOf("\"title\":") + 9;
				end = raw_data.indexOf("\",", start+1);
				title = raw_data.substring(start, end).replace("\\\"", "\"");

				// Data
				start = raw_data.indexOf("\"extract\":") + 11;
				end = raw_data.indexOf("\"}", start+1);
				data = raw_data.substring(start, end);
				data = data.replace("\\n", "\n").replace("\\\"", "\"");

				if(data.contains("may refer to:")){   // "If may refer to:" string occurs then output the following text
					display.setText("Data not found for this keyword");
					return;
				}

				// If missing key occurs then data not found
				if(!raw_data.contains("\"missing\":"))
					display.setText(data);
				else
					display.setText("Data not found try to type keyword correctly without any spelling mistakes");

				display.setCaretPosition(0);

			}catch (java.net.UnknownHostException e){
				JOptionPane.showMessageDialog(null, "Check Your Internet Connection", "Error", JOptionPane.ERROR_MESSAGE);
				display.setText("Check Your Internet Connection");
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				display.setText(e.toString());
			}

		}

	}


}
