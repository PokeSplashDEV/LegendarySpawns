package org.pokesplash.legendaryspawns.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.pokesplash.legendaryspawns.LegendarySpawns;
import org.pokesplash.legendaryspawns.config.TimerProvider;
import org.pokesplash.legendaryspawns.util.LuckPermsUtils;
import org.pokesplash.legendaryspawns.util.Utils;

public class GetCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("get")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(), CommandHandler.basePermission +
								".get");
					} else {
						return true;
					}
				})
				.executes(this::run).build();
	}

	public int run(CommandContext<ServerCommandSource> context) {

		context.getSource().sendMessage(Text.literal("Chance: " +
				LegendarySpawns.config.getSpawnChance() + " - Time: " + LegendarySpawns.config.getTimer()));

		return 1;
	}
}
