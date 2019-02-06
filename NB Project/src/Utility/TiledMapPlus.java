package Utility;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 * Handles map rendering Handles collisions Handles camera
 *
 * Note: Collision layers must have a property called blocked, set to true
 *
 * @author David
 */
public class TiledMapPlus extends TiledMap
{

    // Tile array
    private boolean[][] blocked;

    // Determine X collision tightness
    private final int Xfactor = 25;
    private final int Xadjuster = 50;

    // Determines Y collision tightnesss
    private final int Yfactor = 60;
    private final int Yadjuster = 50;

    // Tile size
    public static final int tileSize = 64;

    /**
     * Initializes array of "tile cells" Each cell represents a tile "True"
     * means blocked
     *
     * Note: - Embed tilesets - Don't change probabilities
     *
     * @param ref
     * @throws SlickException
     */
    public TiledMapPlus(String ref) throws SlickException
    {
        super(ref);

        int HorizontalTileNo = getHeight() - 2;
        int VerticalTileNo = getWidth() - 2;
        int layerCount = getLayerCount();

        blocked = new boolean[HorizontalTileNo][VerticalTileNo];

        for (int l = 0; l < layerCount; l++)
        {
            String layerValue = getLayerProperty(l, "blocked", "false");
            if (layerValue.equals("true"))
            {

                for (int c = 0; c < VerticalTileNo; c++)
                {
                    for (int r = 0; r < HorizontalTileNo; r++)
                    {
                        if (getTileId(c, r, l) != 0)
                        {
                            blocked[c][r] = true;
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if a point is on a blocked tile
     *
     * @param x
     * @param y
     * @return
     */
    private boolean canPass(float x, float y)
    {
        try
        {
            int xBlock = convertXtoCol(x);
            int yBlock = convertYtoRow(y);
            return !blocked[xBlock][yBlock];
        }
        catch (Exception e)
        {
            return true;
        }
    }

    /**
     * Convert a x coordinate to the tile column
     *
     * @param xPos
     * @return
     */
    public static int convertXtoCol(float xPos)
    {
        return getGridFromCoord(xPos);
    }

    /**
     * Convert a x coordinate to the tile column
     *
     * @param yPos
     * @return Column
     */
    public static int convertYtoRow(float yPos)
    {
        return getGridFromCoord(yPos);
    }

    /**
     * Convert x/y to r/c
     */
    private static int getGridFromCoord(float coord)
    {
        return ((int) coord / tileSize);
    }

    /**
     * Checks tile collision Note: Map bounds check not needed Player position
     * cannot have negative values Exiting the topside of the map requires
     * negative values
     *
     * @param playerX
     * @param playerY
     * @param relSpeed Speed as function of delta
     * @return True if up movement allowed
     */
    public boolean isUpAllowed(int playerX, int playerY, float relSpeed)
    {
        // Both True = No blocked tiles upwards
        boolean cond1 = canPass(playerX + Xfactor, playerY - relSpeed);
        boolean cond2 = canPass(playerX + Xadjuster, playerY - relSpeed);

        return (cond1 && cond2);

    }

    /**
     * Checks tile collision and map bounds
     *
     * @param playerX
     * @param playerY
     * @param relSpeed Speed as function of delta
     * @return True if down movement allowed
     */
    public boolean isDownAllowed(int playerX, int playerY, float relSpeed)
    {
        // Both true = No blocked tiles down
        float newY = playerY + Yfactor + relSpeed;
        boolean cond1 = canPass(playerX + Xfactor, newY);
        boolean cond2 = canPass(playerX + Xadjuster, newY);

        // True if within map bounds
        int yLimiter = (getHeight() * getTileHeight()) - tileSize;
        boolean cond3 = !((playerY + relSpeed) > yLimiter);

        return (cond1 && cond2 && cond3);
    }

    /**
     * Checks tile collision Note: Map bounds check not needed Player position
     * cannot have negative values Exiting the left-side of the map requires
     * negative values
     *
     * @param playerX
     * @param playerY
     * @param relSpeed Speed as function of delta
     * @return True if left movement allowed
     */
    public boolean isLeftAllowed(int playerX, int playerY, float relSpeed)
    {
        // True = No blocked tiles to the left
        boolean cond1 = canPass(playerX - relSpeed, playerY + Yadjuster);
        boolean cond2 = canPass(playerX - relSpeed, playerY + Yfactor);

        return (cond1 && cond2);
    }

    /**
     * Checks tile collision and map bounds
     *
     * @param playerX
     * @param playerY
     * @param relSpeed Speed as function of delta
     * @return True if right movement allowed
     */
    public boolean isRightAllowed(int playerX, int playerY, float relSpeed)
    {
        // Both True = No blocked tiles to the right
        float newX = playerX + Xfactor + relSpeed;
        boolean cond1 = canPass(newX, playerY + Yfactor);
        boolean cond2 = canPass(newX, playerY + Yadjuster);

        // True if within map bounds 
        int xLimiter = (getWidth() * getTileWidth()) - (tileSize - tileSize / 6);
        boolean cond3 = !((playerX + relSpeed) > xLimiter);

        return (cond1 && cond2 && cond3);
    }

}
