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

//AsteroidsGame - Antonio Carmona - AP Computer Science

//Array of Background Stars
private Star [] Stars;

//ArrayLists for Asteroids, Bullets, and the ship's Trail
private ArrayList <Asteroid> AsteroidField;
private ArrayList <Bullet> Ammo;
private ArrayList <Trail> ShipTrail;

//The Player's Ship
private SpaceShip Atari = new SpaceShip();

//The starting amount of Asteroids (Placeholder)
private int asteroidCount = 7;

//Menu-related
private int startColor = 0;
private boolean menu = true;
private boolean startBlank = true;

//Booleans that check key inputs
private boolean leftKey = false;
private boolean rightKey = false;
private boolean boostKey = false;
private boolean warpKey = false;
private boolean chargeKey = false;

//Checks if Warp or Charge has been recently used
private boolean warpCooldown = false;
private boolean chargeCooldown = false;

//Checks status of Charge
private boolean chargeMax = false;
private boolean charging = false;

private boolean gameOver = false;

public void setup() 
{
	size(800,800);
	Stars = new Star[100];
	AsteroidField = new ArrayList <Asteroid>();
	Ammo = new ArrayList <Bullet>();
	ShipTrail = new ArrayList <Trail>();
	for(int i=0; i<Stars.length; i++) {Stars[i] = new Star();}
	for(int i=0; i<asteroidCount; i++) {AsteroidField.add(new Asteroid());}
}
public void draw() 
{
	if (menu == false)
	{
		if (warpCooldown == true) 
		{
			fill(0,0,0,60);
		}
		else 
		{
			if (charging == false) 
			{
				fill(0,0,0,250);
			}
			else 
			{	
				fill(0,0,0,120);
			}
		}
		rect(-1,-1,801,801);
				if (gameOver == false)
		{
			Atari.show();
			Atari.move();
			Atari.chargeboost();
			Atari.cooldown();

		if (leftKey == true) 
		{
			if (chargeKey == false && charging == false)
			{
				Atari.rotate(-5);
			}
			else if (chargeKey == true)
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
			if (chargeKey == false && charging == false)
			{
				Atari.rotate(5);
			}
			else if (chargeKey == true)
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
			if (chargeKey == false)
			{
				Atari.accelerate(0.1f);
				ShipTrail.add(new Trail(Atari));
			}
			else 
			{
				//Boosting disabled while Charging
			}
		}
		if (warpKey == true) 
		{
			if (chargeKey == false && charging == false)
			{
				Atari.hyperspace();
			}
			else 
			{
				//Hyperspace disabled while Charging
			}
		}
		}
		for (int i=0; i<Stars.length; i++) 
		{
			Stars[i].show();
		}
		if (ShipTrail.size() > 0)
		{
			for (int i = 0; i < ShipTrail.size(); i++)
			{
				Trail trail = ShipTrail.get(i);
				trail.move();
				trail.show();
				if (trail.getColor() == 0)
				{
					ShipTrail.remove(i);
				}
			}
		}
		if (Ammo.size() > 0)
		{
			for (int i = 0; i < Ammo.size(); i++)
			{
				Bullet bullet = Ammo.get(i);
				bullet.move();
				bullet.show();
				if (bullet.getX() > 799 || bullet.getX() < 1 || bullet.getY() > 799 || bullet.getY() < 1)
				{
					Ammo.remove(i);
				}
			}
		}
		for (int i = 0; i < AsteroidField.size(); i++)
		{
			Asteroid asteroid = AsteroidField.get(i);
			asteroid.move();
			asteroid.show();
			if (dist((float)AsteroidField.get(i).getX(), (float)AsteroidField.get(i).getY(), Atari.getX(), Atari.getY()) < 50)
			{
				if (charging == true)
				{
					AsteroidField.remove(i);
				}
				if (charging == false && gameOver == false)
				{
					background(255,0,0);
					gameOver = true;
				}

			}
		}
		if (gameOver == false)
		{
			fill(255);
			text("X-Position", 10, 650);
			text((int)Atari.getX(), 100, 650);
			text("Y-Position", 10, 670);
			text((int)Atari.getY(), 100, 670);
			text("X-Velocity", 10, 710);
			text((int)Atari.getDirectionX(), 100, 710);
			text("Y-Velocity", 10, 730);
			text((int)Atari.getDirectionY(), 100, 730);
		}
		if (gameOver == true)
		{
			textSize(70);
			fill(255);
			text("GAME OVER",195,400);
			textSize(40);
			text("- Press R to Restart -", 190, 500);
			textSize(12);
			fill(255, 0, 0);
			text("X-Position", 10, 650);
			text("N/A", 100, 650);
			text("Y-Position", 10, 670);
			text("N/A", 100, 670);
			text("X-Velocity", 10, 710);
			text("N/A", 100, 710);
			text("Y-Velocity", 10, 730);
			text("N/A", 100, 730);
		}
		if (chargeKey == false && charging == false)
		{
			if (chargeCooldown == false && gameOver == false)
			{
				fill(255);
				textSize(12);
				text("CHARGE READY", 10, 790);
			}
			else if (gameOver == true)
			{
				fill(255, 0, 0);
				textSize(12);
				text("CHARGE DISABLED", 10, 790);
			}
		}
		if (chargeKey == false && charging == true)
		{
			fill(255);
			textSize(12);
			text("CHARGING", 10, 750);
			noStroke();
			fill(255,255,255,50);
			ellipse(Atari.getX(), Atari.getY(), 40, 40);
		}
		if (charging == false && chargeKey == true && gameOver == false)
		{
			if (chargeMax == false)
			{
				fill(255);
				textSize(12);
				text("CHARGE ACTIVE", 10, 790);
			}
		}
		if (chargeMax == true)
		{
			fill(255,255,0);
			textSize(12);
			text("CHARGE MAXIMUM", 10, 790);
		}
		if (chargeCooldown == true && gameOver == false)
		{
			fill(255,0,0);
			textSize(12);
			text("CHARGE OVERHEAT", 10, 790);
		}
		if (warpCooldown == false && gameOver == false)
		{
			fill(255);
			textSize(12);
			text("WARP READY", 10, 770);
		}
		else if (warpCooldown == true && gameOver == false)
		{
			fill(255,0,0);
			textSize(12);
			text("WARP OVERHEAT", 10, 770);
		}
		else
		{
			fill(255,0,0);
			textSize(12);
			text("WARP DISABLED", 10, 770);
		}
	}
	else if (menu == true)
	{
		background(0);
		fill(255);
		textSize(100);
		text("ASTEROIDS",120,200);
		if(startBlank == true)
		{
			startColor = startColor + 1;
		}
		else if (startBlank == false)
		{
			startColor = startColor - 1;
		}
		if (startColor == 0)
		{
			startBlank = true;
		}
		if (startColor == 255)
		{
			startBlank = false;
		}
		fill(startColor);
		textSize(50);
		text("- Press SPACE to Start -", 110, 500);
	}
}
public void keyPressed()
{
	if (key == ' ') 
	{
		if (menu == false && gameOver == false)
		{
			Ammo.add(new Bullet(Atari));
		}
		else if (menu == true)
		{
			background(255);
			menu = false;
		}
	}
	if (keyCode == UP) {boostKey = true; }
	if (keyCode == LEFT) {leftKey = true;}
	if (keyCode == RIGHT) {rightKey = true;}
	if (keyCode == DOWN) {warpKey = true;}
	if (keyCode == CONTROL) 
	{
		if (charging == false && chargeCooldown == false && gameOver == false)
		{
			chargeKey = true;
		}
		else
		{
			//Charging disabled while in the middle of a Charge
		}
	}
	if (key == 'r' && gameOver == true)
	{
		background(255);
		Atari.setX(400);
		Atari.setY(400);
		Atari.setDirectionX(0);
		Atari.setDirectionY(0);
		Atari.setPointDirection(0);
		chargeCooldown = false;
		warpCooldown = false;
		for(int i=0; i<asteroidCount; i++) {AsteroidField.remove(0);}
		for(int i=0; i<asteroidCount; i++) {AsteroidField.add(new Asteroid());}
		menu = true;
		gameOver = false;
	}
}
public void keyReleased()
{
	//if (key == ' ') {}
	if (keyCode == UP) {boostKey = false;}
	if (keyCode == LEFT) {leftKey = false;}
	if (keyCode == RIGHT) {rightKey = false;}
	if (keyCode == DOWN) {warpKey = false;}
	if (keyCode == CONTROL) 
	{
		if (chargeCooldown == false)
		{
			chargeKey = false;
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
		if(chargeKey == true && chargeCooldown == false)
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
		if(chargeKey == false)
		{
			chargeMax = false;
			if(chargeMeter > 0)
			{
				Atari.accelerate(0.2f);
				ShipTrail.add(new Trail(Atari));
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
class Bullet extends Floater
{
	Bullet(SpaceShip theShip)
	{
		myCenterX = theShip.getX();
		myCenterY = theShip.getY();
		myPointDirection = theShip.getPointDirection();
		double dRadians = myPointDirection*(Math.PI/180);
		myDirectionX = 5*Math.cos(dRadians) + myDirectionX;
		myDirectionY = 5*Math.sin(dRadians) + myDirectionY;
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
	public void show()
	{
		fill(255);
		ellipse((float)myCenterX, (float)myCenterY, 5, 5);
	}
}
class Trail extends Floater
{
		Trail(SpaceShip theShip)
	{
		myColor = 255;
		myCenterX = theShip.getX();
		myCenterY = theShip.getY();
		myPointDirection = theShip.getPointDirection();
		double dRadians = myPointDirection*(Math.PI/180)+(Math.random()*.8f)-.4f;
		myDirectionX = -1*(5*Math.cos(dRadians) + myDirectionX);
		myDirectionY = -1*(5*Math.sin(dRadians) + myDirectionY);
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
	public int getColor() {return myColor;}
	public void show()
	{
		noStroke();
		fill(myColor);
		ellipse((float)myCenterX, (float)myCenterY, 5, 5);
		if(myColor > 0) {myColor = myColor - 17;}
	}
}
class Asteroid extends Floater
{
	private int rotSpeed;
	Asteroid()
	{
		myColor = 150;
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
