package com.pvptutorials.tutorial;

import com.pvptutorials.observation.TutorialEvent;
import com.pvptutorials.observation.TutorialEventType;
import java.util.List;

/**
 * Minimal tutorial that proves the full plugin architecture end-to-end.
 * Requires the GEAR_SWITCHING base arena.  Completes after the player equips
 * two items, demonstrating the generic step progression driven by TutorialEvents.
 */
public class DeveloperTestTutorial implements Tutorial
{
	private TutorialState state = TutorialState.NONE;
	private int currentStep = 0;

	private final List<TutorialStep> steps = List.of(
		new TutorialStep("Wield a weapon",
			e -> e.getType() == TutorialEventType.ITEM_EQUIPPED),
		new TutorialStep("Wield another item",
			e -> e.getType() == TutorialEventType.ITEM_EQUIPPED)
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
	public void onTutorialEvent(TutorialEvent event)
	{
		if (state != TutorialState.ACTIVE)
		{
			return;
		}
		if (steps.get(currentStep).isSatisfiedBy(event))
		{
			advance();
		}
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
