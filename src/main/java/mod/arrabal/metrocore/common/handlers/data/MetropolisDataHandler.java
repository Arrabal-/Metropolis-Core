package mod.arrabal.metrocore.common.handlers.data;

import cpw.mods.fml.common.FMLLog;
import mod.arrabal.metrocore.api.EndOfStreamSignal;
import mod.arrabal.metrocore.common.library.LogHelper;
import mod.arrabal.metrocore.common.world.cities.MetropolisBaseBB;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.storage.ISaveHandler;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Evan on 6/17/2014.
 */
public class MetropolisDataHandler {

    private File urbanDataFile;
    private File urbanDataFileWriting;
    private final String fileName = "MetropolisBlacklist.dat";
    private ConcurrentHashMap<String, MetropolisBaseBB> map;

    public boolean loaded;
    public File saveFolder;


    public MetropolisDataHandler(World world){
        saveFolder = getWorldSaveDir(world);
        urbanDataFile = new File(saveFolder, fileName);
        urbanDataFileWriting = new File(saveFolder, fileName+"_writing");
        loaded = false;
        map = new ConcurrentHashMap<String, MetropolisBaseBB>();
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

    private class LoadThread extends Thread{
        @Override
        public void run(){
            loadBlacklist(urbanDataFile);
        }
    }

    private void loadBlacklist(File file){
        try{
            if (!file.exists()){
                file.createNewFile();
            }
            else{
                ObjectInputStream is = new ObjectInputStream(
                        new FileInputStream(urbanDataFile));
                while (true){
                    Object obj = is.readObject();
                    if (obj instanceof EndOfStreamSignal){
                        break;
                    }
                    MetropolisBaseBB data = (MetropolisBaseBB) obj;
                    map.put(data.coordToString(),data);
                }
                is.close();
            }
        }
        catch(Exception e){
            FMLLog.log(Level.ERROR, "MetropolisCore encountered and error when attempting to load Blacklist data from disk", e);
        }

    }

    private class WriteThread extends Thread{

        private ConcurrentHashMap<String, MetropolisBaseBB> data;

        private WriteThread(ConcurrentHashMap<String, MetropolisBaseBB> map){
            data = map;
        }

        @Override
        public void run(){
            if (urbanDataFileWriting.exists()){
                urbanDataFileWriting.delete();
            }
            EndOfStreamSignal eos = new EndOfStreamSignal();
            try{
                ObjectOutputStream os = new ObjectOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(fileName+"_writing")));
                Iterator iterator = data.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry entry = (Map.Entry) iterator.next();
                    MetropolisBaseBB value = (MetropolisBaseBB) entry.getValue();
                    os.writeObject(value);
                }
                os.writeObject(eos);
                os.close();

                if (urbanDataFile.exists()){
                    urbanDataFile.delete();
                }
                urbanDataFileWriting.renameTo(urbanDataFile);
            }
            catch (IOException e){
                FMLLog.log(Level.ERROR, "MetropolisCore encountered and error when attempting to save Blacklist data to disk", e);
            }
        }
    }

    public void writeBlacklist(ConcurrentHashMap<String, MetropolisBaseBB> map){
        if (map.isEmpty()) {return;}
        new WriteThread(map).start();
    }

    public void readBlacklist(){
        new LoadThread().start();
    }

    public ConcurrentHashMap<String, MetropolisBaseBB> getBlacklistData(){
        return map;
    }
}
