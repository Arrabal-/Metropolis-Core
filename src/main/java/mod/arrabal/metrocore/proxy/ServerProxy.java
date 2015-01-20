package mod.arrabal.metrocore.proxy;

import mod.arrabal.metrocore.common.block.BlockMetroCore;
import mod.arrabal.metrocore.common.block.BlockMetroCoreDoor;
import mod.arrabal.metrocore.common.block.BlockMetroCoreSlab;

/**
 * Created by Arrabal on 6/26/2014.
 */
public class ServerProxy extends CommonProxy {

    // client only methods -- does nothing on the server
    @Override
    public void registerRenderers() {}

    @Override
    public void registerBlockForMeshing(BlockMetroCore block, int meta, String name){}

    @Override
    public void registerBlockForMeshing(BlockMetroCoreSlab block, int meta, String name){}

    @Override
    public void registerBlockForMeshing(BlockMetroCoreDoor block, int meta, String name){}

    @Override
    public void registerSounds() {}

}
