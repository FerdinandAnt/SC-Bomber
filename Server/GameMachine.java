package Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import javax.swing.plaf.synth.SynthSeparatorUI;

public class GameMachine
{
	public static final int TURN_TIME_LIMIT_MS = 1200;
	public static final int TURN_LIMIT = 1000;
	
	public static final int POINTS_DESTROY_WALL = 10;
	public static final int POINTS_GET_POWERUP = 50;
	public static final int POINTS_KILL_PLAYER = 200;
	
	
	public static int turn = 0;
	public static int numberOfPlayer = 0;
	public static String[] playerNames = null;
	public static String[] playerMoves = null;
	public static HashMap<String,Integer> playerIndexMap = null;
	
	// Board/map contents
	public static int boardHeight = 0;
	public static int boardWidth = 0;
	public static ArrayList<ArrayList<ArrayList<String>>> board = null;
	
	// Player status
	public static boolean[] isPlayerConnected = null;
	public static boolean[] isPlayerAlive = null;
	public static int[] playerScores = null;
	public static int[] playerBombsRemaining = null;
	public static int[] playerBombsPower = null;
	public static int[] playerPositionsRow = null;
	public static int[] playerPositionsCol = null;
	
	public GameMachine() {}	
	
	/**
	 * To obtain a cell in board, given row and column index.
	 * @param row - row index (0..boardHeight)
	 * @param col - column index (0..boardWidth)
	 * @return a cell in board
	 */
	public static ArrayList<String> getBoardCell(int row, int col) {
		if (board == null) return null;
		if (board.get(row) == null) return null;
		return board.get(row).get(col);
	}
	
	/**
	 * Initiate the game machine to start a new game.
	 * @param numberOfPlayer - number of players competing
	 */
	public static void initiate(int numberOfPlayer, String[] playerNames, String boardMap) {
		// Prepare player data
		GameMachine.playerNames = playerNames.clone();
		GameMachine.numberOfPlayer = numberOfPlayer;
		GameMachine.turn = 0;
		
		playerIndexMap = new HashMap<String,Integer>();
		playerBombsRemaining = new int[numberOfPlayer];
		playerBombsPower = new int[numberOfPlayer];
		isPlayerConnected = new boolean[numberOfPlayer];
		isPlayerAlive = new boolean[numberOfPlayer];
		playerScores = new int[numberOfPlayer];
		playerPositionsRow = new int[numberOfPlayer];
		playerPositionsCol = new int[numberOfPlayer];
		for (int i = 0; i < numberOfPlayer; i++) {
			playerBombsRemaining[i] = 1;
			playerBombsPower[i] = 1;
			playerScores[i] = 0;
			isPlayerConnected[i] = false;
			playerIndexMap.put(playerNames[i], i);
		}
		
		// Initialize board
		String[] boardMapLines = boardMap.split("\n");
		boardHeight = boardMapLines.length;
		boardWidth = boardMapLines[0].length();
		board = new ArrayList<ArrayList<ArrayList<String>>>();
		
		// Fill board data and try to spawn player
		for (int row = 0; row < boardHeight; row++) {
			board.add(new ArrayList<ArrayList<String>>());
			for (int col = 0; col < boardWidth; col++) {
				board.get(row).add(new ArrayList<String>());
				ArrayList<String> iterBoardCell = getBoardCell(row, col);
				char boardMapCharacter = boardMapLines[row].charAt(col);
				if (boardMapCharacter == '.') {
					// Spawn nothing
				}
				else if (Character.isDigit(boardMapCharacter)) {
					// Spawn player at location, if the player index is valid
					int playerIndex = (boardMapCharacter - '0');
					if ((playerIndex < numberOfPlayer) && !isPlayerConnected[playerIndex]) {
						System.out.println(">> Spawned P" + (playerIndex));
						iterBoardCell.add("P" + (playerIndex));
						playerPositionsRow[playerIndex] = row;
						playerPositionsCol[playerIndex] = col;
						isPlayerConnected[playerIndex] = true;
						isPlayerAlive[playerIndex] = true;
					}					
				}
				else if (boardMapCharacter == '#') {
					// Spawn indestructible wall
					iterBoardCell.add("###");
				}
				else if (boardMapCharacter == 'X') {
					// Spawn destructible wall
					iterBoardCell.add("XXX");
				}
				else if (boardMapCharacter == 'B') {
					// spawn destructible wall with Bomb+ powerup
					iterBoardCell.add("XBX");
				}
				else if (boardMapCharacter == 'P') {
					// spawn destructible wall with Power+ powerup
					iterBoardCell.add("XPX");
				}
			}
		}
				
		// Prepare for first turn
		playerNames = new String[numberOfPlayer];
		playerMoves = new String[numberOfPlayer];
		for (int i = 0; i < numberOfPlayer; i++) {
			playerNames[i] = "1306382032";
			playerMoves[i] = null;
		}
	}
	
	
	//=======================================================================================
	// MECHANIC HELPERS
	
