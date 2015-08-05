package mod.arrabal.metrocore.common.init;

import mod.arrabal.metrocore.common.world.biome.BiomeGenPlainsMetro;
import net.minecraft.world.biome.BiomeGenBase;

/**
 * Created by Evan on 7/10/2015.
 */
public class Biomes {

    public static final BiomeGenBase.Height height_FlatPlains = new BiomeGenBase.Height(0.1F, 0.0125F);

    public static final BiomeGenBase plainsMetro = (new BiomeGenPlainsMetro(64)).setColor(9286496).setBiomeName("Flat Plains");

}
