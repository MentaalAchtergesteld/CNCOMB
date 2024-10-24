package nl.mentaalachtergesteld.cncomb.capability;

import net.minecraft.nbt.IntTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public class Withdrawal implements ICopyCapability<Withdrawal> {
    private int withdrawal;
    private final int MIN_WITHDRAWAL = 0;
    private final int MAX_WITHDRAWAL = 100;

    public int getWithdrawal() {
        return this.withdrawal;
    }

    public void setWithdrawal(int level) {
        this.withdrawal = Math.max(this.MIN_WITHDRAWAL, Math.min(this.MAX_WITHDRAWAL, level));
    }

    public void addWithdrawal(int amount) {
        this.withdrawal = Math.min(this.withdrawal + amount, this.MAX_WITHDRAWAL);
    }

    public void subtractWithdrawal(int amount) {
        this.withdrawal = Math.max(this.withdrawal - amount, this.MIN_WITHDRAWAL);
    }

    public void copyFrom(Withdrawal source) {
        this.withdrawal = source.withdrawal;
    }

    public IntTag toNBT() {
        return IntTag.valueOf(this.withdrawal);
    }

    public void fromNBT(IntTag nbt) {
        this.withdrawal = nbt.getAsInt();
    }
}
