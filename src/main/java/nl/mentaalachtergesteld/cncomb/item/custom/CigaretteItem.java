package nl.mentaalachtergesteld.cncomb.item.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import nl.mentaalachtergesteld.cncomb.capability.NicotineLevelProvider;
import org.jetbrains.annotations.NotNull;

public class CigaretteItem extends Item {

    private static final float nicotinePerTick = 0.25f;

    public CigaretteItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 100;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);

//        if(!pLevel.isClientSide()) {
//            pPlayer.getCapability(NicotineLevelProvider.NICOTINE_LEVEL_CAP).ifPresent(nicotineLevel -> {
//                nicotineLevel.addNicotineLevel(5);
//                pPlayer.sendSystemMessage(Component.literal("y'know what im saying im saying"));
//                pPlayer.sendSystemMessage(Component.literal("Current nicotine level: " + nicotineLevel.getNicotineLevel()));
//            });
//            itemStack.hurtAndBreak(1, pPlayer, player -> player.broadcastBreakEvent(player.getUsedItemHand()));
//        }


        return InteractionResultHolder.consume(itemStack);
    }

    private void onItemBreak(ItemStack stack, Level level, LivingEntity livingEntity) {

    }

    private void spawnSmokeParticles(Level level, Player player) {
    }

    @Override
    public void releaseUsing(@NotNull ItemStack pStack, Level pLevel, @NotNull LivingEntity pLivingEntity, int pTimeCharged) {
        if (pLevel.isClientSide) return;
        int hitDuration = this.getUseDuration(pStack) - pTimeCharged;
        int nicotineAddition = (int)((float)hitDuration * nicotinePerTick);

        pLivingEntity.getCapability(NicotineLevelProvider.NICOTINE_LEVEL_CAP).ifPresent(nicotineLevel -> {
            nicotineLevel.addNicotineLevel(nicotineAddition, (ServerPlayer) pLivingEntity);
            pLivingEntity.sendSystemMessage(Component.literal("y'know what im saying im saying"));
            pLivingEntity.sendSystemMessage(Component.literal("Nicotine level increased by: " + nicotineAddition));
            pLivingEntity.sendSystemMessage(Component.literal("Current nicotine level: " + nicotineLevel.getNicotineLevel()));

            for (int i = 0; i < 10; i++) {
                double x = pLivingEntity.getX();
                double y = pLivingEntity.getEyeY();
                double z = pLivingEntity.getZ();

                // Add smoke particles
                ServerLevel serverLevel = (ServerLevel)pLevel;
                serverLevel.addParticle(ParticleTypes.SMOKE, x, y+1, z, 0, 0, 0);
            }
        });

        pStack.hurtAndBreak(1, pLivingEntity, livingEntity -> {
            onItemBreak(livingEntity.getMainHandItem(), pLivingEntity.level(), pLivingEntity);
        });
    }
}
