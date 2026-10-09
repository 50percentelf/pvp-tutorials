package com.pvptutorials.tutorial;

import com.pvptutorials.arena.BaseTutorial;
import net.runelite.api.events.MenuOptionClicked;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TutorialRegistryTest
{
	private TutorialRegistry registry;

	@Before
	public void setUp()
	{
		registry = new TutorialRegistry();
	}

	// ── TutorialDefinition ────────────────────────────────────────────────────

	@Test
	public void definedTutorialIsRetrievable()
	{
		TutorialDefinition def = new TutorialDefinition(
			TutorialId.DEVELOPER_TEST, BaseTutorial.GEAR_SWITCHING,
			"Developer Test", "Proves architecture");
		registry.define(def);
		assertSame(def, registry.getDefinition(TutorialId.DEVELOPER_TEST));
		assertTrue(registry.isDefined(TutorialId.DEVELOPER_TEST));
	}

	@Test
	public void undefinedIdReturnsNull()
	{
		assertNull(registry.getDefinition(TutorialId.RANGE_TO_STAFF));
		assertFalse(registry.isDefined(TutorialId.RANGE_TO_STAFF));
	}

	@Test
	public void getAllDefinitionsReflectsInsertionOrder()
	{
		TutorialDefinition d1 = new TutorialDefinition(
			TutorialId.DEVELOPER_TEST, BaseTutorial.GEAR_SWITCHING, "Dev", "");
		TutorialDefinition d2 = new TutorialDefinition(
			TutorialId.RANGE_TO_STAFF, BaseTutorial.GEAR_SWITCHING, "R2S", "");
		registry.define(d1);
		registry.define(d2);
		TutorialDefinition[] all = registry.getAllDefinitions().toArray(new TutorialDefinition[0]);
		assertEquals(2, all.length);
		assertEquals(TutorialId.DEVELOPER_TEST, all[0].getId());
		assertEquals(TutorialId.RANGE_TO_STAFF, all[1].getId());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void getAllDefinitionsIsUnmodifiable()
	{
		registry.getAllDefinitions().clear();
	}

	// ── Tutorial implementations ───────────────────────────────────────────────

	@Test
	public void registeredImplementationIsRetrievable()
	{
		Tutorial stub = stubTutorial(TutorialId.DEVELOPER_TEST);
		registry.register(stub);
		assertSame(stub, registry.getImplementation(TutorialId.DEVELOPER_TEST));
		assertTrue(registry.isImplemented(TutorialId.DEVELOPER_TEST));
	}

	@Test
	public void unimplementedIdReturnsNull()
	{
		assertNull(registry.getImplementation(TutorialId.RANGE_TO_STAFF));
		assertFalse(registry.isImplemented(TutorialId.RANGE_TO_STAFF));
	}

	@Test
	public void definedButUnimplementedShowsDefined()
	{
		TutorialDefinition def = new TutorialDefinition(
			TutorialId.BOLT_GMAUL_AGS, BaseTutorial.SPECIAL_ATTACKS, "Bolt→Gmaul→AGS", "");
		registry.define(def);
		assertTrue(registry.isDefined(TutorialId.BOLT_GMAUL_AGS));
		assertFalse(registry.isImplemented(TutorialId.BOLT_GMAUL_AGS));
	}

	// ── Helpers ───────────────────────────────────────────────────────────────

	private Tutorial stubTutorial(TutorialId id)
	{
		return new Tutorial()
		{
			@Override public TutorialId getId()                                { return id; }
			@Override public TutorialState getState()                          { return TutorialState.NONE; }
			@Override public void start()                                      {}
			@Override public void onGameTick()                                 {}
			@Override public void onClientTick()                               {}
			@Override public void onMenuOptionClicked(MenuOptionClicked e)     {}
			@Override public void reset()                                      {}
			@Override public void stop()                                       {}
		};
	}
}
