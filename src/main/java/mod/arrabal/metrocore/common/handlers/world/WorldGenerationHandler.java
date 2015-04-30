package mod.arrabal.metrocore.common.handlers.world;

import mod.arrabal.metrocore.common.world.structure.CityComponent;
import net.minecraft.util.BlockPos;
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

/*        if (!world.isRemote && ConfigHandler.enableCityCreation){
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
                                String hashKey = start.getStartKey();
                                start.cityLayoutStart.cityComponentMap.put(hashKey,start.cityLayoutStart);
                                start.cityLayoutStart.buildComponent(start.cityLayoutStart, random);
                                int radiusIterations = Math.max(start.getMaxGenRadius(true), start.getMaxGenRadius(false));
                                CityComponent cityComponent;
                                LogHelper.debug("Starting build city map with start at " + hashKey);
                                LogHelper.debug("Max gen radius (x,z): " + start.getMaxGenRadius(true) + " " + start.getMaxGenRadius(false));
                                LogHelper.debug("BaseY: " + start.getBaseY());
                                LogHelper.debug("City Class: " + start.cityLayoutStart.citySize + " " + "Road Grid: " + start.cityLayoutStart.roadGrid);
                                for (int iteration = 2; iteration <= radiusIterations; iteration++) {
                                    for (int buildX = -iteration; buildX <= iteration; buildX++){
                                        for (int buildZ = -iteration; buildZ <= iteration; buildZ++){
                                            if ((Math.abs(buildX) < iteration && Math.abs(buildZ) < iteration) || (Math.abs(buildX) > start.getMaxGenRadius(true)) || (Math.abs(buildZ) > start.getMaxGenRadius(false))) continue;
                                            int worldX, worldZ;
                                            worldX = start.getStartX() + buildX;
                                            worldZ = start.getStartZ() + buildZ;
                                            String newChunkKey = "[" + worldX + ", " + worldZ + "]";
                                            if (start.cityLayoutStart.cityComponentMap.containsKey(newChunkKey)) {
                                                cityComponent = start.cityLayoutStart.cityComponentMap.get(newChunkKey);
                                                cityComponent.buildComponent(start.cityLayoutStart, random);
                                            }
                                            else{
                                                // found empty city tile.  Need to generate new tile.  Should take place after chained generation of tiles
                                                LogHelper.debug("Found empty city tile during component build at " + newChunkKey);
                                                start.cityLayoutStart.buildComponent(start.cityLayoutStart, random, buildX, buildZ);
                                            }
                                        }
                                    }
                                }
                                LogHelper.info("Completed building city layout with start at " + start.getStartKey());
                                handler.addToGenerationMap(start.cityLayoutStart.cityPlan);
                            }
                            if(handler.doBuildMetropolis(world, random, handler.currentStart.chunkXPos, handler.currentStart.chunkZPos)){
                                LogHelper.info("Succeeded in building all city component parts");
                            } else LogHelper.info("Failed building one or more city component parts");
                            //generate city layout
                            //generate building layouts
                        }
                    }
                    this.currentlyGenerating.remove(tuple);
                }
            }

        }*/
    }

/*    public static MetropolisGenerationContainer getGenContainerFromWorld(World world){
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
    }*/
}
