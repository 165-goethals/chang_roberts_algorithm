/*
 * Sistemas Distribuidos - Algoritmo Chang-Roberts
 * Edgar H. Rodriguez Diaz 790543
 * <edgar.rd@gmail.com>
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class UINetworkTopologyWin extends JFrame implements ActionListener{

	UINetworkTopology uiTopology;
	JPanel btnsPanel;
	JButton btnExit;
	
	public UINetworkTopologyWin(String title, int numOfProcesses) {
		super(title);
		setSize(new Dimension(800, 500));
		initialize(numOfProcesses);
	}
	
	public void initialize(int numOfProcesses) {
		uiTopology = new UINetworkTopology(numOfProcesses);
		
		btnsPanel = new JPanel();
		
		btnExit = new JButton("Salir");
		btnExit.addActionListener(this);
		
		btnsPanel.add(btnExit);
		
		this.setLayout(new BorderLayout());
		this.add(uiTopology, BorderLayout.NORTH);
		this.add(btnsPanel, BorderLayout.SOUTH);
		
		this.pack();
	}
	
	public void createNewProcess(String label) {
		uiTopology.createNewProcess(label);
	}
	
	public static UINetworkTopologyWin createWindow(String title, int numOfProcesses) {
		return new UINetworkTopologyWin(title, numOfProcesses);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.exit(NORMAL);
	}
	
}
