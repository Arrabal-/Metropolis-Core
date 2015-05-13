package mod.arrabal.metrocore.common.world;


import net.minecraft.util.BlockPos;

/**
 * Created by Arrabal on 6/11/2014.
 */
public class MetropolisBoundingBox implements Comparable<MetropolisBoundingBox> {

    public BlockPos minBlockCoords;
    public BlockPos maxBlockCoords;
    public final String name;

    public MetropolisBoundingBox(BlockPos minPos, BlockPos maxPos){
        this.minBlockCoords = minPos;
        this.maxBlockCoords = maxPos;
        this.name = "MetropolisBoundingBox";
    }

    public MetropolisBoundingBox(BlockPos minPos, BlockPos maxPos, String label){
        this.minBlockCoords = minPos;
        this.maxBlockCoords = maxPos;
        this.name = label;
    }

    public MetropolisBoundingBox(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, String sName){
        this.minBlockCoords = new BlockPos(xMin, yMin, zMin);
        this.maxBlockCoords = new BlockPos(xMax, yMax, zMax);
        name = sName;
    }

    public MetropolisBoundingBox(int xMin, int zMin, int xMax, int zMax){
        this.minBlockCoords = new BlockPos(xMin, 1, zMin);
        this.maxBlockCoords = new BlockPos(xMax, 255, zMax);
        name = "MetropolisBoundingBox";
    }

    public MetropolisBoundingBox(int xMin, int zMin, int xMax, int zMax, String sName){
        this.minBlockCoords = new BlockPos(xMin, 1, zMin);
        this.maxBlockCoords = new BlockPos(xMax, 255, zMax);
        name = sName;
    }

