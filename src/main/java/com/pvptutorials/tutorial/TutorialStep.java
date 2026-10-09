package com.pvptutorials.tutorial;

import com.pvptutorials.observation.TutorialEvent;
import java.util.function.Predicate;
import lombok.Getter;

@Getter
public class TutorialStep
{
	private final String instruction;

	/**
	 * Determines whether a TutorialEvent satisfies this step.
	 * Null means the step has no automatic success condition and must be
	 * advanced programmatically by the tutorial implementation.
	 * For branching tutorials (e.g. hit-confirm drills) the tutorial handles
	 * routing in its onTutorialEvent() and may choose not to use this field.
	 */
	private final Predicate<TutorialEvent> successCondition;

	private TutorialStepState state = TutorialStepState.NOT_STARTED;

	/** Step with an explicit success condition (auto-advance on matching event). */
	public TutorialStep(String instruction, Predicate<TutorialEvent> successCondition)
	{
		this.instruction = instruction;
		this.successCondition = successCondition;
	}

	/** Step with no automatic condition — advance programmatically. */
	public TutorialStep(String instruction)
	{
		this(instruction, null);
	}

	/** Returns true if the given event satisfies this step's success condition. */
	public boolean isSatisfiedBy(TutorialEvent event)
	{
		return successCondition != null && successCondition.test(event);
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
