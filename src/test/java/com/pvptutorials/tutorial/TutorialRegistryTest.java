package com.pvptutorials.tutorial;

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

	@Test
	public void registeredTutorialIsRetrievable()
	{
		Tutorial stub = stubTutorial(TutorialId.DEVELOPER_TEST);
		registry.register(stub);
		assertSame(stub, registry.get(TutorialId.DEVELOPER_TEST));
		assertTrue(registry.contains(TutorialId.DEVELOPER_TEST));
	}

	@Test
	public void unregisteredIdReturnsNull()
	{
		assertNull(registry.get(TutorialId.RANGE_TO_STAFF));
		assertFalse(registry.contains(TutorialId.RANGE_TO_STAFF));
	}

	@Test
	public void getAllReflectsInsertionOrder()
	{
		Tutorial t1 = stubTutorial(TutorialId.DEVELOPER_TEST);
		Tutorial t2 = stubTutorial(TutorialId.RANGE_TO_STAFF);
		registry.register(t1);
		registry.register(t2);
		Tutorial[] all = registry.getAll().toArray(new Tutorial[0]);
		assertEquals(2, all.length);
		assertEquals(TutorialId.DEVELOPER_TEST, all[0].getId());
		assertEquals(TutorialId.RANGE_TO_STAFF, all[1].getId());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void getAllIsUnmodifiable()
	{
		registry.getAll().clear();
	}

	private Tutorial stubTutorial(TutorialId id)
	{
		return new Tutorial()
		{
			@Override public TutorialId getId()                                   { return id; }
			@Override public TutorialState getState()                             { return TutorialState.NONE; }
			@Override public void start()                                         {}
			@Override public void onGameTick()                                    {}
			@Override public void onClientTick()                                  {}
			@Override public void onMenuOptionClicked(MenuOptionClicked e)        {}
			@Override public void reset()                                         {}
			@Override public void stop()                                          {}
		};
	}
}
