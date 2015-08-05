package mod.arrabal.metrocore.common.world.biome;

import static com.google.common.base.Preconditions.checkState;
import mod.arrabal.metrocore.MetropolisCore;
import mod.arrabal.metrocore.common.init.Biomes;
import mod.arrabal.metrocore.proxy.IProxy;

/**
 * Created by Evan on 7/10/2015.
 */
public class BiomeLoader {

    private static boolean hasRegistered = false;

    private static void register(final Biomes biome){

        final IProxy proxy = MetropolisCore.proxy;
        proxy.addBiomes();
    }

    public void register(){
        checkState(!hasRegistered);
        Biomes biomes = new Biomes();
        final IProxy proxy = MetropolisCore.proxy;
        proxy.addBiomes();
        hasRegistered = true;

    }

}
