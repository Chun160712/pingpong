package ppPackage;

import static ppPackage.ppSimParams.*;
import java.awt.Color;
import acm.graphics.*;

/**
 * The ppPaddle class simulates an instance of a ping pong paddle played by human
 * with a green rectangle on the right of the screen moving through the space with 
 * potential collision with the ppBall instance.
 * The X position of the paddle is fixed at creation, but the paddle can move is Y direction
 * depending on the movement of mouse.
 * 
 * @author chun1
 *
 */

public class ppPaddle extends Thread{
	
/*Instance variables*/
	private double PaddleX = ppPaddleXinit;
	private double PaddleY;
	private double lastX, lastY;
	private double Vx = 0;
	private double Vy;
	private ppTable myTable;
	GRect myPaddle;
	
/*Constructor*/
	public ppPaddle (double X, double Y, Color myColor, ppTable myTable) {		
		this.PaddleX = X;
		this.PaddleY = Y;
		this.myTable = myTable;
		
		this.myPaddle = new GRect ((ppPaddleXinit-ppPaddleW/2)*SCALE,(ppPaddleYinit*SCALE-ppPaddleH/2),ppPaddleW*SCALE,ppPaddleH*SCALE);
		myPaddle.setFilled(true);
		myPaddle.setFillColor(myColor);
		myTable.getDisplay().add(myPaddle);		
	}
	
		
/**
 * getVx method return the current velocity of the paddle in X direction from run() method
 * @return the velocity of the paddle in the X direction (which will be 0 here as X is fixed).
 */
	public double getVx() {
		return this.Vx;
	}
	
	
/**
 * getVy method return the current velocity of the paddle in Y direction from run() method
 * @return the velocity of the paddle in the Y direction.
 */
	public double getVy() {
		return this.Vy;
	}
	
	
/**
 * setX method changes the X position of the paddle on the screen.
 * @param X new X position of the paddle.
 */
	public void setX(double X) {
		this.PaddleX = X;
		myPaddle.setLocation(ppTable.toScrX(this.PaddleX-ppPaddleW/2), ppTable.toScrY(lastY));
	}
	
	
/**
 * SetY method changes the Y position of the paddle on the screen.
 * @param Y new Y position of the paddle.
 */
	public void setY(double Y) {
		this.PaddleY = Y;
		myPaddle.setLocation(ppTable.toScrX(lastX), ppTable.toScrY(this.PaddleY-ppPaddleH/2));//the mouse will be at the middle of the paddle
	}
	
	
/**
 * getX method return the current X position of the paddle.
 * @return the X position of the paddle.
 */
	public double getX() {		
		return this.PaddleX;
	}
	
	
/**
 * getY method return the current Y position of the paddle.
 * @return the Y position of the paddle.
 */
	public double getY() {	
		return this.PaddleY;		
	}
	
	
/**
 * getSgnVy method return the sign of current Vy.
 * @return 1 if the sign of the Vy is negative and -1 if the sign of Vy is positive. 
 * 			(e.g. The paddle is moving upwards, Vy(paddle)<0, anad the ball should go upwards as well, Vy(ball)>0 as Y corrdinate is inversed in ppBall)
 */
	public double getSgnVy() {		
		if (Vy<0) {
			return 1;//Vy of ball is the inverse
		}else {
			return -1;
		}		
	}
	
	
/**
 * contact method checks if the ball is actually in contact with the paddle when the ball is reaching the X position of paddle
 * @param Sx X coordinate of the ball
 * @param Sy Y coordinate of the ball
 * @return true if a surface at position (Sx,Sy) is deemed to be in contact with the paddle.
 */
	public boolean contact (double Sx, double Sy) {
		return (Sy>=(PaddleY-ppPaddleH/2) &&
				Sy<=(PaddleY+ppPaddleH/2));
	}
			 
	
	
	public void run() { 
		
		while (true) { 
			Vx=(PaddleX-lastX)/TICK;
			Vy=(PaddleY-lastY)/TICK; 
			lastX=PaddleX; 
			lastY=PaddleY; 
			myTable.getDisplay().pause(TICK * TIMESCALE); // Time to mS 
		}//Ends while	
		
	}//Ends run()
}//Ends class
