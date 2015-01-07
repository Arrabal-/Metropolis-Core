package mod.arrabal.metrocore.proxy;

import mod.arrabal.metrocore.common.block.BlockMetroCore;

/**
 * Created by Arrabal on 6/26/2014.
 */
public interface IProxy {

    //client methods
    public void registerRenderers();

    public void registerBlockForMeshing(BlockMetroCore block, int meta, String name);

    public void registerSounds();
}
