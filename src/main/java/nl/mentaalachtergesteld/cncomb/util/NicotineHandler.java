package nl.mentaalachtergesteld.cncomb.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.common.util.LazyOptional;
import nl.mentaalachtergesteld.cncomb.capability.*;
import nl.mentaalachtergesteld.cncomb.effect.ModEffects;

import java.util.Optional;

public class NicotineHandler {
    private static final float NICOTINE_LEVEL_DECAY_CHANCE_PER_TICK = 0.01f;
    private static final float NICOTINE_ADDICTION_INCREMENT_CHANCE_PER_TICK = 0.01f;
    private static final float NICOTINE_ADDICTION_DECAY_CHANCE_PER_TICK = 0.01f;
    private static final float WITHDRAWAL_DECAY_CHANCE_PER_TICK = 0.01f;

    public static void handleNicotineEffects(ServerPlayer player) {
        RandomSource random = player.getRandom();

        Optional<NicotineLevel> nicotineLevelCap = player.getCapability(NicotineLevelProvider.NICOTINE_LEVEL_CAP).resolve();
        Optional<NicotineAddiction> nicotineAddictionCap = player.getCapability(NicotineAddictionProvider.NICOTINE_ADDICTION_CAP).resolve();
        Optional<Withdrawal> withdrawalCap = player.getCapability(WithdrawalProvider.WITHDRAWAL_CAP).resolve();

        if(nicotineLevelCap.isEmpty()) return;
        NicotineLevel nicotineLevel = nicotineLevelCap.get();
        reduceNicotineLevel(random, nicotineLevel);

        if(nicotineAddictionCap.isEmpty()) return;
        NicotineAddiction nicotineAddiction = nicotineAddictionCap.get();
        adjustNicotineAddiction(random, nicotineLevel, nicotineAddiction);

        if(withdrawalCap.isEmpty()) return;
        Withdrawal withdrawal = withdrawalCap.get();
        manageWithdrawal(random, nicotineLevel, nicotineAddiction, withdrawal);

        applyNicotineEffect(player, nicotineLevel);
        applyWithdrawalEffect(player,withdrawal);
    }

    private static void reduceNicotineLevel(RandomSource random, NicotineLevel nicotineLevel) {
        if(nicotineLevel.getNicotineLevel() <= 0) return;
        if(random.nextFloat() < NICOTINE_LEVEL_DECAY_CHANCE_PER_TICK) nicotineLevel.subtractNicotineLevel(1);
    }

    private static void adjustNicotineAddiction(RandomSource random, NicotineLevel nicotineLevel, NicotineAddiction nicotineAddiction) {
        if(nicotineLevel.getNicotineLevel() > 0) {
            if(random.nextFloat() < NICOTINE_ADDICTION_INCREMENT_CHANCE_PER_TICK) nicotineAddiction.addNicotineLevel(1);
        } else {
            if(random.nextFloat() < NICOTINE_ADDICTION_DECAY_CHANCE_PER_TICK) nicotineAddiction.subtractNicotineLevel(1);
        }
    }

    private static int getNicotineLevelMinimum(NicotineAddiction nicotineAddiction) {
        return nicotineAddiction.getNicotineAddiction() / 4;
    }

    private static float getWithdrawalIncreaseChancePerTick(NicotineAddiction nicotineAddiction) {
        return nicotineAddiction.getNicotineAddiction() / 2000.f;
    }

    private static void manageWithdrawal(RandomSource random, NicotineLevel nicotineLevel, NicotineAddiction nicotineAddiction, Withdrawal withdrawal) {
        if(nicotineAddiction.getNicotineAddiction() > 0 && nicotineLevel.getNicotineLevel() < getNicotineLevelMinimum(nicotineAddiction)) {
            if(random.nextFloat() < getWithdrawalIncreaseChancePerTick(nicotineAddiction)) withdrawal.addWithdrawal(1);
        } else {
            if(random.nextFloat() < WITHDRAWAL_DECAY_CHANCE_PER_TICK) withdrawal.subtractWithdrawal(1);
        }
    }

    private static void applyNicotineEffect(ServerPlayer player, NicotineLevel nicotineLevel) {
        int scaledNicotineLevel = nicotineLevel.getNicotineLevel() / 10;

        switch (scaledNicotineLevel) {
            case 2,3: player.addEffect(new MobEffectInstance(ModEffects.NICOTINE_BUZZ.get(), 40, 0)); break;
            case 4,5: player.addEffect(new MobEffectInstance(ModEffects.NICOTINE_BUZZ.get(), 40, 1)); break;
            case 6,7: player.addEffect(new MobEffectInstance(ModEffects.NICOTINE_BUZZ.get(), 40, 2)); break;
            case 8: player.addEffect(new MobEffectInstance(ModEffects.NICOTINE_SICK.get(), 40, 0)); break;
            case 9,10: player.addEffect(new MobEffectInstance(ModEffects.NICOTINE_SICK.get(), 40, 1)); break;
            default: break;
        }
    }

    private static void applyWithdrawalEffect(ServerPlayer player, Withdrawal withdrawal) {
        int scaledWithdrawal = withdrawal.getWithdrawal() / 20;

        switch (scaledWithdrawal) {
            case 1,2: player.addEffect(new MobEffectInstance(ModEffects.WITHDRAWAL_EFFECT.get(), 20, 0)); break;
            case 3: player.addEffect(new MobEffectInstance(ModEffects.WITHDRAWAL_EFFECT.get(), 20, 1)); break;
            case 4, 5: player.addEffect(new MobEffectInstance(ModEffects.WITHDRAWAL_EFFECT.get(), 20, 2)); break;
            default: break;
        }
    }
}
