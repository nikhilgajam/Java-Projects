import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;

public class Jp{

	public static void main(String[] args){

		new JInternetDownloader();

	}

}

class JInternetDownloader{

	JTextField url_box;
	JButton download_btn;
	JProgressBar progress_bar;
	String pwd = System.getProperty("user.dir") + "\\JD.txt";  // Present working directory

	public JInternetDownloader(){

		try {
			// Nimbus Theme in Swing
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		}catch (Exception ex){
			JOptionPane.showMessageDialog(null, "Theme Error", "Error", JOptionPane.ERROR_MESSAGE);
		}

		// Window
		JFrame window = new JFrame("JInternetDownloader");

		// Url box
		url_box = new JTextField();
		url_box.setToolTipText("Enter URL To Be Downloaded And Press Enter");
		url_box.addActionListener(e -> startDownload());
		window.add(url_box);

		// Download btn
		download_btn = new JButton("Download");
		download_btn.setToolTipText("Click This Button To Start The Download Process");
		download_btn.addActionListener(e -> startDownload());
		window.add(download_btn);

		// Progress bar
		progress_bar = new JProgressBar();
		window.add(progress_bar);

		// Windows settings
		window.setVisible(true);
		window.setSize(400, 160);
		window.setLocationRelativeTo(null);
		window.setLayout(new GridLayout(3, 1));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private void startDownload(){

		try{

			String temp = url_box.getText();
			if(temp.equals("")){
				JOptionPane.showMessageDialog(null, "Enter valid URL",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Choose box
			JFileChooser jc = new JFileChooser();
			jc.showSaveDialog(null);
			pwd = jc.getSelectedFile().getAbsolutePath();
			// System.out.println(pwd);

			// Creating the object of GetData class to start the thread
			GetData get = new GetData(temp);
			get.start();

		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Error In Selecting The Location",
					"Error", JOptionPane.ERROR_MESSAGE);
		}

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

				// Establishing a connection
				URL url = new URL(link);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
				// long length = connection.getContentLengthLong();

				// Creating an object to the data
				InputStream stream = connection.getInputStream();
				FileOutputStream w = new FileOutputStream(pwd);   // Creates a new document in the location present in pwd

				int ch;
				int count = 0;
				// Reading the data from the connection through inputstream object
				while((ch = stream.read()) != -1){
					w.write(ch);    // Writes the data to document or file in pwd

					if(count == 100)
						count = 0;

					progress_bar.setValue(count);   // Calculating the percentage to update progress
					count++;
				}

				// In the last sets the progress bar value to 100
				progress_bar.setValue(100);
				w.close();

				JOptionPane.showMessageDialog(null, "Downloaded Successfully ", "Download Completed", JOptionPane.PLAIN_MESSAGE);

			}catch(Exception e){

				JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);

			}

		}

	}


}
