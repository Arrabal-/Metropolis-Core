package mod.arrabal.metrocore.api.tileentity;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Arrabal on 12/30/2015.
 */
public interface IHeatSource {

    boolean isLit();

    boolean hasEmbers();

    void setState(boolean lit, boolean embers, World worldObj, BlockPos pos);
}
