package Server;

public class Main
{
	public static final int NUMBER_OF_PLAYER = 2;
	
	public static void main(String[] args) {
		// Create and initiate game machine
		GameMachine machine = new GameMachine();
		String[] playerNames = {"C1", "C2"};
		String boardMap = ""
			+ "0....\n"
			+ ".###.\n"
			+ "....1\n";
		machine.initiate(NUMBER_OF_PLAYER, playerNames, boardMap);
				
		// Build player processes
		String classPath = "bin/";
		PlayerProcess[] processes = new PlayerProcess[NUMBER_OF_PLAYER];
		for (int i = 0; i < NUMBER_OF_PLAYER; i++) {
			String className = playerNames[i];
			processes[i] = new PlayerProcess(classPath, className);
		}
				
		// Handle separate player processes
		Thread[] processThreads = new Thread[NUMBER_OF_PLAYER];
		for (int i = 0; i < NUMBER_OF_PLAYER; i++) {
			int threadId = i;
			processThreads[i] = new Thread() {				
				public void run() {
					String playerName = playerNames[threadId];
					PlayerProcess playerProcess = processes[threadId];
					ProcessHandler processHandler = new ProcessHandler(playerName, playerProcess);
					processHandler.run();
				}
			};
		}
		
		// Run the game and run each threads
		Thread machineThread = new Thread() {
			public void run() {
				machine.run();
			}
		};
		machineThread.start();
		for (int i = 0; i < NUMBER_OF_PLAYER; i++) {
			Thread iterProcessThread = processThreads[i];
			iterProcessThread.start();
		}
		
		System.out.println("Done!");
	}
}