	/**
	 * Remove a player from this cell (e.g. because of death/movement).
	 * @param cellContents
	 * @param playerIndex
	 */
	public static void removePlayerFromCell(ArrayList<String> cellContents, int playerIndex) {
		String targetString = "P" + playerIndex;
		cellContents.remove(targetString);
	}
	
	/**
	 * Adds a player to a cell if the player is not already there
	 * (e.g. because of movement).
	 * @param cellContents
	 * @param playerIndex
	 */
	public static void addPlayerToCell(ArrayList<String> cellContents, int playerIndex) {
		String targetString = "P" + playerIndex;
		if (!cellContents.contains(targetString)) {
			cellContents.add(targetString);
		}
	}
		
	/**
	 * Reduce flare counts (and remove those that have expired).
	 * @param cellContents - ArrayList to modify
	 */
	public static void decreaseFlaresInCell(ArrayList<String> cellContents) {
		int cellContentsLength = cellContents.size();
		for (int i = 0; i < cellContentsLength; i++) {
			String cellContent = cellContents.get(i);
			// If it's "F2F", weaken to "F1F"
			// If it's "F1F", remove it from the array
			if (cellContent.startsWith("F")) {
				int flareCountdown = cellContent.charAt(1) - '0';
				flareCountdown = flareCountdown - 1;
				if (flareCountdown == 0) {
					cellContents.remove(i);
					cellContentsLength--;
					i--;
				}
				else {
					cellContents.set(i, "F" + flareCountdown + "F");
				}
			}
		}
	}
		
	//=======================================================================================
	// MECHANIC-RELATED FUNCTIONS
	
	public static void resolveFlares() {
		// If there's a flare, decrease its count.
		for (int row = 0; row < boardHeight; row++) {
			for (int col = 0; col < boardWidth; col++) {
				ArrayList<String> iterBoardCell = getBoardCell(row, col);
				decreaseFlaresInCell(iterBoardCell);
			}
		}
	}
	
	public static void movePlayers() {
		for (int i = 0; i < numberOfPlayer; i++) {
			if (isPlayerConnected[i]) {
				String move = playerMoves[i];
				if (move.startsWith("MOVE ")) {
					String direction = move.substring(5);
					int playerOldPositionRow = playerPositionsRow[i];
					int playerOldPositionCol = playerPositionsCol[i];
					int playerNewPositionRow = playerOldPositionRow;
					int playerNewPositionCol = playerOldPositionCol;
					boolean isPlayerMoved = true;
					
					// Calculate possible player's new location
					if (direction.equals("UP")) {
						playerNewPositionRow = playerOldPositionRow - 1;
					}
					else if (direction.equals("DOWN")) {
						playerNewPositionRow = playerOldPositionRow + 1;
					}
					else if (direction.equals("LEFT")) {
						playerNewPositionCol = playerOldPositionCol - 1;
					}
					else if (direction.equals("RIGHT")) {
						playerNewPositionCol = playerOldPositionCol + 1;
					}
					
					
					// If the player can move there, move there.
					// the player cannot move if there's a wall/bomb there.
					if ((playerNewPositionRow < 0)
						|| (playerNewPositionRow >= boardHeight)
						|| (playerNewPositionCol < 0)
						|| (playerNewPositionCol >= boardWidth)
					) {
						playerNewPositionRow = playerOldPositionRow;
						playerNewPositionCol = playerOldPositionCol;
						isPlayerMoved = false;
					}
					
					ArrayList<String> targetBoardCell = getBoardCell(playerNewPositionRow, playerNewPositionCol);
					int targetBoardCellLength = targetBoardCell.size();
					for (int j = 0; j < targetBoardCellLength; j++) {
						String cellContent = targetBoardCell.get(j);
						char firstCharacter = cellContent.charAt(0);
						if ((firstCharacter == '#')
							|| (firstCharacter == 'X')
							|| (firstCharacter == 'B')
						) {
							playerNewPositionRow = playerOldPositionRow;
							playerNewPositionCol = playerOldPositionCol;
							isPlayerMoved = false;
							break;
						}
					}
					
					// Move player position in the board
					if (isPlayerMoved) {
						ArrayList<String> sourceBoardCell = getBoardCell(playerOldPositionRow, playerOldPositionCol);
						removePlayerFromCell(sourceBoardCell, i);
						addPlayerToCell(targetBoardCell, i);
						playerPositionsRow[i] = playerNewPositionRow;
						playerPositionsCol[i] = playerNewPositionCol;
					}
				}
			}
		}
	}
	
