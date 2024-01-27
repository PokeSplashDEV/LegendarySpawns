package org.pokesplash.legendaryspawns.config;

import com.cobblemon.mod.common.entity.pokemon.ai.goals.SleepOnTrainerGoal;
import net.minecraft.text.Text;
import org.pokesplash.legendaryspawns.LegendarySpawns;
import org.pokesplash.legendaryspawns.util.SpawnUtils;
import org.pokesplash.legendaryspawns.util.Utils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class TimerProvider {
	private static Timer timer;
	private static ArrayList<Booster> boosters = new ArrayList<>();
	private static Booster activeBooster = null;

	public static void newTimer() {

		shutdown();

		Timer newTimer = new Timer();
		newTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				SpawnUtils.SpawnLegendary();
				newTimer();
			}
		}, 1000 * 60 * (long) LegendarySpawns.config.getTimer());
		timer = newTimer;
	}

	public static void init() {
		newTimer();
	}

	public static void shutdown() {
		if (timer != null) {
			timer.cancel();
		}
	}

	public static void setBooster(Booster booster) {

		if (activeBooster == null) {
			activeBooster = booster;
			startBooster(booster);
		} else {
			boosters.add(booster);
		}
	}

	public static void startBooster(Booster booster) {

		LegendarySpawns.config.setTimer(booster.getSpawnTime());
		LegendarySpawns.config.setSpawnChance(booster.getSpawnRate());

		newTimer();

		Timer newTimer = new Timer();
		newTimer.schedule(new TimerTask() {
			@Override
			public void run() {

				Utils.broadcastMessage(Text.of(LegendarySpawns.announcer.getBoosterEndMessage()));

				if (boosters.isEmpty()) {
					LegendarySpawns.config.init();
					newTimer();
					activeBooster = null;
				} else {
					activeBooster = boosters.get(0);
					boosters.remove(0);
					startBooster(activeBooster);
				}
			}
		}, 1000 * 60 * (long) booster.getBoosterDuration());

		Utils.broadcastMessage(Text.of(LegendarySpawns.announcer.getBoosterMessage().replaceAll("\\{time\\}",
				String.valueOf(booster.getBoosterDuration()))));
	}
}
