package com.pvptutorials.simulation;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VirtualCombatStateTest
{
	private VirtualCombatState state;

	@Before
	public void setUp()
	{
		state = new VirtualCombatState();
	}

	@Test
	public void initialStateIsHealthy()
	{
		assertEquals(99, state.getCurrentHp());
		assertEquals(99, state.getMaxHp());
		assertFalse(state.isFrozen());
		assertEquals(0, state.getAttackDelayTicks());
	}

	@Test
	public void tickDecrementsCountdowns()
	{
		state.setFrozenTicks(3);
		state.setAttackDelayTicks(2);
		state.tick();
		assertEquals(2, state.getFrozenTicks());
		assertEquals(1, state.getAttackDelayTicks());
	}

	@Test
	public void frozenWhileTicksRemain()
	{
		state.setFrozenTicks(1);
		assertTrue(state.isFrozen());
		state.tick();
		assertFalse(state.isFrozen());
	}

	@Test
	public void tickDoesNotGoBelowZero()
	{
		state.setFrozenTicks(0);
		state.setAttackDelayTicks(0);
		state.tick();
		assertEquals(0, state.getFrozenTicks());
		assertEquals(0, state.getAttackDelayTicks());
	}

	@Test
	public void resetRestoresFullHp()
	{
		state.setCurrentHp(10);
		state.setFrozenTicks(5);
		state.reset();
		assertEquals(state.getMaxHp(), state.getCurrentHp());
		assertFalse(state.isFrozen());
		assertEquals(0, state.getAttackDelayTicks());
	}
}
