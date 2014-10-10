package mod.arrabal.metrocore.common.handlers.data;

import mod.arrabal.metrocore.common.library.LogHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

import java.util.ArrayList;

/**
 * Created by Arrabal on 6/16/2014.
 */

//need to document
public class ChunkGenerationLogger extends WorldSavedData {

    private final ArrayList<Integer> xCoords;
    private final ArrayList<Integer> zCoords;

    public ChunkGenerationLogger(String name){
        super(name);
        xCoords = new ArrayList<Integer>();
        zCoords = new ArrayList<Integer>();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        int[] xload = nbt.getIntArray("xcoords");
        LogHelper.info("Chunks logged: " + xload.length);
        if (xload.length > 0)
        {
            xCoords.clear();
            for (int i : xload)
            {
                xCoords.add(i);
            }
        }
        int[] zload = nbt.getIntArray("zcoords");
        if (zload.length > 0)
        {
            zCoords.clear();
            for (int i : zload)
            {
                zCoords.add(i);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        int[] xsave = new int[xCoords.size()];
        int[] zsave = new int[zCoords.size()];
        for (int i = 0; i < xsave.length; i++)
        {
            xsave[i] = xCoords.get(i);
            zsave[i] = zCoords.get(i);
        }
        nbt.setIntArray("xcoords", xsave);
        nbt.setIntArray("zcoords", zsave);
    }

    public boolean catchChunkBug(int chunkX, int chunkZ)
    {
        for (int i = 0; i < xCoords.size(); i++)
        {
            if (chunkX == xCoords.get(i) && chunkZ == zCoords.get(i))
            {
                try
                {
                    throw new Exception("MetropoisCore caught IWorldGenerator.generate on already generated coordinates, stacktrace:");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return true;
            }
        }

        xCoords.add(chunkX);
        zCoords.add(chunkZ);
        this.markDirty();

        return false;
    }
}
