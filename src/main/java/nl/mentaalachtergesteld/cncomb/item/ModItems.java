package nl.mentaalachtergesteld.cncomb.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nl.mentaalachtergesteld.cncomb.CNCOMB;
import nl.mentaalachtergesteld.cncomb.item.custom.CigaretteItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CNCOMB.MODID);

    public static final RegistryObject<Item> CIGARETTE = ITEMS.register("cigarette",
            () -> new CigaretteItem(
                    new Item.Properties()
                            .defaultDurability(10)
            ));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
