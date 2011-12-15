/*
 * Sistemas Distribuidos - Algoritmo Chang-Roberts
 * Edgar H. Rodriguez Diaz 790543
 * <edgar.rd@gmail.com>
 */

import java.lang.Exception;

@SuppressWarnings("serial")
public class NetworkException extends Exception {
	
	NetworkException(String msg) {
		super(msg);
	}
	
	public NetworkException() {
		super();
	}
}
