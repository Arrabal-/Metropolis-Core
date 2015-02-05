package mod.arrabal.metrocore.client.GUI;

import mod.arrabal.metrocore.common.library.ModRef;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Evan on 1/23/2015.
 */
public class GuiMetropolisGuide extends GuiScreen {

    int guiWidth = 192;
    int guiHeight = 240;
    int left, top;

    public float gameTime = 0F;
    public float partailTicks = 0F;
    public float timeChange = 0F;

    public String playerName;

    public GuiMetropolisGuide(){

    }

    @Override
    public void initGui(){
        super.initGui();



    }

    private static final ResourceLocation guiTextureOff = new ResourceLocation(ModRef.GUIDE_GUI_TEXTURE);
    private static final ResourceLocation guiTextureOn = new ResourceLocation(ModRef.GUIDE_GUI_TEXTURE_ON);

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.renderEngine.bindTexture(guiTextureOn);
        this.left = (this.width - this.guiWidth) / 2;
        this.top = (this.height - this.guiHeight) / 2;
        drawTexturedModalRect(left, top, 0,0, guiWidth, guiHeight);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
