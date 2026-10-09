package com.pvptutorials.arena;

import com.pvptutorials.tutorial.TutorialId;
import lombok.Getter;

@Getter
public class ArenaSession
{
	private ArenaState arenaState = ArenaState.OUTSIDE;
	private TutorialId pendingTutorialId = null;
	private TutorialId activeTutorialId = null;
	private int arenaRegionId = -1;
	private int entryTick = -1;

	public void selectTutorial(TutorialId id)
	{
		pendingTutorialId = id;
	}

	public void enter(int regionId, int tick)
	{
		arenaRegionId = regionId;
		entryTick = tick;
		arenaState = ArenaState.ENTERING;
	}

	public void activate()
	{
		if (pendingTutorialId != null)
		{
			activeTutorialId = pendingTutorialId;
			pendingTutorialId = null;
		}
		arenaState = ArenaState.ACTIVE;
	}

	public void exit()
	{
		arenaState = ArenaState.EXITING;
	}

	public void reset()
	{
		arenaState = ArenaState.OUTSIDE;
		pendingTutorialId = null;
		activeTutorialId = null;
		arenaRegionId = -1;
		entryTick = -1;
	}

	public boolean isInside()
	{
		return arenaState == ArenaState.ENTERING || arenaState == ArenaState.ACTIVE;
	}

	public boolean isActive()
	{
		return arenaState == ArenaState.ACTIVE;
	}

	public boolean hasPendingTutorial()
	{
		return pendingTutorialId != null;
	}
}
