package com.pvptutorials.observation;

/**
 * Canonical set of observable in-game actions that tutorials can react to.
 *
 * Only ITEM_EQUIPPED is wired in Phase 3. The remaining types are declared now
 * so step conditions and tutorial logic can reference them without a future
 * package restructure.  Payload fields (e.g. hit value for HIT_RESOLVED) will
 * be added to TutorialEvent when each type is wired up.
 */
public enum TutorialEventType
{
	ITEM_EQUIPPED,
	WEAPON_CHANGED,
	ATTACK_STARTED,
	HIT_RESOLVED,
	PRAYER_CHANGED,
	SPELL_CAST,
	PLAYER_MOVED,
	FOOD_CONSUMED,
	POTION_CONSUMED,
	SPECIAL_ATTACK_USED
}
