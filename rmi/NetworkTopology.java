/*
 * Sistemas Distribuidos - Algoritmo Chang-Roberts
 * Edgar H. Rodriguez Diaz 790543
 * <edgar.rd@gmail.com>
 */

import java.util.LinkedList;
import java.rmi.*;
import java.rmi.server.*;
import java.lang.Math;

@SuppressWarnings("serial")
public class NetworkTopology extends UnicastRemoteObject implements INetworkTopology, Runnable {

	LinkedList<IProcessMember> _vProcesses;
	int _maxNumProc;
	UINetworkTopologyWin oUI;
	
	public NetworkTopology(int maxNumProc) throws RemoteException { 
		super();
		_maxNumProc = maxNumProc;
		_vProcesses = new LinkedList<IProcessMember>();
		
		// --- Window
		oUI = UINetworkTopologyWin.createWindow("Network Topology Configuration", _maxNumProc);
		oUI.setVisible(true);
	}
	
	public synchronized boolean allProcessesRegistered() {
		return this._vProcesses.size() == this._maxNumProc;
	}

	@Override
	public void registerProcess(IProcessMember proc) throws java.rmi.RemoteException {
		synchronized (_vProcesses) {
			if (_vProcesses.size() != _maxNumProc) {
				System.out.println("Registering process with PID: "+ proc.getPID());
				_vProcesses.add(proc);
				oUI.createNewProcess("PID: "+ proc.getPID());
			}
		}
	}
	
	public void run() {
		
		// Wait while all processes connect.
		while (!this.allProcessesRegistered());
		System.out.println("All processes registered!");
		
		// Once all processes are connected, organize them in a logical ring.
		for (int i=0; i<this._vProcesses.size(); i++) {
			
			IProcessMember thisProc = _vProcesses.get(i);
			IProcessMember nextProc;

			if (i != this._vProcesses.size()-1) {
				nextProc = _vProcesses.get(i+1);
			} else {
				nextProc = _vProcesses.getFirst();
			}
			
			try {
				thisProc.setNextProcess(nextProc);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		// At this point the topology has been build.
		System.out.println("Network Topology created as a Logical Ring!");
		
		
		// Start election in a random way.
		int iStarter = Math.round((float)(3.5*Math.random()));
		
		try {
			(this._vProcesses.get(iStarter)).startElection();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		while(true);
	}
}
