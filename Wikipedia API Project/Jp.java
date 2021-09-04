import java.util.Scanner;

public class Jp{

	public static void main(String[] args){

		Scanner scan = new Scanner(System.in);
		System.out.print("Enter Keyword To Wikipedia Search: ");
		String search = scan.nextLine();  // Scanning keyword
		Wikipedia wiki = new Wikipedia(search);
		System.out.println("Loading...");
		String data = wiki.getPage();
		String title = wiki.getTitle();
		System.out.println(title);
		System.out.println(data);

	}

}