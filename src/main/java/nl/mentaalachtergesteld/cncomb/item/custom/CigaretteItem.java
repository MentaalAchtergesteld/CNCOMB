package nl.mentaalachtergesteld.cncomb.item.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import nl.mentaalachtergesteld.cncomb.CNCOMB;
import nl.mentaalachtergesteld.cncomb.capability.NicotineLevel;
import nl.mentaalachtergesteld.cncomb.capability.NicotineLevelProvider;
import nl.mentaalachtergesteld.cncomb.item.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CigaretteItem extends Item {

    private static final float nicotinePerTick = 0.25f;

    public CigaretteItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 100;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if(pLevel.isClientSide) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
        if (pUsedHand != InteractionHand.MAIN_HAND) return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));

        ItemStack offhandStack = pPlayer.getOffhandItem();

        if(!offhandStack.is(Items.FLINT_AND_STEEL)) return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));

        ItemStack replacementStack = new ItemStack(ModItems.LIT_CIGARETTE.get());
        pPlayer.setItemInHand(pUsedHand, replacementStack);
        offhandStack.hurtAndBreak(1, pPlayer, p -> p.broadcastBreakEvent(InteractionHand.OFF_HAND));

        return InteractionResultHolder.success(replacementStack);

//        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
//        pPlayer.startUsingItem(pUsedHand);
//        return InteractionResultHolder.success(itemStack);
    }
}
