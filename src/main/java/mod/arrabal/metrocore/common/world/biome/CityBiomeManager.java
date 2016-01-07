package mod.arrabal.metrocore.common.world.biome;

import mod.arrabal.metrocore.common.init.Biomes;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arrabal on 12/9/2015.
 */
public class CityBiomeManager {

    private static List<BiomeGenBase> invalidBiomesForGeneration;
    private static List<BiomeGenBase> cityBiomes;

    public static void init(){
        CityBiomeManager.cityBiomes = new ArrayList();
        CityBiomeManager.cityBiomes.add(Biomes.plainsMetro);
        BiomeGenBase[] biomeList = BiomeGenBase.getBiomeGenArray();
        CityBiomeManager.invalidBiomesForGeneration = new ArrayList();
        for (int i = 0; i < biomeList.length; i ++){
            if (biomeList[i]!= null && !CityBiomeManager.cityBiomes.contains(biomeList[i])){
                CityBiomeManager.invalidBiomesForGeneration.add(biomeList[i]);
            }
        }
    }

    public static List getInvalidBiomes(){
        return CityBiomeManager.invalidBiomesForGeneration;
    }

    public static List getCityBiomes() {
        return CityBiomeManager.cityBiomes;
    }
}
