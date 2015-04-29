package mod.arrabal.metrocore.common.world.cities;

import mod.arrabal.metrocore.common.world.MetropolisBoundingBox;
import net.minecraft.world.ChunkCoordIntPair;

/**
 * Created by Arrabal on 6/13/2014.
 */
public class CityLayoutPlan extends MetropolisBoundingBox {

    protected ChunkCoordIntPair startCoord;
    protected int startX;
    protected int startZ;
    protected int facing;  // 0 = south : +z; 1 = west : -x; 2 = north : -z; 3 = east : +x

    public CityLayoutPlan(int chunkX, int chunkZ, int iFacing, String sType) {
        super(chunkX << 4, chunkZ << 4, (chunkX << 4) + 15, (chunkZ << 4) + 15, sType);
        this.startCoord = new ChunkCoordIntPair(chunkX, chunkZ);
        this.startX = chunkX;
        this.startZ = chunkZ;
        this.facing = iFacing;
    }

    public CityLayoutPlan(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int iFacing, String sType) {
        super(minX, minY, minZ, maxX, maxY, maxZ, sType);
        this.startCoord = new ChunkCoordIntPair(minX >> 4, minZ >> 4);
        this.startX = startCoord.chunkXPos;
        this.startZ = startCoord.chunkZPos;
        this.facing = iFacing;
    }

    public CityLayoutPlan(int minX, int minZ, int maxX, int maxZ, String sType) {
        super(minX, minZ, maxX, maxZ, sType);
        this.startCoord = new ChunkCoordIntPair(minX >> 4, minZ >> 4);
        this.startX = startCoord.chunkXPos;
        this.startZ = startCoord.chunkZPos;
        this.facing = -1;
    }

    public CityLayoutPlan(String sData) {
        super(sData);
        String[] split = sData.split(" ");
        this.startX = Integer.valueOf(split[7]);
        this.startZ = Integer.valueOf(split[8]);
        this.startCoord = new ChunkCoordIntPair(startX, startZ);
        this.facing = Integer.valueOf(split[9]);
    }

    @Override
    public String coordToString() {
        return startCoord.toString();
    }

    @Override
    public String toString() {
        return this.minBlocKCoords.getX() + " " + this.minBlocKCoords.getY() + " " + this.minBlocKCoords.getZ() +
                " " + this.maxBlockCoords.getX() + " " + this.maxBlockCoords.getY() + " " + this.maxBlockCoords.getZ() +
                " " + this.name + " " + this.startX + " " + this.startZ + " " + this.facing;
    }
}
