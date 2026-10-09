package com.pvptutorials.observation;

import java.util.Optional;
import java.util.Set;
import javax.inject.Singleton;
import net.runelite.api.events.MenuOptionClicked;

/**
 * Translates raw RuneLite events into normalised TutorialEvents.
 *
 * This is the boundary between "what happened in RuneScape" and "what the
 * tutorial framework considers significant".  Only ITEM_EQUIPPED is wired in
 * Phase 3; additional translations are added as each TutorialEventType is
 * supported.
 */
@Singleton
public class ObservationLayer
{
	private static final Set<String> EQUIP_OPTIONS = Set.of("Wield", "Wear", "Equip");

	public Optional<TutorialEvent> fromMenuOptionClicked(MenuOptionClicked event)
	{
		if (EQUIP_OPTIONS.contains(event.getMenuOption()))
		{
			return Optional.of(new TutorialEvent(TutorialEventType.ITEM_EQUIPPED));
		}
		return Optional.empty();
	}
}
