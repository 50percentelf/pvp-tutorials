package com.pvptutorials.arena;

import com.pvptutorials.tutorial.TutorialId;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArenaSessionTest
{
	private ArenaSession session;

	@Before
	public void setUp()
	{
		session = new ArenaSession();
	}

	@Test
	public void initialStateIsOutside()
	{
		assertEquals(ArenaState.OUTSIDE, session.getArenaState());
		assertFalse(session.isInside());
		assertFalse(session.isActive());
		assertFalse(session.hasPendingTutorial());
	}

	@Test
	public void selectTutorialSetsPending()
	{
		session.selectTutorial(TutorialId.DEVELOPER_TEST);
		assertTrue(session.hasPendingTutorial());
		assertEquals(TutorialId.DEVELOPER_TEST, session.getPendingTutorialId());
	}

	@Test
	public void enterTransitionsToEntering()
	{
		session.enter(12345, 100);
		assertEquals(ArenaState.ENTERING, session.getArenaState());
		assertEquals(12345, session.getArenaRegionId());
		assertEquals(100, session.getEntryTick());
		assertTrue(session.isInside());
		assertFalse(session.isActive());
	}

	@Test
	public void activatePromotesPendingToActive()
	{
		session.selectTutorial(TutorialId.DEVELOPER_TEST);
		session.enter(12345, 100);
		session.activate();
		assertEquals(ArenaState.ACTIVE, session.getArenaState());
		assertTrue(session.isActive());
		assertEquals(TutorialId.DEVELOPER_TEST, session.getActiveTutorialId());
		assertNull(session.getPendingTutorialId());
		assertFalse(session.hasPendingTutorial());
	}

	@Test
	public void activateWithoutPendingStillBecomesActive()
	{
		session.enter(12345, 100);
		session.activate();
		assertEquals(ArenaState.ACTIVE, session.getArenaState());
		assertNull(session.getActiveTutorialId());
	}

	@Test
	public void exitTransitionsToExiting()
	{
		session.enter(12345, 100);
		session.exit();
		assertEquals(ArenaState.EXITING, session.getArenaState());
		assertFalse(session.isInside());
	}

	@Test
	public void resetReturnsToCleanState()
	{
		session.selectTutorial(TutorialId.DEVELOPER_TEST);
		session.enter(12345, 100);
		session.activate();
		session.reset();
		assertEquals(ArenaState.OUTSIDE, session.getArenaState());
		assertNull(session.getPendingTutorialId());
		assertNull(session.getActiveTutorialId());
		assertEquals(-1, session.getArenaRegionId());
		assertEquals(-1, session.getEntryTick());
		assertFalse(session.isInside());
		assertFalse(session.hasPendingTutorial());
	}
}
