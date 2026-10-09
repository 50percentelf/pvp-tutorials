package com.pvptutorials.tutorial;

import org.junit.Test;
import static org.junit.Assert.*;

public class TutorialStepTest
{
	@Test
	public void initialStateIsNotStarted()
	{
		TutorialStep step = new TutorialStep("Equip ranged weapon");
		assertEquals(TutorialStepState.NOT_STARTED, step.getState());
		assertFalse(step.isActive());
		assertFalse(step.isCompleted());
	}

	@Test
	public void activateTransitions()
	{
		TutorialStep step = new TutorialStep("Attack");
		step.activate();
		assertEquals(TutorialStepState.ACTIVE, step.getState());
		assertTrue(step.isActive());
	}

	@Test
	public void completeTransitions()
	{
		TutorialStep step = new TutorialStep("Attack");
		step.activate();
		step.complete();
		assertEquals(TutorialStepState.COMPLETED, step.getState());
		assertFalse(step.isActive());
		assertTrue(step.isCompleted());
	}

	@Test
	public void resetReturnsToNotStarted()
	{
		TutorialStep step = new TutorialStep("Attack");
		step.activate();
		step.complete();
		step.reset();
		assertEquals(TutorialStepState.NOT_STARTED, step.getState());
	}

	@Test
	public void failTransitions()
	{
		TutorialStep step = new TutorialStep("Attack");
		step.activate();
		step.fail();
		assertEquals(TutorialStepState.FAILED, step.getState());
		assertFalse(step.isActive());
		assertFalse(step.isCompleted());
	}
}
