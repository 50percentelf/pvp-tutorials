package com.pvptutorials.tutorial;

import com.pvptutorials.arena.BaseTutorial;
import java.util.List;

import static com.pvptutorials.arena.BaseTutorial.COMBO_EATING;
import static com.pvptutorials.arena.BaseTutorial.GEAR_SWITCHING;
import static com.pvptutorials.arena.BaseTutorial.POWER_OF_FREEZES;
import static com.pvptutorials.arena.BaseTutorial.PRAYER_PROTECTION;
import static com.pvptutorials.arena.BaseTutorial.SPECIAL_ATTACKS;
import static com.pvptutorials.tutorial.TutorialCategory.EATING;
import static com.pvptutorials.tutorial.TutorialCategory.FREEZES;
import static com.pvptutorials.tutorial.TutorialCategory.FUNDAMENTALS;
import static com.pvptutorials.tutorial.TutorialCategory.KO_COMBOS;
import static com.pvptutorials.tutorial.TutorialCategory.MOVEMENT;
import static com.pvptutorials.tutorial.TutorialCategory.NH;
import static com.pvptutorials.tutorial.TutorialCategory.PRAYER;
import static com.pvptutorials.tutorial.TutorialCategory.SMITE;
import static com.pvptutorials.tutorial.TutorialCategory.SWITCHING;
import static com.pvptutorials.tutorial.TutorialDifficulty.ADVANCED;
import static com.pvptutorials.tutorial.TutorialDifficulty.BEGINNER;
import static com.pvptutorials.tutorial.TutorialDifficulty.INTERMEDIATE;
import static com.pvptutorials.tutorial.TutorialId.*;
import static com.pvptutorials.tutorial.TutorialStatus.IMPLEMENTED;
import static com.pvptutorials.tutorial.TutorialStatus.PLACEHOLDER;

/**
 * Complete built-in tutorial catalog.
 *
 * requiredBase is null when the appropriate Pete base arena has not been
 * confirmed through in-game research — those tutorials cannot be activated
 * until the capability matrix (docs/pete-capabilities.md) is filled in.
 *
 * Base assignments marked "confirmed" were verified in Phase 1/2.
 * Assignments marked "pending" are best-guess and subject to revision.
 */
public final class BuiltInTutorials
{
	private BuiltInTutorials() {}

