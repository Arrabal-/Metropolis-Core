package mod.arrabal.metrocore.common.handlers.data;

import mod.arrabal.metrocore.common.library.ModOptions;
import mod.arrabal.metrocore.common.world.cities.MetropolisBaseBB;
import mod.arrabal.metrocore.common.world.cities.MetropolisStart;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Arrabal on 6/17/2014.
 */
public class MetropolisDataHandler {

    public ConcurrentHashMap<String, MetropolisBaseBB> urbanGenerationMap;
    public ConcurrentHashMap<String, MetropolisStart> startMap;


    public MetropolisDataHandler(){
        this.urbanGenerationMap = new ConcurrentHashMap<String, MetropolisBaseBB>();
        this.startMap = new ConcurrentHashMap<String, MetropolisStart>();
    }

    public boolean ConflictCheck(MetropolisBaseBB boundingBox){
        boolean conflict = false;
        if (!this.urbanGenerationMap.isEmpty()) {
            Iterator iterator = this.urbanGenerationMap.entrySet().iterator();
            while (iterator.hasNext() && !conflict) {
                Map.Entry entry = (Map.Entry) iterator.next();
                MetropolisBaseBB value = (MetropolisBaseBB) entry.getValue();
                conflict = value.intersectsWith(boundingBox) ||
                        value.getSquaredDistance(boundingBox, false) < (ModOptions.metropolisMinDistanceBetween * ModOptions.metropolisMinDistanceBetween);
            }
        }
        return conflict;
    }

    public void addToBoundingBoxMap(MetropolisBaseBB urbanArea){
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

    public void setGenMap(ConcurrentHashMap<String, MetropolisBaseBB> boundingBoxMap){
        this.urbanGenerationMap = boundingBoxMap;
    }

    public void addToStartMap(MetropolisStart start) {
        this.startMap.put(start.getStartKey(), start);
    }

    public ConcurrentHashMap<String, MetropolisBaseBB> getMappedBoundingBoxes(){
        return this.urbanGenerationMap;
    }
}
