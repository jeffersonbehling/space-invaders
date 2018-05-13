package game;

import java.awt.Graphics;
import java.util.LinkedList;

import javafx.scene.image.Image;


public class Alien {
	private Image image;
	private int x;
	private int y;
	private int line;
	private double xAlien;
	private int yAlien;
	private boolean moveLeft;
	
	public int getX()
	{
		return this.x;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public int getLine()
	{
		return this.line;
	}
	
	public void setLine(int line)
	{
		this.line = line;
	}
	
	public double getXAlien()
	{
		return this.xAlien;
	}
	
	public void setXAlien(double xAlien)
	{
		this.xAlien = xAlien;
	}
	
	public int getYAlien()
	{
		return this.yAlien;
	}
	
	public void setYAlien(int yAlien)
	{
		this.yAlien = yAlien;
	}
	
	public Image getImage()
	{
		return this.image;
	}
	
	public void setImage(Image image)
	{
		this.image = image;
	}
	
	public boolean getMoveLeft()
	{
		return this.moveLeft;
	}
	
	public void setMoveLeft(boolean moveLeft)
	{
		this.moveLeft = moveLeft;
	}
	
	public void createAliens(LinkedList<Alien> aliens, boolean moveLeft, int y,  int line) {
		for (int i = -375; i < 375;) {
    		Alien a = new Alien();
			a.setX(i);
			a.setY(y);;
			a.setLine(line);
			a.setMoveLeft(moveLeft);
			aliens.add(a);
			i += 75;
    	}
	}
}
