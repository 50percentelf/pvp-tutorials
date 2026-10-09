package com.pvptutorials.tutorial;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Central registry for all custom tutorials.
 *
 * Two separate maps:
 *  - definitions: metadata for ALL tutorials (including unimplemented placeholders).
 *    Used for menu generation.
 *  - implementations: Tutorial objects for tutorials that are actually runnable.
 *    Only DEVELOPER_TEST is expected to be implemented initially.
 */
public class TutorialRegistry
{
	private final Map<TutorialId, TutorialDefinition> definitions    = new LinkedHashMap<>();
	private final Map<TutorialId, Tutorial>           implementations = new LinkedHashMap<>();

	public void define(TutorialDefinition definition)
	{
		definitions.put(definition.getId(), definition);
	}

	public void register(Tutorial tutorial)
	{
		implementations.put(tutorial.getId(), tutorial);
	}

	public TutorialDefinition getDefinition(TutorialId id)
	{
		return definitions.get(id);
	}

	public Tutorial getImplementation(TutorialId id)
	{
		return implementations.get(id);
	}

	public Collection<TutorialDefinition> getAllDefinitions()
	{
		return Collections.unmodifiableCollection(definitions.values());
	}

	public boolean isDefined(TutorialId id)
	{
		return definitions.containsKey(id);
	}

	public boolean isImplemented(TutorialId id)
	{
		return implementations.containsKey(id);
	}
}
