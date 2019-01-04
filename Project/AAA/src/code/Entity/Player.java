// The only subclass the fully utilizes the
// Entity superclass (no other class requires
// movement in a tile based map).
// Contains all the gameplay associated with
// the Player.

package code.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import code.Manager.Content;
import code.Manager.JukeBox;
import code.TileMap.TileMap;

/**
 *
 * @author CHARKEYD
 */
public class Player extends Entity {
	
	// Sprites
	private final BufferedImage[] downSprites;
	private final BufferedImage[] leftSprites;
	private final BufferedImage[] rightSprites;
	private final BufferedImage[] upSprites;
	private final BufferedImage[] downBoatSprites;
	private final BufferedImage[] leftBoatSprites;
	private final BufferedImage[] rightBoatSprites;
	private final BufferedImage[] upBoatSprites;
	
	// Sprite numbers
	private final int DOWN = 0;
	private final int LEFT = 1;
	private final int RIGHT = 2;
	private final int UP = 3;
	private final int DOWNBOAT = 4;
	private final int LEFTBOAT = 5;
	private final int RIGHTBOAT = 6;
	private final int UPBOAT = 7;
	
	// Game variables
	private int numDiamonds;
	private int totalDiamonds;
	private boolean hasBoat;
	private boolean hasAxe;
	private boolean onWater;
	private long ticks;
	
    /**
     *
     * @param tm
     */
    public Player(TileMap tm) {
		
		super(tm);
		
		width = 16;
		height = 16;
		cwidth = 12;
		cheight = 12;
		
		moveSpeed = 2;
		
		numDiamonds = 0;
		
		downSprites = Content.PLAYER[0];
		leftSprites = Content.PLAYER[1];
		rightSprites = Content.PLAYER[2];
		upSprites = Content.PLAYER[3];
		downBoatSprites = Content.PLAYER[4];
		leftBoatSprites = Content.PLAYER[5];
		rightBoatSprites = Content.PLAYER[6];
		upBoatSprites = Content.PLAYER[7];
		
		animation.setFrames(downSprites);
                //was 10
		animation.setDelay(10);
		
	}
	
	private void setAnimation(int i, BufferedImage[] bi, int d) {
		currentAnimation = i;
		animation.setFrames(bi);
		animation.setDelay(d);
	}
	
    /**
     *
     */
    public void collectedDiamond() { numDiamonds++; }

    /**
     *
     * @return
     */
    public int numDiamonds() { return numDiamonds; }

    /**
     *
     * @return
     */
    public int getTotalDiamonds() { return totalDiamonds; }

    /**
     *
     * @param i
     */
    public void setTotalDiamonds(int i) { totalDiamonds = i; }
	
    /**
     *
     */
    public void gotBoat() { hasBoat = true; tileMap.replace(22, 4); }

    /**
     *
     */
    public void gotAxe() { hasAxe = true; }

    /**
     *
     * @return
     */
    public boolean hasBoat() { return hasBoat; }

    /**
     *
     * @return
     */
    public boolean hasAxe() { return hasAxe; }
	
	// Used to update time.

    /**
     *
     * @return
     */
	public long getTicks() { return ticks; }
	
	// Keyboard input. Moves the player.
        @Override
	public void setDown() 
        {
		super.setDown();
	}
    
        @Override
	public void setLeft() 
        {
		super.setLeft();
	}
        
        @Override
	public void setRight() 
        {
		super.setRight();
	}
        
        @Override
	public void setUp() 
        {
		super.setUp();
	}
	
	// Keyboard input.
	// If Player has axe, dead trees in front
	// of the Player will be chopped down.

    /**
     *
     */
	public void setAction() {
		if(hasAxe) {
			if(currentAnimation == UP && tileMap.getIndex(rowTile - 1, colTile) == 21) {
				tileMap.setTile(rowTile - 1, colTile, 1);
				JukeBox.play("tilechange");
			}
			if(currentAnimation == DOWN && tileMap.getIndex(rowTile + 1, colTile) == 21) {
				tileMap.setTile(rowTile + 1, colTile, 1);
				JukeBox.play("tilechange");
			}
			if(currentAnimation == LEFT && tileMap.getIndex(rowTile, colTile - 1) == 21) {
				tileMap.setTile(rowTile, colTile - 1, 1);
				JukeBox.play("tilechange");
			}
			if(currentAnimation == RIGHT && tileMap.getIndex(rowTile, colTile + 1) == 21) {
				tileMap.setTile(rowTile, colTile + 1, 1);
				JukeBox.play("tilechange");
			}
		}
	}
	
	public void update() {
		
		ticks++;
		
		// check if on water
		boolean current = onWater;
		if(tileMap.getIndex(ydest / tileSize, xdest / tileSize) == 4) {
			onWater = true;
		}
		else {
			onWater = false;
		}
		// if going from land to water
		if(!current && onWater) {
			JukeBox.play("splash");
		}
		
		// set animation
		if(down) {
			if(onWater && currentAnimation != DOWNBOAT) {
				setAnimation(DOWNBOAT, downBoatSprites, 10);
			}
			else if(!onWater && currentAnimation != DOWN) {
				setAnimation(DOWN, downSprites, 10);
			}
		}
		if(left) {
			if(onWater && currentAnimation != LEFTBOAT) {
				setAnimation(LEFTBOAT, leftBoatSprites, 10);
			}
			else if(!onWater && currentAnimation != LEFT) {
				setAnimation(LEFT, leftSprites, 10);
			}
		}
		if(right) {
			if(onWater && currentAnimation != RIGHTBOAT) {
				setAnimation(RIGHTBOAT, rightBoatSprites, 10);
			}
			else if(!onWater && currentAnimation != RIGHT) {
				setAnimation(RIGHT, rightSprites, 10);
			}
		}
		if(up) {
			if(onWater && currentAnimation != UPBOAT) {
				setAnimation(UPBOAT, upBoatSprites, 10);
			}
			else if(!onWater && currentAnimation != UP) {
				setAnimation(UP, upSprites, 10);
			}
		}
		
		// update position
		super.update();
		
	}
	
	// Draw Player.
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	
}