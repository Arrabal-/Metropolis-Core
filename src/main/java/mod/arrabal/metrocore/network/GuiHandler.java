package mod.arrabal.metrocore.network;

import mod.arrabal.metrocore.client.GUI.GuiMetropolisGuide;
import mod.arrabal.metrocore.common.library.GuiIDs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by Evan on 1/23/2015.
 */
public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        switch(ID){

        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        switch(ID){
            case GuiIDs.IN_GAME_GUIDE : {
                GuiMetropolisGuide guide = new GuiMetropolisGuide();
                return guide;
            }
        }
        return null;
    }
}
