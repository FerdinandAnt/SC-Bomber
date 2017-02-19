import java.util.Scanner;

public class ContohAI4
{
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);	
		
		while (true) {
			String input = "";
			
			// Read board state
			// Read until "END" is detected
			while (!input.equals("END")) {
				input = scanner.nextLine();
			}
			
			// Print a move
			System.out.println(">> STAY");
		}
	}
}
