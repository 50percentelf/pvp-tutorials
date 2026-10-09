package com.pvptutorials.tutorial;

import com.pvptutorials.arena.ArenaDetector;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.MenuOptionClicked;

@Slf4j
public class TutorialManager
{
	@Inject
	private TutorialRegistry registry;

	@Inject
	private ArenaDetector arenaDetector;

	@Getter
	private Tutorial activeTutorial = null;

	public void requestTutorial(TutorialId id)
	{
		TutorialDefinition def = registry.getDefinition(id);
		if (def == null)
		{
			log.warn("Requested undefined tutorial: {}", id);
			return;
		}
		if (!registry.isImplemented(id))
		{
			log.warn("Tutorial not yet implemented: {}", id);
			return;
		}
		arenaDetector.getSession().selectTutorial(id, def.getRequiredBase());
		log.debug("Tutorial selected: {} (requires base={})", id, def.getRequiredBase());
	}

	public void onArenaActivated()
	{
		TutorialId id = arenaDetector.getSession().getActiveTutorialId();
		if (id == null)
		{
			return;
		}
		Tutorial tutorial = registry.getImplementation(id);
		if (tutorial == null)
		{
			log.warn("No implementation for tutorial: {}", id);
			return;
		}
		activeTutorial = tutorial;
		activeTutorial.start();
		log.debug("Tutorial started: {}", id);
	}

	public void onGameTick()
	{
		if (activeTutorial != null)
		{
			activeTutorial.onGameTick();
		}
	}

	public void onClientTick()
	{
		if (activeTutorial != null)
		{
			activeTutorial.onClientTick();
		}
	}

	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (activeTutorial != null)
		{
			activeTutorial.onMenuOptionClicked(event);
		}
	}

	public void stopActiveTutorial()
	{
		if (activeTutorial != null)
		{
			activeTutorial.stop();
			activeTutorial = null;
		}
	}

	public void resetActiveTutorial()
	{
		if (activeTutorial != null)
		{
			activeTutorial.reset();
		}
	}

	public TutorialState getActiveState()
	{
		return activeTutorial != null ? activeTutorial.getState() : TutorialState.NONE;
	}
}
