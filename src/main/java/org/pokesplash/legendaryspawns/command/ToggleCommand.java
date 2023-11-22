package org.pokesplash.legendaryspawns.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.pokesplash.legendaryspawns.LegendarySpawns;
import org.pokesplash.legendaryspawns.util.LuckPermsUtils;
import org.pokesplash.legendaryspawns.util.Utils;

import java.util.HashMap;

public class ToggleCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("toggle")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(), CommandHandler.basePermission + ".toggle");
					} else {
						return true;
					}
				})
				.executes(this::run)
				.then(CommandManager.argument("type", StringArgumentType.string())
						.suggests((ctx, builder) -> {
							builder.suggest("shiny");
							builder.suggest("legendary");
							builder.suggest("ub");
							builder.suggest("paradox");
							return builder.buildFuture();
						})
						.executes(this::usage)
						.then(CommandManager.argument("bool", BoolArgumentType.bool())
								.suggests((ctx, builder) -> {
									builder.suggest("true");
									builder.suggest("false");
									return builder.buildFuture();
								})
								.executes(this::run)
						)).build();
	}

	public int run(CommandContext<ServerCommandSource> context) {

		String argument = StringArgumentType.getString(context, "type");
		boolean toggleValue = BoolArgumentType.getBool(context, "bool");

		if (argument.equalsIgnoreCase("shiny")) {
			LegendarySpawns.announcer.setAnnounceShinies(toggleValue);
		} else if (argument.equalsIgnoreCase("legendary")) {
			LegendarySpawns.announcer.setAnnounceLegendaries(toggleValue);
		} else if (argument.equalsIgnoreCase("ub")) {
			LegendarySpawns.announcer.setAnnounceUltrabeasts(toggleValue);
		} else if (argument.equalsIgnoreCase("paradox")) {
			LegendarySpawns.announcer.setAnnounceParadox(toggleValue);
		}

		return 1;
	}

	public int usage(CommandContext<ServerCommandSource> context) {

		context.getSource().sendMessage(Text.literal(
				Utils.formatMessage(
						"&2&lUsage\n&b- toggle <shiny/legendary/ub/paradox> <true/false>",
						context.getSource().isExecutedByPlayer())
		));

		return 1;
	}
}
