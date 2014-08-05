package mod.arrabal.metrocore.common.handlers.world;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mod.arrabal.metrocore.common.world.structure.MapGenUrban;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arrabal on 2/26/14.
 */
public class MapGenEventHandler {

    private static MapGenUrban urbanGenerator = new MapGenUrban();
    public static Map urbanStructureMap;

    public MapGenEventHandler(){}

    public static void init(){
        urbanStructureMap  = new HashMap();
    }

    public static MapGenUrban getUrbanGenerator(){
        return urbanGenerator;
    }

    @SubscribeEvent
    public void onMapGenInit(InitMapGenEvent event){


    }


}
