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
		String configHost = "localhost";

		try {
			if (args.length >= 1) {
				pid = Integer.parseInt(args[0]);
				
				if (args.length == 2) {
					configHost = args[1];
				}
				
			} else {
				System.out.println("Usage: java RunProcess <pid> <?host_config>");
				System.exit(1);
			}
		}catch (NumberFormatException nfe){
			System.out.println("Usage: java RunProcess <pid> <host_config>");
			System.exit(1);
		}
		
		ProcessMember proc;

		System.out.println("Starting process "+ pid);
	
		try{
			proc = new ProcessMember(configHost, 50001, pid);

			proc.registerToNetwork();
			proc.receiveConfig();
	
			Thread pthread = new Thread(proc);
			pthread.start();

		}catch(Exception e){
			e.printStackTrace();
		}
	
	}

}
