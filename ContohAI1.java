import java.util.Scanner;
import java.util.*;

public class ContohAI1
{
	public static ArrayList<Tembok> tembok = new ArrayList<Tembok>();
	public static Player player = new Player("1","1","0");
	public static String nickname = "ContohAI1";
	public static String [][] board = new String[2][2];
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);	
		
		while (true) {
			tembok = new ArrayList<Tembok>();
			String input = "";
			int turn = 0;
                                 int jmlPemain = 0;
                                 
			// Read board state
			// Read until "END" is detected
			while (!input.equals("END")) {
				input = scanner.nextLine();
                                            String [] inputarray =input.split(" ");
                                            if (inputarray[0].equals("TURN")){
                                            	turn = Integer.parseInt(inputarray[1]);
                                            }
                                            else if(inputarray[0].equals("PLAYER")){
                                            	jmlPemain = Integer.parseInt(inputarray[1]);
                                            	for(int i = 0 ; i < jmlPemain ; i++){
                                            		input = scanner.nextLine();
                                            		String [] inputarrayC = input.split(" ");
                                            		if(inputarrayC[1].equals(nickname)){
                                            			player.nomorPlayer = inputarrayC[0].substring(1);
                                            		}
                                            	}
                                            }
                                            else if(inputarray[0].equals("BOARD")){
                                            	// masukin peta ke board dari outputnya
                                            	int row = Integer.parseInt(inputarray[1]);
                                            	int col = Integer.parseInt(inputarray[2]);
                                            	board = new String[row][col];
                                            	for (int i = 0 ; i < row ; i++){
                                            		input = scanner.nextLine();
                                            		input = input.replace(" ","").replace("]","").substring(1).replace("["," ") + " ^";
                                            		inputarray = input.split(" ");
                                            		//System.out.println(input);
                                            		//System.out.println(input +" " + inputarray.length);
                                            		for(int j = 0 ; j < col ; j++){
                                            			input = inputarray[j];
                                            			if(input.equals("")){
                                            				board[i][j] = ".";
                                            			}
                                            			else if(input.substring(0,1).equals("#") || input.substring(0,1).equals("X")){
                                            				if(input.substring(0,1).equals("X")){
                                            					//Tembok baru = new Tembok(""+i , ""+j);
                                            					tembok.add(new Tembok(""+i , ""+j));
                                            				}
                                            				board[i][j] = input.substring(1,2);
                                            			}else {
                                            				String [] inputarrayB = input.split(";");
                                            				if(inputarrayB.length > 0){
	                                            				for(int k = 0 ; k < inputarrayB.length ; k++){
	                                            					input = inputarrayB[k];
	                                            					String awalInput = input.substring(0,1);
	                                            					if(awalInput.equals("B")){
	                                            						String powerBom = input.substring(1,input.length());
	                                            						String timeBom = input.substring(input.length());
	                                            						board[i][j] = "b";
	                                            					}else if (awalInput.equals("F")){
	                                            						String timeFlare = input.substring(1);
	                                            						board[i][j] = "f";
	                                            					}else if(awalInput.equals("+")){
	                                            						String powerUP = input.substring(1);
	                                            						board[i][j] = "p";
	                                            					}else {
	                                            						//Player
	                                            						if(input.equals(player.nomorPlayer)){
	                                            							player.x = ""+i;
	                                            							player.y = ""+j;
	                                            						}
	                                            						//System.out.println(i + " " + j);
	                                            						board[i][j] = input;
	                                            					}
	                                            				}
                                            				}
                                            			}
                                            			//System.out.println(input);
                                            		}
                                            	}
                                            }
			}
			/*for (int i = 0; i < board.length ; i++ ){
				for(int j = 0 ; j < board[i].length ; j++){
					System.out.print(board[i][j]);
				}
				System.out.println();
			}
			System.out.println(player.x + " " + player.y + "#" + player.nomorPlayer + " " + tembok.size());*/
			Tembok sementara = nearestTembok();
			//System.out.println(sementara.x + " " + sementara.y);
			for(int i = 0 ; i < tembok.size() ; i++){
				//System.out.println(tembok.get(i).x);
			}
			moveToTembok(sementara);
			// Print a move
			//.out.println(">> MOVE RIGHT");
		}
	}
	public static void moveToTembok(Tembok tembok){
		// DLS deep 1 LOL + MD
		int tmpA = 9000;
		int tmpKn = 9000;
		int tmpKr = 9000;
		int tmpB = 9000;
		int xP = Integer.parseInt(player.x);
		int yP = Integer.parseInt(player.y);
		int xT = Integer.parseInt(tembok.x);
		int yT = Integer.parseInt(tembok.y);
		if(validMove(xP-1,yP)){
			tmpA = Math.abs(xP-1-xT) + Math.abs(yP-yT);
		}
		if(validMove(xP+1,yP)){
			tmpB = Math.abs(xP+1-xT) + Math.abs(yP-yT);
		}
		if(validMove(xP,yP+1)){
			tmpKn = Math.abs(xP-xT) + Math.abs(yP-yT+1);
		}
		if(validMove(xP,yP-1)){
			tmpKr = Math.abs(xP-xT) + Math.abs(yP-yT-1);
		}
		int hasil = Math.min(tmpA,Math.min(tmpB,Math.min(tmpKn,tmpKr)));
		if(tmpA == hasil){
			System.out.println(">> MOVE UP");
			return;
		}
		if(tmpKr == hasil){
			System.out.println(">> MOVE LEFT");
			return;
		}
		if(tmpB == hasil){
			System.out.println(">> MOVE DOWN");
			return;
		}
		if(tmpKn == hasil){
			System.out.println(">> MOVE RIGHT");
			return;
		}

	}
	public static Tembok nearestTembok(){
		if(tembok.size() < 1){
			return null;
		}
		Tembok tmp = tembok.get(1);
		//System.out.println(tmp.x + " " + tmp.y);
		int resultTmp = Math.abs(Integer.parseInt(tmp.x) - Integer.parseInt(player.x)) + Math.abs(Integer.parseInt(tmp.y) - Integer.parseInt(player.y));
		for(int i = 1 ; i < tembok.size() ; i++){
			int resultTandingan = Math.abs(Integer.parseInt(tembok.get(i).x) - Integer.parseInt(player.x)) + Math.abs(Integer.parseInt(tembok.get(i).y) - Integer.parseInt(player.y));
			
			if(resultTandingan < resultTmp){
				tmp = tembok.get(i);
				resultTmp = resultTandingan;
			}
		}
		return tmp;
	}
	public static boolean validMove(int x, int y){
		if(x < 0 || x >= board.length || y < 0 || y >= board[0].length){
			return false;
		} else if(board[x][y].equals("#") || board[x][y].equals("f")){
			return false;
		}else if(board[x][y].equals("b")){
			return false;
		}
		return true;
	}
	//public static 

}
class Move{
	//public static 
}
class Player{
	public static String x;
	public static String y;
	public static String nomorPlayer;

	public Player(String x, String y, String nomorPlayer){
		this.x = x;
		this.y = y;
		this.nomorPlayer = nomorPlayer;
	}
}
class Tembok{
	public static String x;
	public static String y;

	public Tembok(String x, String y){
		this.x = x;
		this.y = y;
	}
	public String getX(){
		return this.x;
	}
	public String getY(){
		return this.y;
	}
}