package com.pvptutorials.tutorial;

import com.pvptutorials.arena.BaseTutorial;
import lombok.Value;

/**
 * Static metadata for a custom tutorial.
 * Registered for ALL tutorials (including unimplemented placeholders) so the menu
 * can display them. Use TutorialRegistry.isImplemented() before starting.
 */
@Value
public class TutorialDefinition
{
	TutorialId id;
	BaseTutorial requiredBase;
	String displayName;
	String description;
}
