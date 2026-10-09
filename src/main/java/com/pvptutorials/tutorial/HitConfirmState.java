package com.pvptutorials.tutorial;

/**
 * State machine for the hit-confirmation decision loop used in spec tutorials.
 *
 * The core principle: the player's job is not just to execute a combo —
 * it is to read the opening hit and decide GO or ABORT based on whether it
 * justifies spending spec.
 */
public enum HitConfirmState
{
	NONE,
	OPENING_ATTACK,
	WAITING_FOR_HIT,
	HIT_CONFIRMED,
	COMMIT,
	ABORT,
	FOLLOW_UP,
	COMPLETE
}
