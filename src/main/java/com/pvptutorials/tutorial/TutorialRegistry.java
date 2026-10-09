package com.pvptutorials.tutorial;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Central registry of all available custom tutorials.
 * Only DEVELOPER_TEST is functional in Phase 5; others are placeholders.
 */
public class TutorialRegistry
{
	private final Map<TutorialId, Tutorial> tutorials = new LinkedHashMap<>();

	public void register(Tutorial tutorial)
	{
		tutorials.put(tutorial.getId(), tutorial);
	}

	public Tutorial get(TutorialId id)
	{
		return tutorials.get(id);
	}

	public Collection<Tutorial> getAll()
	{
		return Collections.unmodifiableCollection(tutorials.values());
	}

	public boolean contains(TutorialId id)
	{
		return tutorials.containsKey(id);
	}
}
