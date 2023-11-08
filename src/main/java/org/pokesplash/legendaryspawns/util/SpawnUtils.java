package org.pokesplash.legendaryspawns.util;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.pokesplash.legendaryspawns.LegendarySpawns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public abstract class SpawnUtils {

	public static void SpawnLegendary() {

		int randomNumber = ThreadLocalRandom.current().nextInt(1, 11);

		// If spawn chance is success
		if (randomNumber <= LegendarySpawns.config.getSpawnChance() * 10) {

			// Get biome of each player
			HashMap<ServerPlayerEntity, String> playerBiomes = new HashMap<>();

			// Get all players online.
			ArrayList<ServerPlayerEntity> players =
					new ArrayList<>(LegendarySpawns.world.getPlayerManager().getPlayerList());

			// If no players, no spawn.
			if (players.isEmpty()) {
				return;
			}

			// For each player, add them and their biome to the hashmap.
			for (ServerPlayerEntity player : players) {
				Vec3d playerPosition = player.getPos();
				World world = player.getWorld();

				BlockPos pos = new BlockPos(
						(int) playerPosition.getX(),
						(int) playerPosition.getY(),
						(int) playerPosition.getZ());

				RegistryKey<Biome> biome = world.getBiome(pos).getKey().get();
				playerBiomes.put(player, biome.getValue().toString());
			}

			SpawnDetails values = getValidDetails(playerBiomes);

			if (values == null) {
				return;
			}

			ServerPlayerEntity player = values.getPlayer();
			ArrayList<String> pokemon = values.getPokemon();

			Pokemon legendary = new Pokemon().initialize();

			String randomPokemon = Utils.getRandomValue(pokemon);
			Species species = PokemonSpecies.INSTANCE.getByName(randomPokemon);

			if (species == null) {
				LegendarySpawns.LOGGER.error("Could not find species " + randomPokemon + " for LegendarySpawns.");
				return;
			}

			ArrayList<Stat> ivs = new ArrayList<>();
			ivs.add(Stats.HP);
			ivs.add(Stats.ATTACK);
			ivs.add(Stats.DEFENCE);
			ivs.add(Stats.SPECIAL_ATTACK);
			ivs.add(Stats.SPECIAL_DEFENCE);
			ivs.add(Stats.SPEED);

			for (int x=0; x < LegendarySpawns.config.getNumberOfIvs(); x++) {
				Stat stat = Utils.getRandomValue(ivs);
				legendary.setIV(stat, 31);
				ivs.remove(stat);
			}

			legendary.setSpecies(species);
			legendary.setLevel(70);

			PokemonProperties properties = new PokemonProperties();
			properties.apply(legendary);
			PokemonEntity entity = properties.createEntity(player.getWorld());
			entity.setPokemon(legendary);

			BlockPos randomLocation = new BlockPos(
					(int) getRandomPosition(player.getX()),
					(int) getRandomPosition(player.getY()),
					(int) getRandomPosition(player.getZ()));

			BlockPos spawnLocation = player.getWorld().getTopPosition(Heightmap.Type.WORLD_SURFACE, randomLocation);

			entity.setPos(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ());
			entity.setInvulnerable(true);
			player.getWorld().spawnEntity(entity);

			checkConditions(entity, player.getName().getString(), playerBiomes.get(player));
		}

	}

	private static double getRandomPosition(double position) {
		ArrayList<Double> positions = new ArrayList<>();

		int radius = LegendarySpawns.config.getRadius();

		for (double x=(position - radius); x <= position + radius; x++) {
			positions.add(x);
		}

		return Utils.getRandomValue(positions);
	}

	public static void checkConditions(PokemonEntity entity, String playerName, String biome) {
		Pokemon pokemon = entity.getPokemon();

		HashSet<String> labels = pokemon.getSpecies().getLabels();

		if (LegendarySpawns.announcer.isAnnounceLegendaries() && pokemon.isLegendary()) {
			Utils.broadcastMessage(
					Utils.formatPlaceholders(LegendarySpawns.announcer.getLegendarySpawnMessage(),
							entity, playerName, formatBiome(biome)));
			LegendarySpawns.LOGGER.info("Legend Spawn: " + pokemon.getDisplayName().getString() +
					" - " + entity.getX() + " " + entity.getY() + " " + entity.getZ());
		}

		if (LegendarySpawns.announcer.isAnnounceShinies() && pokemon.getShiny()) {
			Utils.broadcastMessage(
					Utils.formatPlaceholders(LegendarySpawns.announcer.getShinySpawnMessage(),
							entity, playerName, formatBiome(biome)));
			LegendarySpawns.LOGGER.info("Shiny Spawn: " + pokemon.getDisplayName().getString() +
					" - " + entity.getX() + " " + entity.getY() + " " + entity.getZ());
		}

		if (LegendarySpawns.announcer.isAnnounceUltrabeasts() && pokemon.isUltraBeast()) {
			Utils.broadcastMessage(
					Utils.formatPlaceholders(LegendarySpawns.announcer.getUltrabeastSpawnMessage(),
							entity, playerName, formatBiome(biome)));
			LegendarySpawns.LOGGER.info("Ultrabeast Spawn: " + pokemon.getDisplayName().getString() +
					" - " + entity.getX() + " " + entity.getY() + " " + entity.getZ());
		}

		if (LegendarySpawns.announcer.isAnnounceParadox() && labels.contains("paradox")) {
			Utils.broadcastMessage(
					Utils.formatPlaceholders(LegendarySpawns.announcer.getParadoxSpawnMessage(),
							entity, playerName, formatBiome(biome)));
			LegendarySpawns.LOGGER.info("Paradox Spawn: " + pokemon.getDisplayName().getString() +
					" - " + entity.getX() + " " + entity.getY() + " " + entity.getZ());
		}
	}

	private static SpawnDetails
	getValidDetails(HashMap<ServerPlayerEntity, String> playerBiomes) {

		if (playerBiomes.isEmpty()) {
			return null;
		}

		ArrayList<ServerPlayerEntity> players = new ArrayList<>(playerBiomes.keySet());

		// Random player
		ServerPlayerEntity player = Utils.getRandomValue(players);

		// Get players biome
		String biome = playerBiomes.get(player);

		// All legendary biomes
		HashMap<String, ArrayList<String>> legendaryBiomes = LegendarySpawns.config.getPokemon();


		ArrayList<String> pokemon = new ArrayList<>();

		for (String mon : legendaryBiomes.keySet()) {
			for (String legendaryBiome : legendaryBiomes.get(mon)) {
				if (legendaryBiome.equals(biome)) {
					pokemon.add(mon);
				}
			}
		}

		if (pokemon.isEmpty()) {
			playerBiomes.remove(player);
			return getValidDetails(playerBiomes);
		}

		return new SpawnDetails(player, pokemon);
	}

	private static String formatBiome(String biome) {
		// Removes namespace
		String[] split = biome.split(":");

		String[] names = split[1].split("_");

		String output = "";

		for (String name : names) {
			output += Utils.capitaliseFirst(name) + " ";
		}

		return output.trim();
	}
}
