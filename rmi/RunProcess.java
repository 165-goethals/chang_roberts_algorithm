/*
 * Sistemas Distribuidos - Algoritmo Chang-Roberts
 * Edgar H. Rodriguez Diaz 790543
 * <edgar.rd@gmail.com>
 */

import java.rmi.*;
import java.lang.Thread;

public class RunProcess {

	public static void main(String[] args) {
	
		int pid = 0;
		String registry = "localhost";

		try {
			if (args.length >= 1) {
				pid = Integer.parseInt(args[0]);
			
				if (args.length == 2) {
					registry = args[1];
				}
			} else {
				System.out.println("Usage: java RunProcess <pid> <?confHost>");
				System.exit(1);
			}
		}catch (NumberFormatException nfe){
			System.out.println("Usage: java RunProcess <pid> <?confHost>\n <pid> is a number");
			System.exit(1);
		}
		
		ProcessMember p1;

		System.out.println("Starting process "+ pid);
	
		try{
			p1 = new ProcessMember(pid);
			
			String registration = "rmi://"+ registry + "/NetworkTopology";

			INetworkTopology iNet = (INetworkTopology) Naming.lookup(registration);

			iNet.registerProcess(p1);

			//Thread p1Thread = new Thread(p1);
			//p1Thread.start();

		}catch(Exception e){
			e.printStackTrace();
		}
	
	}

}
