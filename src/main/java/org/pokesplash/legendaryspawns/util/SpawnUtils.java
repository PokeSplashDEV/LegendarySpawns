package org.pokesplash.legendaryspawns.util;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.api.spawning.context.SpawningContext;
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
import java.util.concurrent.ThreadLocalRandom;

public abstract class SpawnUtils {

	public static void SpawnLegendary() {
		HashMap<ServerPlayerEntity, String> playerBiomes = new HashMap<>();

		ArrayList<ServerPlayerEntity> players =
				new ArrayList<>(LegendarySpawns.world.getPlayerManager().getPlayerList());

		if (players.isEmpty()) {
			return;
		}

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

		int randomNumber = ThreadLocalRandom.current().nextInt(1, 11);

		if (randomNumber <= LegendarySpawns.config.getSpawnChance() * 10) {

			ServerPlayerEntity player = Utils.getRandomValue(players);

			String biome = playerBiomes.get(player);

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
				return;
			}

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
			player.getWorld().spawnEntity(entity);


			checkConditions(entity, player.getName().getString());
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

	public static void checkConditions(PokemonEntity entity, String playerName) {
		Pokemon pokemon = entity.getPokemon();

		if (LegendarySpawns.announcer.isAnnounceLegendaries() && pokemon.isLegendary()) {
			Utils.broadcastMessage(
					Utils.formatPlaceholders(LegendarySpawns.announcer.getLegendaryMessage(),
							entity, playerName));
		}

		if (LegendarySpawns.announcer.isAnnounceShinies() && pokemon.getShiny()) {
			Utils.broadcastMessage(
					Utils.formatPlaceholders(LegendarySpawns.announcer.getShinyMessage(),
							entity, playerName));
		}
	}
}