    public MetropolisBoundingBox(String sData){
        String[] split = sData.split(" ");
        this.minBlockCoords = new BlockPos(Integer.valueOf(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]));
        this.maxBlockCoords = new BlockPos(Integer.valueOf(split[3]), Integer.valueOf(split[4]), Integer.valueOf(split[5]));
        name = split[6];
    }

    public boolean intersectsWith(MetropolisBoundingBox mBB){
        return this.maxBlockCoords.getX() >= mBB.minBlockCoords.getX() && this.minBlockCoords.getX() <= mBB.maxBlockCoords.getX() &&
                this.maxBlockCoords.getZ() >= mBB.minBlockCoords.getZ() && this.minBlockCoords.getZ() <= mBB.maxBlockCoords.getZ() &&
                this.maxBlockCoords.getY() >= mBB.minBlockCoords.getY() && this.minBlockCoords.getY() <= mBB.maxBlockCoords.getY();
    }

    public boolean intersectsWith(int xMinPos, int zMinPos, int xMaxPos, int zMaxPos) {
        return this.maxBlockCoords.getX() >= xMinPos && this.minBlockCoords.getX() <= xMaxPos &&
                this.maxBlockCoords.getZ() >= zMinPos && this.minBlockCoords.getZ() <= zMaxPos;
    }

    public void expandTo(MetropolisBoundingBox mBB){
        this.minBlockCoords = new BlockPos(Math.min(this.minBlockCoords.getX(), mBB.minBlockCoords.getX()),
                Math.min(this.minBlockCoords.getY(), mBB.minBlockCoords.getY()), Math.min(this.minBlockCoords.getZ(), mBB.minBlockCoords.getZ()));
        this.maxBlockCoords = new BlockPos( Math.max(this.maxBlockCoords.getX(), mBB.maxBlockCoords.getX()),
                Math.max(this.maxBlockCoords.getY(), mBB.maxBlockCoords.getY()), Math.max(this.maxBlockCoords.getZ(), mBB.maxBlockCoords.getZ()));
    }

    public void offset(int x, int y, int z)
    {
        this.minBlockCoords.add(x, y, z);
        this.maxBlockCoords.add(x, y, z);
    }

    /**
     * Returns width of a bounding box
     */
    public int getXSize()
    {
        return this.maxBlockCoords.getX() - this.minBlockCoords.getX() + 1;
    }

    /**
     * Returns height of a bounding box
     */
    public int getYSize()
    {
        return this.maxBlockCoords.getY() - this.minBlockCoords.getY() + 1;
    }

    /**
     * Returns length of a bounding box
     */
    public int getZSize()
    {
        return this.maxBlockCoords.getZ() - this.minBlockCoords.getZ() + 1;
    }

    public int getCenterX()
    {
        return this.minBlockCoords.getX() + this.getXSize() / 2;
    }

    public int getCenterY()
    {
        return this.minBlockCoords.getY() + this.getYSize() / 2;
    }

    public int getCenterZ()
    {
        return this.minBlockCoords.getZ() + this.getZSize() / 2;
    }

    public boolean isVecInside(int x, int y, int z)
    {
        return x >= this.minBlockCoords.getX() && x <= this.maxBlockCoords.getX() &&
                z >= this.minBlockCoords.getZ() && z <= this.maxBlockCoords.getZ() &&
                y >= this.minBlockCoords.getY() && y <= this.maxBlockCoords.getY();
    }

    public boolean isVecInside(BlockPos blockPos){
        return blockPos.getX() >= this.minBlockCoords.getX() && blockPos.getX() <= this.maxBlockCoords.getX() &&
                blockPos.getZ() >= this.minBlockCoords.getZ() && blockPos.getZ() <= this.maxBlockCoords.getZ() &&
                blockPos.getY() >= this.minBlockCoords.getY() && blockPos.getY() <= this.maxBlockCoords.getY();
    }

    public int getSquaredDistance(MetropolisBoundingBox mBB, boolean center){
        if (center){
            return ((this.getCenterX() - mBB.getCenterX()) * (this.getCenterX() - mBB.getCenterX())) +
                    ((this.getCenterZ() - mBB.getCenterZ()) * (this.getCenterZ() - mBB.getCenterZ()));
        }
        int xDist = Math.min(Math.abs(this.minBlockCoords.getX() - mBB.maxBlockCoords.getX()), Math.abs(this.maxBlockCoords.getX() - mBB.minBlockCoords.getX()));
        int zDist =  Math.min(Math.abs(this.minBlockCoords.getZ() - mBB.maxBlockCoords.getZ()), Math.abs(this.maxBlockCoords.getZ() - mBB.minBlockCoords.getZ()));
        return xDist * xDist + zDist * zDist;
    }

    /*public void contractHeight(int contract, int direction, boolean symmetrical) {
        if (symmetrical) {
            this.minY += contract;
            this.maxY -= contract;
        } else switch (direction) {
            case 0:
                this.minY += contract;
                break;
            default:
                this.maxY -= contract;
        }
    }

    public void contractPlane(int contract, int direction, boolean symmetrical) {
        if (symmetrical) {
            if (direction == 0 || direction == 2) {
                this.minZ += contract;
                this.maxZ -= contract;
            } else {
                this.minX += contract;
                this.maxX -= contract;
            }
        } else switch (direction) {
            case 0:
                this.maxZ -= contract;
                break;
            case 1:
                this.minX += contract;
                break;
            case 2:
                this.minZ += contract;
                break;
            case 3:
                this.maxX -= contract;
                break;
        }
    }*/

    @Override
    public int compareTo(MetropolisBoundingBox mBB) {
        if(mBB.minBlockCoords.getX() == this.minBlockCoords.getX() && mBB.minBlockCoords.getY() == this.minBlockCoords.getY() &&
                mBB.minBlockCoords.getZ() == this.minBlockCoords.getZ()){
            return 0;
        }
        if (mBB.minBlockCoords.getX() < this.minBlockCoords.getX()){ return 1; }
        return -1;
    }

    public String coordToString(){
        return "[" + (this.minBlockCoords.getX() >> 4) + ", " + (this.minBlockCoords.getZ() >> 4) + "]";
    }

    @Override
    public String toString()
    {
        return this.minBlockCoords.getX() + " " + this.minBlockCoords.getY() + " " + this.minBlockCoords.getZ() +
                " " + this.maxBlockCoords.getX() + " " + this.maxBlockCoords.getY() + " " + this.maxBlockCoords.getZ() + " " + name;
    }
}
