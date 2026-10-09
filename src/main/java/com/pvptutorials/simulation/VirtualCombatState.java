package com.pvptutorials.simulation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VirtualCombatState
{
	private int currentHp        = 99;
	private int maxHp            = 99;
	private int currentPrayer    = 99;
	private int maxPrayer        = 99;
	private int frozenTicks      = 0;
	private int attackDelayTicks = 0;

	private final VirtualPrayerState prayer = new VirtualPrayerState();

	public void tick()
	{
		if (frozenTicks > 0)      frozenTicks--;
		if (attackDelayTicks > 0) attackDelayTicks--;
	}

	public boolean isFrozen()
	{
		return frozenTicks > 0;
	}

	public void reset()
	{
		currentHp        = maxHp;
		currentPrayer    = maxPrayer;
		frozenTicks      = 0;
		attackDelayTicks = 0;
		prayer.reset();
	}
}
