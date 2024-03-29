package org.pokesplash.legendaryspawns.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.pokesplash.legendaryspawns.util.LuckPermsUtils;

public class BaseCommand {
	public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> root = CommandManager
				.literal("legendaryspawns")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(), CommandHandler.basePermission + ".base");
					} else {
						return true;
					}
				})
				.executes(this::run);

		LiteralCommandNode<ServerCommandSource> registeredCommand = dispatcher.register(root);

		registeredCommand.addChild(new ReloadCommand().build());
		registeredCommand.addChild(new ToggleCommand().build());
		registeredCommand.addChild(new BoostCommand().build());
		registeredCommand.addChild(new GetCommand().build());
	}

	public int run(CommandContext<ServerCommandSource> context) {
		System.out.println("§b§lLegendary Spawns");
		return 1;
	}
}
