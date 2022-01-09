package mcjty.lostcities.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mcjty.lostcities.LostCities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> commands = dispatcher.register(
                Commands.literal(LostCities.MODID)
                        .then(CommandDebug.register(dispatcher))
                        .then(CommandMap.register(dispatcher))
                        .then(CommandSaveProfile.register(dispatcher))
        );

        dispatcher.register(Commands.literal("lost").redirect(commands));
    }

}
