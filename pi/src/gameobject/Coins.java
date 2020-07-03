package gameobject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Coins {
	private int posX;
	private int posY;
	private int width;
	private int height;
	
	private BufferedImage image;
	private MainCharacter mainCharacter;
	
	private Rectangle rectBound;
	
	public Coins(MainCharacter mainCharacter, int posX, int posY, int width, int height, BufferedImage image) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.image = image;
		this.mainCharacter = mainCharacter;
		rectBound = new Rectangle();
	}
	
	public void update() {
		posX -= mainCharacter.getSpeedX();
	}
	
	public void draw(Graphics g) {
		g.drawImage(image, posX, posY - image.getHeight(), null);
		g.setColor(Color.yellow);
	}
	
	public Rectangle getBound() {
		rectBound = new Rectangle();
		rectBound.x = (int) posX + (image.getWidth() - width) / 2;
		rectBound.y = (int) posY - image.getHeight() + (image.getHeight() - height) / 2;
		rectBound.width = width;
		rectBound.height = height;
		return rectBound;
	}
	
	public boolean isOutOfScreen() {
		if(posX < -image.getWidth()) {
			return true;
		}
		return false;
	}
	
	public int getX() {
		return posX;
	}
}
