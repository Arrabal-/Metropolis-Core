package mod.arrabal.metrocore.common.world.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Arrabal on 6/27/2014.
 */
public class CityComponentPieces {

    public static void registerCityTiles(){

    }

    public static List getCityTileWeightsList(Random random){

        ArrayList arrayList = new ArrayList();
        // add default tiles to the list
        // check for new tiles added by other mods and add them to the list
        return null;
    }

    public static class CityTileWeight{

        public Class cityTileClass;
        public final int cityTileWeight;
        public int cityTilesSpawned;
        public int cityTilesMax;

        public CityTileWeight(Class cityTileClass, int weight, int maxTiles){
            this.cityTileClass = cityTileClass;
            this.cityTileWeight = weight;
            this.cityTilesMax = maxTiles;
        }

        public boolean canSpawnMoreTiles(){

            return false;
        }
    }
}
