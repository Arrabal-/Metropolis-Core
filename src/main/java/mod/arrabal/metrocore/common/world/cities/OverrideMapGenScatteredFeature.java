package mod.arrabal.metrocore.common.world.cities;

import net.minecraft.world.gen.structure.MapGenScatteredFeature;

/**
 * Created by Evan on 4/7/2015.
 */
public class OverrideMapGenScatteredFeature extends MapGenScatteredFeature {

    public OverrideMapGenScatteredFeature(){
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
