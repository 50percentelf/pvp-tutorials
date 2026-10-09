package com.pvptutorials.tutorial;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TutorialId
{
	DEVELOPER_TEST("Developer Test", "Proves the full architecture end-to-end"),
	RANGE_TO_STAFF("Range → Staff", "Practice switching from ranged to staff"),
	MAGE_TO_DHIDE("Mage → D'hide", "Practice switching from mage to dragonhide");

	private final String displayName;
	private final String description;
}
