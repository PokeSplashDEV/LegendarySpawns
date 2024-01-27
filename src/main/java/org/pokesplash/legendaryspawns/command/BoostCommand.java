package org.pokesplash.legendaryspawns.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.pokesplash.legendaryspawns.LegendarySpawns;
import org.pokesplash.legendaryspawns.config.Booster;
import org.pokesplash.legendaryspawns.config.TimerProvider;
import org.pokesplash.legendaryspawns.util.LuckPermsUtils;
import org.pokesplash.legendaryspawns.util.Utils;

public class BoostCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("boost")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(), CommandHandler.basePermission +
								".booster");
					} else {
						return true;
					}
				})
				.executes(this::usage)
				.then(CommandManager.argument("chance", DoubleArgumentType.doubleArg(0, 1))
						.executes(this::usage)
						.then(CommandManager.argument("time", DoubleArgumentType.doubleArg(1))
								.executes(this::usage)
								.then(CommandManager.argument("duration", IntegerArgumentType.integer(1))
										.executes(this::run)
								))).build();
	}

	public int run(CommandContext<ServerCommandSource> context) {

		double chance = DoubleArgumentType.getDouble(context, "chance");
		double time = DoubleArgumentType.getDouble(context, "time");
		int duration = IntegerArgumentType.getInteger(context, "duration");

		TimerProvider.setBooster(new Booster(chance, time, duration));

		return 1;
	}

	public int usage(CommandContext<ServerCommandSource> context) {

		context.getSource().sendMessage(Text.literal(
				Utils.formatMessage(
						"&2&lUsage\n&b- boost <rate change> <time change> <boost duration>",
						context.getSource().isExecutedByPlayer())
		));

		return 1;
	}
}
