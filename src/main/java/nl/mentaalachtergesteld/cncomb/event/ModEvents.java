package nl.mentaalachtergesteld.cncomb.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import nl.mentaalachtergesteld.cncomb.CNCOMB;
import nl.mentaalachtergesteld.cncomb.capability.*;
import nl.mentaalachtergesteld.cncomb.commands.SetNicotineCommand;
import nl.mentaalachtergesteld.cncomb.util.NicotineHandler;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = CNCOMB.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new SetNicotineCommand(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(CNCOMB.MODID, "nicotine_addiction"), new NicotineAddictionProvider());
            event.addCapability(new ResourceLocation(CNCOMB.MODID, "nicotine_level"), new NicotineLevelProvider());
            event.addCapability(new ResourceLocation(CNCOMB.MODID, "withdrawal"), new WithdrawalProvider());
        }
    }

    private static <T extends ICopyCapability<T>> void copyPlayerCapability(PlayerEvent.Clone event, Capability<T> capability) {
        event.getOriginal().getCapability(capability).ifPresent(oldStore -> {
            event.getEntity().getCapability(capability).ifPresent(newStore -> {
                newStore.copyFrom(oldStore);
            });
        });
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().reviveCaps();

            copyPlayerCapability(event, NicotineLevelProvider.NICOTINE_LEVEL_CAP);
            copyPlayerCapability(event, NicotineAddictionProvider.NICOTINE_ADDICTION_CAP);
            copyPlayerCapability(event, WithdrawalProvider.WITHDRAWAL_CAP);

            event.getOriginal().invalidateCaps();
        }
    }

    public static void onPlayerServerTick(TickEvent.PlayerTickEvent event) {
        NicotineHandler.handleNicotineEffects((ServerPlayer) event.player);
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
