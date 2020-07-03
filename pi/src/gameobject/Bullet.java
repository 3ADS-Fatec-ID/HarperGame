package gameobject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import util.Resource;

public class Bullet {
	
	private int posX, posY, speedX;
	private boolean visible;
	
	public Bullet(int startX, int startY) {
		posX = 55;
		posY = startY;
		speedX = 7;
		visible = true;
	}
	
	public void update() {
		posX += speedX;
		if(posX > 800) {
			visible = false;
		}
	}
	
	public int getX() {
		return posX;
	}
	
	public int getY() {
		return posY;
	}
	
	public void setX(int x) {
		this.posX = x;
	}
	
	public void setY(int y) {
		this.posY = y;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	
}
