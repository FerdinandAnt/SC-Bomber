package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class PlayerProcess
{
	public ProcessBuilder processBuilder = null;
	public Process process = null;
	public Scanner in = null;
	public PrintWriter out = null;
	
	
	public PlayerProcess(String classPath, String className) {
		try {
			processBuilder = new ProcessBuilder("java.exe", "-cp", classPath, className);
			process = processBuilder.start();
			in = new Scanner(process.getInputStream());
			out = new PrintWriter(process.getOutputStream());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasNextLine() {
		return in.hasNextLine();
	}
	
	public String getNextLine() {
		return in.nextLine();
	}
	
	public void sendLine(String line) {
		// Wait until output stream is ready
		while (out == null);
		out.println(line);
		out.flush();
	}
}
