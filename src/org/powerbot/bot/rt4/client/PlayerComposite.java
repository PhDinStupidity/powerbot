package org.powerbot.bot.rt4.client;

import org.powerbot.bot.ContextAccessor;
import org.powerbot.bot.ReflectionEngine;

public class PlayerComposite extends ContextAccessor {
	public PlayerComposite(final ReflectionEngine engine, final Object parent) {
		super(engine, parent);
	}

	public int[] getAppearance() {
		return engine.access(this, int[].class);
	}
}
