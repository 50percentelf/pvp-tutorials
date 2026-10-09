package com.pvptutorials.simulation;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.coords.LocalPoint;

/**
 * Client-side simulated opponent.
 * Phase 3 proof of concept: fixed position, configurable equipment, one animation.
 */
@Getter
@Setter
public class VirtualActor
{
	private LocalPoint position  = null;
	private int orientation      = 0;
	private int animationId      = -1;
	private boolean visible      = false;

	private final VirtualEquipment equipment = new VirtualEquipment();
	private final VirtualCombatState combat  = new VirtualCombatState();

	public void show(LocalPoint at)
	{
		position = at;
		visible  = true;
	}

	public void hide()
	{
		visible = false;
	}

	public void tick()
	{
		combat.tick();
	}

	public void reset()
	{
		position    = null;
		orientation = 0;
		animationId = -1;
		visible     = false;
		equipment.reset();
		combat.reset();
	}
}
