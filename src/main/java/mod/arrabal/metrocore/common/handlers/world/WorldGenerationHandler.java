package mod.arrabal.metrocore.common.handlers.world;

import net.minecraftforge.fml.common.IWorldGenerator;
import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.library.LogHelper;
import mod.arrabal.metrocore.common.world.cities.MetropolisGenerationContainer;
import mod.arrabal.metrocore.common.world.cities.MetropolisStart;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.storage.ISaveHandler;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Created by Arrabal on 6/6/2014.
 */
public class WorldGenerationHandler implements IWorldGenerator {

    private ConcurrentLinkedQueue<int[]> currentlyGenerating;
    private static ConcurrentHashMap<Integer, MetropolisGenerationContainer> generatorMap;

    public WorldGenerationHandler() {
        currentlyGenerating = new ConcurrentLinkedQueue<int[]>();
        generatorMap = new ConcurrentHashMap<Integer, MetropolisGenerationContainer>();
    }

    public static File getWorldSaveDir(World world){
        ISaveHandler saveHandler = world.getSaveHandler();
        if (saveHandler.getChunkLoader(world.provider) instanceof AnvilChunkLoader){
            AnvilChunkLoader loader = (AnvilChunkLoader) saveHandler.getChunkLoader(world.provider);
            for (Field f : loader.getClass().getDeclaredFields()){
                if (f.getType().equals(File.class)){
                    try{
                        f.setAccessible((true));
                        File saveLocation = (File) f.get(loader);
                        LogHelper.debug("Found world save directory as: " + saveLocation);
                        return saveLocation;
                    }
                    catch (Exception e){
                        LogHelper.error("MetropolisCore failed trying to locate world save directory");
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

        if (!world.isRemote && ConfigHandler.enableCityCreation){
            int[] tuple = {chunkX,chunkZ};
            if (this.currentlyGenerating.contains(tuple)){
                LogHelper.info("MetropolisCore caught recursive generator call at [" + chunkX + "' " + chunkZ + "]");
            }
            else{
                MetropolisGenerationContainer handler = getGenContainerFromWorld(world);
                if (!handler.catchChunkBug(chunkX, chunkZ)){
                    this.currentlyGenerating.add(tuple);
                    if (world.provider instanceof WorldProviderHell){
                        //nether
                    }
                    else if(world.provider instanceof WorldProviderEnd){
                        //the End
                    }
                    else{
                        //normal
                        if (handler.doGenerateSurfaceMetropolis(world, random, chunkX, chunkZ)){
                            if (handler.startMapContainsKey(handler.currentStart.toString())){
                                MetropolisStart start = handler.getStartFromKey(handler.currentStart.toString());

                            }
                            //generate city layout
                            //generate building layouts
                        }
                    }
                    this.currentlyGenerating.remove(tuple);
                }
            }

        }
    }

    public static MetropolisGenerationContainer getGenContainerFromWorld(World world){
        MetropolisGenerationContainer handler = null;
        if (!world.isRemote){
            if (!generatorMap.containsKey(world.provider.getDimensionId())){
                handler = new MetropolisGenerationContainer(world);
                generatorMap.put(world.provider.getDimensionId(), handler);
            }
            else{
                handler = generatorMap.get(world.provider.getDimensionId());
            }
        }
        return handler;
    }
}
