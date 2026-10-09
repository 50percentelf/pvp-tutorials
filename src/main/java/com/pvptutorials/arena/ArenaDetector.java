package com.pvptutorials.arena;

import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.VarbitChanged;

/**
 * Detects entry and exit of Pete Kayer's instanced tutorial arenas.
 *
 * Five base tutorials share region 10588 and are identified by a varbit that fires
 * value=1 shortly after entry. The final two (PENULTIMATE_CHALLENGE, FINAL_CHALLENGE)
 * share region 11100 — no distinguishing varbit was observed for either; a follow-up
 * using chat-message detection is needed to distinguish them.
 * See docs/pete-arena.md for the full region/varbit mapping.
 */
@Slf4j
@Singleton
public class ArenaDetector
{
	// Regions used by Pete Kayer tutorial instances (Phase 1 finding).
	// 10588 — five regular tutorials (identified by varbit).
	// 11100 — PENULTIMATE_CHALLENGE and FINAL_CHALLENGE (no distinguishing varbit found).
	static final Set<Integer> PETE_ARENA_REGIONS = Set.of(10588, 11100);

	// Varbit ID → BaseTutorial: fires value=1 on arena entry for the 10588 tutorials.
	// PENULTIMATE_CHALLENGE and FINAL_CHALLENGE (region 11100) have no entry in this map.
	private static final Map<Integer, BaseTutorial> VARBIT_TO_BASE = Map.of(
		16309, BaseTutorial.GEAR_SWITCHING,     // varpId=5889
		16303, BaseTutorial.SPECIAL_ATTACKS,    // varpId=5888
		10670, BaseTutorial.PRAYER_PROTECTION,  // varpId=1021
		16306, BaseTutorial.POWER_OF_FREEZES,   // varpId=5888
		16315, BaseTutorial.COMBO_EATING        // varpId=5890
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

		if (PETE_ARENA_REGIONS.contains(regionId))
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
			session.resetArena();
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
