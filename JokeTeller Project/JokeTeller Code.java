import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class Jp{

	public static void main(String[] args){

		new JokeTeller();

	}

}

class JokeTeller{

	JLabel title;
	JTextArea display;
	JButton new_joke;

	public JokeTeller(){

		// Window
		JFrame window = new JFrame("JokeTeller");
		window.setLayout(new BorderLayout(4, 4));

		try {
			// Nimbus Theme in Swing
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		}catch (Exception ex){
			JOptionPane.showMessageDialog(null, "Theme Error", "Error", JOptionPane.ERROR_MESSAGE);
		}

		// Title label
		title = new JLabel("JokeTeller");
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setFont(new Font("Stencil", Font.PLAIN, 50));
		window.add(title, BorderLayout.NORTH);

		// Display textarea
		display = new JTextArea();
		display.setOpaque(true);
		display.setFont(new Font("MV Boli", Font.BOLD, 25));
		display.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));   // Changing cursor to arrow
		display.setWrapStyleWord(true);
		display.setLineWrap(true);
		display.setFocusable(false);
		display.setSize(400, 400);
		window.add(new JScrollPane(display), BorderLayout.CENTER);

		// New joke request button
		new_joke = new JButton("New Joke");
		new_joke.setToolTipText("Press Space To Load New Joke");
		new_joke.setFont(new Font("Stencil", Font.PLAIN, 25));
		new_joke.addActionListener(e -> showJoke());
		window.add(new_joke, BorderLayout.SOUTH);

		// Window settings
		window.setVisible(true);
		window.setSize(500, 500);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Loads a random joke
		showJoke();

	}

	private void showJoke(){

		GetData obj = new GetData("https://v2.jokeapi.dev/joke/Any?format=txt");  // Joke api url link
		display.setText(display.getText() + "\n\nRandom Joke Is Loading...");   // Printing the loading message
		obj.start();  // Starting the thread

	}

	// Inner class
	class GetData extends Thread{
		// This class gets the data from internet it takes some time that's why we are using thread
		String link;

		public GetData(String link){
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

				// Outputs data to the display textarea
				display.setText(str.toString());

			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "Check Your Internet Connection", "Error", JOptionPane.ERROR_MESSAGE);
				display.setText("Check Your Internet Connection");
			}

		}

	}


}
