package mod.arrabal.metrocore.common.world.structure;

import mod.arrabal.metrocore.common.world.cities.MetropolisBaseBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Arrabal on 6/27/2014.
 *
 * Class container for the various classes that make up the different possible city tiles / structures
 * Each class will either extend the CityComponent class or a common base class that extends the
 * CityComponent class
 */
public class CityComponentPieces {

    private static ArrayList cityTiles;
    private static ArrayList buildingComponents;

    public static void registerCityComponents(){

    }

    public static List getCityComponentWeightsLists(Random random, boolean buildingList){

        ArrayList arrayList = new ArrayList();
        // add default tiles to the list
        // check for new tiles added by other mods and add them to the list
        return null;
    }

    public static class CityWeight{

        public Class cityComponentClass;
        public final int cityComponentWeight;
        public int cityComponentSpawned;
        public int cityComponentMax;

        public CityWeight(Class cityClass, int weight, int maxComponents){
            this.cityComponentClass = cityClass;
            this.cityComponentWeight = weight;
            this.cityComponentMax = maxComponents;
        }

        public boolean canSpawnMoreTiles(){

            return this.cityComponentMax == 0 || this.cityComponentSpawned < this.cityComponentMax;
        }
    }

    private static CityComponent getNextValidCityComponent(){

        return null;
    }

    public abstract static class Metropolis extends CityComponent {

        protected int maxBuildingLevels;
        protected int minBuildingLevels;
        protected int orientation;
        protected CityComponentPieces.Start startPiece;


        public Metropolis() {}

        protected Metropolis(int tileTypeID, CityComponentPieces.Start start){
            super(tileTypeID);
            startPiece = start;
        }

        protected CityComponent getNextCityComponent() {

            return null;
        }

        protected CityComponent getNextCityComponentX() {

            return null;
        }

        protected CityComponent getNextCityComponentZ() {

            return null;
        }
    }

    public static class CitySquare extends CityComponentPieces.Metropolis {

        public CitySquare() {}

        protected CitySquare(int tileTypeID, CityComponentPieces.Start start, Random random, int chunkX, int chunkZ) {
            super(tileTypeID, start);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Start extends CityComponentPieces.CitySquare {

        public boolean isRuins;
        public CityComponentPieces.CityWeight cityWeight;
        public List weightedCityComponentList;
        public List weightedBuildingList;

        public Start() {}

        public Start(int tileTypeID, CityComponentPieces.Start cityStart, Random random, int chunkX, int chunkZ, List cityList, List buildingList) {
            super(0, null, random, chunkX, chunkZ);
        }

    }

    public static class CityPark extends CityComponentPieces.Metropolis {

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Street extends CityComponentPieces.Metropolis {

        public Street() {}

        protected Street(int tileTypeID, CityComponentPieces.Start start){
            super(tileTypeID, start);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class CityPlaza extends CityComponentPieces.Metropolis {

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Avenue extends CityComponentPieces.Street {

    }

    public static class Alley extends CityComponentPieces.Street {

    }

    public static class Monument extends CityComponentPieces.CityPlaza {

    }

    public static class NeighborhoodDistrict extends CityComponentPieces.Street {

        public NeighborhoodDistrict() {}

        protected NeighborhoodDistrict(int tileTypeID, CityComponentPieces.Start start) {
            super(tileTypeID, start);
            maxBuildingLevels = 3;
            minBuildingLevels = 1;
        }

    }

    public static class MunicipalDistrict extends CityComponentPieces.CityPlaza {

    }

    public abstract static class Building extends CityComponent {

        protected int totalLevels;
        protected int orientation;
        protected CityComponentPieces.Start startPiece;

        public Building() {}

        protected Building(int tileTypeID, int stories, int facing){
            super(tileTypeID);
            totalLevels = stories;
            orientation = facing;
        }

        protected CityComponent getNextBuldingLevel() {

            return null;
        }
    }

}