	/**
	 * Update player's strength if he grabs a powerup
	 */
	public static void grabPowerups() {
		// Iterate only from players: If a player steps on a powerup,
		// all players in that cell gains the buff and the points.
		// TODO
		for (int i = 0; i < numberOfPlayer; i++) {
			if (isPlayerConnected[i]) {
				int playerPositionRow = playerPositionsRow[i];
				int playerPositionCol = playerPositionsCol[i];
			}
		}
	}
	
	public static void tickBombs() {
		// TODO
	}
			
	public static void placeBombs() {
		// TODO
	}
	
	public static void killTimeouts() {
		for (int i = 0; i < numberOfPlayer; i++) {
			if (isPlayerConnected[i] && playerMoves[i].equals("TIMEOUT")) {
				isPlayerConnected[i] = false;
			}
		}
	}
	
	/**
	 * Run the game.
	 */
	public static void run() {
		while (true) {
			System.out.println("=======================");
			System.out.println("TURN " + turn);
			
			boolean allPlayerHasMoved = false;
			long turnStartTimestamp = System.currentTimeMillis();
			while (!allPlayerHasMoved) {
				// Wait until all player has moved
				allPlayerHasMoved = true;
				for (int i = 0; i < numberOfPlayer; i++) {
					if (playerMoves[i] == null && isPlayerConnected[i]) {
						allPlayerHasMoved = false;
					}
				}
				
				// Check if turn limit expires
				// Any player that has not move will "STAY"
				long turnTimeElapsedMs = System.currentTimeMillis() - turnStartTimestamp;
				if (turnTimeElapsedMs > TURN_TIME_LIMIT_MS) {
					System.out.println("TIMEOUT!");
					for (int i = 0; i < numberOfPlayer; i++) {
						if (playerMoves[i] == null) {
							playerMoves[i] = "TIMEOUT";
						}
					}
					allPlayerHasMoved = true;
				}
			}
			
			// Process the board
			System.out.println("Processing board!");
			for (int i = 0; i < numberOfPlayer; i++) {
				System.out.println("MOVE " + playerNames[i] + ": " + playerMoves[i]);
			}
			resolveFlares();
			movePlayers();
			grabPowerups();
			tickBombs();
			placeBombs();
			killTimeouts();
			
			// Prepare for next turn
			turn++;
			for (int i = 0; i < numberOfPlayer; i++) {
				playerMoves[i] = null;
			}			
		}
	}
	
	/**
	 * Register a player's move.
	 * @param playerName - playerName
	 * @param move - String of move made
	 * @return true if the move is successfully registered.
	 */
	public static boolean reportMove(String playerName, String move) {
		System.out.println(">> Report from " + playerName + ": " + move);
		Integer playerNamesIndex = playerIndexMap.get(playerName);
		
		// Register player's move
		if (playerNamesIndex != null) {
			playerMoves[playerNamesIndex] = move;
			return true;
		}
		else {
			return false;
		}
	}
}
