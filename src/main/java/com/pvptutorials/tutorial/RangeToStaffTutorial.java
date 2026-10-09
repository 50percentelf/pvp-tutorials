package com.pvptutorials.tutorial;

import com.pvptutorials.observation.TutorialEvent;
import com.pvptutorials.observation.TutorialEventType;
import java.util.List;
import javax.inject.Singleton;

/**
 * Requires the player to attack with a ranged weapon, then equip a staff.
 * Uses Pete's Gear Switching base arena.
 */
@Singleton
public class RangeToStaffTutorial implements Tutorial
{
	private TutorialState state = TutorialState.NONE;
	private int currentStep = 0;

	private final List<TutorialStep> steps = List.of(
		new TutorialStep("Attack with your ranged weapon",
			e -> e.getType() == TutorialEventType.ATTACK_STARTED),
		new TutorialStep("Equip your staff",
			e -> e.getType() == TutorialEventType.ITEM_EQUIPPED)
	);

	@Override
	public TutorialId getId()
	{
		return TutorialId.RANGE_TO_STAFF;
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
