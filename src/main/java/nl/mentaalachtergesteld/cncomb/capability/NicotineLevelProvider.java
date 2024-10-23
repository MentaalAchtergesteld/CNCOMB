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

public class NicotineLevelProvider implements ICapabilityProvider, INBTSerializable<IntTag> {
    public static Capability<NicotineLevel> NICOTINE_LEVEL_CAP = CapabilityManager.get(new CapabilityToken<NicotineLevel>() {});
    private NicotineLevel nicotineLevel = null;
    private final LazyOptional<NicotineLevel> lazyOptional = LazyOptional.of(this::createNicotineLevel);

    private NicotineLevel createNicotineLevel() {
        if(this.nicotineLevel == null) {
            this.nicotineLevel = new NicotineLevel();
        }

        return this.nicotineLevel;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == NICOTINE_LEVEL_CAP) {
            return lazyOptional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public IntTag serializeNBT() {
        return createNicotineLevel().toNBT();
    }

    @Override
    public void deserializeNBT(IntTag nbt) {
        createNicotineLevel().fromNBT(nbt);
    }
}
