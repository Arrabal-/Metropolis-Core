package mod.arrabal.metrocore.common.library;

/**
 * Created by Arrabal on 12/19/13.
 */
public class ModRef {
    // Mod hierarchy constants
    public static final String MOD_ID = "metrocore";
    public static final String MOD_NAME = "Metropolis_Core";
    public static final String VERSION = "1.8.0-0.0.1a";
    public static final String CHANNEL = MOD_ID;
    public static final String PROXY_STEM = "mod.arrabal.metrocore.proxy";
    public static final String CLIENT_PROXY_LOCATION = PROXY_STEM + ".ClientProxy";
    public static final String COMMON_PROXY_LOCATION = PROXY_STEM + ".CommonProxy";
    public static final String SERVER_PROXY_LOCATION = PROXY_STEM + ".ServerProxy";
    public static final String GUI_FACTORY_CLASS = "mod.arrabal.metrocore.client.Gui.GuiFactory";
    public static final String CATEGORY_MOD_MECHANICS = "mod_mechanics";
    public static final String CATEGORY_CITY_GENERATION = "city_generation";
    public static final String GUI_TEXTURE_LOCATION = MOD_ID + ":" + "textures/gui/";
    public static final String GUIDE_GUI_TEXTURE = GUI_TEXTURE_LOCATION + "guiDataTablet.png";
    public static final String GUIDE_GUI_TEXTURE_ON = GUI_TEXTURE_LOCATION + "guiDataTablet_on.png";

    // World generation constants
    public static final int CITY_SQUARE_ID = 0;
    public static final int STREET_ID = 1;
    public static final int ALLEY_ID = 2;
    public static final int NEIGHBORHOOD_ID = 3;
    public static final int N_ALLEY_ID = 4;
    public static final int AVENUE_ID = 5;
    public static final int CITY_PARK_ID = 6;
    public static final int CITY_PLAZA_ID = 7;
    public static final int MONUMENT_ID = 8;
    public static final int MUNICIPAL_DIST_ID = 9;
    public static final int TOWER_ID = 100;
    public static final int INDUSTRIAL_ID = 101;
    public static final int INSTITUTION_ID = 102;
    public static final int RESIDENTIAL_ID = 103;
    public static final int FLAT_ID = 104;
    public static final int PRISON_ID = 105;
    public static final int MUNICIPAL_BUILDING_ID = 106;

}
