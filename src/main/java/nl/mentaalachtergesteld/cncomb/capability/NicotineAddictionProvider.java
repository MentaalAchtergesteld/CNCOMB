package nl.mentaalachtergesteld.cncomb.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.IntTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NicotineAddictionProvider implements ICapabilityProvider, INBTSerializable<IntTag> {
    public static Capability<NicotineAddiction> NICOTINE_ADDICTION_CAP = CapabilityManager.get(new CapabilityToken<NicotineAddiction>() {});
    private NicotineAddiction nicotineAddiction = null;
    private final LazyOptional<NicotineAddiction> lazyOptional = LazyOptional.of(this::createNicotineAddiction);

    private NicotineAddiction createNicotineAddiction() {
        if(this.nicotineAddiction == null) {
            this.nicotineAddiction = new NicotineAddiction();
        }

        return this.nicotineAddiction;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == NICOTINE_ADDICTION_CAP) {
            return lazyOptional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public IntTag serializeNBT() {
        return createNicotineAddiction().toNBT();
    }

    @Override
    public void deserializeNBT(IntTag nbt) {
        createNicotineAddiction().fromNBT(nbt);
    }
}
