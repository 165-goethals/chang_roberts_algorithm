/*
 * Sistemas Distribuidos - Algoritmo Chang-Roberts
 * Edgar H. Rodriguez Diaz 790543
 * <edgar.rd@gmail.com>
 */

import java.lang.Runnable;
import java.util.LinkedList;
import java.net.ConnectException;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.io.*;

public class ProcessMember implements Runnable {
	
	public enum ElectionState {
		NON_PARTICIPANT, PARTICIPANT
	}
	
	String _hostNetTopology;
	int _portNetTopology;
	int _pid;
	
	ElectionState _status;
	int _elected_pid;
	Socket _oConnNet;
	Socket _oConnNext;
	ServerSocket _oConServ;
	
	ConnectionMsg _conNextInfo;
	AlgoMessage _aMsg;
	LinkedList<Socket> _queue;
	boolean _gotMsg;
	boolean _startElec;
	
	public ProcessMember(String hostNetTopology, int portNetTopology, int pid) {
		this._hostNetTopology = hostNetTopology;
		this._portNetTopology = portNetTopology;
		this._pid = pid;
		this.initialize();
	}
	
	public void initialize() {
		printMsg("Initializing Proc "+this._pid);
		
		this._elected_pid = -1;
		this._status = ElectionState.NON_PARTICIPANT;
		this._gotMsg = false;
	}
	
	public void startElection(boolean startE) {
		_startElec = startE;
	}
	
	public void printMsg(String msg) {
		System.out.println("<Process "+ this._pid +"> "+ msg);
	}
	
	public void registerToNetwork() {
		try {
			_oConnNet = new Socket(this._hostNetTopology, this._portNetTopology);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void receiveConfig() {
		try {
			ObjectInputStream oinput = new ObjectInputStream(_oConnNet.getInputStream());
			
			_conNextInfo = (ConnectionMsg) oinput.readObject();
			oinput.close();
			
			printMsg(_conNextInfo.toString());
			
			// Start message server for this process.
			_oConServ = new ServerSocket(_conNextInfo._port);
			printMsg("Running on port: "+ _oConServ.getLocalPort());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public synchronized void electionCompleted() {
		printMsg("Elected process: "+ _elected_pid);
	}
	
	public synchronized void listenToMessages() {
		_queue = new LinkedList<Socket>();
		while (true) {
			try {
				
				Socket oMsgSocket = _oConServ.accept();
				_queue.addFirst(oMsgSocket);
				printMsg("Got Connection from: "+ oMsgSocket.getRemoteSocketAddress().toString());
				
				if (!_queue.isEmpty() && !_gotMsg) {
					Socket procMsg = _queue.getFirst();
					ObjectInputStream msgInput = new ObjectInputStream(procMsg.getInputStream());
					_aMsg = (AlgoMessage)msgInput.readObject();
					_gotMsg = true;
					procMessage(_aMsg);
					_gotMsg = false;
					_queue.removeFirst();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	public void forwardMessage(AlgoMessage aMsg) {
		printMsg("Forwarding message");
		sendMessage(aMsg);
		_status = ElectionState.PARTICIPANT;
	}
	
	public void sendMessage(AlgoMessage aMsg) {
		boolean done = false;
		do {
			try {
				printMsg("Sending Message");
				_oConnNext = new Socket(_conNextInfo.getInetAddress(), _conNextInfo._nextPort);
				printMsg("Connected to: "+ _oConnNext.getPort());
				
				ObjectOutputStream oOut = new ObjectOutputStream(_oConnNext.getOutputStream());
				oOut.writeObject(aMsg);
				oOut.flush();
				oOut.reset();
				oOut.close();
				done = true;
			} catch (ConnectException ce) {
				done = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (!done);
	}
	
	// Implementation of the algorithm
	public synchronized void procMessage(AlgoMessage msg) {
		AlgoMessage.MessageType msgType = _aMsg.getType();
		boolean recvGreater = false;
		
		if (msgType == AlgoMessage.MessageType.ELECTION) {
			
			if (_pid != _aMsg.getMaxPID()) {
				if (_pid > _aMsg.getMaxPID()) {
					_aMsg.setMaxPID(_pid);
				} else {
					recvGreater = true;
				}
				
				if (_status == ElectionState.NON_PARTICIPANT || (_status == ElectionState.PARTICIPANT && recvGreater)) {
					forwardMessage(_aMsg);
				}
			} else {
				AlgoMessage aElected = new AlgoMessage(AlgoMessage.MessageType.ELECTED, _pid, _pid);
				_status = ElectionState.NON_PARTICIPANT;
				sendMessage(aElected);				
			}
		} else if (msgType == AlgoMessage.MessageType.ELECTED) {
			_status = ElectionState.NON_PARTICIPANT;
			
			_elected_pid = _aMsg.getElected();
			
			if (_elected_pid != _pid) {
				sendMessage(_aMsg);
			} 
			
			electionCompleted();
		}
	}
	
	public void detectStart() {
		if (_startElec) {
			printMsg("Detected Start");
			AlgoMessage aMsg = new AlgoMessage(AlgoMessage.MessageType.ELECTION, -1, _pid);
			forwardMessage(aMsg);
			_startElec = false;
		}
	}
	
	public void run() {
		printMsg("Running");
		_startElec = (_elected_pid == -1);
		Runnable listenRun = new Runnable() {
			
			@Override
			public void run() {
				listenToMessages();
			}
		};
		
		Thread listenThread = new Thread(listenRun);
		listenThread.start();
		
		detectStart();
		try {
			listenThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
