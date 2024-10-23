package nl.mentaalachtergesteld.cncomb.event;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import nl.mentaalachtergesteld.cncomb.CNCOMB;
import nl.mentaalachtergesteld.cncomb.capability.NicotineLevelProvider;

@Mod.EventBusSubscriber(modid = CNCOMB.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(CNCOMB.MODID, "nicotine_level"), new NicotineLevelProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(NicotineLevelProvider.NICOTINE_LEVEL_CAP).ifPresent(oldStore -> {
                event.getEntity().getCapability(NicotineLevelProvider.NICOTINE_LEVEL_CAP).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().invalidateCaps();
        }
    }

    public static void onPlayerServerTick(TickEvent.PlayerTickEvent event) {
        event.player.getCapability(NicotineLevelProvider.NICOTINE_LEVEL_CAP).ifPresent(nicotineLevel -> {
            if(nicotineLevel.getNicotineLevel() > 0 && event.player.getRandom().nextFloat() < 0.005f) {
                nicotineLevel.subtractNicotineLevel(1);
            }
        });
    }

    public static void onPlayerClientTick(TickEvent.PlayerTickEvent event) {

    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.side == LogicalSide.SERVER) {
            onPlayerServerTick(event);
        } else {
            onPlayerClientTick(event);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
    }
}
