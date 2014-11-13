import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class AsteroidsGame extends PApplet {

private Star [] Stars;
private ArrayList <Asteroid> AsteroidField;
private SpaceShip Atari = new SpaceShip();
public int asteroidCount = 7;
public boolean leftKey = false;
public boolean rightKey = false;
public boolean boostKey = false;
public boolean warpKey = false;
public boolean warpCooldown = false;
public boolean charge = false;
public boolean chargeMax = false;
public boolean charging = false;
public boolean chargeCooldown = false;
public void setup() 
{
	size(800,800);
	Stars = new Star[100];
	AsteroidField = new ArrayList <Asteroid>();
	for(int i=0; i<Stars.length; i++) {Stars[i] = new Star();}
	for(int i=0; i<asteroidCount; i++) {AsteroidField.add(new Asteroid());}
}
public void draw() 
{
	if (warpCooldown == true) {fill(0,0,0,60);}
	else 
	{
		if (charging == false) {fill(0,0,0,250);}
		else {fill(0,0,0,120);}
	}
	rect(0,0,799,799);
	if (charge == false && charging == false)
	{
		if (chargeCooldown == false)
		{
			fill(255);
			text("CHARGE READY", 10, 790);
		}
	}
	if (charge == false && charging == true)
	{
		fill(255);
		text("CHARGING", 10, 750);
		noStroke();
		fill(255,255,255,50);
		ellipse(Atari.getX(), Atari.getY(), 40, 40);
	}
	if (charging == false && charge == true)
	{
		if (chargeMax == false)
		{
			fill(255);
			text("CHARGE ACTIVE", 10, 790);
		}
	}
	if (chargeMax == true)
	{
		fill(255,255,0);
		text("CHARGE MAXIMUM", 10, 790);
	}
	if (chargeCooldown == true)
	{
		fill(255,0,0);
		text("CHARGE OVERHEAT", 10, 790);
	}
	if (warpCooldown == false)
	{
		fill(255);
		text("WARP READY", 10, 770);
	}
	else
	{
		fill(255,0,0);
		text("WARP OVERHEAT", 10, 770);
	}
	for (int i=0; i<Stars.length; i++) {Stars[i].show();}
	for (int i = 0; i < AsteroidField.size(); i++)
	{
		Asteroid asteroid = AsteroidField.get(i);
		asteroid.move();
		asteroid.show();
		if (dist((float)AsteroidField.get(i).getX(), (float)AsteroidField.get(i).getY(), Atari.getX(), Atari.getY()) < 20)
		{
			AsteroidField.remove(i);
		}
	}
	Atari.show();
	Atari.move();
	Atari.chargeboost();
	Atari.cooldown();
	if (leftKey == true) 
	{
		if (charge == false && charging == false)
		{
			Atari.rotate(-5);
		}
		else if (charge == true)
		{
			Atari.rotate(-9);
		}
		else if (charging == true)
		{
			Atari.rotate(-1);
		}
	}
	if (rightKey == true) 
	{
		if (charge == false && charging == false)
		{
			Atari.rotate(5);
		}
		else if (charge == true)
		{
			Atari.rotate(9);
		}
		else if (charging == true)
		{
			Atari.rotate(1);
		}
	}
	if (boostKey == true) 
	{
		if (charge == false)
		{
			Atari.accelerate(0.1f);
		}
		else 
		{
			//Boosting disabled while Charging
		}
	}
	if (warpKey == true) 
	{
		if (charge == false && charging == false)
		{
			Atari.hyperspace();
		}
		else 
		{
			//Hyperspace disabled while Charging
		}
	}
}
public void keyPressed()
{
	if (keyCode == UP) {boostKey = true;}
	if (keyCode == LEFT) {leftKey = true;}
	if (keyCode == RIGHT) {rightKey = true;}
	if (keyCode == DOWN) {warpKey = true;}
	if (keyCode == CONTROL) 
	{
		if (charging == false && chargeCooldown == false)
		{
			charge = true;
		}
		else
		{
			//Charging disabled while in the middle of a Charge
		}
	}
}
public void keyReleased()
{
	if (keyCode == UP) {boostKey = false;}
	if (keyCode == LEFT) {leftKey = false;}
	if (keyCode == RIGHT) {rightKey = false;}
	if (keyCode == DOWN) {warpKey = false;}
	if (keyCode == CONTROL) 
	{
		if (chargeCooldown == false)
		{
			charge = false;
			chargeCooldown = true;
			Atari.setDirectionX(0);
			Atari.setDirectionY(0);
		}
	}
}
class Star
{
	private int myPositionX;
	private int myPositionY;
	private float lineSize;
	private float lineSizeD;
	private boolean blank;
	public Star()
	{
		myPositionX = (int)(Math.random()*800);
		myPositionY = (int)(Math.random()*800);
		lineSize = (int)(Math.random()*3);
		lineSizeD = lineSize - 1;
	}
	public void show()
	{
		stroke(255);
		line(myPositionX + lineSize, myPositionY, myPositionX - lineSize, myPositionY);
		line(myPositionX, myPositionY + lineSize, myPositionX, myPositionY - lineSize);
		line(myPositionX - lineSizeD, myPositionY - lineSizeD, myPositionX + lineSizeD, myPositionY + lineSizeD);
		line(myPositionX + lineSizeD, myPositionY -lineSizeD, myPositionX -lineSizeD, myPositionY + lineSizeD);	
		if (lineSize == 3 && blank == false) {blank = true;}
		else if (lineSize == 0 && blank == true) {blank = false;}
		if (blank == false) {lineSize = lineSize + .5f;}
		else if (blank == true) {lineSize = lineSize - .5f;}
		if (lineSizeD == 2 && blank == false) {blank = true;}
		else if (lineSizeD == 0 && blank == true) {blank = false;}
		if (blank == false) {lineSizeD = lineSizeD + .5f;}
		else if (blank == true) {lineSizeD = lineSizeD - .5f;}
	}
}
class SpaceShip extends Floater  
{   
	private int cooldownCount = 0;
	private int chargeCount = 0;
	private float chargeMeter;
	SpaceShip()
	{ 
		myColor = 255;
		corners = 7;
		xCorners = new int[corners];
		yCorners = new int[corners];
		xCorners[0] = 16;
		yCorners[0] = 0;
		xCorners[1] = -8;
		yCorners[1] = -8;
		xCorners[2] = -8;
		yCorners[2] = -6;
		xCorners[3] = -10;
		yCorners[3] = -4;
		xCorners[4] = -10;
		yCorners[4] = 4;
		xCorners[5] = -8;
		yCorners[5] = 6;
		xCorners[6] = -8;
		yCorners[6] = 8;
		myCenterX = 400;
		myCenterY = 400;
		myDirectionX = 0;
		myDirectionY = 0;
		myPointDirection = 0;
		cooldownCount = 0;
	}
	public void setX(int x) {myCenterX = x;}
	public int getX() {return (int)myCenterX;} 
	public void setY(int y) {myCenterY = y;}  
	public int getY() {return (int)myCenterY;}
	public void setDirectionX(double x) {myDirectionX = x;}   
	public double getDirectionX() {return myDirectionX;} 
	public void setDirectionY(double y) {myDirectionY = y;}   
	public double getDirectionY() {return myDirectionY;}  
	public void setPointDirection(int degrees) {myPointDirection = degrees;}   
	public double getPointDirection() {return myPointDirection;}
	private void hyperspace()
	{
		if(warpCooldown == false)
		{
			myColor = myColor - 15;
			if(myColor == 0)
			{
				myColor = 255;
				myDirectionX = 0;
				myDirectionY = 0;
				myPointDirection = (int)(Math.random()*360);
				background(255);
				myCenterX = (int)(Math.random()*800);
				myCenterY = (int)(Math.random()*800);
				warpCooldown = true;
			}
		}
	}
	private void cooldown()
	{
		if(warpCooldown == true)
		{
			cooldownCount = cooldownCount + 1;
			if (cooldownCount == 200)
			{
				background(220);
				warpCooldown = false;
				cooldownCount = 0;
			}
		}
		if(chargeCooldown == true)
		{
			chargeCount = chargeCount + 1;
			if (chargeCount == 200)
			{
				chargeCooldown = false;
				chargeCount = 0;
			}
		}
	}
	private void chargeboost()
	{
		if(charge == true && chargeCooldown == false)
		{
			if (chargeMeter < 1)
			{
				chargeMeter = chargeMeter + 0.01f;
			}
			else 
			{
				chargeMax = true;
			}
		}
		if(charge == false)
		{
			chargeMax = false;
			if(chargeMeter > 0)
			{
				Atari.accelerate(0.2f);
				charging = true;
				chargeMeter = chargeMeter - 0.01f;
			}
			else
			{
				charging = false;
			}
		}
	}
}
class Asteroid extends Floater
{
	private int rotSpeed;
	Asteroid()
	{
		myColor = 254;
		corners = 8;
		xCorners = new int[corners];
		yCorners = new int[corners];
		xCorners[0] = 0;
		yCorners[0] = (int)(Math.random()*41)-42;
		xCorners[1] = (int)(Math.random()*41)+1;
		yCorners[1] = (int)(Math.random()*41)-42;
		xCorners[2] = (int)(Math.random()*41)+1;
		yCorners[2] = 0;
		xCorners[3] = (int)(Math.random()*41)+1;
		yCorners[3] = (int)(Math.random()*41)+1;
		xCorners[4] = 0;
		yCorners[4] = (int)(Math.random()*41)+1;
		xCorners[5] = (int)(Math.random()*41)-42;
		yCorners[5] = (int)(Math.random()*41)+1;
		xCorners[6] = (int)(Math.random()*41)-42;
		yCorners[6] = 0;
		xCorners[7] = (int)(Math.random()*41)-42;
		yCorners[7] = (int)(Math.random()*41)-42;
		myCenterX = (int)(Math.random()*800);
		myCenterY = (int)(Math.random()*800);
		myDirectionX = (int)(Math.random()*7)-4;
		myDirectionY = (int)(Math.random()*7)-4;
		myPointDirection = (int)(Math.random()*360);
		rotSpeed = (int)(Math.random()*11)-6;
	}
	public void setX(int x) {myCenterX = x;}
	public int getX() {return (int)myCenterX;} 
	public void setY(int y) {myCenterY = y;}  
	public int getY() {return (int)myCenterY;}
	public void setDirectionX(double x) {myDirectionX = x;}   
	public double getDirectionX() {return myDirectionX;} 
	public void setDirectionY(double y) {myDirectionY = y;}   
	public double getDirectionY() {return myDirectionY;}  
	public void setPointDirection(int degrees) {myPointDirection = degrees;}   
	public double getPointDirection() {return myPointDirection;}
	public void move()
	{
			rotate(rotSpeed);
			super.move();
	}
}
abstract class Floater //Do NOT modify the Floater class! Make changes in the SpaceShip class 
{   
	protected int corners;  //the number of corners, a triangular floater has 3   
	protected int[] xCorners;   
	protected int[] yCorners;   
	protected int myColor;   
	protected double myCenterX, myCenterY; //holds center coordinates   
	protected double myDirectionX, myDirectionY; //holds x and y coordinates of the vector for direction of travel   
	protected double myPointDirection; //holds current direction the ship is pointing in degrees    
	abstract public void setX(int x);  
	abstract public int getX();   
	abstract public void setY(int y);   
	abstract public int getY();   
	abstract public void setDirectionX(double x);   
	abstract public double getDirectionX();   
	abstract public void setDirectionY(double y);   
	abstract public double getDirectionY();   
	abstract public void setPointDirection(int degrees);   
	abstract public double getPointDirection(); 
	//Accelerates the floater in the direction it is pointing (myPointDirection)   
	public void accelerate (double dAmount)   
	{          
		//convert the current direction the floater is pointing to radians    
		double dRadians =myPointDirection*(Math.PI/180);     
		//change coordinates of direction of travel    
		myDirectionX += ((dAmount) * Math.cos(dRadians));    
		myDirectionY += ((dAmount) * Math.sin(dRadians));       
	}   
	public void rotate (int nDegreesOfRotation)   
	{     
		//rotates the floater by a given number of degrees    
		myPointDirection+=nDegreesOfRotation;   
	}   
	public void move()   //move the floater in the current direction of travel
	{      
		//change the x and y coordinates by myDirectionX and myDirectionY       
		myCenterX += myDirectionX;    
		myCenterY += myDirectionY;     

		//wrap around screen    
		if(myCenterX >width)
		{     
			myCenterX = 0;    
		}    
		else if (myCenterX<0)
		{     
			myCenterX = width;    
		}    
		if(myCenterY >height)
		{    
			myCenterY = 0;    
		}   
		else if (myCenterY < 0)
		{     
			myCenterY = height;    
		}   
	}	   
	public void show ()  //Draws the floater at the current position  
	{             
		fill(myColor);   
		stroke(myColor);    
		//convert degrees to radians for sin and cos         
		double dRadians = myPointDirection*(Math.PI/180);                 
		int xRotatedTranslated, yRotatedTranslated;    
		beginShape();         
		for(int nI = 0; nI < corners; nI++)    
		{     
  			//rotate and translate the coordinates of the floater using current direction 
 			 xRotatedTranslated = (int)((xCorners[nI]* Math.cos(dRadians)) - (yCorners[nI] * Math.sin(dRadians))+myCenterX);     
  			yRotatedTranslated = (int)((xCorners[nI]* Math.sin(dRadians)) + (yCorners[nI] * Math.cos(dRadians))+myCenterY);      
  			vertex(xRotatedTranslated,yRotatedTranslated);    
		}   
	endShape(CLOSE);  
	}   
} 

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "AsteroidsGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
