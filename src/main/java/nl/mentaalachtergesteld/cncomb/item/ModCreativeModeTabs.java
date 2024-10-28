package nl.mentaalachtergesteld.cncomb.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import nl.mentaalachtergesteld.cncomb.CNCOMB;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CNCOMB.MODID);

    public static final RegistryObject<CreativeModeTab> CNOMB_TAB = CREATIVE_MODE_TABS.register("cncomb_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.CIGARETTE.get()))
                    .title(Component.translatable("creativetab.cncomb_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.CIGARETTE.get());
                        output.accept(ModItems.CIGARETTE_PACK.get());
                        output.accept(ModItems.FILTER.get());
                        output.accept(ModItems.TOBACCO_SEEDS.get());
                        output.accept(ModItems.TOBACCO_LEAF.get());
                        output.accept(ModItems.DRIED_TOBACCO_LEAF.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
