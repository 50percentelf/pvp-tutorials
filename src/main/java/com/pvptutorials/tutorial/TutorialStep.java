package com.pvptutorials.tutorial;

import lombok.Getter;

@Getter
public class TutorialStep
{
	private final String instruction;
	private TutorialStepState state = TutorialStepState.NOT_STARTED;

	public TutorialStep(String instruction)
	{
		this.instruction = instruction;
	}

	public void activate()
	{
		state = TutorialStepState.ACTIVE;
	}

	public void complete()
	{
		state = TutorialStepState.COMPLETED;
	}

	public void fail()
	{
		state = TutorialStepState.FAILED;
	}

	public void reset()
	{
		state = TutorialStepState.NOT_STARTED;
	}

	public boolean isActive()
	{
		return state == TutorialStepState.ACTIVE;
	}

	public boolean isCompleted()
	{
		return state == TutorialStepState.COMPLETED;
	}
}
