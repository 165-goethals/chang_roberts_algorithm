/*
 * Sistemas Distribuidos - Algoritmo Chang-Roberts
 * Edgar H. Rodriguez Diaz 790543
 * <edgar.rd@gmail.com>
 */

public class NetworkMain {

	public static void main(String[] args) {
		
		int numProc = 0;
		
		try {
			if (args.length >= 1) {
				numProc = Integer.parseInt(args[0]);
			} else {
				System.out.println("Usage: java NetworkMain <numOfProcesses>");
				System.exit(1);
			}
		} catch(NumberFormatException nfe) {
			System.out.println("Usage: java NetworkMain <numOfProcesses>\n<numOfProcesses> is a number.");
			System.exit(1);
		}
		
		NetworkTopology nt = new NetworkTopology(numProc);

		try {
			Thread ntThread = new Thread(nt);
			ntThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//p1.startElection(true);
	}
	
}
