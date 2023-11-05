package org.pokesplash.legendaryspawns.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import kotlin.Unit;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.pokesplash.legendaryspawns.util.SpawnUtils;

public class PokemonSpawnEvent {
	public void registerEvent() {
		CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.NORMAL , e -> {

			World world = e.getCtx().getWorld();

			BlockPos pos = new BlockPos(
					(int) e.getEntity().getX(),
					(int) e.getEntity().getY(),
					(int) e.getEntity().getZ());

			RegistryKey<Biome> biome = world.getBiome(pos).getKey().get();

			SpawnUtils.checkConditions(e.getEntity(),
					e.getCtx().getCause().getEntity().getName().getString(),
					biome.getValue().toString());

			return Unit.INSTANCE;
		});
	}
}
