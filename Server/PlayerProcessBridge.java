package Server;

public class PlayerProcessBridge
{
	public static final int DELAY_DURATION_MS = 0;
	
	public String playerName = null;
	public PlayerProcess playerProcess = null;
	public int lastTurnHandled = 0;
	
	public PlayerProcessBridge(String playerName, PlayerProcess process) {
		this.playerName = playerName;
		this.playerProcess = process;
		this.lastTurnHandled = -1;
	}
	
	public void sleep(int msDuration) {
		try {
			Thread.sleep(msDuration);
		}
		catch (InterruptedException e) {}			
	}
	
	/**
	 * Handle input/output from player process
	 */
	public void run() {		
		while (true) {
			int currentTurn = GameMachine.turn;
			
			if (currentTurn > lastTurnHandled) {
				// Give input (board state)
				String boardStateString = GameMachine.getBoardStateString();
				playerProcess.sendLine("boardStateString");
				playerProcess.sendLine("END");
				
				// Fetch player move (starts with ">> ")
				// If a move is detected, report it to GameMachine.
				boolean isplayerMoveObtained = false;
				while (!isplayerMoveObtained) {
					String playerMove = playerProcess.getNextLine();
					//System.out.println(playerName + ": " + playerMove);
					if (playerMove.startsWith(">> ")) {
						String parsedPlayerMove = playerMove.substring(3);
						//System.out.println(">> Receiving from " + playerName + ":");
						//System.out.println(parsedPlayerMove);
						GameMachine.reportMove(playerName, parsedPlayerMove);
						lastTurnHandled = currentTurn;
						isplayerMoveObtained = true;
					}
				}
			}
			
			// Sleep to prevent race condition (?)
			sleep(DELAY_DURATION_MS);
		}
	}
	
}
