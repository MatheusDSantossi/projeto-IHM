package entities;

import static utilz.Constants.PlayerConstants.GetSpriteAmount; 
import static utilz.Constants.PlayerConstants.*;

import static utilz.HelpMethods.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gameStates.Playing;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.*;

public class Player extends Entity {

	/*
	 * CHANGE THE PLAYER SCALE
	 */

	private BufferedImage[][] animations;

	private int aniTick, aniIndex;

//	private int playerDir = -1;
	private boolean moving = false, attacking = false;

	private boolean left, right, jump;

	private int[][] lvlData;

	private float xDrawOffset = 21 * Game.SCALE;
	private float yDrawOffset = 4 * Game.SCALE;

	// Jumping / Gravity
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

	// StatusBarUI
	private BufferedImage statusBarImg;
	
	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);
	
	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);
	
//	private int maxHealth = 100;
//	private int currentHealth = maxHealth;
	private int healthWidth = healthBarWidth;
	
	
	
	private int flipX = 0;
	private int flipW = 1;
	
	private boolean attackChecked = false;
	private Playing playing;
		
	private int tileY = 0;
	
	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);

		this.playing = playing;
		this.state = IDLE;
		this.maxHealth = 100;
		this.currentHealth = maxHealth;
		this.walkSpeed = Game.SCALE * 1.0f;
		
		loadAnimation();

		initHitbox(20,  27);
		initAttackBox();

	}

	public void setSpawn(Point spawn) {
		
		this.x = spawn.x;
		this.y = spawn.y;
		
		hitbox.x = x;
		hitbox.y = y;
		
	}
	
	private void initAttackBox() {

		attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
		
	}

	public void update() {
		
		updateHealthBar();	
		
		if(currentHealth <= 0) {
			playing.setGameOver(true);
			return;
		}

			
		
		updateAttackBox();
						
		updatePos();
		
		if(moving) {
			
			checkPotionTouched();
			checkSpikesTouched();
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
			
		}
		
		if(attacking)
			checkAttack();
		
//		updateHitbox();
		updateAnimationTick();

		setAnimation();

	}

	private void checkSpikesTouched() {

		playing.checkSpikesToched(this);
		
	}

	private void checkPotionTouched() {
		
		playing.checkPotionTouched(hitbox);
		
	}

	private void checkAttack() {
		
		if(attackChecked || aniIndex != 1)
			return;
		
		attackChecked = true;
		playing.checkEnemyHit(attackBox);
		playing.checkObjectHit(attackBox);
		
	}

	private void updateAttackBox() {

		if(right) {
			
			attackBox.x = hitbox.x + hitbox.width + (int) (Game.SCALE * 10);
			
		} else if(left) {
			
			attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);
			
		}						
		
		attackBox.y = hitbox.y +(Game.SCALE * 10);
		
	}

	private void updateHealthBar() {
		
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
		
	}

	public void render(Graphics g, int lvlOffset) {

		g.drawImage(animations[state][aniIndex], 
				(int) (hitbox.x - xDrawOffset) - lvlOffset + flipX, 
				(int) (hitbox.y - yDrawOffset),
				width * flipW, height, null);
		
//		drawAttackBox(g, lvlOffset);
		
		drawUI(g);
				                      
//		drawHitbox(g, lvlOffset);
	}

//	public void setDirection(int direction) {
//
//		this.playerDir = direction;
//
//		moving = true;
//
//	}

//	public void setMoving(boolean moving) {
//
//		this.moving = moving;
//
//	}

