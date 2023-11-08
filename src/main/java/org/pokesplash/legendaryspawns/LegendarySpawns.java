package org.pokesplash.legendaryspawns;

import com.cobblemon.mod.common.api.events.CobblemonEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pokesplash.legendaryspawns.command.CommandHandler;
import org.pokesplash.legendaryspawns.config.Announcer;
import org.pokesplash.legendaryspawns.config.Config;
import org.pokesplash.legendaryspawns.config.TimerProvider;
import org.pokesplash.legendaryspawns.event.PokemonCatchEvent;
import org.pokesplash.legendaryspawns.event.PokemonSpawnEvent;

public class LegendarySpawns implements ModInitializer {
	public static final String MOD_ID = "LegendarySpawns";
	public static final String BASE_PATH = "/config/" + MOD_ID + "/";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Config config = new Config();
	public static final Announcer announcer = new Announcer();
	public static MinecraftServer world;

	/**
	 * Runs the mod initializer.
	 */
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(CommandHandler::registerCommands);
		ServerWorldEvents.LOAD.register((t, e) -> world = t);
		new PokemonSpawnEvent().registerEvent();
		new PokemonCatchEvent().registerEvent();
		load();

		ServerLifecycleEvents.SERVER_STOPPING.register(e -> {
			TimerProvider.shutdown();
		});
	}

	public static void load() {
		TimerProvider.shutdown();
		config.init();
		announcer.init();
		TimerProvider.init();
	}
}
