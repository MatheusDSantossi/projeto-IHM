package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Player;
import gameStates.Playing;
import levels.Level;
import main.Game;
import utilz.LoadSave;

import static utilz.Constants.ObjectCostants.*;
import static utilz.HelpMethods.CanCannonSeePlayer;

public class ObjectManager {

	private Playing playing;
	private BufferedImage[][] potionImgs, containerImgs;
	private BufferedImage[] cannonImgs;
	private BufferedImage spikeImg;
	private ArrayList<Potion> potions;
	private ArrayList<GameContainer> containers;
	private ArrayList<Spike> spikes;
	private ArrayList<Cannon> cannons;

	public ObjectManager(Playing playing) {

		this.playing = playing;
		loadImgs();

//		potions = new ArrayList<>();
//		
//		potions.add(new Potion(300, 200, RED_POTION));
//		potions.add(new Potion(400, 200, BLUE_POTION));
//	
//		containers = new ArrayList<>();
//		
//		containers.add(new GameContainer(500, 200, BARREL));
//		containers.add(new GameContainer(600, 200, BOX));

	}

	public void checkpiesTouced(Player p) {

		for (Spike s : spikes)
			if (s.getHitbox().intersects(p.getHitbox()))
				p.kill();

	}

	public void checkObjectTouched(Rectangle2D.Float hitbox) {

		for (Potion p : potions)
			if (p.isActive()) {

				if (hitbox.intersects(p.getHitbox())) {
					p.setActive(false);

					applyEffectToPlayer(p);

				}

			}

	}

	public void applyEffectToPlayer(Potion p) {

		if (p.getObjType() == RED_POTION) {

			playing.getPlayer().changeHealth(RED_POTION_VALUE);

		} else {

			playing.getPlayer().changePower(BLUE_POTION_VALUE);

		}

	}

	public void checkObjectHit(Rectangle2D.Float attackbox) {

		for (GameContainer gc : containers)
			if (gc.isActive() && !gc.doAnimation) {

				if (gc.getHitbox().intersects(attackbox)) {

					gc.setAnimation(true);

					int type = 0;

					if (gc.getObjType() == BARREL)
						type = 1;

					potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width / 2),
							(int) (gc.getHitbox().y - gc.getHitbox().height / 2), type));

					return;
				}

			}

	}

	private void loadImgs() {

		BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
		potionImgs = new BufferedImage[2][7];

		for (int j = 0; j < potionImgs.length; j++)
			for (int i = 0; i < potionImgs[j].length; i++)
				potionImgs[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);

		BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
		containerImgs = new BufferedImage[2][8];

		for (int j = 0; j < containerImgs.length; j++)
			for (int i = 0; i < containerImgs[j].length; i++)
				containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);

		spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);

		cannonImgs = new BufferedImage[7];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);

		for (int i = 0; i > cannonImgs.length; i++)
			cannonImgs[i] = temp.getSubimage(i * 40, 0, 40, 26);

	}

	public void update(int lvlData[][], Player player) {

		for (Potion p : potions)
			if (p.isActive())
				p.update();

		for (GameContainer gc : containers)
			if (gc.isActive())
				gc.update();

		updateCannons(lvlData, player);

	}

	private boolean isPlayerInRange(Cannon c, Player player) {
		int absValue = (int) Math.abs(player.getHitbox().x - c.getHitbox().x);
		return absValue <= Game.TILES_SIZE * 5;
	}

	private boolean isPlayerInfrontOfCannon(Cannon c, Player player) {

		if (c.getObjType() == CANNON_LEFT) {

			if (c.getHitbox().x > player.getHitbox().y)
				return true;

		} else if (c.getHitbox().x < player.getHitbox().x)
			return true;

		return false;
	}

	private void updateCannons(int lvlData[][], Player player) {

		for (Cannon c : cannons) {
			System.out.println("CANNON EXIST");
			if (!c.doAnimation)
				if (c.getTileY() == player.getTileY())
					if (isPlayerInRange(c, player))
						if (isPlayerInfrontOfCannon(c, player))
							if (CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY())) {

								shootCannon(c);

							}
			c.update();

		}

		/*
		 * if the cannon is not Animating tileY is same ifPlayer is in range is player
		 * infront of cannon los shoot the cannon
		 * 
		 */

	}

	private void shootCannon(Cannon c) {

		c.setAnimation(true);

	}

	public void draw(Graphics g, int xLvlOffset) {

		drawPotions(g, xLvlOffset);
		drawContainers(g, xLvlOffset);
		drawTrap(g, xLvlOffset);
		drawCannons(g, xLvlOffset);

	}

	private void drawCannons(Graphics g, int xLvlOffset) {

		for (Cannon c : cannons) {

			int x = (int) (c.getHitbox().x - xLvlOffset);
			int width = CANNON_WIDTH;

			if (c.getObjType() == CANNON_RIGHT) {

				x += width;
				width *= -1;

			}

			g.drawImage(cannonImgs[c.getAniIndex()], x, (int) (c.getHitbox().y), width, CANNON_HEIGHT, null);

		}
	}

	private void drawTrap(Graphics g, int xLvlOffset) {

		for (Spike s : spikes)
			g.drawImage(spikeImg, (int) (s.getHitbox().x - xLvlOffset), (int) (s.getHitbox().y - s.getyDrawOffset()),
					SPIKE_WIDTH, SPIKE_HEIGHT, null);

	}

	private void drawContainers(Graphics g, int xLvlOffset) {

		for (GameContainer gc : containers)
			if (gc.isActive()) {
				int type = 0;

				if (gc.getObjType() == BARREL)
					type = 1;

				g.drawImage(containerImgs[type][gc.getAniIndex()],
						(int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset),
						(int) (gc.getHitbox().y - gc.getyDrawOffset()), CONTAINER_WIDTH, CONTAINER_HEIGHT, null);

			}
	}

	private void drawPotions(Graphics g, int xLvlOffset) {

		for (Potion p : potions)
			if (p.isActive()) {

				int type = 0;

				if (p.getObjType() == RED_POTION)
					type = 1;

				g.drawImage(potionImgs[type][p.getAniIndex()],
						(int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset),
						(int) (p.getHitbox().y - p.getyDrawOffset()), POTION_WIDTH, POTION_HEIGHT, null);

			}

	}

	public void loadObject(Level newLevel) {

//		potions = newLevel.getPotions();
		potions = new ArrayList<>(newLevel.getPotions());
//		containers = newLevel.getContainers();
		containers = new ArrayList<>(newLevel.getContainers());
		spikes = newLevel.getSpikes();
		cannons = newLevel.getCannons();

	}

	public void resetAllObjects() {

		System.out.println("Size of arrays: " + potions.size() + " - " + containers.size());

		loadObject(playing.getLevelManager().getCurrentLevel());

		for (Potion p : potions)
			p.reset();

		for (GameContainer gc : containers)
			gc.reset();

		for (Cannon c : cannons)
			c.reset();

//		System.out.println("Size of arrays: " + potions.size() + " - " + containers.size());

	}

}
