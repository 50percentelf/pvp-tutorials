package com.pvptutorials.simulation;

import lombok.Getter;
import lombok.Setter;

/**
 * Visual equipment loadout for a virtual actor.
 * Tutorial logic references semantic Loadout values rather than raw item IDs.
 */
@Getter
@Setter
public class VirtualEquipment
{
	public enum Loadout
	{
		BARE,
		RANGE,
		MAGE,
		TANK,
		AGS,
		VOIDWAKER
	}

	private int headItemId    = -1;
	private int capeItemId    = -1;
	private int amuletItemId  = -1;
	private int weaponItemId  = -1;
	private int bodyItemId    = -1;
	private int shieldItemId  = -1;
	private int legsItemId    = -1;
	private int glovesItemId  = -1;
	private int bootsItemId   = -1;
	private int ringItemId    = -1;

	public void reset()
	{
		headItemId    = -1;
		capeItemId    = -1;
		amuletItemId  = -1;
		weaponItemId  = -1;
		bodyItemId    = -1;
		shieldItemId  = -1;
		legsItemId    = -1;
		glovesItemId  = -1;
		bootsItemId   = -1;
		ringItemId    = -1;
	}
}
