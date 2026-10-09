package com.pvptutorials.observation;

import java.util.Optional;
import net.runelite.api.events.MenuOptionClicked;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ObservationLayerTest
{
	private ObservationLayer layer;

	@Before
	public void setUp()
	{
		layer = new ObservationLayer();
	}

	@Test
	public void wieldProducesItemEquipped()
	{
		Optional<TutorialEvent> result = layer.fromMenuOptionClicked(menuClick("Wield"));
		assertTrue(result.isPresent());
		assertEquals(TutorialEventType.ITEM_EQUIPPED, result.get().getType());
	}

	@Test
	public void wearProducesItemEquipped()
	{
		Optional<TutorialEvent> result = layer.fromMenuOptionClicked(menuClick("Wear"));
		assertTrue(result.isPresent());
		assertEquals(TutorialEventType.ITEM_EQUIPPED, result.get().getType());
	}

	@Test
	public void equipProducesItemEquipped()
	{
		Optional<TutorialEvent> result = layer.fromMenuOptionClicked(menuClick("Equip"));
		assertTrue(result.isPresent());
		assertEquals(TutorialEventType.ITEM_EQUIPPED, result.get().getType());
	}

	@Test
	public void useOptionProducesEmpty()
	{
		Optional<TutorialEvent> result = layer.fromMenuOptionClicked(menuClick("Use"));
		assertFalse(result.isPresent());
	}

	@Test
	public void dropOptionProducesEmpty()
	{
		Optional<TutorialEvent> result = layer.fromMenuOptionClicked(menuClick("Drop"));
		assertFalse(result.isPresent());
	}

	@Test
	public void examineOptionProducesEmpty()
	{
		Optional<TutorialEvent> result = layer.fromMenuOptionClicked(menuClick("Examine"));
		assertFalse(result.isPresent());
	}

	// ── helpers ───────────────────────────────────────────────────────────────

	private static MenuOptionClicked menuClick(String option)
	{
		MenuOptionClicked event = mock(MenuOptionClicked.class);
		when(event.getMenuOption()).thenReturn(option);
		return event;
	}
}
