package gameobject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.Resource;

public class CoinsManager {
	
	private BufferedImage cactus1;
	private Random rand;
	
	private List<Coins> coins;
	private MainCharacter mainCharacter;
	
	public CoinsManager(MainCharacter mainCharacter) {
		rand = new Random();
		cactus1 = Resource.getResouceImage("data/moeda.png");
		coins = new ArrayList<Coins>();
		this.mainCharacter = mainCharacter;
		coins.add(createCoins());
	}
	
	public void update() {
		for(Coins c : coins) {
			c.update();
		}
		Coins coin = coins.get(0);
		if(isCollision()) {
			mainCharacter.upScore();
			mainCharacter.upCoin();
			coins.clear();
			coins.add(createCoins());
		}
		if(coin.isOutOfScreen()) {
			coins.clear();
			coins.add(createCoins());
		}
	}
	
	public void draw(Graphics g) {
		for(Coins c: coins) {
			c.draw(g);
		}
	}
	
	private Coins createCoins() {
		int posY = (int) (Math.random() * 400 + 20);
		return new Coins(mainCharacter, 800, (int) posY, cactus1.getWidth() - 10, cactus1.getHeight() - 10, cactus1);
	}
	
	public boolean isCollision() {
		for(Coins c: coins) {
			if(mainCharacter.getBound().intersects(c.getBound())) {
				return true;
			}
		}
		return false;
	}
	
	public void reset() {
		coins.clear();
		coins.add(createCoins());
	}
}
