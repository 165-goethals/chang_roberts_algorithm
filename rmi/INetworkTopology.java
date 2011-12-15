/*
 * Sistemas Distribuidos - Algoritmo Chang-Roberts
 * Edgar H. Rodriguez Diaz 790543
 * <edgar.rd@gmail.com>
 */

import java.rmi.Remote;

public interface INetworkTopology extends Remote {

	public void registerProcess(IProcessMember proc) throws java.rmi.RemoteException;
	
}
