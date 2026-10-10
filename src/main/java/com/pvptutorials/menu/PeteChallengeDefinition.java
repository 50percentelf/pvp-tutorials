package com.pvptutorials.menu;

import com.pvptutorials.tutorial.TutorialId;
import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Defines a single entry in the Pete menu — both the list-row data and the
 * detail-view content.  id is null for stubs not yet implemented.
 */
@Value
@Builder
public class PeteChallengeDefinition
{
	TutorialId id;       // null → stub (shows "Coming Soon")
	int peteRowIndex;    // index into component 9's dyn array for the matching Pete challenge row
	String title;
	String rowDesc;      // one-line description shown in the list row
	String overview;     // paragraph shown in the detail view
	List<String> tips;
	List<String> keyPoints;
}
