package com.pvptutorials.arena;

import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;

/**
 * Detects entry and exit of Pete Kayer's instanced tutorial arenas.
 * Region IDs are populated during Phase 1 instrumentation — see docs/pete-arena.md.
 */
@Slf4j
public class ArenaDetector
{
	// Populated after Phase 1 arena lifecycle discovery.
	private static final int[] PETE_ARENA_REGIONS = {};

	@Inject
	private Client client;

	@Getter
	private final ArenaSession session = new ArenaSession();

	private int lastRegionId = -1;

	public void onGameTick()
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		WorldPoint pos = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation());
		int regionId = pos.getRegionID();

		if (regionId != lastRegionId)
		{
			log.debug("Region changed: {} -> {}", lastRegionId, regionId);
			lastRegionId = regionId;

			if (isPeteArenaRegion(regionId))
			{
				if (!session.isInside())
				{
					int tick = client.getTickCount();
					session.enter(regionId, tick);
					log.debug("Entered Pete arena region {} at tick {}", regionId, tick);

					if (session.hasPendingTutorial())
					{
						session.activate();
						log.debug("Custom tutorial activated: {}", session.getActiveTutorialId());
					}
				}
			}
			else if (session.isInside())
			{
				log.debug("Exited Pete arena region");
				session.exit();
				session.reset();
			}
		}
	}

	public void onGameStateChanged(GameState newState)
	{
		if (newState == GameState.LOGGING_IN || newState == GameState.HOPPING)
		{
			lastRegionId = -1;
			session.reset();
		}
	}

	public void reset()
	{
		lastRegionId = -1;
		session.reset();
	}

	private boolean isPeteArenaRegion(int regionId)
	{
		for (int r : PETE_ARENA_REGIONS)
		{
			if (r == regionId)
			{
				return true;
			}
		}
		return false;
	}
}
