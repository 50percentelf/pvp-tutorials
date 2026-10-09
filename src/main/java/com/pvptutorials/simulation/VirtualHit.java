package com.pvptutorials.simulation;

import lombok.Value;

@Value
public class VirtualHit
{
	int damage;
	HitType type;
	int displayTick;

	public enum HitType
	{
		NORMAL,
		SPLASH,
		POISON,
		VENOM,
		BLOCK
	}
}
