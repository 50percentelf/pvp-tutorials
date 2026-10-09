package com.pvptutorials.tutorial;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TutorialDifficulty
{
	BEGINNER("Beginner"),
	INTERMEDIATE("Intermediate"),
	ADVANCED("Advanced");

	private final String displayName;
}
