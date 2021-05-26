import java.util.Random;
import java.util.Scanner;

public class Jp{

	public static void main(String[] args) {

		TicTacToe tictactoe = new TicTacToe();

	}

}

class TicTacToe{

	Scanner scan = new Scanner(System.in);
	Random rand = new Random();

	// Tic-tac-toe box
	char[] boxes = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
	// Variables
	int get;
	int turn = 1;
	int checks = 0;
	int comp;
	int play_count = 0;

	TicTacToe(){

		// Main loop
		while(true){

			show_turn();
			show_grid();

			if(turn == 1)
				getInput();

			validates();
			checks = check();

			if(checks == 1)
				break;
			if(play_count == 9){   // If play_count == 9 and doesn't match with any condition then it is a tie
				System.out.println("\nTie\n");
				break;
			}

		}

	}

	private void show_turn(){

		// Showing the turn
		if(turn == 1){
			System.out.println("X Turn");
		}else{
			System.out.println("O Turn");
		}

	}

	private void show_grid(){

		// Box printing
		System.out.println(boxes[0] + " | " + boxes[1] + " | " + boxes[2]);
		System.out.println("--+---+--");
		System.out.println(boxes[3] + " | " + boxes[4] + " | " + boxes[5]);
		System.out.println("--+---+--");
		System.out.println(boxes[6] + " | " + boxes[7] + " | " + boxes[8]);

	}

	private void getInput(){

		// Takes input
		System.out.print("Enter numbers in range (1-9): ");
		get = scan.nextInt();

		if((get > 9) || (get < 1)){
			System.out.println("\nJust enter numbers from 1 to 9\n");
			return;
		}

	}

	private void validates(){

		if(turn == 1){
			if(boxes[get-1] == ' '){
				boxes[get-1] = 'X';
				turn = 2;
			}else{
				System.out.println("\nThis box is already occupied try another\n");
			}
		}else{
			while(boxes[comp] != ' '){
				comp = rand.nextInt(9);
			}

			boxes[comp] = 'O';
			turn = 1;
		}

		play_count++;

	}

	private int check(){

		// X wins
		if(boxes[0] == 'X' && boxes[1] == 'X' && boxes[2] == 'X'){
			show_grid();
			System.out.println("\nX Won\n");
			return 1;
		}else if(boxes[3] == 'X' && boxes[4] == 'X' && boxes[5] == 'X'){
			show_grid();
			System.out.println("\nX Won\n");
			return 1;
		}else if(boxes[6] == 'X' && boxes[7] == 'X' && boxes[8] == 'X'){
			show_grid();
			System.out.println("\nX Won\n");
			return 1;
		}else if(boxes[0] == 'X' && boxes[3] == 'X' && boxes[6] == 'X'){
			show_grid();
			System.out.println("\nX Won\n");
			return 1;
		}else if(boxes[1] == 'X' && boxes[4] == 'X' && boxes[7] == 'X'){
			show_grid();
			System.out.println("\nX Won\n");
			return 1;
		}else if(boxes[2] == 'X' && boxes[5] == 'X' && boxes[8] == 'X'){
			show_grid();
			System.out.println("\nX Won\n");
			return 1;
		}else if(boxes[0] == 'X' && boxes[4] == 'X' && boxes[8] == 'X'){
			show_grid();
			System.out.println("\nX Won\n");
			return 1;
		}else if(boxes[2] == 'X' && boxes[4] == 'X' && boxes[6] == 'X'){
			show_grid();
			System.out.println("\nX Won\n");
			return 1;
		}

		// O wins
		if(boxes[0] == 'O' && boxes[1] == 'O' && boxes[2] == 'O'){
			show_grid();
			System.out.println("\nO Won\n");
			return 1;
		}else if(boxes[3] == 'O' && boxes[4] == 'O' && boxes[5] == 'O'){
			show_grid();
			System.out.println("\nO Won\n");
			return 1;
		}else if(boxes[6] == 'O' && boxes[7] == 'O' && boxes[8] == 'O'){
			show_grid();
			System.out.println("\nO Won\n");
			return 1;
		}else if(boxes[0] == 'O' && boxes[3] == 'O' && boxes[6] == 'O'){
			show_grid();
			System.out.println("\nO Won\n");
			return 1;
		}else if(boxes[1] == 'O' && boxes[4] == 'O' && boxes[7] == 'O'){
			show_grid();
			System.out.println("\nO Won\n");
			return 1;
		}else if(boxes[2] == 'O' && boxes[5] == 'O' && boxes[8] == 'O'){
			show_grid();
			System.out.println("\nO Won\n");
			return 1;
		}else if(boxes[0] == 'O' && boxes[4] == 'O' && boxes[8] == 'O'){
			show_grid();
			System.out.println("\nO Won\n");
			return 1;
		}else if(boxes[2] == 'O' && boxes[4] == 'O' && boxes[6] == 'O'){
			show_grid();
			System.out.println("\nO Won\n");
			return 1;
		}

		return 0;
	}

}
