package mod.arrabal.metrocore.proxy;

import mod.arrabal.metrocore.common.block.BlockMetroCore;

/**
 * Created by Arrabal on 6/26/2014.
 */
public class ServerProxy extends CommonProxy {

    @Override
    public void registerRenderers() {
        // nothing as the server doesn't render anything
    }

    public void registerBlockForMeshing(BlockMetroCore block, int meta, String name){}

    @Override
    public void registerSounds() {
        // nothing as the server doesn't play sounds
    }
}
