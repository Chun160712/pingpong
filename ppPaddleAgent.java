package ppPackage;

import static ppPackage.ppSimParams.*;

import java.awt.Color;
import acm.graphics.*;

/**
 * The ppPaddleAgent class simulates an instance of a ping pong paddle played by computer
 * with a blue rectangle on the left of the screen moving through the space with 
 * potential collision with the ppBall instance.
 * The X position of the paddle is fixed at creation, but the paddle can move is Y direction
 * depending on the position of the ppBall instance.
 * 
 * @author chun1
 *
 */

public class ppPaddleAgent extends ppPaddle{
	
/*Instance variables*/
	private double AgentX = ppAgentXinit;
	private double AgentY;
	private double lastX, lastY;
	private double Vx = 0;
	private double Vy;
	private ppTable myTable;
	private ppBall myBall;
	GRect theAgent;
	
/*Constructor*/
	public ppPaddleAgent(double X, double Y, Color myColor, ppTable myTable) {
		super(X,Y,myColor,myTable);//Necessary for subclass constructors
		this.AgentX = X;
		this.AgentY = Y;
		this.myTable = myTable;
		this.theAgent = super.myPaddle;
	}
	
	
/**
 * Sets the value of the myBall instance variable in ppPaddleAgent
 */
	public void attachBall (ppBall myBall) {
		this.myBall = myBall;
	}
	
	
/**
 * contact method checks if the ball is actually in contact with the agent paddle when the ball is reaching the X position of agent paddle
 * @param Sx X coordinate of the ball
 * @param Sy Y coordinate of the ball
 * @return true if a surface at position (Sx,Sy) is deemed to be in contact with the agent paddle.
 */
	public boolean contact (double Sx, double Sy) {
		return (Sy>=(AgentY-ppPaddleH/2) &&
				Sy<=(AgentY+ppPaddleH/2));
	}
	
	
	public void run() {
		while(myBall.ballInPlay()) {
			for (int i=0; i>=0; i++) {
				int n = ppSimPaddleAgent.getAgentLag();
				if (i%n == 0) { //Update the position of agent paddle every n loops
					AgentY = YMAX-myBall.getY();//The agent does not have a inverse coordinate while the ball does
					theAgent.setLocation(ppTable.toScrX(AgentX-ppPaddleW/2), ppTable.toScrY(AgentY-ppPaddleH/2));
					myTable.getDisplay().pause(TICK * TIMESCALE); // Time to mS  
				}
			}//Ends for loop
		}//Ends while loop
	}//Ends run
}//Ends class
