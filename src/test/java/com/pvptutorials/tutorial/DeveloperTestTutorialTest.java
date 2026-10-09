package com.pvptutorials.tutorial;

import net.runelite.api.events.MenuOptionClicked;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DeveloperTestTutorialTest
{
	private DeveloperTestTutorial tutorial;

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
	public void wieldAdvancesToSecondStep()
	{
		tutorial.start();
		tutorial.onMenuOptionClicked(menuClick("Wield"));
		assertEquals("Wield another item", tutorial.getActiveInstruction());
	}

	@Test
	public void wearAlsoAdvancesStep()
	{
		tutorial.start();
		tutorial.onMenuOptionClicked(menuClick("Wear"));
		assertEquals("Wield another item", tutorial.getActiveInstruction());
	}

	@Test
	public void equipAlsoAdvancesStep()
	{
		tutorial.start();
		tutorial.onMenuOptionClicked(menuClick("Equip"));
		assertEquals("Wield another item", tutorial.getActiveInstruction());
	}

	@Test
	public void twoWieldsCompletes()
	{
		tutorial.start();
		tutorial.onMenuOptionClicked(menuClick("Wield"));
		tutorial.onMenuOptionClicked(menuClick("Wield"));
		assertEquals(TutorialState.COMPLETED, tutorial.getState());
		assertNull(tutorial.getActiveInstruction());
	}

	@Test
	public void nonEquipOptionIgnored()
	{
		tutorial.start();
		tutorial.onMenuOptionClicked(menuClick("Use"));
		assertEquals("Wield a weapon", tutorial.getActiveInstruction());
	}

	@Test
	public void menuClickBeforeStartIgnored()
	{
		tutorial.onMenuOptionClicked(menuClick("Wield"));
		assertEquals(TutorialState.NONE, tutorial.getState());
	}

	@Test
	public void resetReturnsToNone()
	{
		tutorial.start();
		tutorial.onMenuOptionClicked(menuClick("Wield"));
		tutorial.reset();
		assertEquals(TutorialState.NONE, tutorial.getState());
		assertNull(tutorial.getActiveInstruction());
	}

	@Test
	public void canRestartAfterReset()
	{
		tutorial.start();
		tutorial.onMenuOptionClicked(menuClick("Wield"));
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

	// ── helpers ───────────────────────────────────────────────────────────────

	private static MenuOptionClicked menuClick(String option)
	{
		MenuOptionClicked event = mock(MenuOptionClicked.class);
		when(event.getMenuOption()).thenReturn(option);
		return event;
	}
}
