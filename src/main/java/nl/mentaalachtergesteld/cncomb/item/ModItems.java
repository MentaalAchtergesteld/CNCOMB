package nl.mentaalachtergesteld.cncomb.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nl.mentaalachtergesteld.cncomb.CNCOMB;
import nl.mentaalachtergesteld.cncomb.block.ModBlocks;
import nl.mentaalachtergesteld.cncomb.item.custom.CigaretteItem;
import nl.mentaalachtergesteld.cncomb.item.custom.CigarettePackItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CNCOMB.MODID);

    public static final RegistryObject<Item> CIGARETTE = ITEMS.register("cigarette",
            () -> new CigaretteItem(
                    new Item.Properties()
                            .defaultDurability(10)
            ));

    public static final RegistryObject<Item> LIT_CIGARETTE = ITEMS.register("lit_cigarette",
            () -> new CigaretteItem(
                    new Item.Properties()
            ));

    public static final RegistryObject<Item> CIGARETTE_PACK = ITEMS.register("cigarette_pack",
            () -> new CigarettePackItem(
                    new Item.Properties()
                            .stacksTo(1)
            ));

    public static final RegistryObject<Item> TOBACCO_SEEDS = ITEMS.register("tobacco_seeds",
            () -> new ItemNameBlockItem(ModBlocks.TOBACCO_CROP.get(), new Item.Properties()));

    public static final RegistryObject<Item> TOBACCO_LEAF = ITEMS.register("tobacco_leaf",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DRIED_TOBACCO_LEAF = ITEMS.register("dried_tobacco_leaf",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FILTER = ITEMS.register("filter",
            () -> new Item(new Item.Properties()));
    
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
