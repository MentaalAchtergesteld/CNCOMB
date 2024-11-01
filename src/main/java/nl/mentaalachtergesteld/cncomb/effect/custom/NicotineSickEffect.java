package nl.mentaalachtergesteld.cncomb.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import nl.mentaalachtergesteld.cncomb.effect.ModEffects;
import org.jetbrains.annotations.Nullable;

public class NicotineSickEffect extends MobEffect {
    public NicotineSickEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF0000);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if(pLivingEntity.level().isClientSide) return;
        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 1));
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
