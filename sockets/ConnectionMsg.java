/*
 * Sistemas Distribuidos - Algoritmo Chang-Roberts
 * Edgar H. Rodriguez Diaz 790543
 * <edgar.rd@gmail.com>
 */

import java.io.*;

@SuppressWarnings("serial")
public class ConnectionMsg implements Serializable {
	
	String _inetAddr;
	int _port;
	int _nextPort;
	
	public ConnectionMsg(String inetAddr, int port, int nextPort) {
		this._inetAddr = inetAddr;
		this._port = port;
		this._nextPort = nextPort;
	}
	
	public void setInetAddress(String inetAddr) {
		this._inetAddr = inetAddr;
	}
	
	public void setPort(int port) {
		this._port = port;
	}
	
	public void setNextPort(int nextPort) {
		this._nextPort = nextPort;
	}
	
	public String getInetAddress() {
		return this._inetAddr;
	}
	
	public int getPort() {
		return this._port;
	}
	
	public int getNextPort() {
		return this._nextPort;
	}
	
	public String toString() {
		return "ConnectionMsg <"+ this.getInetAddress() +":"+ this.getPort() +", "+ this.getNextPort() +">";
	}

}
