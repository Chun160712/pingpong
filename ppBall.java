package ppPackage;

import static ppPackage.ppSimParams.*;
import java.awt.Color;
import acm.graphics.*;

/**
 * The ppBall class simulates an instance of a ping pong ball moving through space
 * with potential collisions with the ground and two paddles. 
 * Most of the code is taken from the Bounce class of Assignment 1 with some modification.
 * 
 * @author chun1
 * 
 */

public class ppBall extends Thread{
	
/*Instance variables*/
	private double Xinit; // Initial position of ball - X
	private double Yinit; // Initial position of ball - Y
	private double Vo; // Initial velocity (Magnitude)
	private double theta; // Initial direction
	private double loss; // Energy loss on collision
	private Color color; // Color of ball
	private ppTable table; // Instance of ppTable class (ping-pong table)
	private ppPaddle myPaddle;//Instance of ppPaddle class (paddle)	
	private ppPaddleAgent theAgent;//Instance of the ppPaddleAgent class
	GOval myBall; // Graphics object representing ball
	private boolean energyEnough;
	private double ballY;
	private double ballX;
	private double Yo;
	private double Xo;
	private boolean traceOn;
	
	
/**
* The constructor for the ppBall class copies parameters to instance variables, 
* creates an instance of a GOval to represent the ping-pong ball, and adds it to the display.
* @param Xinit - starting position of the ball X (meters)
* @param Yinit - starting position of the ball Y (meters)
* @param Vo - initial velocity (meters/second)
* @param theta - initial angle to the horizontal (degrees)
* @param color - ball color (Color)
* @param loss - loss on collision ([0,1])
* @param table - a reference to the ppTable class used to manage the display
* @param traceOn - enables trace points to be displayed when true
*/
	public ppBall (double Xinit, double Yinit, double Vo, double theta, 
			Color color, double loss, ppTable table, boolean traceOn) {
		this.Xinit=Xinit;
		this.Yinit=Yinit;
		this.Vo=Vo;
		this.theta=theta;
		this.loss=loss;
		this.table=table;
		this.color=color;
		this.traceOn=traceOn;
		
		myBall = new GOval (ppTable.toScrX(Xinit-bSize),ppTable.toScrY(Yinit-bSize),ppTable.toScrX(2*bSize),ppTable.toScrY(2*bSize));
		myBall.setFilled(true);
		myBall.setFillColor(color);
		table.getDisplay().add(myBall);
	}
	
	
/**
 * Sets the value of the reference to the Player paddle.
 */
	public void setPaddle (ppPaddle myPaddle) {
		this.myPaddle = myPaddle;
	}
	
	
/**
 * Sets the value of the reference to the Agent paddle.
 */
	public void setAgent (ppPaddleAgent theAgent) {
		this.theAgent = theAgent;
	}
	
	
/**
 * A predicate that is true if a ppBall simulation is running.
 * @return true if a ppBall simulation is running.
 */
	public boolean ballInPlay() {
		return energyEnough;
	}
	
	
/**
* Methods exported by the ppBall class
*/
	public GObject getBall() {
		return myBall;
	}	
	
	
/**
 * getY method return the Y position of the ball (used in ppPaddleAgent class for the agent paddle to follow up)
 * @return current Y position of the ball
 */
	public double getY() {
		return this.ballY+Yo;
	}
	
	
	
