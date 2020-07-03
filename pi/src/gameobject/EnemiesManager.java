package gameobject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.Resource;

public class EnemiesManager {
	
	private BufferedImage cactus1;
	private BufferedImage cactus2;
	private Random rand;
	
	private List<Enemy> enemies;
	private MainCharacter mainCharacter;
	
	public EnemiesManager(MainCharacter mainCharacter) {
		rand = new Random();
		cactus1 = Resource.getResouceImage("data/asteroide.png");
		cactus2 = Resource.getResouceImage("data/inimigo.png");
		enemies = new ArrayList<Enemy>();
		this.mainCharacter = mainCharacter;
		enemies.add(createEnemy(0));
		enemies.add(createEnemy(1));
		enemies.add(createEnemy(1));
		enemies.add(createEnemy(1));
	}
	
	public void update() {
		for(Enemy e : enemies) {
			e.update();
		}
		Enemy enemy = enemies.get(0);
		if(enemy.isOutOfScreen()) {
			mainCharacter.upScore();
			enemies.clear();
			enemies.add(createEnemy(0));
			enemies.add(createEnemy(1));
			enemies.add(createEnemy(1));
			enemies.add(createEnemy(1));
		}
	}
	
	public void draw(Graphics g) {
		for(Enemy e : enemies) {
			e.draw(g);
		}
	}
	
	private Enemy createEnemy(int t) {
		// if (enemyType = getRandom)
		int type = t;
		if(type == 0) {
			return new Cactus(mainCharacter, (int) (Math.random() * 280 + 40), (int) (500 + Math.random() * 300), cactus1.getWidth() - 10, cactus1.getHeight() - 10, cactus1);
		} else {
			return new Cactus(mainCharacter, (int) (Math.random() * 280 + 40), (int) (500 + Math.random() * 300), cactus2.getWidth() - 10, cactus2.getHeight() - 10, cactus2);
		}
	}
	
	public boolean isCollision() {
		for(Enemy e : enemies) {
			if (mainCharacter.getBound().intersects(e.getBound())) {
				return true;
			}
		}
		return false;
	}
	
	public void reset() {
		enemies.clear();
		enemies.add(createEnemy(0));
		enemies.add(createEnemy(1));
		enemies.add(createEnemy(1));
		enemies.add(createEnemy(1));
	}
	
}
