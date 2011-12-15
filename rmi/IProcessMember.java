/*
 * Sistemas Distribuidos - Algoritmo Chang-Roberts
 * Edgar H. Rodriguez Diaz 790543
 * <edgar.rd@gmail.com>
 */

import java.rmi.Remote;

public interface IProcessMember extends Remote {

	public void setNextProcess(IProcessMember proc) throws java.rmi.RemoteException;
	
	public int getPID() throws java.rmi.RemoteException;
	
	public void receiveMsg(AlgoMessage aMsg) throws java.rmi.RemoteException;
	
	public void startElection() throws java.rmi.RemoteException;
	
}
