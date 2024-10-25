package nl.mentaalachtergesteld.cncomb.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Attr;

import java.util.Objects;
import java.util.UUID;

public class NicotineBuzzEffect  extends MobEffect {
    private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("c848b05e-25b3-496a-ad0b-90029b0dcd84");

    public NicotineBuzzEffect() { super(MobEffectCategory.BENEFICIAL, 0xFF0000); }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {
        double speedIncrease = 0.1 * (pAmplifier + 1);
        AttributeModifier modifier = new AttributeModifier(SPEED_MODIFIER_UUID, "Nicotine Buzz Speed", speedIncrease, AttributeModifier.Operation.MULTIPLY_TOTAL);
        Objects.requireNonNull(pLivingEntity.getAttribute(Attributes.MOVEMENT_SPEED)).addTransientModifier(modifier);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {
        Objects.requireNonNull(pLivingEntity.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(SPEED_MODIFIER_UUID);
    }
}
