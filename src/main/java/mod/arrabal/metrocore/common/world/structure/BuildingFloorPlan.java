package mod.arrabal.metrocore.common.world.structure;

import mod.arrabal.metrocore.common.world.cities.CityLayoutPlan;

/**
 * Created by Arrabal on 10/24/14.
 */
public class BuildingFloorPlan extends CityLayoutPlan {

    public BuildingFloorPlan(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int iFacing, String sType) {
        super(minX, minY, minZ, maxX, maxY, maxZ, iFacing, sType);
    }
}
