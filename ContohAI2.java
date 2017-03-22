import java.util.Scanner;
import java.util.*;

public class ContohAI2
{
	public static ArrayList<Tembok> tembok = new ArrayList<Tembok>();
	public static Player2 player = new Player2("1","1","0",1);
	public static String nickname = "ContohAI2";
	public static String [][] board = new String[2][2];
	public static String status = "AMAN";
	public static Item sementara = new Item("0","0","TEMBOK");
	public static ArrayList<Item> itemArr = new ArrayList<Item>();
	public static String move= "";
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);	
		
		while (true) {
			itemArr = new ArrayList<Item>();
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
                                            		for(int j = 0 ; j < col ; j++){
                                            			input = inputarray[j];
                                            			if(input.equals("")){
                                            				board[i][j] = ".";
                                            			}
                                            			else if(input.substring(0,1).equals("#") || input.substring(0,1).equals("X")){
                                            				if(input.substring(0,1).equals("X")){
                                            					itemArr.add(new Item(""+i , ""+j, "TEMBOK"));
                                            				}
                                            				board[i][j] = input.substring(0,1);
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
	                                            						itemArr.add(new Item(""+i,""+j, "BOM"));
	                                            					}else if (awalInput.equals("F")){
	                                            						String timeFlare = input.substring(1);
	                                            						board[i][j] = "f";
	                                            					}else if(awalInput.equals("+")){
	                                            						String powerUP = input.substring(1);
	                                            						board[i][j] = "p";
	                                            					}else {
	                                            						if(input.equals(player.nomorPlayer)){
	                                            							player.x = ""+i;
	                                            							player.y = ""+j;
	                                            						}
	                                            						board[i][j] = input;
	                                            					}
	                                            				}
                                            				}
                                            			}
                                            		}
                                            	}
                                            }
			}
			sementara = nearestItem(itemArr);

			if(sementara.getJenis().equals("TEMBOK") && player.jmlBom > 0){
				moveToTembok(sementara);
			}else{
				kaburFromBom(sementara);
			}
		}
	}
	public static void moveToTembok(Item tembok){
		// DLS deep 1 LOL + MD
		int tmpA = 9000;
		int tmpKn = 9000;
		int tmpKr = 9000;
		int tmpB = 9000;
		int xP = Integer.parseInt(player.x);
		int yP = Integer.parseInt(player.y);
		int xT = Integer.parseInt(tembok.x);
		int yT = Integer.parseInt(tembok.y);
		System.out.println(status);
		if(validMove(xP-1,yP)){
			tmpA = Math.abs(xP-1-xT) + Math.abs(yP-yT);
			if(move.equals("DOWN")) tmpA +=2;
		}
		if(validMove(xP+1,yP)){
			tmpB = Math.abs(xP+1-xT) + Math.abs(yP-yT);
			if(move.equals("UP")) tmpB +=2;
		}
		if(validMove(xP,yP+1)){
			tmpKn = Math.abs(xP-xT) + Math.abs(yP-yT+1);
			if(move.equals("LEFT")) tmpKn +=2;
		}
		if(validMove(xP,yP-1)){
			tmpKr = Math.abs(xP-xT) + Math.abs(yP-yT-1);
			if(move.equals("RIGHT")) tmpKr +=2;
		}
		if(status.equals("PASANG")){
			move = "DROP";
			System.out.println(">> DROP BOMB");
			player.jmlBom -= 1;
			return;
		}
		int hasil = Math.min(tmpA,Math.min(tmpB,Math.min(tmpKn,tmpKr)));
		if(hasil == 0){
			move = "STAY";
			System.out.println(">> STAY");
			return;
		}
		if(tmpA == hasil){
			move = "UP";
			System.out.println(">> MOVE UP");
			return;
		}
		if(tmpKr == hasil){
			move = "LEFT";
			System.out.println(">> MOVE LEFT");
			return;
		}
		if(tmpB == hasil){
			move = "DOWN";
			System.out.println(">> MOVE DOWN");
			return;
		}
		if(tmpKn == hasil){
			move = "RIGHT";
			System.out.println(">> MOVE RIGHT");
			return;
		}

	}
	public static void kaburFromBom(Item tembok){
		// DLS deep 1 LOL + MD
		int tmpA = -1;
		int tmpKn = -1;
		int tmpKr = -1;
		int tmpB = -1;
		int xP = Integer.parseInt(player.x);
		int yP = Integer.parseInt(player.y);
		int xT = Integer.parseInt(tembok.getX());
		int yT = Integer.parseInt(tembok.getY());
		if(validMove(xP-1,yP)){
			tmpA = Math.abs(xP-1-xT) + Math.abs(yP-yT);
			//if(move.equals("DOWN")) tmpA -=2;
		}
		if(validMove(xP+1,yP)){
			tmpB = Math.abs(xP+1-xT) + Math.abs(yP-yT);
			//if(move.equals("UP")) tmpB -=2;
		}
		if(validMove(xP,yP+1)){
			tmpKn = Math.abs(xP-xT) + Math.abs(yP-yT+1);
			//if(move.equals("LEFT")) tmpKn -=2;
		}
		if(validMove(xP,yP-1)){
			tmpKr = Math.abs(xP-xT) + Math.abs(yP-yT-1);
			//if(move.equals("RIGHT")) tmpKr -=2;
		}
		int hasil = Math.max(tmpA,Math.max(tmpB,Math.max(tmpKn,tmpKr)));
		if(hasil <= 1 && xP != xT && yP != yT){
			move = "STAY";
			System.out.println(">> STAY");
			return;
		}
		if(tmpA == hasil){
			move = "UP";
			System.out.println(">> MOVE UP");
			return;
		}
		if(tmpKr == hasil){
			move = "LEFT";
			System.out.println(">> MOVE LEFT");
			return;
		}
		if(tmpB == hasil){
			move = "DOWN";
			System.out.println(">> MOVE DOWN");
			return;
		}
		if(tmpKn == hasil){
			move = "RIGHT";
			System.out.println(">> MOVE RIGHT");
			return;
		}

	}
	public static Item nearestItem(ArrayList<Item> tembok){
		if(tembok.size() < 1){
			return null;
		}
		Item tmp = tembok.get(0);
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
		} else if(board[x][y].equals("#") || board[x][y].equals("f") || board[x][y].equals("b")){
			return false;
		}else if(board[x][y].equals("X")){
			status = "PASANG";
			return false;
		}
		return true;
	}

}
class Player2{
	public static String x;
	public static String y;
	public static String nomorPlayer;
	public static int jmlBom;

	public Player2(String x, String y, String nomorPlayer, int jmlBom){
		this.x = x;
		this.y = y;
		this.nomorPlayer = nomorPlayer;
		this.jmlBom = jmlBom;
	}
}
class Item{
	public String x;
	public String y;
	public String jenis;

	public Item(String x, String y, String jenis){
		this.x = x;
		this.y = y;
		this.jenis = jenis;
	}
	public String getX(){
		return this.x;
	}
	public String getY(){
		return this.y;
	}
	public String getJenis(){
		return this.jenis;
	}
}