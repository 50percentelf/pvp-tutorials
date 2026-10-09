package com.pvptutorials.tutorial;

import com.pvptutorials.arena.ArenaDetector;
import com.pvptutorials.observation.ObservationLayer;
import com.pvptutorials.observation.TutorialEvent;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.MenuOptionClicked;

@Slf4j
@Singleton
public class TutorialManager
{
	@Inject
	private TutorialRegistry registry;

	@Inject
	private ArenaDetector arenaDetector;

	@Inject
	private ObservationLayer observationLayer;

	@Getter
	private Tutorial activeTutorial = null;
	@Getter
	private boolean arenaInitiated = false;

	public void requestTutorial(TutorialId id)
	{
		TutorialMetadata meta = registry.getMetadata(id);
		if (meta == null)
		{
			log.warn("Requested undefined tutorial: {}", id);
			return;
		}
		if (!registry.isImplemented(id))
		{
			log.warn("Tutorial not yet implemented: {}", id);
			return;
		}
		arenaDetector.getSession().selectTutorial(id, meta.getRequiredBase());
		log.debug("Tutorial selected: {} (requires base={})", id, meta.getRequiredBase());
	}

	/**
	 * Starts a tutorial immediately without waiting for arena entry.
	 * Used by client-side menu entries that don't trigger a server teleport.
	 */
	public void startTutorial(TutorialId id)
	{
		Tutorial tutorial = registry.getImplementation(id);
		if (tutorial == null)
		{
			log.warn("No implementation for tutorial: {}", id);
			return;
		}
		if (activeTutorial != null)
		{
			activeTutorial.stop();
		}
		activeTutorial = tutorial;
		activeTutorial.start();
		arenaInitiated = false;
		log.debug("Tutorial started directly: {}", id);
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
		arenaInitiated = true;
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
		if (activeTutorial == null)
		{
			return;
		}
		observationLayer.fromMenuOptionClicked(event)
			.ifPresent(this::onTutorialEvent);
		activeTutorial.onMenuOptionClicked(event);
	}

	public void onTutorialEvent(TutorialEvent event)
	{
		if (activeTutorial != null)
		{
			activeTutorial.onTutorialEvent(event);
		}
	}

	public void stopActiveTutorial()
	{
		if (activeTutorial != null)
		{
			activeTutorial.stop();
			activeTutorial = null;
			arenaInitiated = false;
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
