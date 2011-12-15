/*
 * Sistemas Distribuidos - Algoritmo Chang-Roberts
 * Edgar H. Rodriguez Diaz 790543
 * <edgar.rd@gmail.com>
 */

import java.awt.*;
import java.util.LinkedList;

import javax.swing.*;

@SuppressWarnings("serial")
public class UINetworkTopology extends JPanel {

	Graphics g;
	int numOfProcesses;
	int processWidth = 30;
	int processHeight = 30;
	int panelWidth = 600;
	int panelHeight = 600;
	int r = 260;
	int x,y;
	
	int padding = 3;
	LinkedList<ProcessGraphix> processes;
	
	public UINetworkTopology(int numOfProcesses) {
		setPreferredSize(new Dimension(panelWidth, panelHeight));
		setBackground(Color.WHITE);
		this.numOfProcesses = numOfProcesses;
		processes = new LinkedList<ProcessGraphix>();
		
		x = panelWidth/2 - processWidth/2;
		y = panelHeight/2 - processHeight/2;
		
		calculateProcessesPosition();
	}
	
	private void drawRing() {
		g = getGraphics();
		g.drawOval(40, 40, r*2, r*2);
	}
	
	private void calculateProcessesPosition() {
		for (int i=0; i<this.numOfProcesses; i++) {
			int xf = x + (int) (r * Math.cos(2 * Math.PI * i / numOfProcesses));
			int yf = y + (int) (r * Math.sin(2 * Math.PI * i / numOfProcesses));
			processes.add(new ProcessGraphix(xf, yf, processWidth, processHeight, ""));
		}
	}
	
	public void createNewProcess(String label) {
		g = getGraphics();
		
		ProcessGraphix pg = processes.getFirst();
		pg.setLabel(label);
		
		g.fillOval(pg.xpos, pg.ypos, pg.width, pg.height);
		g.drawString(pg.label, pg.xpos, pg.ypos - padding);
		
		processes.removeFirst();
		this.drawRing();
	}
	
	public void paintComponent(Graphics g)  {
	    super.paintComponent(g);
	}

}
