package nl.mentaalachtergesteld.cncomb.event;

import net.minecraft.resources.ResourceLocation;
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
        // Multiply random numbers by 2000 to get % per second.

        event.player.getCapability(NicotineLevelProvider.NICOTINE_LEVEL_CAP).ifPresent(nicotineLevel -> {
            if(nicotineLevel.getNicotineLevel() > 0 && event.player.getRandom().nextFloat() < 0.01f) {
                nicotineLevel.subtractNicotineLevel(1);
            }
        });

        event.player.getCapability(NicotineAddictionProvider.NICOTINE_ADDICTION_CAP).ifPresent(nicotineAddiction -> {
            event.player.getCapability(NicotineLevelProvider.NICOTINE_LEVEL_CAP).ifPresent(nicotineLevel -> {
                if(nicotineLevel.getNicotineLevel() > 0) {
                    if(event.player.getRandom().nextFloat() < 0.01) {
                        nicotineAddiction.addNicotineLevel(1);
                    }
                }
            });

            if(nicotineAddiction.getNicotineAddiction() > 0 && event.player.getRandom().nextFloat() < 0.001f) {
                nicotineAddiction.subtractNicotineLevel(1);
            }
        });

        event.player.getCapability(WithdrawalProvider.WITHDRAWAL_CAP).ifPresent(withdrawal -> {
            event.player.getCapability(NicotineLevelProvider.NICOTINE_LEVEL_CAP).ifPresent(nicotineLevel -> {
                event.player.getCapability(NicotineAddictionProvider.NICOTINE_ADDICTION_CAP).ifPresent(nicotineAddiction -> {
                    if (nicotineAddiction.getNicotineAddiction() <= 0) return;
                    if (nicotineLevel.getNicotineLevel() < nicotineAddiction.getNicotineAddiction() / 4) {
                        if(event.player.getRandom().nextFloat() < (float)nicotineAddiction.getNicotineAddiction()/2000.f) {
                            withdrawal.addWithdrawal(1);
                        }
                    }
                });
            });
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
