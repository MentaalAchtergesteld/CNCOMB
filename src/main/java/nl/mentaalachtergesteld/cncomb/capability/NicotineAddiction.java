package nl.mentaalachtergesteld.cncomb.capability;

import net.minecraft.nbt.IntTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public class NicotineAddiction implements ICopyCapability<NicotineAddiction> {
    private int nicotine_addiction;
    private final int MIN_ADDICTION = 0;
    private final int MAX_ADDICTION = 100;

    public int getNicotineAddiction() {
        return this.nicotine_addiction;
    }

    public void setNicotineAddiction(int level) {
        this.nicotine_addiction = Math.max(this.MIN_ADDICTION, Math.min(this.MAX_ADDICTION, level));
    }

    public void addNicotineLevel(int amount) {
        this.nicotine_addiction = Math.min(this.nicotine_addiction + amount, this.MAX_ADDICTION);
    }

    public void subtractNicotineLevel(int amount) {
        this.nicotine_addiction = Math.max(this.nicotine_addiction - amount, this.MIN_ADDICTION);
    }

    public void copyFrom(NicotineAddiction source) {
        this.nicotine_addiction = source.nicotine_addiction;
    }

    public IntTag toNBT() {
        return IntTag.valueOf(this.nicotine_addiction);
    }

    public void fromNBT(IntTag nbt) {
        this.nicotine_addiction = nbt.getAsInt();
    }
}
