package org.pokesplash.legendaryspawns.util;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;

public class SpawnDetails {
	private ServerPlayerEntity player;
	private ArrayList<String> pokemon;

	public SpawnDetails(ServerPlayerEntity player, ArrayList<String> pokemon) {
		this.player = player;
		this.pokemon = pokemon;
	}

	public ServerPlayerEntity getPlayer() {
		return player;
	}

	public ArrayList<String> getPokemon() {
		return pokemon;
	}
}
