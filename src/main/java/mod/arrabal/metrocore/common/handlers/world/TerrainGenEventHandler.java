package mod.arrabal.metrocore.common.handlers.world;

import mod.arrabal.metrocore.common.world.gen.ModdedBiomeDecorator;
import net.minecraftforge.event.terraingen.*;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Arrabal on 6/24/2014.
 */
public class TerrainGenEventHandler {

    public TerrainGenEventHandler() {}

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onCreateDecoratorEvent(BiomeEvent.CreateDecorator event){
        event.newBiomeDecorator = new ModdedBiomeDecorator();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDecorateBiomeEvent(DecorateBiomeEvent.Decorate event){
        if (event.type != DecorateBiomeEvent.Decorate.EventType.CUSTOM){

        }
    }
}
