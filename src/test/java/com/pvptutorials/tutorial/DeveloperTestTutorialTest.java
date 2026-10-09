package com.pvptutorials.tutorial;

import com.pvptutorials.observation.TutorialEvent;
import com.pvptutorials.observation.TutorialEventType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DeveloperTestTutorialTest
{
	private DeveloperTestTutorial tutorial;

	private static final TutorialEvent EQUIP = new TutorialEvent(TutorialEventType.ITEM_EQUIPPED);
	private static final TutorialEvent OTHER = new TutorialEvent(TutorialEventType.PLAYER_MOVED);

	@Before
	public void setUp()
	{
		tutorial = new DeveloperTestTutorial();
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
		assertEquals("Wield a weapon", tutorial.getActiveInstruction());
	}

	@Test
	public void equipEventAdvancesToSecondStep()
	{
		tutorial.start();
		tutorial.onTutorialEvent(EQUIP);
		assertEquals("Wield another item", tutorial.getActiveInstruction());
	}

	@Test
	public void twoEquipEventsCompletes()
	{
		tutorial.start();
		tutorial.onTutorialEvent(EQUIP);
		tutorial.onTutorialEvent(EQUIP);
		assertEquals(TutorialState.COMPLETED, tutorial.getState());
		assertNull(tutorial.getActiveInstruction());
	}

	@Test
	public void nonEquipEventIgnored()
	{
		tutorial.start();
		tutorial.onTutorialEvent(OTHER);
		assertEquals("Wield a weapon", tutorial.getActiveInstruction());
	}

	@Test
	public void eventBeforeStartIgnored()
	{
		tutorial.onTutorialEvent(EQUIP);
		assertEquals(TutorialState.NONE, tutorial.getState());
	}

	@Test
	public void resetReturnsToNone()
	{
		tutorial.start();
		tutorial.onTutorialEvent(EQUIP);
		tutorial.reset();
		assertEquals(TutorialState.NONE, tutorial.getState());
		assertNull(tutorial.getActiveInstruction());
	}

	@Test
	public void canRestartAfterReset()
	{
		tutorial.start();
		tutorial.onTutorialEvent(EQUIP);
		tutorial.reset();
		tutorial.start();
		assertEquals("Wield a weapon", tutorial.getActiveInstruction());
	}

	@Test
	public void stopBehavesLikeReset()
	{
		tutorial.start();
		tutorial.stop();
		assertEquals(TutorialState.NONE, tutorial.getState());
	}

	@Test
	public void completedStateHasNoInstruction()
	{
		tutorial.start();
		tutorial.onTutorialEvent(EQUIP);
		tutorial.onTutorialEvent(EQUIP);
		assertNull(tutorial.getActiveInstruction());
	}

	@Test
	public void extraEventAfterCompletionIgnored()
	{
		tutorial.start();
		tutorial.onTutorialEvent(EQUIP);
		tutorial.onTutorialEvent(EQUIP);
		tutorial.onTutorialEvent(EQUIP);
		assertEquals(TutorialState.COMPLETED, tutorial.getState());
	}
}
