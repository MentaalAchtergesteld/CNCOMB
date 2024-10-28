package nl.mentaalachtergesteld.cncomb.item.custom;

import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.particles.ParticleOptions;
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
import net.minecraft.world.phys.Vec3;
import nl.mentaalachtergesteld.cncomb.CNCOMB;
import nl.mentaalachtergesteld.cncomb.capability.NicotineLevel;
import nl.mentaalachtergesteld.cncomb.capability.NicotineLevelProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

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
        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pLevel.isClientSide) return;
        if(pLivingEntity.getRandom().nextFloat() > 0.25) return;
        double x = pLivingEntity.getX();
        double y = pLivingEntity.getEyeY();
        double z = pLivingEntity.getZ();
        Vec3 pos = pLivingEntity.getEyePosition()
                .add(Vec3.directionFromRotation(pLivingEntity.getRotationVector()));
        ServerLevel serverLevel = (ServerLevel)pLevel;
        serverLevel.sendParticles(
                ParticleTypes.SMOKE,
                pos.x, pos.y, pos.z,
                1,
                0.0, 0.0, 0.0,
                0.0
        );
    }

    @Override
    public void releaseUsing(@NotNull ItemStack pStack, Level pLevel, @NotNull LivingEntity pLivingEntity, int pTimeCharged) {
        if (pLevel.isClientSide) return;
        int hitDuration = this.getUseDuration(pStack) - pTimeCharged;
        int nicotineAddition = (int)((float)hitDuration * nicotinePerTick);

        Optional<NicotineLevel> nicotineLevelOptional = pLivingEntity.getCapability(NicotineLevelProvider.NICOTINE_LEVEL_CAP).resolve();

        if(nicotineLevelOptional.isPresent()) {
            CNCOMB.LOGGER.debug("Hi");
            NicotineLevel nicotineLevel = nicotineLevelOptional.get();
            nicotineLevel.addNicotineLevel(nicotineAddition);
        };

        pStack.hurtAndBreak(1, pLivingEntity, livingEntity -> {});
    }
}
