package org.pokesplash.legendaryspawns.config;

import org.pokesplash.legendaryspawns.LegendarySpawns;
import org.pokesplash.legendaryspawns.util.SpawnUtils;

import java.util.Timer;
import java.util.TimerTask;

public abstract class TimerProvider {
	private static Timer timer;

	public static void newTimer() {
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
		timer.cancel();
	}

}
