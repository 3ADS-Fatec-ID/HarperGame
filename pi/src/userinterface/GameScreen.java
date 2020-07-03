package userinterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import gameobject.Bullet;
import gameobject.CoinsManager;
import gameobject.EnemiesManager;
import gameobject.ImagePanel;
import gameobject.Land;
import gameobject.MainCharacter;
import util.Resource;

public class GameScreen extends JPanel implements Runnable, KeyListener {

	private static final int START_GAME_STATE = 0;
	private static final int GAME_PLAYING_STATE = 1;
	private static final int GAME_OVER_STATE = 2;
	private static final int RANKING_STATE = 3;

	private Land land;
	private ImagePanel panel;
	private MainCharacter mainCharacter;
	private EnemiesManager enemiesManager;
	private CoinsManager coinsManager;
	private Thread thread;

	private boolean isKeyPressed;

	private int gameState = START_GAME_STATE;

	private BufferedImage replayButtonImage;
	private BufferedImage gameOverButtonImage;
	private BufferedImage backgroundImage;

	public GameScreen() {
		backgroundImage = Resource.getResouceImage("data/fundo.jpg");
		mainCharacter = new MainCharacter();
		land = new Land(GameWindow.SCREEN_WIDTH, mainCharacter);
		mainCharacter.setSpeedX(4);
		replayButtonImage = Resource.getResouceImage("data/replay_button.png");
		gameOverButtonImage = Resource.getResouceImage("data/gameover_text.png");
		enemiesManager = new EnemiesManager(mainCharacter);
		coinsManager = new CoinsManager(mainCharacter);
	}

	public void startGame() {
		thread = new Thread(this);
		thread.start();
	}

	public void gameUpdate() {
		if (gameState == GAME_PLAYING_STATE) {
			land.update();
			mainCharacter.update();
			enemiesManager.update();
			coinsManager.update();
			if (enemiesManager.isCollision()) {
				mainCharacter.playDeadSound();
				gameState = GAME_OVER_STATE;
				mainCharacter.dead(true);
			}
		}
	}

	public void paint(Graphics g) {
		backgroundImage = Resource.getResouceImage("data/fundo.jpg");
		g.setColor(Color.decode("#f7f7f7"));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(backgroundImage, 0, 0, null);

		switch (gameState) {
		case START_GAME_STATE:
			g.setColor(Color.WHITE);
			g.drawString("HARPER", 260, 80);
			g.drawString("ESPAÇO - Iniciar Jogo", 200, 150);
			g.drawString("L - Loja", 200, 180);
			g.drawString("R - Ranking", 200, 210);
			break;
		case GAME_PLAYING_STATE:
			mainCharacter.draw(g);
			ArrayList bullets = mainCharacter.getBullets();
			for (int i = 0; i < bullets.size(); i++) {
				Bullet b = (Bullet) bullets.get(i);
				g.setColor(Color.YELLOW);
				g.fillRect(b.getX(), b.getY(), 10, 5);
			}
		case GAME_OVER_STATE:
			land.draw(g);
			enemiesManager.draw(g);
			coinsManager.draw(g);
			mainCharacter.draw(g);
			g.setColor(Color.RED);
			g.drawString("Distância: " + mainCharacter.score, 480, 20);
			g.drawString("Moedas: " + mainCharacter.coins, 20, 20);
			if (gameState == GAME_OVER_STATE) {
				g.drawImage(gameOverButtonImage, 280, 50, null);
			}
			break;
		case RANKING_STATE:
			backgroundImage = Resource.getResouceImage("data/ranking.jpg");
			g.setColor(Color.WHITE);
			g.drawString("RANKING", 260, 50);
			g.drawString("Jogador 1", 100, 100);
			g.drawString("Distância: " + mainCharacter.score, 400, 100);
		}
		
	}

	@Override
	public void run() {

		int fps = 100;
		long msPerFrame = 1000 * 1000000 / fps;
		long lastTime = 0;
		long elapsed;

		int msSleep;
		int nanoSleep;

		long endProcessGame;
		long lag = 0;

		while (true) {
			gameUpdate();
			repaint();
			endProcessGame = System.nanoTime();
			elapsed = (lastTime + msPerFrame - System.nanoTime());
			msSleep = (int) (elapsed / 1000000);
			nanoSleep = (int) (elapsed % 1000000);
			if (msSleep <= 0) {
				lastTime = System.nanoTime();
				continue;
			}
			try {
				Thread.sleep(msSleep, nanoSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lastTime = System.nanoTime();

			ArrayList bullets = mainCharacter.getBullets();
			for (int i = 0; i < bullets.size(); i++) {
				Bullet b = (Bullet) bullets.get(i);
				if (b.isVisible() == true) {
					b.update();
				} else {
					bullets.remove(i);
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!isKeyPressed) {
			isKeyPressed = true;
			switch (gameState) {
			case START_GAME_STATE:
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					gameState = GAME_PLAYING_STATE;
				}
				if (e.getKeyCode() == KeyEvent.VK_R) {
					gameState = RANKING_STATE;
				}
				break;
			case GAME_PLAYING_STATE:
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					mainCharacter.shoot();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					mainCharacter.flyDown(true);
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					mainCharacter.flyUp(true);
				}
				break;
			case GAME_OVER_STATE:
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					gameState = START_GAME_STATE;
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					gameState = RANKING_STATE;
				}
				break;
			case RANKING_STATE:
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					gameState = START_GAME_STATE;
				}

			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		isKeyPressed = false;
		if (gameState == GAME_PLAYING_STATE) {
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				mainCharacter.flyDown(false);
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				mainCharacter.flyUp(false);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	private void resetGame() {
		enemiesManager.reset();
		coinsManager.reset();
		mainCharacter.dead(false);
		mainCharacter.reset();
	}

}
