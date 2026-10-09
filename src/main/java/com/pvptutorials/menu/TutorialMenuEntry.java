package com.pvptutorials.menu;

import com.pvptutorials.tutorial.TutorialId;
import lombok.Value;

@Value
public class TutorialMenuEntry
{
	TutorialId id;
	String displayName;
	String description;
}
