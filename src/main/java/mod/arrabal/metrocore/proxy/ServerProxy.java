package mod.arrabal.metrocore.proxy;

/**
 * Created by Arrabal on 6/26/2014.
 */
public class ServerProxy extends CommonProxy {

    @Override
    public void registerRenderers() {
        // nothing as the server doesn't render anything
    }

    @Override
    public void registerSounds() {
        // nothing as the server doesn't play sounds
    }
}
