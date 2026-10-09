package com.pvptutorials.arena;

import com.pvptutorials.tutorial.TutorialId;
import lombok.Getter;

@Getter
public class ArenaSession
{
	private ArenaState   arenaState        = ArenaState.OUTSIDE;
	private TutorialId   pendingTutorialId = null;
	private TutorialId   activeTutorialId  = null;
	private BaseTutorial requiredBase      = null;
	private BaseTutorial detectedBase      = null;
	private int          arenaRegionId     = -1;
	private int          entryTick         = -1;

	/**
	 * Records the custom tutorial the player has selected and the Jagex base tutorial
	 * they must enter to obtain the required server-side mechanics.
	 */
	public void selectTutorial(TutorialId id, BaseTutorial required)
	{
		pendingTutorialId = id;
		requiredBase      = required;
	}

	/**
	 * Called when the player's region changes to a known Pete arena region.
	 * @param detectedBase  which Jagex tutorial corresponds to this region
	 */
	public void enter(int regionId, BaseTutorial detectedBase, int tick)
	{
		arenaRegionId     = regionId;
		this.detectedBase = detectedBase;
		entryTick         = tick;
		arenaState        = ArenaState.ENTERING;
	}

	/**
	 * Promotes a pending tutorial to active once the correct base arena is confirmed.
	 * If no custom tutorial was pending, the session still becomes ACTIVE (vanilla observation).
	 */
	public void activate()
	{
		if (pendingTutorialId != null)
		{
			activeTutorialId  = pendingTutorialId;
			pendingTutorialId = null;
		}
		arenaState = ArenaState.ACTIVE;
	}

	public void exit()
	{
		arenaState = ArenaState.EXITING;
	}

	/**
	 * Resets only the per-visit arena state. The pending tutorial selection is
	 * intentionally preserved so the next arena entry re-activates it automatically.
	 * Call {@link #reset()} on logout/hop to also clear the selection.
	 */
	public void resetArena()
	{
		arenaState       = ArenaState.OUTSIDE;
		activeTutorialId = null;
		detectedBase     = null;
		arenaRegionId    = -1;
		entryTick        = -1;
	}

	/** Full reset including tutorial selection — use on logout or world hop. */
	public void reset()
	{
		resetArena();
		pendingTutorialId = null;
		requiredBase      = null;
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

	public void setDetectedBase(BaseTutorial base)
	{
		detectedBase = base;
	}

	/** True when the arena we entered matches the base the pending tutorial requires. */
	public boolean baseMatchesPending()
	{
		return requiredBase != null && requiredBase == detectedBase;
	}
}
