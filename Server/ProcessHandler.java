package Server;

public class ProcessHandler
{
	public String playerName = null;
	public PlayerProcess playerProcess = null;
	public int lastTurnHandled = 0;
	
	public ProcessHandler(String playerName, PlayerProcess process) {
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
				System.out.println(">> Sending to " + playerName);
				playerProcess.sendLine("TURN " + currentTurn);
				
				// Fetch player move (starts with ">> ")
				// If a move is detected, report it to GameMachine.
				boolean isplayerMoveObtained = false;
				while (!isplayerMoveObtained) {
					String playerMove = playerProcess.getNextLine();
					if (playerMove.startsWith(">> ")) {
						String parsedPlayerMove = playerMove.substring(3);
						System.out.println(">> Receiving from " + playerName + ":");
						System.out.println(parsedPlayerMove);
						GameMachine.reportMove(playerName, parsedPlayerMove);
						lastTurnHandled = currentTurn;
						isplayerMoveObtained = true;
					}
				}
			}
			
			// Sleep for 100ms before trying another round
			sleep(100);
		}
	}
	
}
