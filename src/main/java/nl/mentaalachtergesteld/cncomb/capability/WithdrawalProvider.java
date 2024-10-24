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

public class WithdrawalProvider implements ICapabilityProvider, INBTSerializable<IntTag> {
    public static Capability<Withdrawal> WITHDRAWAL_CAP = CapabilityManager.get(new CapabilityToken<Withdrawal>() {});
    private Withdrawal withdrawal = null;
    private final LazyOptional<Withdrawal> lazyOptional = LazyOptional.of(this::createWithdrawal);

    private Withdrawal createWithdrawal() {
        if(this.withdrawal == null) {
            this.withdrawal = new Withdrawal();
        }

        return this.withdrawal;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == WITHDRAWAL_CAP) {
            return lazyOptional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public IntTag serializeNBT() {
        return createWithdrawal().toNBT();
    }

    @Override
    public void deserializeNBT(IntTag nbt) {
        createWithdrawal().fromNBT(nbt);
    }
}
