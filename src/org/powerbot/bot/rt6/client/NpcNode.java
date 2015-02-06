package org.powerbot.bot.rt6.client;

import org.powerbot.bot.Reflector;

public class NpcNode extends Node {
	public NpcNode(final Reflector engine, final Object parent) {
		super(engine, parent);
	}

	public Npc getNpc() {
		return new Npc(reflector, reflector.access(this));
	}
}
