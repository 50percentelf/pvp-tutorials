package com.pvptutorials.tutorial;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * In-memory search and filter helpers for the tutorial catalog.
 * All parameters are optional (null = no filter applied for that dimension).
 */
public final class TutorialSearch
{
	private TutorialSearch() {}

	/**
	 * Returns all entries that match every non-null filter.
	 * Query is matched case-insensitively against displayName, description, and tags.
	 */
	public static List<TutorialMetadata> filter(
		Collection<TutorialMetadata> all,
		TutorialCategory category,
		TutorialDifficulty difficulty,
		String query)
	{
		return all.stream()
			.filter(m -> category == null || m.getCategory() == category)
			.filter(m -> difficulty == null || m.getDifficulty() == difficulty)
			.filter(m -> query == null || query.isBlank() || matchesQuery(m, query))
			.collect(Collectors.toList());
	}

	/** Convenience overload — no text query. */
	public static List<TutorialMetadata> filter(
		Collection<TutorialMetadata> all,
		TutorialCategory category,
		TutorialDifficulty difficulty)
	{
		return filter(all, category, difficulty, null);
	}

	private static boolean matchesQuery(TutorialMetadata m, String query)
	{
		String q = query.toLowerCase(Locale.ROOT);
		if (m.getDisplayName().toLowerCase(Locale.ROOT).contains(q))   return true;
		if (m.getDescription().toLowerCase(Locale.ROOT).contains(q))   return true;
		return m.getTags().stream().anyMatch(t -> t.toLowerCase(Locale.ROOT).contains(q));
	}
}
