package com.pvptutorials.tutorial;

import com.pvptutorials.arena.BaseTutorial;
import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Immutable metadata for a tutorial entry in the built-in catalog.
 * Registered for every tutorial (including PLACEHOLDER entries) so the library
 * UI can display the full list.  Use TutorialRegistry.isImplemented() before
 * attempting to start one.
 *
 * requiredBase is nullable — null means the correct Pete base has not yet been
 * confirmed through in-game research and the tutorial cannot be activated.
 */
@Value
@Builder
public class TutorialMetadata
{
	TutorialId id;
	String displayName;
	String description;
	TutorialCategory category;
	TutorialDifficulty difficulty;
	List<String> tags;
	BaseTutorial requiredBase;
	TutorialStatus status;
}
