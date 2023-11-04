package org.pokesplash.legendaryspawns.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import kotlin.Unit;
import org.pokesplash.legendaryspawns.util.SpawnUtils;

public class PokemonSpawnEvent {
	public void registerEvent() {
		CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.NORMAL , e -> {

			SpawnUtils.checkConditions(e.getEntity(),
					e.getCtx().getCause().getEntity().getName().getString());

			return Unit.INSTANCE;
		});
	}
}
