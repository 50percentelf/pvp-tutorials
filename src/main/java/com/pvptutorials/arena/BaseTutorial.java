package com.pvptutorials.arena;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The official Jagex tutorials Pete Kayer provides.
 * Each custom tutorial declares which BaseTutorial it requires to obtain the right
 * server-side mechanics (spec energy, real freezes, equipment state, etc.).
 * Arena region IDs are populated in ArenaDetector after Phase 1/2 research.
 */
@Getter
@RequiredArgsConstructor
public enum BaseTutorial
{
	PRAYER_PROTECTION("Prayer Protection"),
	POWER_OF_FREEZES("Power of Freezes"),
	SPECIAL_ATTACKS("Special Attacks"),
	GEAR_SWITCHING("Gear Switching"),
	COMBO_EATING("Combo Eating"),
	PENULTIMATE_CHALLENGE("Penultimate Challenge"),
	FINAL_CHALLENGE("Final Challenge");

	private final String displayName;
}
