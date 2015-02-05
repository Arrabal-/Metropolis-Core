package mod.arrabal.metrocore.client.Gui;

import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import mod.arrabal.metrocore.common.handlers.config.ConfigHandler;
import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arrabal on 7/7/2014.
 */
public class MetropolisCoreGuiConfig extends GuiConfig {

    @SuppressWarnings("unchecked")
    private static List<IConfigElement> getConfigElements(){
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.add(new DummyConfigElement.DummyCategoryElement("Mod Mechanics Settings",ModRef.MOD_ID,
                new ConfigElement(ConfigHandler.config.getCategory(ModRef.CATEGORY_MOD_MECHANICS)).getChildElements()));
        list.add(new DummyConfigElement.DummyCategoryElement("City Generation Settings",ModRef.MOD_ID,
                new ConfigElement(ConfigHandler.config.getCategory(ModRef.CATEGORY_CITY_GENERATION)).getChildElements()));
        return list;
    }

    public MetropolisCoreGuiConfig(GuiScreen guiScreen){
        super(guiScreen, getConfigElements(), ModRef.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));

    }
}
