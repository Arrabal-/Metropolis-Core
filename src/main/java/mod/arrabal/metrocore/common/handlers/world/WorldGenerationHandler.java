package mod.arrabal.metrocore.common.handlers.world;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IWorldGenerator;
import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.handlers.data.MetropolisDataHandler;
import mod.arrabal.metrocore.common.library.LogHelper;
import mod.arrabal.metrocore.common.world.ChunkGenerationLogger;
import mod.arrabal.metrocore.common.world.cities.Metropolis;
import mod.arrabal.metrocore.common.world.cities.MetropolisBaseBB;
import mod.arrabal.metrocore.common.world.cities.MetropolisStart;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.chunk.IChunkProvider;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.storage.ISaveHandler;
import org.apache.logging.log4j.Level;


/**
 * Created by Arrabal on 6/6/2014.
 */
public class WorldGenerationHandler implements IWorldGenerator {

    private ConcurrentLinkedQueue<int[]> currentlyGenerating;
    private ConcurrentHashMap<Integer, GenerationHandle> generatorMap;
    private static ConcurrentHashMap<String, MetropolisBaseBB> urbanGenerationMap;
    public static ConcurrentHashMap<String, MetropolisStart> cityStartMap;

    public WorldGenerationHandler() {
        currentlyGenerating = new ConcurrentLinkedQueue<int[]>();
        generatorMap = new ConcurrentHashMap<Integer, GenerationHandle>();
        urbanGenerationMap = new ConcurrentHashMap<String, MetropolisBaseBB>();
        cityStartMap = new ConcurrentHashMap<String, MetropolisStart>();
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
                        FMLLog.log(Level.ERROR, "MetropolisCore failed trying to locate world save directory", e);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

        if (world.isRemote){return;}
        if (ConfigHandler.enableCityCreation){
            int[] tuple = {chunkX,chunkZ};
            if (currentlyGenerating.contains(tuple)){
                FMLCommonHandler.instance().getFMLLogger().log(Level.INFO, "MetropolisCore caught recursive generator call at [" + chunkX + "' " + chunkZ + "]");
            }
            else{
                if (!getGenerationHandle(world).chunkLogger.catchChunkBug(chunkX, chunkZ)){
                    currentlyGenerating.add(tuple);
                    if (world.provider instanceof WorldProviderHell){
                        //nether
                    }
                    else{
                        //normal
                       generateSurfaceMetropolis(world, random, chunkX, chunkZ, chunkGenerator, chunkProvider);
                    }
                    currentlyGenerating.remove(tuple);
                }
            }

        }
    }

    private void generateSurfaceMetropolis(World world, Random random, int chunkX, int chunkZ, IChunkProvider chunkGenerator, IChunkProvider chunkProvider){
        GenerationHandle handle = getGenerationHandle(world);
        if (handle.generator != null){
            if (urbanGenerationMap.isEmpty()){
                urbanGenerationMap = handle.dataHandler.getBlacklistData();
                if (urbanGenerationMap.isEmpty()){
                    handle.generator.spawnPointBlock = handle.generator.doBlockSpawnArea(world);
                    urbanGenerationMap.put(handle.generator.spawnPointBlock.coordToString(), handle.generator.spawnPointBlock);
                }

            }
            handle.generator.generateMetropolis(random, chunkX, chunkZ, world, handle.dataHandler);
            //handle.dataHandler.writeBlacklist(urbanGenerationMap);
        }
    }

    private class GenerationHandle{
        MetropolisDataHandler dataHandler;
        Metropolis generator;
        ChunkGenerationLogger chunkLogger;
    }

    private GenerationHandle getGenerationHandle(World world){
        GenerationHandle handle = null;
        if (!world.isRemote){
            if (!generatorMap.containsKey(world.provider.dimensionId)){
                handle = new GenerationHandle();
                initGenerationHandle(handle, world);
                generatorMap.put(world.provider.dimensionId, handle);
            }
            else{
                handle = generatorMap.get(world.provider.dimensionId);
            }
        }
        return handle;
    }

    private void initGenerationHandle(GenerationHandle handle, World world){
        try{
            handle.dataHandler = new MetropolisDataHandler(world);
            handle.generator = new Metropolis();
            if (urbanGenerationMap.isEmpty()){
                handle.dataHandler.readBlacklist();
            }
            handle.chunkLogger = (ChunkGenerationLogger) world.perWorldStorage.loadData(ChunkGenerationLogger.class, "metropolisChunkLogger");
            if (handle.chunkLogger == null){
                handle.chunkLogger = new ChunkGenerationLogger("metropolisChunkLogger");
                world.perWorldStorage.setData("metropolisChunkLogger", handle.chunkLogger);
            }
        }
        catch (Exception e){
            FMLLog.log(Level.ERROR, "There was a problem loading the MetropolisCore mod", e);
        }
    }

    private static boolean doConflictCheck(World world, MetropolisBaseBB layout){
        if (urbanGenerationMap.isEmpty()) {return false;}
        boolean conflict = false;
        Iterator iterator = urbanGenerationMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            MetropolisBaseBB value = (MetropolisBaseBB) entry.getValue();
            conflict = value.intersectsWith(layout) ||
            value.getSquaredDistance(layout, true) < (ConfigHandler.metropolisMinDistanceBetween * ConfigHandler.metropolisMinDistanceBetween);
            if (conflict) {break;}
        }
        return conflict;
    }

    private static boolean doConflictCheck(int posX, int posY, int posZ){
        if (urbanGenerationMap.isEmpty()) {return false;}
        boolean conflict = false;
        Iterator iterator = urbanGenerationMap.entrySet().iterator();
        while (iterator.hasNext() || !conflict){
            Map.Entry entry = (Map.Entry) iterator.next();
            MetropolisBaseBB value = (MetropolisBaseBB) entry.getValue();
            conflict = value.isVecInside(posX, posY, posZ);
        }
        return conflict;
    }

    public static boolean checkUrbanGenerationConflict(World world, MetropolisBaseBB layout, MetropolisDataHandler generationData){
        if (urbanGenerationMap.isEmpty()){
            urbanGenerationMap = generationData.getBlacklistData();
        }
        return doConflictCheck(world, layout);
    }

    public static void addToBlacklistMap(MetropolisBaseBB urbanArea){
        urbanGenerationMap.put(urbanArea.coordToString(),urbanArea);
    }

    public ConcurrentHashMap<String, MetropolisBaseBB> getUrbanGenerationMap(){
        return urbanGenerationMap;
    }

    public static void clearUrbanGenerationMap(){ urbanGenerationMap.clear();}

    public static boolean doCityGeneration(World world, int chunkX, int chunkZ){
        ChunkCoordIntPair genCoords = new ChunkCoordIntPair(chunkX, chunkZ);
        if (cityStartMap.containsKey(genCoords.toString())){
            MetropolisStart start = cityStartMap.get(genCoords.toString());
            if (!start.getCurrentlyBuilding()){
                return start.generate(world);
            }
        }
        MetropolisStart start = null;
        boolean validGenChunk = false;
        Iterator iterator = cityStartMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            start = (MetropolisStart) entry.getValue();
            if (start.getMaxBBIntersection(chunkX * 16, chunkZ * 16, chunkX * 16 + 15, chunkZ * 16 + 15)){
                validGenChunk = true;
                break;
            }
        }
        if (validGenChunk) { return start.generate(world, chunkX, chunkZ); }
        return false;
    }

}
