package com.pvptutorials.arena;

import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.VarbitChanged;

/**
 * Detects entry and exit of Pete Kayer's instanced tutorial arenas.
 *
 * All Pete tutorials share the same base region (10588). The specific tutorial
 * is identified by a varbit that fires with value=1 shortly after entry.
 * See docs/pete-arena.md for the full mapping once all tutorials are observed.
 */
@Slf4j
public class ArenaDetector
{
	// All Pete Kayer tutorial instances share this base region (Phase 1 finding).
	static final int PETE_ARENA_REGION = 10588;

	// Varbit ID → BaseTutorial: fires value=1 on arena entry, identifies which tutorial.
	// Incomplete — only GEAR_SWITCHING and SPECIAL_ATTACKS observed so far.
	// Run the remaining 5 tutorials with debug enabled to populate the rest.
	private static final Map<Integer, BaseTutorial> VARBIT_TO_BASE = Map.of(
		16309, BaseTutorial.GEAR_SWITCHING,    // varpId=5889
		16303, BaseTutorial.SPECIAL_ATTACKS    // varpId=5888
	);

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

		if (regionId == PETE_ARENA_REGION)
		{
			if (!session.isInside())
			{
				int tick = client.getTickCount();
				session.enter(regionId, null, tick);
				log.debug("Entered Pete arena at tick {} — base TBD (awaiting varbit)", tick);
			}
		}
		else if (session.isInside())
		{
			log.debug("Exited Pete arena (was {})", session.getDetectedBase());
			session.exit();
			session.reset();
		}
	}

	public void onVarbitChanged(VarbitChanged event)
	{
		if (!session.isInside() || session.getDetectedBase() != null || event.getValue() != 1)
		{
			return;
		}
		BaseTutorial detected = VARBIT_TO_BASE.get(event.getVarbitId());
		if (detected == null)
		{
			return;
		}
		session.setDetectedBase(detected);
		log.debug("Tutorial base identified via varbit {}: {}", event.getVarbitId(), detected);

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
