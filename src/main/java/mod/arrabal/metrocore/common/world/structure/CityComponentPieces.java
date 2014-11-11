package mod.arrabal.metrocore.common.world.structure;

import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.world.cities.MetropolisBaseBB;
import mod.arrabal.metrocore.common.world.cities.MetropolisStart;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.*;

/**
 * Created by Arrabal on 6/27/2014.
 *
 * Class container for the various classes that make up the different possible city tiles / structures
 * Each class will either extend the CityComponent class or a common base class that extends the
 * CityComponent class
 */
public class CityComponentPieces {

    public static void registerCityComponents(){

    }

    @SuppressWarnings("unchecked")
    public static List getCityComponentWeightsLists(Random random, MetropolisStart.UrbanType cityClass, boolean buildingList){

        ArrayList arrayList = new ArrayList();
        //TODO:  Add ability to handle max gen radius > 5
        int maxChunks = (ConfigHandler.metropolisMaxGenRadius * 2 + 1) * (ConfigHandler.metropolisMaxGenRadius * 2 + 1);
        if (!buildingList) {
            //Additions to the list need to be in the desired order of evaluation
            //Tiles more likely to spawn in the center of the city should be first
            if (cityClass == MetropolisStart.UrbanType.METROPOLIS) {
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.MunicipalDistrict.class, 10, MathHelper.getRandomIntegerInRange(random, 1, 2)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Monument.class, 12, MathHelper.getRandomIntegerInRange(random, 1, 2)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.CityPlaza.class, 12, MathHelper.getRandomIntegerInRange(random, 1, 2)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Avenue.class, 20, MathHelper.getRandomIntegerInRange(random, 8, 20)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.CityPark.class, 15, MathHelper.getRandomIntegerInRange(random, 2, 6)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.CitySquare.class, 15, MathHelper.getRandomIntegerInRange(random, 1, 5)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.NeighborhoodDistrict.class, 25, MathHelper.getRandomIntegerInRange(random, 3, 24)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.NeighborhoodAlley.class, 20, MathHelper.getRandomIntegerInRange(random, 1, 12)));
                //TODO:  check for new tiles added by other mods and add them to the list
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Alley.class, 25, MathHelper.getRandomIntegerInRange(random, 16, 36)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Street.class, 50, MathHelper.getRandomIntegerInRange(random, 32, 72)));
            } else if (cityClass == MetropolisStart.UrbanType.CITY) {
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.CityPark.class, 10, MathHelper.getRandomIntegerInRange(random, 1, 2)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Avenue.class, 20, MathHelper.getRandomIntegerInRange(random, 8, 16)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.CitySquare.class, 15, MathHelper.getRandomIntegerInRange(random, 1, 5)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.NeighborhoodAlley.class, 20, MathHelper.getRandomIntegerInRange(random, 1, 10)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.NeighborhoodDistrict.class, 15, MathHelper.getRandomIntegerInRange(random, 3, 20)));
                //TODO:  check for new tiles added by other mods and add them to the list
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Alley.class, 16, MathHelper.getRandomIntegerInRange(random, 8, 22)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Street.class, 35, MathHelper.getRandomIntegerInRange(random, 20, 46)));
            } else {
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Avenue.class, 6, MathHelper.getRandomIntegerInRange(random, 8, 12)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.CitySquare.class, 6, MathHelper.getRandomIntegerInRange(random, 1, 3)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.NeighborhoodDistrict.class, 15, MathHelper.getRandomIntegerInRange(random, 1, 7)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.NeighborhoodAlley.class, 7, MathHelper.getRandomIntegerInRange(random, 1, 3)));
                //TODO:  check for new tiles added by other mods and add them to the list
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Alley.class, 7, MathHelper.getRandomIntegerInRange(random, 4, 13)));
                arrayList.add(new CityComponentPieces.CityWeight(CityComponentPieces.Street.class, 15, MathHelper.getRandomIntegerInRange(random, 20, 27)));
            }
            return arrayList;
        }
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

        public boolean canSpawnMoreComponents(){

            return this.cityComponentMax == 0 || this.cityComponentSpawned < this.cityComponentMax;
        }
    }

    private static int getComponentTotalWeightedValue(List componentList){
        boolean flag = false;
        int i = 0;
        CityWeight cityWeight;
        for (Iterator iterator = componentList.iterator(); iterator.hasNext(); i+= cityWeight.cityComponentWeight){
            cityWeight = (CityComponentPieces.CityWeight)iterator.next();
            if (cityWeight.cityComponentMax > 0 && cityWeight.cityComponentSpawned < cityWeight.cityComponentMax){
                flag = true;
            }
        }
        return flag ? i : -1;
    }

    private static CityComponent getCityComponent(CityComponentPieces.Start start, CityComponentPieces.CityWeight cityWeight, Random random, int minX, int minY, int minZ,
            int maxX, int maxY, int maxZ, boolean buildings) {
        Class componentClass = cityWeight.cityComponentClass;
        Object object = null;

        if (componentClass == CityComponentPieces.CitySquare.class){

        }
        else if (componentClass == CityComponentPieces.Street.class){

        }
        else if (componentClass == CityComponentPieces.NeighborhoodAlley.class){

        }
        else if (componentClass == CityComponentPieces.Monument.class){

        }
        else if (componentClass == CityComponentPieces.Alley.class){

        }
        else if (componentClass == CityComponentPieces.CityPark.class){

        }
        else if (componentClass == CityComponentPieces.MunicipalDistrict.class){

        }
        else if (componentClass == CityComponentPieces.Avenue.class){

        }
        else if (componentClass == CityComponentPieces.CityPlaza.class){

        }
        else if (componentClass == CityComponentPieces.NeighborhoodDistrict.class){

        }
        else if (componentClass == CityComponentPieces.Tower.class){

        }
        else if (componentClass == CityComponentPieces.Industrial.class){

        }
        else if (componentClass == CityComponentPieces.Institution.class){

        }
        else if (componentClass == CityComponentPieces.Residential.class){

        }
        else if (componentClass == CityComponentPieces.nFlat.class){

        }
        else if (componentClass == CityComponentPieces.Prison.class){

        }
        else if (componentClass == CityComponentPieces.Municipal.class){

        }
        return (CityComponent)object;
    }

    private static CityComponent getNextValidComponent(CityComponentPieces.Start start, Random random, int chunkX, int chunkZ, int minY, int maxY, boolean buildings){
        int totalWeights = buildings ? getComponentTotalWeightedValue(start.weightedBuildingList) : getComponentTotalWeightedValue(start.weightedCityComponentList);
        int threshold = random.nextInt(totalWeights);
        int listSize;
        int listThreshold = buildings ? 1 : 4;
        int i = 0;

        Iterator iterator = buildings ? start.weightedBuildingList.iterator() : start.weightedCityComponentList.iterator();

        while (iterator.hasNext()){
            listSize = buildings ? start.weightedBuildingList.size() : start.weightedCityComponentList.size();
            CityComponentPieces.CityWeight cityWeight = (CityComponentPieces.CityWeight)iterator.next();
            if (threshold < cityWeight.cityComponentWeight + i){
                if (!cityWeight.canSpawnMoreComponents() || cityWeight == start.cityWeight && listSize > listThreshold){
                    i+=cityWeight.cityComponentWeight;
                    continue;
                }
                //TODO: add arguments to method call
                CityComponent cityComponent = getCityComponent(start, cityWeight, random, (chunkX << 4), minY, (chunkZ << 4),
                        (chunkX << 4) + 15, maxY, (chunkZ << 4) + 15, buildings);

                if (cityComponent != null){
                    ++cityWeight.cityComponentSpawned;
                    start.cityWeight = cityWeight;

                    if (!cityWeight.canSpawnMoreComponents()){
                        if (buildings){
                            start.weightedBuildingList.remove(cityWeight);
                        }
                        else {
                            if (cityWeight.cityComponentClass == CityComponentPieces.Street.class){
                                cityWeight.cityComponentMax = 0;
                            }
                            else start.weightedCityComponentList.remove(cityWeight);
                        }
                    }
                    return cityComponent;
                }
            }
            i+=cityWeight.cityComponentWeight;
        }

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

        protected CitySquare(int tileTypeID, int tileVariant, CityComponentPieces.Start start, Random random) {
            super(tileTypeID, start);
        }

        protected CitySquare(int tileTypeID, int tileVairant, int chunkX, int chunkZ, int baseY, CityComponentPieces.Start start, Random random){
            super(tileTypeID, start);
            this.boundingBox = new MetropolisCityPlan(chunkX << 4, 1, chunkZ << 4, (chunkX << 4) + 15, baseY, (chunkZ << 4) + 15,
                    random.nextInt(4), "OriginSquare");
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Start extends CityComponentPieces.CitySquare {

        public boolean isRuins;
        public CityComponentPieces.CityWeight cityWeight;
        public HashMap<String, CityComponentPieces.Metropolis> cityComponentMap;
        public HashMap<String, CityComponentPieces.Building> buildingMap;
        public List weightedCityComponentList;
        public List weightedBuildingList;

        public MetropolisStart.UrbanType citySize;
        public MetropolisStart.RoadGrid roadGrid;
        public MetropolisBaseBB maxSize;
        public MetropolisCityPlan cityPlan;

        public int baseY;

        public Start() {}

        public Start(int tileTypeID, int tileVariant, Random random, int chunkX, int chunkZ, int baseY, List cityList, List buildingList) {
            super(0, 0, chunkX, chunkZ, baseY, null, random);
            this.weightedCityComponentList = cityList;
            this.weightedBuildingList = buildingList;
            this.baseY = baseY;
            this.cityPlan = new MetropolisCityPlan(chunkX << 4, 1, chunkZ << 4, (chunkX << 4) + 15, baseY, (chunkZ << 4) + 15,
                    random.nextInt(4), "UrbanLayout");
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

        protected Street(int tileTypeID, int tileVariant, CityComponentPieces.Start start){
            super(tileTypeID, start);
        }

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class CityPlaza extends CityComponentPieces.CitySquare {

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

        protected NeighborhoodDistrict(int tileTypeID, int tileVariant, CityComponentPieces.Start start) {
            super(tileTypeID, tileVariant, start);
            maxBuildingLevels = 3;
            minBuildingLevels = 1;
        }

    }

    public static class NeighborhoodAlley extends CityComponentPieces.NeighborhoodDistrict {

        public NeighborhoodAlley() {}

        protected NeighborhoodAlley(int tileTypeID, int tileVariant, CityComponentPieces.Start start) {
            super(tileTypeID, tileVariant, start);
        }
    }

    public static class MunicipalDistrict extends CityComponentPieces.CityPlaza {

    }

    public abstract static class Building extends CityComponent {

        protected int totalLevels;
        protected int orientation;
        protected CityComponentPieces.Start startPiece;
        private ArrayList floorPlan;

        public Building() {}

        protected Building(int tileTypeID, int variant, int stories, int facing){
            super(tileTypeID);
            this.totalLevels = stories;
            this.orientation = facing;
            this.floorPlan = new ArrayList();

        }

        protected CityComponent getNextBuldingLevel() {

            return null;
        }
    }

    public static class Tower extends CityComponentPieces.Building {

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Institution extends CityComponentPieces.Building {

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Industrial extends CityComponentPieces.Building {

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class Residential extends CityComponentPieces.Building {

        @Override
        public boolean addComponentParts(World world, Random random, MetropolisBaseBB boundingBox) {
            return false;
        }
    }

    public static class nFlat extends CityComponentPieces.Residential {

    }

    public static class Prison extends CityComponentPieces.Tower {

    }

    public static class Municipal extends CityComponentPieces.Institution {

    }
}
