package mod.arrabal.metrocore.common.handlers.data;

import mod.arrabal.metrocore.common.library.LogHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

import java.util.ArrayList;

/**
 * Created by Arrabal on 8/16/14.
 */
public class CityBoundsSaveData extends WorldSavedData
{
    private final ArrayList<Integer> xMin;
    private final ArrayList<Integer> xMax;
    private final ArrayList<Integer> zMin;
    private final ArrayList<Integer> zMax;

    public CityBoundsSaveData(String sName){
        super(sName);
        xMin = new ArrayList<Integer>();
        xMax = new ArrayList<Integer>();
        zMin = new ArrayList<Integer>();
        zMax = new ArrayList<Integer>();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        int[] xMinLoad = nbt.getIntArray("xMin");
        LogHelper.info("City locations logged: " + xMinLoad.length);
        if (xMinLoad.length > 0)
        {
            xMin.clear();
            for (int i : xMinLoad)
            {
                xMin.add(i);
            }
        }
        int[] xMaxLoad = nbt.getIntArray("xMax");
        if (xMaxLoad.length > 0)
        {
            xMax.clear();
            for (int i : xMaxLoad)
            {
                xMax.add(i);
            }
        }
        int[] zMinLoad = nbt.getIntArray("zMin");
        if (zMinLoad.length > 0)
        {
            zMin.clear();
            for (int i : zMinLoad)
            {
                zMin.add(i);
            }
        }
        int[] zMaxLoad = nbt.getIntArray("zMax");
        if (zMaxLoad.length > 0)
        {
            zMax.clear();
            for (int i : zMaxLoad)
            {
                zMax.add(i);
            }
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        int[] xMinSave = new int[xMin.size()];
        int[] xMaxSave = new int[xMax.size()];
        int[] zMinSave = new int[zMin.size()];
        int[] zMaxSave = new int[zMax.size()];
        for (int i = 0; i < xMinSave.length; i++)
        {
            xMinSave[i] = xMin.get(i);
            xMaxSave[i] = xMax.get(i);
            zMinSave[i] = zMin.get(i);
            zMaxSave[i] = zMax.get(i);
        }
        nbt.setIntArray("xMin", xMinSave);
        nbt.setIntArray("xMax", xMaxSave);
        nbt.setIntArray("zMin", zMinSave);
        nbt.setIntArray("zMax", zMaxSave);
    }
}
