package org.pokesplash.legendaryspawns.event;

import org.pokesplash.legendaryspawns.event.events.ExampleEvent;
import org.pokesplash.legendaryspawns.event.obj.Event;

/**
 * Class that holds all of the events.
 */
public abstract class Events {
	public static Event<ExampleEvent> EXAMPLE = new Event<>();

}