package com.pvptutorials.tutorial;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;

/**
 * Central registry for all tutorials.
 *
 * Two separate maps:
 *  - metadata: full TutorialMetadata for every tutorial (including PLACEHOLDER entries).
 *    Used for library display and search.
 *  - implementations: Tutorial objects for IMPLEMENTED tutorials only.
 *    Use isImplemented() before requesting one.
 */
@Singleton
public class TutorialRegistry
{
	private final Map<TutorialId, TutorialMetadata> metadata       = new LinkedHashMap<>();
	private final Map<TutorialId, Tutorial>         implementations = new LinkedHashMap<>();

	public void define(TutorialMetadata meta)
	{
		metadata.put(meta.getId(), meta);
	}

	public void register(Tutorial tutorial)
	{
		implementations.put(tutorial.getId(), tutorial);
	}

	public TutorialMetadata getMetadata(TutorialId id)
	{
		return metadata.get(id);
	}

	public Tutorial getImplementation(TutorialId id)
	{
		return implementations.get(id);
	}

	public Collection<TutorialMetadata> getAllMetadata()
	{
		return Collections.unmodifiableCollection(metadata.values());
	}

	public List<TutorialMetadata> search(TutorialCategory category, TutorialDifficulty difficulty, String query)
	{
		return TutorialSearch.filter(metadata.values(), category, difficulty, query);
	}

	public boolean isDefined(TutorialId id)
	{
		return metadata.containsKey(id);
	}

	public boolean isImplemented(TutorialId id)
	{
		return implementations.containsKey(id);
	}
}
