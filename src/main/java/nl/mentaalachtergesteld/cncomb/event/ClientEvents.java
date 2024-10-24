package nl.mentaalachtergesteld.cncomb.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import nl.mentaalachtergesteld.cncomb.CNCOMB;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = CNCOMB.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
    }

    @Mod.EventBusSubscriber(modid = CNCOMB.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
    }
}
