package mod.arrabal.metrocore.common.world.cities;

import net.minecraft.world.gen.structure.MapGenVillage;

/**
 * Created by Evan on 4/7/2015.
 */
public class OverrideMapGenVillage extends MapGenVillage {

    public OverrideMapGenVillage(){
        super();
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ){
        if (true) {
            // if chunkX, chunkZ has city
            return false;
        }
        return super.canSpawnStructureAtCoords(chunkX,chunkZ);
    }
}
