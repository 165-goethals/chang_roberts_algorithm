/*
 * Sistemas Distribuidos - Algoritmo Chang-Roberts
 * Edgar H. Rodriguez Diaz 790543
 * <edgar.rd@gmail.com>
 */

import java.rmi.*;
import java.lang.Thread;

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

		System.out.println("Running Network Topology Configurator\n");
		
		try {
			NetworkTopology oNet = new NetworkTopology(numProc);
			String registry = "localhost";
			
			if (args.length >= 2) {
				registry = args[1];
			}
			
			String regUrl = "rmi://"+ registry +"/NetworkTopology";
			
			Naming.rebind(regUrl, oNet);
			
			Thread oNetThread = new Thread(oNet);
			oNetThread.start();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
