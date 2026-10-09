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
		assertNull(session.getRequiredBase());
		assertNull(session.getDetectedBase());
	}

	@Test
	public void selectTutorialSetsPendingAndRequiredBase()
	{
		session.selectTutorial(TutorialId.DEVELOPER_TEST, BaseTutorial.GEAR_SWITCHING);
		assertTrue(session.hasPendingTutorial());
		assertEquals(TutorialId.DEVELOPER_TEST, session.getPendingTutorialId());
		assertEquals(BaseTutorial.GEAR_SWITCHING, session.getRequiredBase());
	}

	@Test
	public void enterRecordsRegionAndDetectedBase()
	{
		session.enter(12345, BaseTutorial.GEAR_SWITCHING, 100);
		assertEquals(ArenaState.ENTERING, session.getArenaState());
		assertEquals(12345, session.getArenaRegionId());
		assertEquals(100, session.getEntryTick());
		assertEquals(BaseTutorial.GEAR_SWITCHING, session.getDetectedBase());
		assertTrue(session.isInside());
		assertFalse(session.isActive());
	}

	@Test
	public void baseMatchesPendingWhenCorrectArenaEntered()
	{
		session.selectTutorial(TutorialId.DEVELOPER_TEST, BaseTutorial.GEAR_SWITCHING);
		session.enter(12345, BaseTutorial.GEAR_SWITCHING, 100);
		assertTrue(session.baseMatchesPending());
	}

	@Test
	public void baseDoesNotMatchWhenWrongArenaEntered()
	{
		session.selectTutorial(TutorialId.DEVELOPER_TEST, BaseTutorial.SPECIAL_ATTACKS);
		session.enter(99999, BaseTutorial.GEAR_SWITCHING, 100);
		assertFalse(session.baseMatchesPending());
	}

	@Test
	public void enterWithNullBaseThenSetDetectedBase()
	{
		session.selectTutorial(TutorialId.DEVELOPER_TEST, BaseTutorial.GEAR_SWITCHING);
		session.enter(10588, null, 100);
		assertTrue(session.isInside());
		assertNull(session.getDetectedBase());
		assertFalse(session.baseMatchesPending());

		session.setDetectedBase(BaseTutorial.GEAR_SWITCHING);
		assertEquals(BaseTutorial.GEAR_SWITCHING, session.getDetectedBase());
		assertTrue(session.baseMatchesPending());
	}

	@Test
	public void activatePromotesPendingToActive()
	{
		session.selectTutorial(TutorialId.DEVELOPER_TEST, BaseTutorial.GEAR_SWITCHING);
		session.enter(12345, BaseTutorial.GEAR_SWITCHING, 100);
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
		session.enter(12345, BaseTutorial.COMBO_EATING, 100);
		session.activate();
		assertEquals(ArenaState.ACTIVE, session.getArenaState());
		assertNull(session.getActiveTutorialId());
	}

	@Test
	public void exitTransitionsToExiting()
	{
		session.enter(12345, BaseTutorial.GEAR_SWITCHING, 100);
		session.exit();
		assertEquals(ArenaState.EXITING, session.getArenaState());
		assertFalse(session.isInside());
	}

	@Test
	public void resetReturnsToCleanState()
	{
		session.selectTutorial(TutorialId.DEVELOPER_TEST, BaseTutorial.GEAR_SWITCHING);
		session.enter(12345, BaseTutorial.GEAR_SWITCHING, 100);
		session.activate();
		session.reset();
		assertEquals(ArenaState.OUTSIDE, session.getArenaState());
		assertNull(session.getPendingTutorialId());
		assertNull(session.getActiveTutorialId());
		assertNull(session.getRequiredBase());
		assertNull(session.getDetectedBase());
		assertEquals(-1, session.getArenaRegionId());
		assertEquals(-1, session.getEntryTick());
		assertFalse(session.isInside());
		assertFalse(session.hasPendingTutorial());
	}
}
