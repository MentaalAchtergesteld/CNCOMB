package nl.mentaalachtergesteld.cncomb.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import nl.mentaalachtergesteld.cncomb.CNCOMB;
import nl.mentaalachtergesteld.cncomb.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CigarettePackItem extends Item {
    public CigarettePackItem(Properties pProperties) {
        super(pProperties);
    }

    private static final String CIGARETTES_KEY = "StoredCigarettes";
    private static final int MAX_STACK_LIMIT = 20;

    public boolean addCigarette(ItemStack cigarettePack, ItemStack cigarette) {
        if (!(cigarette.getItem() instanceof CigaretteItem)) return false;

        CompoundTag tag = cigarettePack.getOrCreateTag();
        ListTag cigarettes = tag.getList(CIGARETTES_KEY, ListTag.TAG_COMPOUND);

        if(cigarettes.size() >= MAX_STACK_LIMIT) return false;

        CompoundTag cigaretteTag = cigarette.serializeNBT();
        cigarettes.add(cigaretteTag);
        tag.put(CIGARETTES_KEY, cigarettes);
        return true;
    }

    public List<ItemStack> getCigarettes(ItemStack cigarettePack) {
        List<ItemStack> cigaretteList = new ArrayList<ItemStack>();

        ListTag cigarettes = cigarettePack.getOrCreateTag().getList(CIGARETTES_KEY, ListTag.TAG_COMPOUND);

        for(int i = 0; i < cigarettes.size(); i++) {
            cigaretteList.add(ItemStack.of(cigarettes.getCompound(i)));
        }

        return cigaretteList;
    }

    private boolean takeOneCigarette(ItemStack cigarettePack, ServerPlayer player) {
        CompoundTag tag = cigarettePack.getOrCreateTag();
        ListTag cigarettes = tag.getList(CIGARETTES_KEY, ListTag.TAG_COMPOUND);

        if(cigarettes.isEmpty()) return false;

        CompoundTag cigarette = cigarettes.getCompound(0);
        ItemStack cigaretteItem = ItemStack.of(cigarette);

        cigarettes.remove(0);
        tag.put(CIGARETTES_KEY, cigarettes);

        if(player.getInventory().getFreeSlot() == -1) {
            player.drop(cigaretteItem, true);

        } else {
            player.getInventory().add(cigaretteItem);
        }

        return true;
    }

    private boolean takeAllCigarettes(ItemStack cigarettePack, ServerPlayer player) {
        CompoundTag tag = cigarettePack.getOrCreateTag();
        ListTag cigarettes = tag.getList(CIGARETTES_KEY, ListTag.TAG_COMPOUND);

        if(cigarettes.isEmpty()) return false;

        int amountToTake = cigarettes.size();
        for(int i = 0; i < amountToTake; i++) {
            takeOneCigarette(cigarettePack, player);
        }

        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack cigarettePack = pPlayer.getItemInHand(pUsedHand);
        if(pLevel.isClientSide) return InteractionResultHolder.pass(cigarettePack);

        if(pPlayer.isCrouching()) {
            if(!takeAllCigarettes(cigarettePack, (ServerPlayer) pPlayer)) return InteractionResultHolder.fail(cigarettePack);
        } else {
            if(!takeOneCigarette(cigarettePack, (ServerPlayer) pPlayer)) return InteractionResultHolder.fail(cigarettePack);
        }

        return InteractionResultHolder.success(cigarettePack);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        if (!(pOther.getItem() instanceof CigaretteItem)) return super.overrideOtherStackedOnMe(pStack, pOther, pSlot, pAction, pPlayer, pAccess);

        if(!addCigarette(pStack, pOther)) return false;
        pOther.shrink(1);
        return true;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        CompoundTag tag = pStack.getOrCreateTag();
        ListTag cigarettes = tag.getList(CIGARETTES_KEY, ListTag.TAG_COMPOUND);

        if (cigarettes.size() == 1) {
            pTooltipComponents.add(Component.literal(cigarettes.size() + " Cigarette"));
        } else {
            pTooltipComponents.add(Component.literal(cigarettes.size() + " Cigarettes"));
        }
    }
}
