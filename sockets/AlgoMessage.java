/*
 * Sistemas Distribuidos - Algoritmo Chang-Roberts
 * Edgar H. Rodriguez Diaz 790543
 * <edgar.rd@gmail.com>
 */

import java.io.Serializable;

@SuppressWarnings("serial")
public class AlgoMessage implements Serializable {

	private MessageType _type;
	private int _elected;
	private int _pid;
	
	public enum MessageType {
			ELECTION, ELECTED
	}
	
	public AlgoMessage(MessageType type, int elected, int myPid) {
		this._type = type;
		this._elected = elected;
		this._pid = myPid;
	}
	
	public AlgoMessage() { }
	
	public MessageType getType() {
		return this._type;
	}
	
	public void setType(MessageType type) {
		this._type = type;
	}
	
	public int getElected() {
		return this._elected;
	}
	
	public void setElected(int elected) {
		this._elected = elected;
	}
	
	public int getMaxPID() {
		return this._pid;
	}
	
	public void setMaxPID(int pid) {
		this._pid = pid;
	}
	
}