//	public void spawnRect(int x, int y) {
//		
//		rects.add(new MyRect(x, y));
//		
//	}

	private void drawUI(Graphics g) {
		
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		
		g.setColor(Color.red);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
		
		}

	private void updateAnimationTick() {

		aniTick++;

		if (aniTick >= ANI_SPEED) {

			aniTick = 0;
			aniIndex++;

			if (aniIndex >= GetSpriteAmount(state)) {

				aniIndex = 0;
				attacking = false;
				attackChecked = false;

			}

		}

	}

	private void setAnimation() {

		int startAni = state;

		if (moving) {

			state = RUNNING;

		} else {

			state = IDLE;

		}
		
		if(inAir) {
			
			if(airSpeed < 0) {
				
				state = JUMP;
				
			} else {
				
				state = FALLING;
				
			}
			
		}

		if (attacking) {
		
			state = ATTACK_1;
			if(startAni != ATTACK_1) {
				
				aniIndex = 1;
				aniTick = 0;
				return;
				
			}
				
		}
		if (startAni != state) {

			resetAniTick();

		}

	}

	private void resetAniTick() {

		aniTick = 0;
		aniIndex = 0;

	}

	private void updatePos() {

		moving = false;
		
		if(jump) {
			
			jump();
			
		}

//		if (!left && !right && !up && !down)
//		if (!left && !right && !inAir)
//			return;

		if(!inAir)
			if((!left && !right) || (right && left))
				return;
		
		float xSpeed = 0; // , ySpeed = 0; // temporary stospeed x and y

		if (left) { // && !right) {

			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1; 

//			x -= playerSpeed;
//			moving = true;
		}

		if (right) { // && !left) {

			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1; 

//			x += playerSpeed;
//			moving = true;

		}
		
	
		if(!inAir) {
			
			if(!IsEntityOnFloor(hitbox, lvlData)) {
				
				inAir = true;
				
			}
			
		}

		if (inAir) {

			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {

				hitbox.y += airSpeed;
				airSpeed += GRAVITY;

				updateXPos(xSpeed);

			} else {

				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if (airSpeed > 0) {

					resetInAir();

				} else {
					
					airSpeed = fallSpeedAfterCollision;
					
				}
				
				updateXPos(xSpeed);

			}

		} else {
			updateXPos(xSpeed);

		}
		
		moving = true;

//		if (up && !down) {
//
//			ySpeed = -playerSpeed;
//
////			y -= playerSpeed;
////			moving = true;
//
//		} else if (down && !up) {
//
//			ySpeed = playerSpeed;
//
////			y += playerSpeed;
////			moving = true;
//
//		}

//		if (CanMoveHere(x + xSpeed, y + ySpeed, width, height, lvlData)) {
//
//			this.x += xSpeed;
//			this.y += ySpeed;
//			moving = true;
//		}

//		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y + ySpeed, hitbox.width, hitbox.height, lvlData)) {
//
//			hitbox.x += xSpeed;
//			hitbox.y += ySpeed;
//			moving = true;
//		}

//		if (moving) {
//
//			switch (playerDir) {
//
//			case LEFT:
//				x -= 1;
//				break;
//
//			case UP:
//				y -= 1;
//				break;
//
//			case RIGHT:
//				x += 1;
//				break;
//
//			case DOWN:
//				y += 1;
//				break;
//			}
//
//		}

	}

	private void jump() {

		if(inAir) {
			
			return;
			
		}
		
		inAir = true;
		airSpeed = jumpSpeed;
		
	}

	private void resetInAir() {

		inAir = false;
		airSpeed = 0; 
		
	}

	private void updateXPos(float xSpeed) {

		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {

			hitbox.x += xSpeed;

		} else {

			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);

		}

	}
	
	public void changeHealth(int value) {
		
		currentHealth += value;
		
		if(currentHealth <= 0) {
			
			currentHealth = 0;
			// gameOver();
			
		} else if(currentHealth >= maxHealth) 
			currentHealth = maxHealth;
		
	}
	
	public void kill() {

		currentHealth = 0;
		
	}

	public void changePower(int bluePotionValue) {

		System.out.println("Added power!");
		
	}

	public void loadAnimation() {

		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

//		animations = new BufferedImage[9][6]; // v1 player
		animations = new BufferedImage[7][8];

		for (int j = 0; j < animations.length; j++) {

			for (int i = 0; i < animations[j].length; i++) {

				animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);

			}

		}

		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
		
		
		
//		InputStream is = getClass().getResourceAsStream("/player_sprites.png");

//		try {

//		BufferedImage img = ImageIO.read(is);

//		animations = new BufferedImage[9][6];

//		} catch (IOException e) {
//
//			e.printStackTrace();
//
//		} finally {
//
//			try {
//				is.close();
//			} catch (IOException e) {
//
//				e.printStackTrace();
//
//			}
//
//		}

	}

	public void loadLvlData(int[][] lvlData) {

		this.lvlData = lvlData;
		
		if(!IsEntityOnFloor(hitbox, lvlData)) {
			
			inAir = true;
			
		}

	}

	public void resetDirBooleans() {

		left = false;
		right = false;
//		up = false;
//		down = false;

	}

	public void setAttacking(boolean attacking) {

		this.attacking = attacking;

	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setJump(boolean jump) {
		this.jump= jump;
	}

	public void resetAll() {

		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		state = IDLE;
		currentHealth = maxHealth;
		
		hitbox.x = x;
		hitbox.y = y;
		
		if(!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		
	}

	public int getTileY() {
		
		return tileY;
		
	}

}
