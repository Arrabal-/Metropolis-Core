package mod.arrabal.metrocore.common.world.cities;

import mod.arrabal.metrocore.common.world.MetropolisBoundingBox;
import net.minecraft.world.ChunkCoordIntPair;

/**
 * Created by Arrabal on 6/13/2014.
 */
public class MetropolisBaseBB extends MetropolisBoundingBox {

    protected ChunkCoordIntPair startCoord;
    protected int startX;
    protected int startZ;
    protected int facing;  // 0 = south : +z; 1 = west : -x; 2 = north : -z; 3 = east : +x

    public MetropolisBaseBB(int chunkX, int chunkZ, int iFacing, String sType){
        super(chunkX << 4, chunkZ << 4, (chunkX << 4) + 15, (chunkZ << 4) + 15, sType);
        this.startCoord = new ChunkCoordIntPair(chunkX, chunkZ);
        this.startX = chunkX;
        this.startZ = chunkZ;
        this.facing = iFacing;
    }

    public MetropolisBaseBB(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int iFacing, String sType){
        super(minX, minY, minZ, maxX, maxY, maxZ, sType);
        this.startCoord = new ChunkCoordIntPair(minX >> 4, minZ >> 4);
        this.startX = startCoord.chunkXPos;
        this.startZ = startCoord.chunkZPos;
        this.facing = iFacing;
    }

    public MetropolisBaseBB(int minX, int minZ, int maxX, int maxZ, String sType){
        super(minX, minZ, maxX, maxZ, sType);
        this.startCoord = new ChunkCoordIntPair(minX >> 4, minZ >> 4);
        this.startX = startCoord.chunkXPos;
        this.startZ = startCoord.chunkZPos;
        this.facing = 0;
    }

    public MetropolisBaseBB(String sData){
        super(sData);
        String[] split = sData.split(" ");
        this.startX = Integer.valueOf(split[7]);
        this.startZ = Integer.valueOf(split[8]);
        this.startCoord = new ChunkCoordIntPair(startX, startZ);
        this.facing = Integer.valueOf(split[9]);
    }

    public String coordToString(){
        return startCoord.toString();
    }

    public void contractPlane(int contract, int direction, boolean symmetrical){
        if (symmetrical){
            if (direction == 0 || direction == 2){
                this.minZ += contract;
                this.maxZ -= contract;
            }
            else {
                this.minX += contract;
                this.maxX -= contract;
            }
        }
        else switch (direction) {
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
    }

    public void contractHeight(int contract, int direction, boolean symmetrical){
        if (symmetrical){
            this.minY += contract;
            this.maxY -= contract;
        }
        else switch(direction){
            case 0:
                this.minY += contract;
                break;
            default:
                this.maxY -= contract;
        }
    }

    @Override
    public String toString(){
        return this.minX + " " + this.minY + " " + this.minZ + " " + this.maxX +
                " " + this.maxY + " " + this.maxZ + " " + this.name + " " + this.startX + " " +
                this.startZ + " " + this.facing;
    }

    public int getSquaredDistance(MetropolisBaseBB mBB, boolean center){
        if (center){
            return ((this.getCenterX() - mBB.getCenterX()) * (this.getCenterX() - mBB.getCenterX())) +
                    ((this.getCenterZ() - mBB.getCenterZ()) * (this.getCenterZ() - mBB.getCenterZ()));
        }
        int xDist = Math.min(Math.abs(this.minX - mBB.maxX), Math.abs(this.maxX - mBB.minX));
        int zDist =  Math.min(Math.abs(this.minZ - mBB.maxZ), Math.abs(this.maxZ - mBB.minZ));
        return xDist * xDist + zDist * zDist;
    }
}
