package nl.mentaalachtergesteld.cncomb.capability;

import net.minecraft.nbt.IntTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public class NicotineLevel {
    private int nicotine_level;
    private final int MIN_NICOTINE_LEVEL = 0;
    private final int MAX_NICOTINE_LEVEL = 100;

    public int getNicotineLevel() {
        return nicotine_level;
    }

    public void setNicotineLevel(int level) {
        this.nicotine_level = Math.max(this.MIN_NICOTINE_LEVEL, Math.min(this.MAX_NICOTINE_LEVEL, level));
    }

    public void addNicotineLevel(int amount) {
        this.nicotine_level = Math.min(this.nicotine_level + amount, this.MAX_NICOTINE_LEVEL);
    }

    public void subtractNicotineLevel(int amount) {
        this.nicotine_level = Math.max(this.nicotine_level - amount, this.MIN_NICOTINE_LEVEL);
    }

    public void copyFrom(NicotineLevel source) {
        this.nicotine_level = source.nicotine_level;
    }

    public IntTag toNBT() {
        return IntTag.valueOf(this.nicotine_level);
    }

    public void fromNBT(IntTag nbt) {
        this.nicotine_level = nbt.getAsInt();
    }
}
