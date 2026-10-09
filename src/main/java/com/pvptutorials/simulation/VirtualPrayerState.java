package com.pvptutorials.simulation;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.HeadIcon;

@Getter
@Setter
public class VirtualPrayerState
{
	private HeadIcon overheadPrayer = null;
	private boolean protectMelee   = false;
	private boolean protectRange   = false;
	private boolean protectMagic   = false;
	private boolean smite          = false;

	public void reset()
	{
		overheadPrayer = null;
		protectMelee   = false;
		protectRange   = false;
		protectMagic   = false;
		smite          = false;
	}
}
