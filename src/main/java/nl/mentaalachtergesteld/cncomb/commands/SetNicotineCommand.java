package nl.mentaalachtergesteld.cncomb.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameModeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import nl.mentaalachtergesteld.cncomb.CNCOMB;
import nl.mentaalachtergesteld.cncomb.capability.*;

import java.util.Collections;
import java.util.Optional;

public class SetNicotineCommand {

    private enum Modifier {
        Add,
        Subtract,
        Set,
        Get
    }

    private enum Type {
        NicotineLevel,
        NicotineAddiction,
        Withdrawal
    }

//    private static final SuggestionProvider<CommandSourceStack> TYPE_SUGGESTIONS = (context, builder) -> {
//        return builder.suggest("nicotine_level").suggest("nicotine_addiction").suggest("withdrawal").buildFuture();
//    };

    public SetNicotineCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> argumentBuilder =  Commands.literal("nicotine");

        for(Modifier modifier : Modifier.values()) {

            LiteralArgumentBuilder<CommandSourceStack> modifierBuilder = switch (modifier) {
                case Add -> Commands.literal("add");
                case Subtract -> Commands.literal("subtract");
                case Set -> Commands.literal("set");
                case Get -> Commands.literal("get");
            };

            for(Type type: Type.values()) {
                LiteralArgumentBuilder<CommandSourceStack> typeBuilder = switch (type) {
                    case NicotineLevel -> Commands.literal("nicotine_level");
                    case NicotineAddiction ->  Commands.literal("nicotine_addiction");
                    case Withdrawal -> Commands.literal("withdrawal");
                };

                if(modifier != Modifier.Get) {
                    RequiredArgumentBuilder<CommandSourceStack, Integer> numberArgument = Commands.argument("amount", IntegerArgumentType.integer())
                            .executes(context -> execute(context.getSource(), modifier, type, IntegerArgumentType.getInteger(context, "amount")));
                    typeBuilder = typeBuilder.then(numberArgument);
                } else {
                    typeBuilder = typeBuilder.executes(context -> executeGet(context.getSource(), type));
                }

                modifierBuilder = modifierBuilder.then(typeBuilder);
            }

            argumentBuilder = argumentBuilder.then(modifierBuilder);
        }

        dispatcher.register(argumentBuilder);
    }

    private static int executeGet(CommandSourceStack source, Type type) {
        if(source.getLevel().isClientSide) return 0;
        if(!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("This command can only be run on a player."));
            return 0;
        }

        int amount = switch (type) {
            case NicotineLevel:
                Optional<NicotineLevel> nicotineLevelOptional = player.getCapability(NicotineLevelProvider.NICOTINE_LEVEL_CAP).resolve();
                if(nicotineLevelOptional.isEmpty()) yield -1;
                yield nicotineLevelOptional.get().getNicotineLevel();
            case NicotineAddiction:
                Optional<NicotineAddiction> nicotineAddictionOptional = player.getCapability(NicotineAddictionProvider.NICOTINE_ADDICTION_CAP).resolve();
                if(nicotineAddictionOptional.isEmpty()) yield -1;
                yield nicotineAddictionOptional.get().getNicotineAddiction();
            case Withdrawal:
                Optional<Withdrawal> withdrawalOptional = player.getCapability(WithdrawalProvider.WITHDRAWAL_CAP).resolve();
                if(withdrawalOptional.isEmpty()) yield -1;
                yield withdrawalOptional.get().getWithdrawal();
        };

        if(amount < 0) {
            source.sendFailure(Component.literal(type + " doesn't exist."));
        } else {
            source.sendSuccess(() -> Component.literal(type + " is " + amount), true);
        }
        return 1;
    }

    private static int execute(CommandSourceStack source, Modifier modifier, Type type, int amount) {
        if(source.getLevel().isClientSide) return 0;
        if(!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("This command can only be run on a player."));
            return 0;
        }

        switch (type) {
            case NicotineLevel:
                Optional<NicotineLevel> nicotineLevelOptional = player.getCapability(NicotineLevelProvider.NICOTINE_LEVEL_CAP).resolve();
                if(nicotineLevelOptional.isEmpty()) return 0;
                NicotineLevel nicotineLevel = nicotineLevelOptional.get();
                modifyNicotineLevel(nicotineLevel, modifier, amount);
                break;
            case NicotineAddiction:
                Optional<NicotineAddiction> nicotineAddictionOptional = player.getCapability(NicotineAddictionProvider.NICOTINE_ADDICTION_CAP).resolve();
                if(nicotineAddictionOptional.isEmpty()) return 0;
                NicotineAddiction nicotineAddiction = nicotineAddictionOptional.get();
                modifyNicotineAddiction(nicotineAddiction, modifier, amount);
                break;
            case Withdrawal:
                Optional<Withdrawal> withdrawalOptional = player.getCapability(WithdrawalProvider.WITHDRAWAL_CAP).resolve();
                if(withdrawalOptional.isEmpty()) return 0;
                Withdrawal withdrawal = withdrawalOptional.get();
                modifyWithdrawal(withdrawal, modifier, amount);
                break;
        }

        switch (modifier) {
            case Add: source.sendSuccess(() -> Component.literal("Added " + amount + " to " + type), true); break;
            case Subtract: source.sendSuccess(() -> Component.literal("Subtracted " + amount + " from " + type), true);
            case Set: source.sendSuccess(() -> Component.literal("Set " + type + " to " + amount), true);
        }

        return 1;
    }

    private static void modifyNicotineLevel(NicotineLevel nicotineLevel, Modifier modifier, int amount) {
        switch (modifier) {
            case Add: nicotineLevel.addNicotineLevel(amount); break;
            case Subtract: nicotineLevel.subtractNicotineLevel(amount); break;
            case Set: nicotineLevel.setNicotineLevel(amount); break;
        }
    }

    private static void modifyNicotineAddiction(NicotineAddiction nicotineAddiction, Modifier modifier, int amount) {
        switch (modifier) {
            case Add: nicotineAddiction.addNicotineLevel(amount); break;
            case Subtract: nicotineAddiction.subtractNicotineLevel(amount); break;
            case Set: nicotineAddiction.setNicotineAddiction(amount); break;
        }
    }

    private static void modifyWithdrawal(Withdrawal withdrawal, Modifier modifier, int amount) {
        switch (modifier) {
            case Add: withdrawal.addWithdrawal(amount); break;
            case Subtract: withdrawal.subtractWithdrawal(amount); break;
            case Set: withdrawal.setWithdrawal(amount); break;
        }
    }
}
