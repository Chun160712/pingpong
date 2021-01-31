package ppPackage;

import static ppPackage.ppSimParams.*;
import java.awt.Color;
import java.awt.event.*;
import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import javax.swing.*;

/**
 * The ppSimPaddle class simulates a ping pong game played by a human (the user)
 * and a computer. The paddle on the right side of the screen is played by the user
 * by moving the mouse up and down. The position of the paddle on the left side of 
 * the screen is determined by the Y position of the ball.
 * 
 * @author chun1
 *
 */


public class ppSimPaddleAgent extends GraphicsProgram{
	
/*Instance variables*/
	ppPaddle myPaddle;
	ppTable table;
	ppBall myBall;
	ppPaddleAgent theAgent;
	JButton clear, serve, quit;
	JToggleButton trace;
	JTextField agent, human;
	static JTextField agentScore, humanScore;
	static JSlider timescale, agentSpeed;
	
	
/**
* Mouse Handler - a moved event moves the paddle up and down in Y
*/
	public void mouseMoved(MouseEvent event) {
		myPaddle.setY(table.ScrtoY((double)event.getY()));
	}	
	

/**
 * Button Handler - performs actions according to the button clicked
 * If "Clear" is clicked - removes everything from the window then add a new ground line
 * If "New Serve" is clicked - start a new game with a newly-generated ball
 * If "Quit" is clicked - close the window to quit the game
 */
	public void actionPerformed (ActionEvent event) {
		String cmd = event.getActionCommand();
	/*Clear*/
		if (cmd.equals("Clear")) {
			table.newScreen();
			agentScore.setText("0");
			humanScore.setText("0");
		}
	/*New Serve*/	
		else if (cmd.equals("New Serve")) {
			if (myBall.ballInPlay()==false) {
				table.newScreen();
				myBall = newBall();
				myPaddle = new ppPaddle (ppPaddleXinit, ppPaddleYinit, ColorPaddle, table);
				theAgent = new ppPaddleAgent (ppAgentXinit, ppAgentYinit, ColorAgent,table);
				myBall.setAgent(theAgent);
				myBall.setPaddle(myPaddle);
				theAgent.attachBall(myBall);
				myBall.start();
				myPaddle.start();
				theAgent.start();
			}
		}
	/*Quit*/
		else if (cmd.equals("Quit"))  System.exit(0);			
	}

	
/**
 * newBall method encapsulates generating random parameters and creating a ppBall instance,
 * so that this process can be repeated each time a new serve is requested.
 */
	public ppBall newBall() {
		RandomGenerator rgen = RandomGenerator.getInstance();
		rgen.setSeed(RSEED);
		double iYinit = rgen.nextDouble(YinitMIN,YinitMAX);
		double iLoss = rgen.nextDouble(EMIN,EMAX);
		double iVel = rgen.nextDouble(VoMIN,VoMAX);
		double iTheta = rgen.nextDouble(ThetaMIN,ThetaMAX);
		myBall = new ppBall(XINIT,iYinit,iVel,iTheta,ColorBall,iLoss,table,trace.isSelected());
		return myBall;	
	}
	

/**
 * the agentScore method increases the current score of agent by 1
 */
	public static void agentScore() {
		agentScore.setText(String.valueOf(Integer.parseInt(agentScore.getText())+1));
		//Convert the current score to integer. then increase by 1, and then convert back to String
	}
	
	
/**
 * the humanScore method increases the current score of human by 1
 */
	public static void humanScore() {
		humanScore.setText(String.valueOf(Integer.parseInt(humanScore.getText())+1));
		//Convert the current score to integer, then increase by 1, and then convert back to String
	}
	

/**
 * getTimescaleOfBall method retrieve the current value of timescale slider 
 * which will then be used to determine how often the location of the ball is updated in ppBall
 * @return current value of timescale slider as an int
 */
	public static int getTimescaleOfBall() {
		return timescale.getValue();
	}
	
	
/**
 * getAgentLag method retrieve the current value of the agentSpeed slider 
 * which will then be used to determine how often the location of the agent paddle is updated in ppPaddleAgent
 * @return current value of the agentSpeed slider as int
 */
	public static int getAgentLag() {
		return agentSpeed.getValue();
	}
	
	
/**
 * This is the main class which nominally uses the default constructor. 
 * To get around problems with the acm package, the following code is 
 * explicitly used to make the entry point unambiguous.
 */
	public static void main(String[] args) { 
		new ppSimPaddleAgent().start(args);
	}
	
	
/**
 * The run method is the entry point of this program.
 */
	public void init() {		
		this.resize(scrWIDTH+OFFSET,scrHEIGHT+OFFSET);//Set size of display
	
	//Implementation of user interfaces
		clear = new JButton ("Clear");
		serve = new JButton ("New Serve");
		trace = new JToggleButton ("Trace",true);
		quit = new JButton ("Quit");
		agent = new JTextField ("Agent");
		human = new JTextField ("Human");
		agentScore = new JTextField ("0",5);
		humanScore = new JTextField("0",5);
		timescale = new JSlider (1000,10000,(int)TIMESCALE);
		agentSpeed = new JSlider (1000,10000000);
		add(clear,SOUTH);
		add(serve,SOUTH);
		add(trace,SOUTH);
		add(quit,SOUTH);	
		add(agent,NORTH);
		add(agentScore,NORTH);
		add(human,NORTH);
		add(humanScore,NORTH);
		add(new JLabel("+t"),SOUTH);
		add(timescale,SOUTH);
		add(new JLabel("-t"),SOUTH);
		add(new JLabel("-lag"),SOUTH);
		add(agentSpeed,SOUTH);
		add(new JLabel("+lag"),SOUTH);
		addMouseListeners();
		addActionListeners();
		
	//Create instances of other classes
		table = new ppTable(this);
		myBall = newBall();
		myPaddle = new ppPaddle (ppPaddleXinit, ppPaddleYinit, ColorPaddle, table);
		theAgent = new ppPaddleAgent (ppAgentXinit, ppAgentYinit, ColorAgent,table);
	
	//Inform ball of paddle and agents
		myBall.setAgent(theAgent);
		myBall.setPaddle(myPaddle);
		theAgent.attachBall(myBall);

	//Add ball to display and start
		//add(myBall.getBall());
		myBall.start();
		myPaddle.start();
		theAgent.start();
		
	}//Ends init
}//Ends class
