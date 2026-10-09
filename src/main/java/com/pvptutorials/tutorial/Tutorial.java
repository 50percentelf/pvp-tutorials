package com.pvptutorials.tutorial;

import com.pvptutorials.observation.TutorialEvent;
import net.runelite.api.events.MenuOptionClicked;

/**
 * Implemented by each individual tutorial. Lifecycle methods are driven by TutorialManager.
 *
 * Tutorials should respond to normalised TutorialEvents via onTutorialEvent() rather than
 * directly consuming raw RuneLite events.  onMenuOptionClicked() remains available as an
 * escape hatch for tutorials that need access to the raw event before the observation layer
 * is extended to cover their required event type.
 */
public interface Tutorial
{
	TutorialId getId();

	TutorialState getState();

	/** Called when the tutorial becomes active inside the arena. */
	void start();

	/** Called once per game tick while the tutorial is active. */
	default void onGameTick() {}

	/** Called once per client tick while the tutorial is active. */
	default void onClientTick() {}

	/**
	 * Called with a normalised event from the observation layer.
	 * This is the primary input path for tutorials — prefer this over onMenuOptionClicked.
	 */
	default void onTutorialEvent(TutorialEvent event) {}

	/**
	 * Called with the raw RuneLite menu event — observe only, never automate.
	 * Use this only when the observation layer does not yet translate the event you need.
	 */
	default void onMenuOptionClicked(MenuOptionClicked event) {}

	/** Resets the tutorial to its initial state without exiting the arena. */
	void reset();

	/** Fully stops and cleans up the tutorial. */
	void stop();

	/** Returns the instruction text for the current step, or null if none. */
	default String getActiveInstruction()
	{
		return null;
	}
}
