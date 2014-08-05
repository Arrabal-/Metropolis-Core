package mod.arrabal.metrocore.common.world;

import mod.arrabal.metrocore.common.world.cities.Metropolis;

/**
 * Created by Arrabal on 6/11/2014.
 */
public class MetropolisBoundingBox implements Comparable<MetropolisBoundingBox> {

    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;
    public final String name;

    public MetropolisBoundingBox(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, String sName){
        minX = xMin;
        minY = yMin;
        minZ = zMin;
        maxX = xMax;
        maxY = yMax;
        maxZ = zMax;
        name = sName;
    }

    public MetropolisBoundingBox(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax){
        minX = xMin;
        minY = yMin;
        minZ = zMin;
        maxX = xMax;
        maxY = yMax;
        maxZ = zMax;
        name = "MetropolisBoundingBox";
    }

    public MetropolisBoundingBox(int xMin, int zMin, int xMax, int zMax){
        minX = xMin;
        minZ = zMin;
        maxX = xMax;
        maxZ = zMax;
        minY = 1;
        maxY = 512;
        name = "MetropolisBoundingBox";
    }

    public MetropolisBoundingBox(int xMin, int zMin, int xMax, int zMax, String sName){
        minX = xMin;
        minZ = zMin;
        maxX = xMax;
        maxZ = zMax;
        minY = 1;
        maxY = 512;
        name = sName;
    }

    public MetropolisBoundingBox(String sData){
        String[] split = sData.split(" ");
        minX = Integer.valueOf(split[0]);
        minY = Integer.valueOf(split[1]);
        minZ = Integer.valueOf(split[2]);
        maxX = Integer.valueOf(split[3]);
        maxY = Integer.valueOf(split[4]);
        maxZ = Integer.valueOf(split[5]);
        name = split[6];
    }

    public boolean intersectsWith(MetropolisBoundingBox mBB){
        return this.maxX >= mBB.minX && this.minX <= mBB.maxX &&
                this.maxZ >= mBB.minZ && this.minZ <= mBB.maxZ &&
                this.maxY >= mBB.minY && this.minY <= mBB.maxY;
    }

    public boolean intersectsWith(int xMinPos, int zMinPos, int xMaxPos, int zMaxPos)
    {
        return this.maxX >= xMinPos && this.minX <= xMaxPos && this.maxZ >= zMinPos && this.minZ <= zMaxPos;
    }

    public void expandTo(MetropolisBoundingBox mBB){
        this.minX = Math.min(this.minX, mBB.minX);
        this.minY = Math.min(this.minY, mBB.minY);
        this.minZ = Math.min(this.minZ, mBB.minZ);
        this.maxX = Math.max(this.maxX, mBB.maxX);
        this.maxY = Math.max(this.maxY, mBB.maxY);
        this.maxZ = Math.max(this.maxZ, mBB.maxZ);
    }

    public void offset(int x, int y, int z)
    {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
    }

    /**
     * Returns width of a bounding box
     */
    public int getXSize()
    {
        return this.maxX - this.minX + 1;
    }

    /**
     * Returns height of a bounding box
     */
    public int getYSize()
    {
        return this.maxY - this.minY + 1;
    }

    /**
     * Returns length of a bounding box
     */
    public int getZSize()
    {
        return this.maxZ - this.minZ + 1;
    }

    public int getCenterX()
    {
        return this.minX + (this.maxX - this.minX + 1) / 2;
    }

    public int getCenterY()
    {
        return this.minY + (this.maxY - this.minY + 1) / 2;
    }

    public int getCenterZ()
    {
        return this.minZ + (this.maxZ - this.minZ + 1) / 2;
    }

    public boolean isVecInside(int x, int y, int z)
    {
        return x >= this.minX && x <= this.maxX && z >= this.minZ && z <= this.maxZ && y >= this.minY && y <= this.maxY;
    }

    @Override
    public int compareTo(MetropolisBoundingBox mBB) {
        if(mBB.minX == minX && mBB.minY == minY && mBB.minZ == minZ){
            return 0;
        }
        if (mBB.minX < minX){ return 1; }
        return -1;
    }

    @Override
    public String toString()
    {
        return minX + " " + minY + " " + minZ + " " + maxX + " " + maxY + " " + maxZ + " " + name;
    }
}
