package org.pokesplash.legendaryspawns.event;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import kotlin.Unit;
import org.pokesplash.legendaryspawns.LegendarySpawns;
import org.pokesplash.legendaryspawns.util.Utils;

public class PokemonCatchEvent {
	public void registerEvent() {
		CobblemonEvents.POKEMON_CAPTURED.subscribe(Priority.NORMAL, e -> {

			if (LegendarySpawns.announcer.isAnnounceShinies() && e.getPokemon().getShiny()) {
				Utils.broadcastMessage(Utils.formatPlaceholders(LegendarySpawns.announcer.getShinyCaughtMessage(),
						e.getPokemon(), e.getPlayer().getName().getString(), LegendarySpawns.announcer.isCaptureHoverable()));
			}

			if (LegendarySpawns.announcer.isAnnounceLegendaries() && e.getPokemon().isLegendary()) {
				Utils.broadcastMessage(Utils.formatPlaceholders(LegendarySpawns.announcer.getLegendaryCaughtMessage(),
						e.getPokemon(), e.getPlayer().getName().getString(), LegendarySpawns.announcer.isCaptureHoverable()));
			}

			if (LegendarySpawns.announcer.isAnnounceUltrabeasts() && e.getPokemon().isUltraBeast()) {
				Utils.broadcastMessage(Utils.formatPlaceholders(LegendarySpawns.announcer.getUltrabeastCaughtMessage(),
						e.getPokemon(), e.getPlayer().getName().getString(), LegendarySpawns.announcer.isCaptureHoverable()));
			}

			if (LegendarySpawns.announcer.isAnnounceParadox() && e.getPokemon().getSpecies().getLabels().contains(
					"paradox")) {
				Utils.broadcastMessage(Utils.formatPlaceholders(LegendarySpawns.announcer.getParadoxCaughtMessage(),
						e.getPokemon(), e.getPlayer().getName().getString(), LegendarySpawns.announcer.isCaptureHoverable()));
			}

			return Unit.INSTANCE;
		});
	}
}
