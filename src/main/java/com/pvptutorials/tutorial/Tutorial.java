package com.pvptutorials.tutorial;

import net.runelite.api.events.MenuOptionClicked;

/**
 * Implemented by each individual tutorial. Lifecycle methods are driven by TutorialManager.
 */
public interface Tutorial
{
	TutorialId getId();

	TutorialState getState();

	/** Called when the tutorial becomes active inside the arena. */
	void start();

	/** Called once per game tick while the tutorial is active. */
	void onGameTick();

	/** Called once per client tick while the tutorial is active. */
	void onClientTick();

	/** Called when the player performs a menu action — observe only, never automate. */
	void onMenuOptionClicked(MenuOptionClicked event);

	/** Resets the tutorial to its initial state without exiting the arena. */
	void reset();

	/** Fully stops and cleans up the tutorial. */
	void stop();
}
