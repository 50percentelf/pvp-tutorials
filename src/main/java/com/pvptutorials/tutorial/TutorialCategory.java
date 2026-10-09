package com.pvptutorials.tutorial;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TutorialCategory
{
	FUNDAMENTALS("Fundamentals"),
	SWITCHING("Switching"),
	PRAYER("Prayer"),
	FREEZES("Freezes"),
	EATING("Eating"),
	SMITE("Smite"),
	KO_COMBOS("KO Combos"),
	MOVEMENT("Movement"),
	NH("NH");

	private final String displayName;
}