	public static final List<TutorialMetadata> ALL = List.of(

		// ── Fundamentals ──────────────────────────────────────────────────────

		meta(DEVELOPER_TEST,
			"Developer Test",
			"Proves the full plugin architecture end-to-end",
			FUNDAMENTALS, BEGINNER,
			List.of("debug", "dev", "architecture"),
			GEAR_SWITCHING, // confirmed Phase 2
			IMPLEMENTED),

		// ── Switching ─────────────────────────────────────────────────────────

		meta(RANGE_TO_STAFF,
			"Range → Staff",
			"Switch from ranged to staff",
			SWITCHING, BEGINNER,
			List.of("switching", "range", "staff", "mage", "one-way"),
			GEAR_SWITCHING,
			IMPLEMENTED),

		meta(STAFF_TO_RANGE,
			"Staff → Range",
			"Switch from staff to ranged",
			SWITCHING, BEGINNER,
			List.of("switching", "range", "staff", "one-way"),
			GEAR_SWITCHING,
			PLACEHOLDER),

		meta(MAGE_TO_DHIDE,
			"Mage → D'hide",
			"Switch from mage to dragonhide top and bottom",
			SWITCHING, BEGINNER,
			List.of("switching", "mage", "dhide", "dragonhide", "two-way"),
			GEAR_SWITCHING, // spec example in Phase 3 doc
			PLACEHOLDER),

		meta(RANGE_TO_TANK,
			"Range → Tank",
			"Switch from ranged to full tank gear",
			SWITCHING, INTERMEDIATE,
			List.of("switching", "range", "tank", "tanking"),
			GEAR_SWITCHING,
			PLACEHOLDER),

		meta(ONE_WAY_SWITCH,
			"One-Way Switch",
			"Single weapon swap fundamentals",
			SWITCHING, BEGINNER,
			List.of("switching", "one-way", "fundamentals"),
			GEAR_SWITCHING,
			PLACEHOLDER),

		meta(TWO_WAY_SWITCH,
			"Two-Way Switch",
			"Two-weapon rotation",
			SWITCHING, BEGINNER,
			List.of("switching", "two-way"),
			GEAR_SWITCHING,
			PLACEHOLDER),

		meta(THREE_WAY_SWITCH,
			"Three-Way Switch",
			"Three-weapon rotation",
			SWITCHING, INTERMEDIATE,
			List.of("switching", "three-way"),
			GEAR_SWITCHING,
			PLACEHOLDER),

		meta(SWITCH_SPEED_LADDER,
			"Switch Speed Ladder",
			"Progressive switching speed drills",
			SWITCHING, INTERMEDIATE,
			List.of("switching", "speed", "ladder", "progression"),
			GEAR_SWITCHING,
			PLACEHOLDER),

		meta(MAGE_TO_TANK,
			"Mage → Tank",
			"Switch from mage to full tank gear",
			SWITCHING, INTERMEDIATE,
			List.of("switching", "mage", "tank", "tanking"),
			GEAR_SWITCHING,
			PLACEHOLDER),

		meta(ATTACK_PROTECT_PRAYER,
			"Attack → Protect Prayer",
			"Attack while simultaneously activating a protection prayer",
			SWITCHING, BEGINNER,
			List.of("switching", "prayer", "protect", "reaction"),
			GEAR_SWITCHING,
			PLACEHOLDER),

		meta(PRAYER_GEAR_REACTION,
			"Prayer + Gear Reaction",
			"Simultaneous prayer swap and gear change",
			SWITCHING, INTERMEDIATE,
			List.of("switching", "prayer", "reaction", "combination"),
			GEAR_SWITCHING,
			PLACEHOLDER),

		// ── Prayer ────────────────────────────────────────────────────────────

		meta(RECOGNIZE_ATTACK_STYLE,
			"Recognize Attack Style",
			"Identify incoming attack style and activate the correct protection prayer",
			PRAYER, BEGINNER,
			List.of("prayer", "protect", "recognition", "reading"),
			PRAYER_PROTECTION, // pending confirmation
			PLACEHOLDER),

		meta(PROTECT_YOUR_PRAYER,
			"Protect Your Prayer",
			"Maintain prayer points under sustained smite pressure",
			PRAYER, INTERMEDIATE,
			List.of("prayer", "smite", "protect", "prayer-points"),
			PRAYER_PROTECTION,
			PLACEHOLDER),

		// ── Freezes ──────────────────────────────────────────────────────────

		meta(BASIC_FREEZE_CAST,
			"Basic Freeze Cast",
			"Land a freeze spell on a moving target",
			FREEZES, BEGINNER,
			List.of("freeze", "ice-barrage", "ice-blitz", "spell", "cast"),
			POWER_OF_FREEZES, // pending confirmation
			PLACEHOLDER),

		meta(FREEZE_TO_DHIDE,
			"Freeze → D'hide",
			"Land a freeze then switch to dragonhide before attacking",
			FREEZES, BEGINNER,
			List.of("freeze", "dhide", "dragonhide", "switching", "combo"),
			POWER_OF_FREEZES,
			PLACEHOLDER),

		meta(FREEZE_TO_STEP_AWAY,
			"Freeze → Step Away",
			"Freeze an opponent then create safe distance",
			FREEZES, BEGINNER,
			List.of("freeze", "movement", "escape", "spacing"),
			POWER_OF_FREEZES,
			PLACEHOLDER),

		meta(FREEZE_TO_DD,
			"Freeze → DD",
			"Freeze an opponent then stack on them",
			FREEZES, INTERMEDIATE,
			List.of("freeze", "dd", "dragon-dagger", "stack", "movement"),
			POWER_OF_FREEZES, // spec example in Phase 3 doc
			PLACEHOLDER),

		meta(REFREEZE_TIMING,
			"Re-freeze Timing",
			"Apply a second freeze as the first one expires",
			FREEZES, ADVANCED,
			List.of("freeze", "refreeze", "timing", "ice-barrage"),
			POWER_OF_FREEZES,
			PLACEHOLDER),

		// ── Eating ───────────────────────────────────────────────────────────

		meta(SHARK_KARAMBWAN,
			"Shark → Karambwan",
			"Combo eat shark followed by karambwan for double healing",
			EATING, BEGINNER,
			List.of("eating", "combo-eat", "shark", "karambwan", "food"),
			COMBO_EATING, // pending confirmation
			PLACEHOLDER),

		meta(TRIPLE_EAT,
			"Triple Eat",
			"Execute a three-item combo eat sequence",
			EATING, INTERMEDIATE,
			List.of("eating", "combo-eat", "triple", "food"),
			COMBO_EATING,
			PLACEHOLDER),

		meta(BREW_RESTORE,
			"Brew → Restore",
			"Brew down stats then restore with super restore",
			EATING, BEGINNER,
			List.of("eating", "brew", "restore", "potion", "stats"),
			COMBO_EATING,
			PLACEHOLDER),

		meta(EAT_UNDER_PRESSURE,
			"Eat Under Pressure",
			"Heal while being attacked without wasting ticks",
			EATING, INTERMEDIATE,
			List.of("eating", "pressure", "heal", "survival"),
			COMBO_EATING,
			PLACEHOLDER),

		meta(DONT_OVEREAT,
			"Don't Overeat",
			"Recognise when eating would waste HP and hold",
			EATING, INTERMEDIATE,
			List.of("eating", "efficiency", "overheal", "decision"),
			COMBO_EATING,
			PLACEHOLDER),

		// ── Movement ─────────────────────────────────────────────────────────

		meta(SEED_ESCAPE,
			"Seed Escape",
			"Use a seed pod to escape a sticky situation",
			MOVEMENT, BEGINNER,
			List.of("movement", "escape", "seed-pod", "positioning"),
			null, // pending research
			PLACEHOLDER),

		meta(TELEBLOCK_CAST,
			"Teleblock Cast",
			"Land a teleblock spell on an opponent",
			MOVEMENT, BEGINNER,
			List.of("movement", "teleblock", "tb", "spell"),
			POWER_OF_FREEZES,
			PLACEHOLDER),

		meta(TB_TO_DHIDE,
			"Teleblock → D'hide",
			"Cast teleblock then switch to dragonhide",
			MOVEMENT, BEGINNER,
			List.of("movement", "teleblock", "tb", "dhide", "switching"),
			POWER_OF_FREEZES,
			PLACEHOLDER),

		meta(DD_FUNDAMENTALS,
			"DD Fundamentals",
			"Position correctly to stack on a frozen target",
			MOVEMENT, BEGINNER,
			List.of("movement", "dd", "stacking", "positioning"),
			POWER_OF_FREEZES,
			PLACEHOLDER),

		meta(DD_ATTACK_TIMING,
			"DD Attack Timing",
			"Attack from a stacked position at the correct tick",
			MOVEMENT, INTERMEDIATE,
			List.of("movement", "dd", "stacking", "timing", "attack-tick"),
			POWER_OF_FREEZES,
			PLACEHOLDER),

		meta(ESCAPE_PRACTICE,
			"Escape Practice",
			"Break away from a pursuer using movement and abilities",
			MOVEMENT, INTERMEDIATE,
			List.of("movement", "escape", "positioning", "survival"),
			null,
			PLACEHOLDER),

		meta(MOVE_BETWEEN_ATTACKS,
			"Move Between Attacks",
			"Reposition safely inside your own attack window",
			MOVEMENT, INTERMEDIATE,
			List.of("movement", "attack-tick", "positioning", "spacing"),
			null,
			PLACEHOLDER),

		meta(ANTI_PK_TANKING,
			"Anti-PK Tanking",
			"Survive an ambush and escape cleanly",
			MOVEMENT, ADVANCED,
			List.of("movement", "tanking", "survival", "escape", "anti-pk"),
			null,
			PLACEHOLDER),

		// ── Smite ────────────────────────────────────────────────────────────

		meta(SMITE_BASICS,
			"Smite Basics",
			"Drain opponent prayer with Smite",
			SMITE, BEGINNER,
			List.of("smite", "prayer-drain", "smiting"),
			PRAYER_PROTECTION,
			PLACEHOLDER),

		meta(TutorialId.SMITE_YOUR_OPPONENT,
			"Smite Your Opponent",
			"Sustain Smite on an active opponent to drain their prayer",
			SMITE, INTERMEDIATE,
			List.of("smite", "prayer-drain", "pressure"),
			PRAYER_PROTECTION,
			PLACEHOLDER),

		meta(PROTECT_SMITE_PROTECT,
			"Protect → Smite → Protect",
			"Swap between Smite and protection prayer at the correct moment",
			SMITE, INTERMEDIATE,
			List.of("smite", "prayer", "swap", "reaction"),
			PRAYER_PROTECTION,
			PLACEHOLDER),

		meta(SMITE_PRESSURE,
			"Smite Pressure",
			"Maintain sustained Smite drain across an extended fight",
			SMITE, ADVANCED,
			List.of("smite", "prayer-drain", "pressure", "sustained"),
			PRAYER_PROTECTION,
			PLACEHOLDER),

		meta(SMITE_SPEC_SETUP,
			"Smite + Spec Setup",
			"Drain enough prayer before committing a spec",
			SMITE, ADVANCED,
			List.of("smite", "spec", "setup", "prayer-drain"),
			PRAYER_PROTECTION,
			PLACEHOLDER),

		meta(EAT_RESTORE_UNDER_SMITE,
			"Eat + Restore Under Smite",
			"Recover prayer while being actively smited",
			SMITE, INTERMEDIATE,
			List.of("smite", "eating", "restore", "prayer", "survival"),
			PRAYER_PROTECTION,
			PLACEHOLDER),

		meta(ANTI_SMITE,
			"Anti-Smite",
			"Protect prayer while being smited",
			SMITE, BEGINNER,
			List.of("smite", "prayer", "protect", "anti-smite"),
			PRAYER_PROTECTION,
			PLACEHOLDER),

		meta(PROTECT_ITEM_AWARENESS,
			"Protect Item Awareness",
			"Account for Protect Item when calculating spec value",
			SMITE, INTERMEDIATE,
			List.of("smite", "protect-item", "spec", "ko", "calculation"),
			PRAYER_PROTECTION,
			PLACEHOLDER),

		meta(SMITE_DUEL,
			"Smite Duel",
			"Full smite-focused engagement from start to finish",
			SMITE, ADVANCED,
			List.of("smite", "duel", "full-fight", "prayer-drain"),
			PRAYER_PROTECTION,
			PLACEHOLDER),

		// ── NH (No Honour) ───────────────────────────────────────────────────

		meta(OFF_PRAYER_ATTACKING,
			"Off-Prayer Attacking",
			"Attack an opponent who has dropped their protection prayer",
			NH, BEGINNER,
			List.of("nh", "off-prayer", "reading", "awareness"),
			null,
			PLACEHOLDER),

		meta(ATTACK_TICK_AWARENESS,
			"Attack Tick Awareness",
			"Recognise your own attack delay and act in the window",
			NH, BEGINNER,
			List.of("nh", "attack-tick", "timing", "awareness"),
			null,
			PLACEHOLDER),

		meta(BARRAGE_TO_RANGE,
			"Barrage → Range",
			"Freeze with ice barrage then switch to ranged",
			NH, INTERMEDIATE,
			List.of("nh", "barrage", "freeze", "range", "switching", "combo"),
			POWER_OF_FREEZES,
			PLACEHOLDER),

		meta(BARRAGE_TO_MELEE,
			"Barrage → Melee",
			"Freeze with ice barrage then switch to melee",
			NH, INTERMEDIATE,
			List.of("nh", "barrage", "freeze", "melee", "switching", "combo"),
			POWER_OF_FREEZES,
			PLACEHOLDER),

		meta(RANGE_TO_MAGE,
			"Range → Mage",
			"Switch from ranged to mage mid-fight",
			NH, INTERMEDIATE,
			List.of("nh", "range", "mage", "switching"),
			GEAR_SWITCHING,
			PLACEHOLDER),

		meta(BASIC_NH_ROTATION,
			"Basic NH Rotation",
			"Basic no-honour switching pattern",
			NH, BEGINNER,
			List.of("nh", "switching", "rotation", "fundamentals"),
			GEAR_SWITCHING,
			PLACEHOLDER),

		meta(INCOMING_STACK_RECOGNITION,
			"Incoming Stack Recognition",
			"Identify when the opponent is attempting to stack hits",
			NH, ADVANCED,
			List.of("nh", "stack", "recognition", "reading", "awareness"),
			null,
			PLACEHOLDER),

		meta(BEGINNER_NH_FIGHT,
			"Beginner NH Fight",
			"Full beginner no-honour sparring session",
			NH, BEGINNER,
			List.of("nh", "fight", "full-fight", "integrated"),
			null,
			PLACEHOLDER),

		meta(INTERMEDIATE_NH_FIGHT,
			"Intermediate NH Fight",
			"Full intermediate NH fight",
			NH, INTERMEDIATE,
			List.of("nh", "fight", "full-fight", "integrated"),
			null,
			PLACEHOLDER),

		meta(ADVANCED_NH_FIGHT,
			"Advanced NH Fight",
			"Full advanced NH fight",
			NH, ADVANCED,
			List.of("nh", "fight", "full-fight", "integrated"),
			null,
			PLACEHOLDER),

		// ── KO Combos ────────────────────────────────────────────────────────

		meta(GMAUL_HIT_CONFIRM,
			"Gmaul Hit Confirm",
			"Confirm a Gmaul opportunity after an opening hit",
			KO_COMBOS, BEGINNER,
			List.of("gmaul", "hit-confirm", "ko", "spec"),
			SPECIAL_ATTACKS,
			PLACEHOLDER),

		meta(BOLT_GMAUL,
			"Bolt → Gmaul",
			"Confirm a bolt hit then follow with Gmaul",
			KO_COMBOS, BEGINNER,
			List.of("gmaul", "bolt", "hit-confirm", "ko", "spec", "crossbow"),
			SPECIAL_ATTACKS, // spec example in Phase 3 doc
			PLACEHOLDER),

		meta(BOLT_AGS,
			"Bolt → AGS",
			"Confirm a bolt hit then follow with Armadyl Godsword",
			KO_COMBOS, INTERMEDIATE,
			List.of("ags", "bolt", "hit-confirm", "ko", "spec", "crossbow"),
			SPECIAL_ATTACKS,
			PLACEHOLDER),

		meta(BOLT_VOIDWAKER,
			"Bolt → Voidwaker",
			"Confirm a bolt hit then follow with Voidwaker",
			KO_COMBOS, BEGINNER,
			List.of("voidwaker", "bolt", "hit-confirm", "ko", "spec", "crossbow"),
			SPECIAL_ATTACKS,
			PLACEHOLDER),

		meta(BOLT_GMAUL_AGS,
			"Bolt → Gmaul → AGS",
			"Bolt opener, confirm, Gmaul spec, then AGS spec",
			KO_COMBOS, ADVANCED,
			List.of("gmaul", "ags", "bolt", "hit-confirm", "ko", "spec", "three-spec"),
			SPECIAL_ATTACKS,
			PLACEHOLDER),

		meta(BOLT_AGS_GMAUL,
			"Bolt → AGS → Gmaul",
			"Bolt opener, confirm, AGS spec, then Gmaul spec",
			KO_COMBOS, ADVANCED,
			List.of("ags", "gmaul", "bolt", "hit-confirm", "ko", "spec", "three-spec"),
			SPECIAL_ATTACKS,
			PLACEHOLDER),

		meta(BOLT_GMAUL_VOIDWAKER,
			"Bolt → Gmaul → Voidwaker",
			"Bolt opener, confirm, Gmaul spec, then Voidwaker spec",
			KO_COMBOS, ADVANCED,
			List.of("gmaul", "voidwaker", "bolt", "hit-confirm", "ko", "spec", "three-spec"),
			SPECIAL_ATTACKS,
			PLACEHOLDER),

		meta(BOLT_VOIDWAKER_GMAUL,
			"Bolt → Voidwaker → Gmaul",
			"Bolt opener, confirm, Voidwaker spec, then Gmaul spec",
			KO_COMBOS, ADVANCED,
			List.of("voidwaker", "gmaul", "bolt", "hit-confirm", "ko", "spec", "three-spec"),
			SPECIAL_ATTACKS,
			PLACEHOLDER),

		meta(MELEE_HIT_GMAUL,
			"Melee Hit → Gmaul",
			"Confirm a melee hit then follow with Gmaul",
			KO_COMBOS, BEGINNER,
			List.of("gmaul", "melee", "hit-confirm", "ko", "spec"),
			SPECIAL_ATTACKS,
			PLACEHOLDER),

		meta(SPEC_ABORT,
			"Spec Abort",
			"Correctly decline to commit a spec after a weak opening hit",
			KO_COMBOS, INTERMEDIATE,
			List.of("spec", "abort", "hit-confirm", "decision"),
			SPECIAL_ATTACKS,
			PLACEHOLDER),

		meta(KO_THRESHOLD_CONFIRM,
			"KO Threshold Confirm",
			"Recognise when combined specs can finish the opponent",
			KO_COMBOS, INTERMEDIATE,
			List.of("ko", "spec", "threshold", "calculation", "hit-confirm"),
			SPECIAL_ATTACKS,
			PLACEHOLDER),

		meta(KO_THRESHOLD_RECOGNITION,
			"KO Threshold Recognition",
			"Identify when the opponent is killable based on current HP and spec weapons",
			KO_COMBOS, INTERMEDIATE,
			List.of("ko", "threshold", "recognition", "calculation"),
			SPECIAL_ATTACKS,
			PLACEHOLDER),

		meta(VARIABLE_HIT_CONFIRM_DRILL,
			"Variable Hit Confirm Drill",
			"Mixed GO/ABORT decisions across randomised hit outcomes",
			KO_COMBOS, ADVANCED,
			List.of("hit-confirm", "variable", "decision", "drill", "spec"),
			SPECIAL_ATTACKS,
			PLACEHOLDER)
	);

	private static TutorialMetadata meta(
		TutorialId id,
		String displayName,
		String description,
		TutorialCategory category,
		TutorialDifficulty difficulty,
		List<String> tags,
		BaseTutorial requiredBase,
		TutorialStatus status)
	{
		return TutorialMetadata.builder()
			.id(id)
			.displayName(displayName)
			.description(description)
			.category(category)
			.difficulty(difficulty)
			.tags(tags)
			.requiredBase(requiredBase)
			.status(status)
			.build();
	}
}
