package nl.mentaalachtergesteld.cncomb.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nl.mentaalachtergesteld.cncomb.CNCOMB;
import nl.mentaalachtergesteld.cncomb.effect.custom.NicotineBuzzEffect;
import nl.mentaalachtergesteld.cncomb.effect.custom.NicotineSickEffect;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CNCOMB.MODID);

    public static final RegistryObject<MobEffect> NICOTINE_BUZZ = EFFECTS.register("nicotine_buzz", NicotineBuzzEffect::new);
    public static final RegistryObject<MobEffect> NICOTINE_SICK = EFFECTS.register("nicotine_sick", NicotineSickEffect::new);

    public static void register(IEventBus bus) {
        EFFECTS.register(bus);
    }
}
