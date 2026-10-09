package com.pvptutorials.tutorial;

import java.util.List;
import net.runelite.api.events.MenuOptionClicked;

/**
 * Minimal tutorial that proves the full plugin architecture end-to-end.
 * Requires the GEAR_SWITCHING base arena. Completes after the player performs
 * two equip actions (Wield / Wear / Equip menu options).
 */
public class DeveloperTestTutorial implements Tutorial
{
	private static final List<String> EQUIP_OPTIONS = List.of("Wield", "Wear", "Equip");

	private TutorialState state = TutorialState.NONE;
	private int currentStep = 0;

	private final List<TutorialStep> steps = List.of(
		new TutorialStep("Wield a weapon"),
		new TutorialStep("Wield another item")
	);

	@Override
	public TutorialId getId()
	{
		return TutorialId.DEVELOPER_TEST;
	}

	@Override
	public TutorialState getState()
	{
		return state;
	}

	@Override
	public String getActiveInstruction()
	{
		if (state != TutorialState.ACTIVE || currentStep >= steps.size())
		{
			return null;
		}
		return steps.get(currentStep).getInstruction();
	}

	@Override
	public void start()
	{
		state = TutorialState.ACTIVE;
		currentStep = 0;
		steps.get(0).activate();
	}

	@Override
	public void onGameTick() {}

	@Override
	public void onClientTick() {}

	@Override
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (state != TutorialState.ACTIVE)
		{
			return;
		}
		if (!EQUIP_OPTIONS.contains(event.getMenuOption()))
		{
			return;
		}
		advance();
	}

	@Override
	public void reset()
	{
		state = TutorialState.NONE;
		currentStep = 0;
		steps.forEach(TutorialStep::reset);
	}

	@Override
	public void stop()
	{
		reset();
	}

	private void advance()
	{
		steps.get(currentStep).complete();
		currentStep++;
		if (currentStep >= steps.size())
		{
			state = TutorialState.COMPLETED;
		}
		else
		{
			steps.get(currentStep).activate();
		}
	}
}
