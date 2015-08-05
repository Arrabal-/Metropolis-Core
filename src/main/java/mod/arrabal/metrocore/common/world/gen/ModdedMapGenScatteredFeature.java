package mod.arrabal.metrocore.common.world.gen;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;

import java.util.Map;

/**
 * Created by Arrabal on 4/14/2015.
 */
public class ModdedMapGenScatteredFeature extends MapGenScatteredFeature {

    public ModdedMapGenScatteredFeature(){
        super();
    }

    public ModdedMapGenScatteredFeature(Map map){
        super(map);
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int x, int z){
        return false;
    }
}
