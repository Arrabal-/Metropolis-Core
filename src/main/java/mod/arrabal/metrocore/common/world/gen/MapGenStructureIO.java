package mod.arrabal.metrocore.common.world.gen;

import mod.arrabal.metrocore.common.world.MetropolisBoundingBox;
import mod.arrabal.metrocore.common.world.cities.MetropolisStart;
import mod.arrabal.metrocore.common.world.structure.CityComponentPieces;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Evan on 8/6/2015.
 */
public class MapGenStructureIO {

    public HashMap<String, CityComponentPieces.Metropolis> currentBuildCity;
    public HashMap<String, CityComponentPieces.Building> currentBuildStructures;
    public ConcurrentHashMap<String, MetropolisBoundingBox> urbanGenerationMap = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, MetropolisStart> startMap = new ConcurrentHashMap<>();

    public MapGenStructureIO(){
        currentBuildCity = new HashMap<>();
        currentBuildStructures = new HashMap<>();
        urbanGenerationMap = new ConcurrentHashMap<>();
        startMap = new ConcurrentHashMap<>();
    }

    public void addToBoundingBoxMap(MetropolisBoundingBox urbanArea){
        this.urbanGenerationMap.put(urbanArea.coordToString(),urbanArea);
    }

    public boolean startMapContainsKey(Object key){
        return this.startMap.containsKey(key);
    }

    public MetropolisStart getStartFromKey(Object key){
        return this.startMap.get(key);
    }

    public boolean isGenMapEmpty(){
        return this.urbanGenerationMap.isEmpty();
    }

    public void setGenMap(ConcurrentHashMap<String, MetropolisBoundingBox> boundingBoxMap){
        this.urbanGenerationMap = boundingBoxMap;
    }

    public void addToStartMap(MetropolisStart start) {
        this.startMap.put(start.getStartKey(), start);
    }

    public ConcurrentHashMap<String, MetropolisBoundingBox> getMappedBoundingBoxes(){
        return this.urbanGenerationMap;
    }
}
