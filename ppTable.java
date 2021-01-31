package ppPackage;

import static ppPackage.ppSimParams.*;
import java.awt.Color;
import acm.graphics.*;

/**
 * The ppTable class is responsible for creating the initial display and 
 * providing utility methods for conversion between world coordinates and 
 * display coordinates.
 * 
 * @author chun1
 *
 */

public class ppTable {
	
/*Instance variable*/
	private ppSimPaddleAgent dispRef;
		
/*Constructor*/
	public ppTable(ppSimPaddleAgent dispRef) {
		
		this.dispRef=dispRef;
				
		GLine GROUND = new GLine (0,scrHEIGHT,scrWIDTH+OFFSET,scrHEIGHT);
		dispRef.add(GROUND);			
	}
		
		
/**
 * toScrX method converts world X to screen X
 * @param X world X
 * @return screen X
 */
	public static double toScrX (double X) {
		return X*SCALE;
	}
		
/**
 * toScrY method converts world Y to screen Y
 * @param Y world Y
 * @return screen Y
 */
	public static double toScrY (double Y) {
		return Y*SCALE;
	}
		
/**	 
 * ScrtoX method convert screen X to world X
 * @param ScrX screen X
 * @return world X
 */
	public double ScrtoX (double ScrX) {
		return ScrX/SCALE;
	}
		
/**
 * ScrtoY method convert screen Y to world Y
 * @param ScrY screen Y
 * @return world Y
 */
	public double ScrtoY (double ScrY) {
		return ScrY/SCALE;
	}
		
	public ppSimPaddleAgent getDisplay() {
		return dispRef;	
	}	
		
	
/**
 * This method erases the current display by removing
 * all objects and then regenerating the display;
 * called whenever a new serve is requested.
 */
	public void newScreen() {
		dispRef.removeAll();
		GLine GROUND = new GLine (0,scrHEIGHT,scrWIDTH+OFFSET,scrHEIGHT);
		dispRef.add(GROUND);	
	}

}
