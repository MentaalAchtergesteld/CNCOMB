package nl.mentaalachtergesteld.cncomb.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameModeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import nl.mentaalachtergesteld.cncomb.capability.NicotineLevelProvider;

import java.util.Collections;

public class SetNicotineCommand {
    public SetNicotineCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("setnicotine")
                .then(Commands.literal("clear")
                        .executes(this::execute)));
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        assert player != null;
        player.getCapability(NicotineLevelProvider.NICOTINE_LEVEL_CAP).ifPresent(nicotineLevel -> {
            nicotineLevel.setNicotineLevel(0);
        });

        player.sendSystemMessage(Component.literal("Cleared nicotine level."));

        return 1;
    }
}
