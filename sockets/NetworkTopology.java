/*
 * Sistemas Distribuidos - Algoritmo Chang-Roberts
 * Edgar H. Rodriguez Diaz 790543
 * <edgar.rd@gmail.com>
 */

import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.Runnable;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkTopology implements Runnable {
	
	ArrayList<Socket> _processesRing;
	ServerSocket _server;
	int _maxNumProc;
	
	int _init_ports = 60000;
	int _init_ports_ui = 60000;
	
	UINetworkTopologyWin _oUI;
	
	final int SERV_PORT = 50001;
	
	public NetworkTopology(int maxNumProc) {
		this._processesRing = new ArrayList<Socket>();
		this._maxNumProc = maxNumProc;
		
		this._oUI = new UINetworkTopologyWin("Network Topology Configuration", this._maxNumProc);
		this._oUI.setVisible(true);
	}

	public synchronized void configClients() {
		while (true) {
			try {
				
				if (_processesRing.size() != _maxNumProc) {
					Socket sockProc = _server.accept();					
					sockProc.setKeepAlive(true);
					
					// ---- Add process to display
					this._oUI.createNewProcess(sockProc.getInetAddress().getHostName()+":"+ this._init_ports_ui);
					this._init_ports_ui++;
					// ----
					
					_processesRing.add(sockProc);
				} else {
					System.out.println("All Processes registered! "+ this._processesRing.size());
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		
		synchronized (_processesRing) {
			// Communicate every process its next neighbor
			for (int i=0; i<_processesRing.size(); i++) {
				Socket thisSocket;
				Socket rightSocket;
				
				if (i == _processesRing.size() - 1) {
					thisSocket = _processesRing.get(i);
					rightSocket = _processesRing.get(0);
				} else {
					thisSocket = _processesRing.get(i);
					rightSocket = _processesRing.get(i+1);
				}
				
				String inetAddr = rightSocket.getInetAddress().getHostAddress();
				int port;
				int nextPort;
				
				if (i == _processesRing.size() - 1) {
					port = _init_ports;
					nextPort = _init_ports - i;
				} else {
					port = _init_ports;
					nextPort = ++_init_ports;
				}
				
				ConnectionMsg conInfo = new ConnectionMsg(inetAddr, 
						port, nextPort);
				//System.out.println(i +":"+ conInfo.toString());
				
				try {
					ObjectOutputStream oOut = new ObjectOutputStream(thisSocket.getOutputStream());
					oOut.writeObject(conInfo);
					oOut.flush();
					oOut.reset();
					oOut.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}
		
	}
	
	@Override
	public void run() {
		
		// Start the server
		try {
			this._server = new ServerSocket(SERV_PORT);
			System.out.println("Server running on port: "+ SERV_PORT +"\nAccepting terminals...");
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
		
		this.configClients();
		System.out.println("Network Topology created as a Logical Ring!");
	}
	
}
