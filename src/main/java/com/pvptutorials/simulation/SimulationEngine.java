package com.pvptutorials.simulation;

import javax.inject.Inject;
import lombok.Getter;
import net.runelite.api.Client;

/**
 * Drives per-tick simulation updates for the active tutorial session.
 * Full combat simulation is deferred until the architecture is proven in Phase 5.
 */
public class SimulationEngine
{
	@Inject
	private Client client;

	@Getter
	private final VirtualActor opponent = new VirtualActor();

	public void onGameTick()
	{
		if (opponent.isVisible())
		{
			opponent.tick();
		}
	}

	public void reset()
	{
		opponent.reset();
	}
}
