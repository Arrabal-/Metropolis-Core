package mod.arrabal.metrocore.common.handlers.data;

import mod.arrabal.metrocore.common.library.LogHelper;
import mod.arrabal.metrocore.common.world.MetropolisBoundingBox;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Arrabal on 8/16/14.
 */
public class CityBoundsSaveData extends WorldSavedData
{
    private final ArrayList<Integer> xMin;
    private final ArrayList<Integer> xMax;
    private final ArrayList<Integer> zMin;
    private final ArrayList<Integer> zMax;
    private final ArrayList<Integer> xCenter;
    private final ArrayList<Integer> zCenter;

    public CityBoundsSaveData(String sName){
        super(sName);
        xMin = new ArrayList<Integer>();
        xMax = new ArrayList<Integer>();
        zMin = new ArrayList<Integer>();
        zMax = new ArrayList<Integer>();
        xCenter = new ArrayList<Integer>();
        zCenter = new ArrayList<Integer>();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        int[] xMinLoad = nbt.getIntArray("xMin");
        LogHelper.info("City locations logged: " + xMinLoad.length);
        if (xMinLoad.length > 0) {
            xMin.clear();
            for (int i : xMinLoad) {
                xMin.add(i);
            }
        }
        int[] xMaxLoad = nbt.getIntArray("xMax");
        if (xMaxLoad.length > 0) {
            xMax.clear();
            for (int i : xMaxLoad) {
                xMax.add(i);
            }
        }
        int[] zMinLoad = nbt.getIntArray("zMin");
        if (zMinLoad.length > 0) {
            zMin.clear();
            for (int i : zMinLoad) {
                zMin.add(i);
            }
        }
        int[] zMaxLoad = nbt.getIntArray("zMax");
        if (zMaxLoad.length > 0) {
            zMax.clear();
            for (int i : zMaxLoad) {
                zMax.add(i);
            }
        }
        int[] xCenterLoad = nbt.getIntArray("xCenter");
        if (xCenterLoad.length > 0) {
            xCenter.clear();
            for (int i : xCenterLoad) {
                xCenter.add(i);
            }
        }
        int[] zCenterLoad = nbt.getIntArray("zCenter");
        if (zCenterLoad.length > 0) {
            zCenter.clear();
            for (int i : zCenterLoad) {
                zCenter.add(i);
            }
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        int[] xMinSave = new int[xMin.size()];
        int[] xMaxSave = new int[xMax.size()];
        int[] zMinSave = new int[zMin.size()];
        int[] zMaxSave = new int[zMax.size()];
        int[] xCenterSave = new int[xCenter.size()];
        int[] zCenterSave = new int[zCenter.size()];
        for (int i = 0; i < xMinSave.length; i++) {
            xMinSave[i] = xMin.get(i);
            xMaxSave[i] = xMax.get(i);
            zMinSave[i] = zMin.get(i);
            zMaxSave[i] = zMax.get(i);
            xCenterSave[i] = xCenter.get(i);
            zCenterSave[i] = zCenter.get(i);
        }
        nbt.setIntArray("xMin", xMinSave);
        nbt.setIntArray("xMax", xMaxSave);
        nbt.setIntArray("zMin", zMinSave);
        nbt.setIntArray("zMax", zMaxSave);
        nbt.setIntArray("xCenter", xCenterSave);
        nbt.setIntArray("zCenter", zCenterSave);
    }

    public void saveBoundingBoxData(MetropolisBoundingBox mBB){
        xMin.add(mBB.minBlockCoords.getX());
        xMax.add(mBB.maxBlockCoords.getX());
        zMin.add(mBB.minBlockCoords.getZ());
        zMax.add(mBB.maxBlockCoords.getZ());
        xCenter.add(mBB.getCenterX());
        zCenter.add(mBB.getCenterZ());
        this.markDirty();
    }

    public ConcurrentHashMap<String, MetropolisBoundingBox> getBoundingBoxMap(){
        ConcurrentHashMap<String, MetropolisBoundingBox> map = new ConcurrentHashMap<String, MetropolisBoundingBox>();
        int size = this.xMin.size();
        for (int i = 0; i < size; i++){
            MetropolisBoundingBox boundingBox = new MetropolisBoundingBox(xMin.get(i), zMin.get(i), xMax.get(i), zMax.get(i), "Citybounds");
            String mapKey = "[" + xCenter.get(i) + ", " + zCenter.get(i) + "]";
            map.put(mapKey, boundingBox);
        }
        return map;
    }

}
