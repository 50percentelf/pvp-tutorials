package com.pvptutorials.menu;

import com.pvptutorials.tutorial.TutorialId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Ordered list of challenges shown under "Additional Training" in Pete's menu.
 * Add new entries here to have them appear automatically in the list.
 */
public final class PeteChallengeManifest
{
	private PeteChallengeManifest() {}

	public static final List<PeteChallengeDefinition> ALL = Collections.unmodifiableList(Arrays.asList(

		PeteChallengeDefinition.builder()
			.id(TutorialId.RANGE_TO_STAFF)
			.peteRowIndex(3)  // Gear Switching arena
			.title("Range to Staff")
			.rowDesc("Attack at range, then equip your staff.")
			.overview("Transition from ranged to magic combat by landing a ranged hit on your target, then immediately equipping a magic staff. This drill trains the weapon swap that opens most mage-based combos.")
			.tips(Arrays.asList(
				"Any bow or crossbow counts — you don't need bolts",
				"Equip the staff before the opponent's next attack lands"))
			.keyPoints(Arrays.asList(
				"Ranged attack confirmed",
				"Staff equipped"))
			.build(),

		PeteChallengeDefinition.builder()
			.id(TutorialId.STAFF_TO_RANGE)
			.peteRowIndex(3)  // Gear Switching arena
			.title("Staff to Range")
			.rowDesc("Cast a spell, then switch to your ranged setup.")
			.overview("After landing a magic spell, swap to your ranged gear to apply pressure with a different attack style. This is the reverse transition to Range → Staff and is essential for tribrid play.")
			.tips(Arrays.asList(
				"Cast any offensive spell to complete the magic step",
				"Pre-position your ranged gear in easy-to-reach inventory slots"))
			.keyPoints(Arrays.asList(
				"Magic spell cast",
				"Ranged weapon equipped"))
			.build(),

		PeteChallengeDefinition.builder()
			.id(TutorialId.FREEZE_TO_DHIDE)
			.peteRowIndex(1)  // Power of Freezes arena
			.title("Freeze to D'hide")
			.rowDesc("Land a freeze, then switch to dragonhide armour.")
			.overview("Freeze your opponent with an ice spell and immediately switch into dragonhide armour to tank the incoming ranged retaliation. This transition is a core defensive move after committing a freeze.")
			.tips(Arrays.asList(
				"Ice Rush is fine for practice — barrage is not required",
				"Pre-equip your d'hide body and chaps before the fight"))
			.keyPoints(Arrays.asList(
				"Freeze spell lands",
				"D'hide body or chaps equipped"))
			.build(),

		PeteChallengeDefinition.builder()
			.id(TutorialId.SHARK_KARAMBWAN)
			.peteRowIndex(4)  // Combo Eating arena
			.title("Shark + Karambwan")
			.rowDesc("Combo eat: shark then karambwan in the same tick.")
			.overview("Eat a shark and a karambwan in the same game tick to recover a large amount of HP without losing an attack. Combo eating is one of the most impactful mechanical skills in PvP and this drill isolates the timing.")
			.tips(Arrays.asList(
				"Click shark, immediately click karambwan — do not wait",
				"Both items must be in your inventory; prepare them before the fight"))
			.keyPoints(Arrays.asList(
				"Shark consumed",
				"Karambwan consumed same tick"))
			.build()

	));
}
