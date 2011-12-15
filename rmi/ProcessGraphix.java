/*
 * Sistemas Distribuidos - Algoritmo Chang-Roberts
 * Edgar H. Rodriguez Diaz 790543
 * <edgar.rd@gmail.com>
 */

public class ProcessGraphix {

	int xpos;
	int ypos;
	int width;
	int height;
	String label;
	
	public ProcessGraphix(int x, int y, int width, int height, String label) {
		this.xpos = x;
		this.ypos = y;
		this.width = width;
		this.height = height;
		this.label = label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public int getX() {
		return this.xpos;
	}
	
	public int getY() {
		return this.ypos;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
}
