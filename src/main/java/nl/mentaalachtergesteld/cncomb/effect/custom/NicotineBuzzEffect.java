package nl.mentaalachtergesteld.cncomb.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class NicotineBuzzEffect  extends MobEffect {
    public NicotineBuzzEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF0000);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if(pLivingEntity.level().isClientSide) return;
//        pLivingEntity.setSpeed(pLivingEntity.getSpeed() + 0.5f * (pAmplifier + 1));
        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 2));
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 20 == 0;
    }
}
