package com.pvptutorials.observation;

import lombok.Value;

/**
 * A normalised in-game action dispatched to the active tutorial.
 *
 * Intentionally minimal for Phase 3 — carries only the type.
 * Typed payload fields (e.g. int hitValue for HIT_RESOLVED) will be added
 * as each event type is wired up in later phases.
 */
@Value
public class TutorialEvent
{
	TutorialEventType type;
}