	public void run() {
		
	/*Initialize simulation parameters*/
		energyEnough = true;
			
		double t=0; // time (reset at each interval)
				 
		double KEx=ETHR,KEy=ETHR; // Kinetic energy in X and Y directions
		double PE=0;

		double Vt = bMass*g/(4*Pi*bSize*bSize*k); // Terminal velocity
		double Vox=Vo*Math.cos(theta*Pi/180); // Initial velocity components in X direction
		double Voy=Vo*Math.sin(theta*Pi/180);// Initial velocity components in Y direction

		double Vx; 
		double Vy;		
		Xo=Xinit;
		Yo=Yinit; 
		ballX=0;
		ballY=0;				
				
	/*While loop*/
		while(energyEnough) {
			
			ballX = Vox*Vt/g*(1-Math.exp(-g*t/Vt)); // Update parameters
			ballY = Vt/g*(Voy+Vt)*(1-Math.exp(-g*t/Vt))-Vt*t;
			Vx = Vox*Math.exp(-g*t/Vt);
			Vy = (Voy+Vt)*Math.exp(-g*t/Vt)-Vt;
				
		/*Colliding with the ground*/
			if (Vy<0 && Yo+ballY<=bSize){
				
				KEx = 0.5*bMass*Vx*Vx*(1-loss);
				KEy = 0.5*bMass*Vy*Vy*(1-loss);
				PE=0;			
				if ((KEx+KEy+PE)<ETHR) break;
					
				Vox=Math.sqrt(2*KEx/bMass);
				Voy=Math.sqrt(2*KEy/bMass);							
				if (Vx<0) {
					Vox=-Vox;
				}
							
				t=0; // time is reset at every collision
				Xo+=ballX; // need to accumulate distance between collisions
				Yo=bSize; // the absolute position of the ball on the ground
				ballX=0; // (X,Y) is the instantaneous position along an arc,
				ballY=0; // Absolute position is (Xo+X,Yo+Y)						
			}
			
			
		/*Out or bound check*/
			if (ballY+Yo>YMAX-bSize) {
				energyEnough=false; // Break the loop if the ball goes out of the "ceiling"
				if (Vx<0) ppSimPaddleAgent.agentScore(); // If the ball was moving to the left before stopping, 
														 // meaning that human hit the ball out of bound, so agent scores
				else ppSimPaddleAgent.humanScore(); // If the ball was moving to the right before stopping, 
													// meaning that agent hit the ball out of bound, so human scores
			}
					
						
		/*Colliding with the paddle*/
			if(Vx>0 && (Xo+ballX)>=myPaddle.getX()-ppPaddleW/2-bSize) {
				if (myPaddle.contact(Xo+ballX,YMAX-(Yo+ballY))) {
					
					KEx = 0.5*bMass*Vx*Vx*(1-loss);
					KEy = 0.5*bMass*Vy*Vy*(1-loss);
					KEx = Math.min(KEx, HighestEnergy);
					KEy = Math.min(KEy, HighestEnergy);
					PE=bMass*g*(ballY+Yo);
								
					Vox = -Math.sqrt(2*KEx/bMass)*ppPaddleXgain; // Scale X component of velocity
					Voy = Math.sqrt(2*KEy/bMass)*ppPaddleYgain*myPaddle.getSgnVy(); // Scale Y + same direction as paddle
					// Voy=Voy*ppPaddleYgain*myPaddle.getSgnVy(); 
					// Voy=Voy*ppPaddleYgain*myPaddle.getVy();
								
					t=0; // Time is reset at every collision
					Xo=myPaddle.getX()-bSize-ppPaddleW/2;
					Yo+=ballY;
					ballX=0; // (X,Y) is the instantaneous position along an arc,
					ballY=0; // Absolute position is (Xo+X,Yo+Y)
					
				}else {
					energyEnough=false; // Break the loop if paddle is no tin contact with the ball
					ppSimPaddleAgent.agentScore(); // Human loses and agent scores
				}
			}
						
						
		/*Colliding with the Agent paddle*/
			if(Vx<0 && (Xo+ballX)<=theAgent.getX()+ppPaddleW/2+bSize) {
				if (theAgent.contact(Xo+ballX,YMAX-(Yo+ballY))) {
							
					KEx = 0.5*bMass*Vx*Vx*(1-loss);
					KEy = 0.5*bMass*Vy*Vy*(1-loss);
					KEx = Math.min(KEx, HighestEnergy);
					KEy = Math.min(KEy, HighestEnergy);
					PE=bMass*g*(ballY+Yo);
								
					Vox=Math.sqrt(2*KEx/bMass)*ppAgentXgain;
					Voy=Math.sqrt(2*KEy/bMass)*ppAgentYgain*myPaddle.getSgnVy();
					// Voy=Voy*ppPaddleYgain*myPaddle.getSgnVy(); 
					// Voy=Voy*ppPaddleYgain*myPaddle.getVy();
											
					t=0;// time is reset at every collision
					Xo=theAgent.getX()+ppPaddleW/2+bSize;
					Yo+=ballY;
					ballX=0; // (X,Y) is the instantaneous position along an arc,
					ballY=0; // Absolute position is (Xo+X,Yo+Y)
					
				}else {
					energyEnough = false; // Break the loop if paddle is no tin contact with the ball
					ppSimPaddleAgent.humanScore(); // Agent loses and human scores

				}
			}
				
				 
		/*Update and display*/
			int XBall = (int) (ppTable.toScrX(Xo+ballX-bSize));
			int YBall = (int) (scrHEIGHT-ppTable.toScrY(Yo+ballY+bSize));
			myBall.setLocation(XBall,YBall);//The X & Y refer to the upper left corner of the ball in this program
				
			if (traceOn) {
				GOval track = new GOval(ppTable.toScrX(Xo+ballX),scrHEIGHT-ppTable.toScrY(Yo+ballY),PD,PD);//To make the track display at the center of the ball
				track.setFilled(true);
				track.setFillColor(Color.BLACK);
				table.getDisplay().add(track);				
			}

					
			if (true) {
				System.out.printf("t: %.2f X: %.2f Y: %.2f Vx: %.2f Vy:%.2f\n",t,Xo+ballX,YMAX-(ballY+Yo),Vx,Vy);
			}

			t+=TICK;//Time update								
			table.getDisplay().pause(TICK*ppSimPaddleAgent.getTimescaleOfBall());
					
		}//Ends while
	}//Ends run()

}//Ends class
