package com.pvptutorials.tutorial;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TutorialId
{
	// ── Internal ─────────────────────────────────────────────────────────────
	DEVELOPER_TEST("Developer Test", "Proves the full architecture end-to-end"),

	// ── Switching ─────────────────────────────────────────────────────────────
	RANGE_TO_STAFF("Range → Staff", "Switch from ranged to staff"),
	STAFF_TO_RANGE("Staff → Range", "Switch from staff to ranged"),
	MAGE_TO_DHIDE("Mage → D'hide", "Switch from mage to dragonhide"),
	RANGE_TO_TANK("Range → Tank", "Switch from ranged to tank"),
	ONE_WAY_SWITCH("One-Way Switch", "Single weapon swap fundamentals"),
	TWO_WAY_SWITCH("Two-Way Switch", "Two-weapon rotation"),
	THREE_WAY_SWITCH("Three-Way Switch", "Three-weapon rotation"),
	MAGE_TO_TANK("Mage → Tank", "Switch from mage to full tank"),

	// ── Prayer ────────────────────────────────────────────────────────────────
	ATTACK_PROTECT_PRAYER("Attack → Protect Prayer", "Attack while activating protection prayer"),
	RECOGNIZE_ATTACK_STYLE("Recognize Attack Style", "Identify incoming attack style and pray correctly"),
	PROTECT_YOUR_PRAYER("Protect Your Prayer", "Maintain prayer points under smite pressure"),

	// ── Freezes ──────────────────────────────────────────────────────────────
	BASIC_FREEZE_CAST("Basic Freeze Cast", "Land a freeze spell on a moving target"),
	FREEZE_TO_DHIDE("Freeze → D'hide", "Freeze then switch to dragonhide"),
	FREEZE_TO_STEP_AWAY("Freeze → Step Away", "Freeze then create distance"),
	FREEZE_TO_DD("Freeze → DD", "Freeze then stack on opponent"),
	REFREEZE_TIMING("Re-freeze Timing", "Apply a second freeze as the first expires"),

	// ── Eating ────────────────────────────────────────────────────────────────
	SHARK_KARAMBWAN("Shark → Karambwan", "Combo eat shark then karambwan"),
	TRIPLE_EAT("Triple Eat", "Three-item combo eat sequence"),
	BREW_RESTORE("Brew → Restore", "Brew down stats then restore"),
	EAT_UNDER_PRESSURE("Eat Under Pressure", "Heal while under active attack"),
	DONT_OVEREAT("Don't Overeat", "Recognise when eating wastes HP"),

	// ── Positioning ──────────────────────────────────────────────────────────
	SEED_ESCAPE("Seed Escape", "Use seed pod to escape a sticky situation"),
	TELEBLOCK_CAST("Teleblock Cast", "Land a teleblock on an opponent"),
	TB_TO_DHIDE("Teleblock → D'hide", "Cast teleblock then switch to dragonhide"),
	DD_FUNDAMENTALS("DD Fundamentals", "Position correctly to stack on a frozen target"),
	ESCAPE_PRACTICE("Escape Practice", "Break away from a pursuer"),
	ANTI_PK_TANKING("Anti-PK Tanking", "Survive an ambush and escape"),

	// ── Combat awareness ─────────────────────────────────────────────────────
	OFF_PRAYER_ATTACKING("Off-Prayer Attacking", "Attack an opponent who has dropped prayer"),
	ATTACK_TICK_AWARENESS("Attack Tick Awareness", "Recognise your own attack delay"),
	MOVE_BETWEEN_ATTACKS("Move Between Attacks", "Reposition safely in the attack window"),
	BARRAGE_TO_RANGE("Barrage → Range", "Freeze with barrage then switch to ranged"),
	BARRAGE_TO_MELEE("Barrage → Melee", "Freeze with barrage then switch to melee"),
	RANGE_TO_MAGE("Range → Mage", "Switch from ranged to mage mid-fight"),
	BASIC_NH_ROTATION("Basic NH Rotation", "Basic no-honour switching pattern"),

	// ── Smite ────────────────────────────────────────────────────────────────
	SMITE_BASICS("Smite Basics", "Drain opponent prayer with Smite"),
	SMITE_YOUR_OPPONENT("Smite Your Opponent", "Sustain Smite on an active opponent to drain their prayer"),
	ANTI_SMITE("Anti-Smite", "Protect prayer while being smited"),
	PROTECT_SMITE_PROTECT("Protect → Smite → Protect", "Swap between Smite and protection at the right moment"),
	SMITE_PRESSURE("Smite Pressure", "Maintain sustained Smite drain"),
	SMITE_SPEC_SETUP("Smite Spec Setup", "Drain enough prayer before committing a spec"),
	EAT_RESTORE_UNDER_SMITE("Eat + Restore Under Smite", "Recover prayer while smited"),
	SMITE_DUEL("Smite Duel", "Full smite-focused engagement"),

	// ── Spec / KO ────────────────────────────────────────────────────────────
	GMAUL_HIT_CONFIRM("Gmaul Hit Confirm", "Confirm a Gmaul opportunity after an opener"),
	BOLT_GMAUL("Bolt → Gmaul", "Confirm bolt then Gmaul"),
	BOLT_AGS("Bolt → AGS", "Confirm bolt then AGS"),
	BOLT_VOIDWAKER("Bolt → Voidwaker", "Confirm bolt then Voidwaker"),
	BOLT_GMAUL_AGS("Bolt → Gmaul → AGS", "Bolt, confirm, Gmaul, then AGS"),
	BOLT_AGS_GMAUL("Bolt → AGS → Gmaul", "Bolt, confirm, AGS, then Gmaul"),
	BOLT_GMAUL_VOIDWAKER("Bolt → Gmaul → Voidwaker", "Bolt, confirm, Gmaul, then Voidwaker"),
	BOLT_VOIDWAKER_GMAUL("Bolt → Voidwaker → Gmaul", "Bolt, confirm, Voidwaker, then Gmaul"),
	MELEE_HIT_GMAUL("Melee Hit → Gmaul", "Confirm melee hit then follow with Gmaul"),
	SPEC_ABORT("Spec Abort", "Correctly decline to commit spec after a weak opener"),
	KO_THRESHOLD_CONFIRM("KO Threshold Confirm", "Recognise when combined specs can finish the opponent"),
	PROTECT_ITEM_AWARENESS("Protect Item Awareness", "Account for Protect Item when calculating spec value"),
	VARIABLE_HIT_CONFIRM_DRILL("Variable Hit Confirm Drill", "Mixed GO/ABORT decisions across randomised outcomes"),

	// ── Integrated ────────────────────────────────────────────────────────────
	KO_THRESHOLD_RECOGNITION("KO Threshold Recognition", "Identify when the opponent is killable"),
	SWITCH_SPEED_LADDER("Switch Speed Ladder", "Progressive switching speed drills"),
	PRAYER_GEAR_REACTION("Prayer + Gear Reaction", "Simultaneous prayer and gear change"),
	INCOMING_STACK_RECOGNITION("Incoming Stack Recognition", "Identify when opponent is trying to stack hits"),
	DD_ATTACK_TIMING("DD Attack Timing", "Attack correctly from a stacked position"),
	BEGINNER_NH_FIGHT("Beginner NH Fight", "Full beginner no-honour sparring session"),
	INTERMEDIATE_NH_FIGHT("Intermediate NH Fight", "Full intermediate NH fight"),
	ADVANCED_NH_FIGHT("Advanced NH Fight", "Full advanced NH fight");

	private final String displayName;
	private final String description;
}
