package org.powerbot.script.internal.randoms;

import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.internal.InternalScript;
import org.powerbot.script.methods.Game;
import org.powerbot.script.util.Random;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.Component;
import org.powerbot.script.wrappers.Item;
import org.powerbot.script.wrappers.Player;
import org.powerbot.script.wrappers.Widget;
import org.powerbot.util.Tracker;

@Manifest(name = "Spin ticket destroyer", authors = {"Timer"}, description = "Claims or destroys spin tickets")
public class TicketDestroy extends PollingScript implements InternalScript {
	private static final int[] ITEM_IDS = {24154, 24155};
	private Component component;

	@Override
	public boolean isValid() {
		if (!ctx.game.isLoggedIn() || ctx.game.getCurrentTab() != Game.TAB_INVENTORY) {
			return false;
		}

		final Player player;
		if ((player = ctx.players.getLocal()) == null ||
				player.isInCombat() || player.getAnimation() != -1 || player.getInteracting() != null) {
			return false;
		}

		this.component = null;
		Item item = ctx.inventory.select().id(ITEM_IDS).first();
		if (item.isValid()) {
			this.component = item.getComponent();
			return this.component.isValid();
		}
		return false;
	}

	@Override
	public int poll() {
		Component item = this.component;
		if (item == null) return -1;

		Tracker.getInstance().trackPage("randoms/TicketDestroy/", "");

		if (((ctx.settings.get(1448) & 0xFF00) >>> 8) < (item.getItemId() == ITEM_IDS[0] ? 10 : 9)) {
			item.interact("Claim spin");
			sleep(1000, 2000);
		}

		if (!item.interact("Destroy")) {
			return Random.nextInt(1000, 2000);
		}

		Widget widget = ctx.widgets.get(1183);
		final Timer timer = new Timer(Random.nextInt(4000, 6000));
		while (timer.isRunning() && !widget.isValid()) {
			sleep(100, 200);
		}
		if (!widget.isValid()) {
			return -1;
		}
		Component component = null;
		for (final Component c : widget.getComponents()) {
			if (c.isVisible() && c.getTooltip().trim().equalsIgnoreCase("destroy")) {
				component = c;
				break;
			}
		}
		if (component != null && component.interact("Destroy")) {
			final Timer t = new Timer(Random.nextInt(1500, 2000));
			while (t.isRunning() && item.getItemId() != -1) {
				sleep(100, 250);
			}
		}
		return -1;
	}

	@Override
	public int getPriority() {
		return PRIORITY_HIGH;
	}
}
