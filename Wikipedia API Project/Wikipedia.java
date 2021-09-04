import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wikipedia{
	// This class gets the data from internet it takes some time that's why we are using thread

	private String keyword;
	private String title = "Run \"getSummary()\" or \"getPage()\" first then try using getTitle() to get the title.";
	private String data;

	public Wikipedia(String keyword){
		// link variable holds the link to request
		this.keyword = keyword;
		if(!this.keyword.equals("")){
			this.keyword = this.keyword.toLowerCase().replaceAll(" ", "%20");  // %20 is equivalent to space in url
		}else{
			title = "Enter correct keyword";
			data = "Enter correct keyword";
		}
	}

	// Requests the wikipedia page
	private void getData(String link){

		try{

			// Used in loop to make a string
			StringBuilder str = new StringBuilder();

			// Establishing a connection
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();

			/*
			How to use above wikipedia query in different ways:

			You can get wiki data in Text formats. If you need to access many title's informations, you can get all title's wiki data in a single call. Use pipe character ( | ) to separate each titles.

			Original url => http://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exlimit=max&explaintext&exintro&titles=Yahoo|Google&redirects=

			Here this api call return both Googles and Yahoos data.

			explaintext => Return extracts as plain text instead of limited HTML.

			exlimit = max (now its 20); Otherwise only one result will return.

			exintro => Return only content before the first section. If you want full data, just remove this.

			redirects= Resolve redirect issues.

		 */

			// Creating an object to the data
			InputStream stream = connection.getInputStream();

			int ch;
			// Reading the data from the connection through inputstream object
			while((ch = stream.read()) != -1){
				str.append((char)ch);
			}

			String raw_data = str.toString();
//			System.out.println(raw_data);

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

			int start, end;

			// Title
			start = raw_data.indexOf("\"title\":") + 9;
			end = raw_data.indexOf("\",", start+1);
			title = raw_data.substring(start, end).replace("\\\"", "\"");

			// Data
			start = raw_data.indexOf("\"extract\":") + 11;
			end = raw_data.indexOf("\"}", start+1);

			if(start == -1 || end == -1){
				data = "Data not found for this keyword";
				return;
			}

			data = raw_data.substring(start, end).replace("\\n", "\n").replace("\\\"", "\"");

			if(data.contains("may refer to:")){   // "If may refer to:" string occurs then output the following text
				data = "Data not found for this keyword";
				return;
			}

			// If missing key occurs then data not found
			if(!raw_data.contains("\"missing\":"))
				data = data;
			else
				data = "Wikipedia data not found try to type keyword correctly";

		}catch (java.net.UnknownHostException e){
			data = "Check Your Internet Connection";
		}catch(Exception e){
			data = e.toString();
		}

	}

	// Returns title of the search
	public String getTitle(){
		return title;
	}

	// Returns summary
	public String getSummary(){
		getData("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exlimit=max&explaintext&exintro&titles=" + keyword + "&redirects=");
		return data;
	}

	// Returns the entire data
	public String getPage(){
		getData("https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&explaintext&titles=" + keyword + "&redirects=");
		return data;
	}


}
