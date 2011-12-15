/*
 * Sistemas Distribuidos - Algoritmo Chang-Roberts
 * Edgar H. Rodriguez Diaz 790543
 * <edgar.rd@gmail.com>
 */

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings("serial")
public class ProcessMember extends UnicastRemoteObject implements IProcessMember, Runnable {

	public enum ElectionState {
		NON_PARTICIPANT, PARTICIPANT
	}
	
	IProcessMember _nextProc;
	int _pid;
	ElectionState _status;
	int _elected_pid;
	boolean _startElec;
	
	public ProcessMember(int pid) throws RemoteException {
		super();
		this._pid = pid;
		this.initialize();
	}
	
	private void initialize() {
		printMsg("Initializing Proc "+this._pid);
		
		this._nextProc = null;
		this._status = ElectionState.NON_PARTICIPANT;
		_elected_pid = -1;
	}
	
	public void printMsg(String msg) {
		System.out.println("<Process "+ this._pid +"> "+ msg);
	}
	
	public void setStartElection(boolean start) {
		this._startElec = start;
	}
	
	@Override
	public int getPID() throws RemoteException {
		// TODO Auto-generated method stub
		return _pid;
	}

	@Override
	public void setNextProcess(IProcessMember proc) throws RemoteException {
		this._nextProc = proc;
		printMsg("Next Process is: "+ this._nextProc.getPID());
	}

	@Override
	public void receiveMsg(AlgoMessage aMsg) throws RemoteException {
		this.procMessage(aMsg);
	}
	
	public void sendMessage(AlgoMessage aMsg) throws RemoteException {
		if (this._nextProc != null)
			this._nextProc.receiveMsg(aMsg);
	}
	
	public void forwardMessage(AlgoMessage aMsg) throws RemoteException {
		this.sendMessage(aMsg);
		this._status = ElectionState.PARTICIPANT;
	}
	
	public void electionCompleted() {
		printMsg("Elected process: "+ _elected_pid);
	}
	
	public void procMessage(AlgoMessage aMsg) throws RemoteException {
		AlgoMessage.MessageType msgType = aMsg.getType();
		boolean recvGreater = false;
		
		if (msgType == AlgoMessage.MessageType.ELECTION) {
			
			if (_pid != aMsg.getMaxPID()) {
				if (_pid > aMsg.getMaxPID()) {
					aMsg.setMaxPID(_pid);
				} else {
					recvGreater = true;
				}
				
				if (_status == ElectionState.NON_PARTICIPANT || (_status == ElectionState.PARTICIPANT && recvGreater)) {
					forwardMessage(aMsg);
				}
			} else {
				AlgoMessage aElected = new AlgoMessage(AlgoMessage.MessageType.ELECTED, _pid, _pid);
				_status = ElectionState.NON_PARTICIPANT;
				sendMessage(aElected);				
			}
		} else if (msgType == AlgoMessage.MessageType.ELECTED) {
			_status = ElectionState.NON_PARTICIPANT;
			
			_elected_pid = aMsg.getElected();
			
			if (_elected_pid != _pid) {
				sendMessage(aMsg);
			} 
			
			electionCompleted();
		}
	}
	
	public void detectStart() throws RemoteException {
		if (this._startElec && this._nextProc != null) {
			printMsg("Detected Start");
			AlgoMessage aMsg = new AlgoMessage(AlgoMessage.MessageType.ELECTION, -1, _pid);
			forwardMessage(aMsg);
			_startElec = false;
		}
	}
	
	public void startElection() throws RemoteException {
		printMsg("Starting Election");
		AlgoMessage aMsg = new AlgoMessage(AlgoMessage.MessageType.ELECTION, -1, _pid);
		forwardMessage(aMsg);
	}
	
	public void run() {
		
		while (true);
		
	}
	
}
