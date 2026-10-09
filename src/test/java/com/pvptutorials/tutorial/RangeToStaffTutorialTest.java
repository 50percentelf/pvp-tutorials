package com.pvptutorials.tutorial;

import com.pvptutorials.observation.TutorialEvent;
import com.pvptutorials.observation.TutorialEventType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RangeToStaffTutorialTest
{
	private RangeToStaffTutorial tutorial;

	private static final TutorialEvent ATTACK = new TutorialEvent(TutorialEventType.ATTACK_STARTED);
	private static final TutorialEvent EQUIP  = new TutorialEvent(TutorialEventType.ITEM_EQUIPPED);
	private static final TutorialEvent OTHER  = new TutorialEvent(TutorialEventType.PLAYER_MOVED);

	@Before
	public void setUp()
	{
		tutorial = new RangeToStaffTutorial();
	}

	@Test
	public void initialStateIsNone()
	{
		assertEquals(TutorialState.NONE, tutorial.getState());
		assertNull(tutorial.getActiveInstruction());
	}

	@Test
	public void startTransitionsToActive()
	{
		tutorial.start();
		assertEquals(TutorialState.ACTIVE, tutorial.getState());
	}

	@Test
	public void firstInstructionShownAfterStart()
	{
		tutorial.start();
		assertEquals("Attack with your ranged weapon", tutorial.getActiveInstruction());
	}

	@Test
	public void attackAdvancesToEquipStep()
	{
		tutorial.start();
		tutorial.onTutorialEvent(ATTACK);
		assertEquals("Equip your staff", tutorial.getActiveInstruction());
	}

	@Test
	public void equipBeforeAttackIsIgnored()
	{
		tutorial.start();
		tutorial.onTutorialEvent(EQUIP);
		assertEquals("Attack with your ranged weapon", tutorial.getActiveInstruction());
	}

	@Test
	public void attackThenEquipCompletes()
	{
		tutorial.start();
		tutorial.onTutorialEvent(ATTACK);
		tutorial.onTutorialEvent(EQUIP);
		assertEquals(TutorialState.COMPLETED, tutorial.getState());
		assertNull(tutorial.getActiveInstruction());
	}

	@Test
	public void nonRelevantEventIgnored()
	{
		tutorial.start();
		tutorial.onTutorialEvent(OTHER);
		assertEquals("Attack with your ranged weapon", tutorial.getActiveInstruction());
	}

	@Test
	public void eventBeforeStartIgnored()
	{
		tutorial.onTutorialEvent(ATTACK);
		assertEquals(TutorialState.NONE, tutorial.getState());
	}

	@Test
	public void resetReturnsToNone()
	{
		tutorial.start();
		tutorial.onTutorialEvent(ATTACK);
		tutorial.reset();
		assertEquals(TutorialState.NONE, tutorial.getState());
		assertNull(tutorial.getActiveInstruction());
	}

	@Test
	public void canRestartAfterReset()
	{
		tutorial.start();
		tutorial.onTutorialEvent(ATTACK);
		tutorial.reset();
		tutorial.start();
		assertEquals("Attack with your ranged weapon", tutorial.getActiveInstruction());
	}

	@Test
	public void stopBehavesLikeReset()
	{
		tutorial.start();
		tutorial.stop();
		assertEquals(TutorialState.NONE, tutorial.getState());
	}

	@Test
	public void extraEventAfterCompletionIgnored()
	{
		tutorial.start();
		tutorial.onTutorialEvent(ATTACK);
		tutorial.onTutorialEvent(EQUIP);
		tutorial.onTutorialEvent(EQUIP);
		assertEquals(TutorialState.COMPLETED, tutorial.getState());
	}
}
