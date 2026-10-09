package com.pvptutorials.arena;

import java.util.Collections;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;

/**
 * Detects entry and exit of Pete Kayer's instanced tutorial arenas.
 *
 * REGION_TO_BASE maps region ID → BaseTutorial.
 * Populated during Phase 1/2 research — see docs/pete-arena.md and docs/pete-capabilities.md.
 * Each BaseTutorial may have multiple region IDs (instanced regions share geometry
 * but have unique IDs per instance).
 */
@Slf4j
public class ArenaDetector
{
	// Populated after Phase 1/2 arena lifecycle discovery.
	// Key: region ID   Value: which Jagex tutorial corresponds to that region
	private static final Map<Integer, BaseTutorial> REGION_TO_BASE = Collections.emptyMap();

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

		if (regionId == lastRegionId)
		{
			return;
		}

		log.debug("Region changed: {} -> {}", lastRegionId, regionId);
		lastRegionId = regionId;

		BaseTutorial detected = REGION_TO_BASE.get(regionId);

		if (detected != null)
		{
			if (!session.isInside())
			{
				int tick = client.getTickCount();
				session.enter(regionId, detected, tick);
				log.debug("Entered Pete arena region {} ({}) at tick {}", regionId, detected, tick);

				if (session.hasPendingTutorial() && session.baseMatchesPending())
				{
					session.activate();
					log.debug("Custom tutorial activated: {} (base={})",
						session.getActiveTutorialId(), detected);
				}
				else if (session.hasPendingTutorial())
				{
					log.debug("Entered wrong base for pending tutorial {} (needed {}, got {})",
						session.getPendingTutorialId(), session.getRequiredBase(), detected);
				}
			}
		}
		else if (session.isInside())
		{
			log.debug("Exited Pete arena region (was {})", session.getDetectedBase());
			session.exit();
			session.reset();
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
}
