package com.pvptutorials.tutorial;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class TutorialSearchTest
{
	private final List<TutorialMetadata> all = BuiltInTutorials.ALL;

	// ── catalog completeness ─────────────────────────────────────────────────

	@Test
	public void catalogIsNonEmpty()
	{
		assertFalse(all.isEmpty());
	}

	@Test
	public void developerTestIsInCatalog()
	{
		assertTrue(all.stream().anyMatch(m -> m.getId() == TutorialId.DEVELOPER_TEST));
	}

	@Test
	public void developerTestIsImplemented()
	{
		TutorialMetadata m = all.stream()
			.filter(t -> t.getId() == TutorialId.DEVELOPER_TEST)
			.findFirst().orElseThrow();
		assertEquals(TutorialStatus.IMPLEMENTED, m.getStatus());
	}

	@Test
	public void everyEntryHasNonBlankDisplayName()
	{
		all.forEach(m -> assertFalse(
			"blank displayName for " + m.getId(),
			m.getDisplayName().isBlank()));
	}

	@Test
	public void everyEntryHasCategory()
	{
		all.forEach(m -> assertNotNull("null category for " + m.getId(), m.getCategory()));
	}

	@Test
	public void everyEntryHasDifficulty()
	{
		all.forEach(m -> assertNotNull("null difficulty for " + m.getId(), m.getDifficulty()));
	}

	// ── category filter ──────────────────────────────────────────────────────

	@Test
	public void filterByCategoryReturnsOnlyThatCategory()
	{
		List<TutorialMetadata> results = TutorialSearch.filter(all, TutorialCategory.KO_COMBOS, null, null);
		assertFalse(results.isEmpty());
		results.forEach(m -> assertEquals(TutorialCategory.KO_COMBOS, m.getCategory()));
	}

	@Test
	public void filterBySmiteCategoryFindsSmiteEntries()
	{
		List<TutorialMetadata> results = TutorialSearch.filter(all, TutorialCategory.SMITE, null, null);
		assertFalse(results.isEmpty());
	}

	// ── difficulty filter ────────────────────────────────────────────────────

	@Test
	public void filterByBeginnerDifficulty()
	{
		List<TutorialMetadata> results = TutorialSearch.filter(all, null, TutorialDifficulty.BEGINNER, null);
		assertFalse(results.isEmpty());
		results.forEach(m -> assertEquals(TutorialDifficulty.BEGINNER, m.getDifficulty()));
	}

	@Test
	public void filterByAdvancedDifficulty()
	{
		List<TutorialMetadata> results = TutorialSearch.filter(all, null, TutorialDifficulty.ADVANCED, null);
		assertFalse(results.isEmpty());
		results.forEach(m -> assertEquals(TutorialDifficulty.ADVANCED, m.getDifficulty()));
	}

	// ── combined filter ──────────────────────────────────────────────────────

	@Test
	public void combinedCategoryAndDifficultyFilter()
	{
		List<TutorialMetadata> results = TutorialSearch.filter(
			all, TutorialCategory.KO_COMBOS, TutorialDifficulty.ADVANCED, null);
		assertFalse(results.isEmpty());
		results.forEach(m -> {
			assertEquals(TutorialCategory.KO_COMBOS, m.getCategory());
			assertEquals(TutorialDifficulty.ADVANCED, m.getDifficulty());
		});
	}

	// ── text query ──────────────────────────────────────────────────────────

	@Test
	public void queryByNameFindsMatch()
	{
		List<TutorialMetadata> results = TutorialSearch.filter(all, null, null, "gmaul");
		assertFalse("expected gmaul results", results.isEmpty());
		results.forEach(m -> assertTrue(
			m.getDisplayName().toLowerCase().contains("gmaul")
				|| m.getTags().stream().anyMatch(t -> t.contains("gmaul"))));
	}

	@Test
	public void queryByTagFindsMatch()
	{
		List<TutorialMetadata> results = TutorialSearch.filter(all, null, null, "voidwaker");
		assertFalse("expected voidwaker results", results.isEmpty());
	}

	@Test
	public void queryBySingleWeaponFindsAllCombosContainingIt()
	{
		List<TutorialMetadata> ags = TutorialSearch.filter(all, null, null, "ags");
		assertTrue("at least two combos should mention AGS", ags.size() >= 2);
	}

	@Test
	public void queryIsCaseInsensitive()
	{
		List<TutorialMetadata> lower = TutorialSearch.filter(all, null, null, "gmaul");
		List<TutorialMetadata> upper = TutorialSearch.filter(all, null, null, "GMAUL");
		assertEquals(lower.size(), upper.size());
	}

	@Test
	public void emptyQueryReturnsAll()
	{
		List<TutorialMetadata> results = TutorialSearch.filter(all, null, null, "");
		assertEquals(all.size(), results.size());
	}

	@Test
	public void nullQueryReturnsAll()
	{
		List<TutorialMetadata> results = TutorialSearch.filter(all, null, null, null);
		assertEquals(all.size(), results.size());
	}

	@Test
	public void queryWithNoMatchReturnsEmpty()
	{
		List<TutorialMetadata> results = TutorialSearch.filter(all, null, null, "zzznomatch999");
		assertTrue(results.isEmpty());
	}

	// ── exact combo order — separate IDs ────────────────────────────────────

	@Test
	public void boltGmaulAgsAndBoltAgsGmaulAreDistinctEntries()
	{
		boolean hasGmaulAgs = all.stream().anyMatch(m -> m.getId() == TutorialId.BOLT_GMAUL_AGS);
		boolean hasAgsGmaul = all.stream().anyMatch(m -> m.getId() == TutorialId.BOLT_AGS_GMAUL);
		assertTrue(hasGmaulAgs);
		assertTrue(hasAgsGmaul);
		assertNotEquals(TutorialId.BOLT_GMAUL_AGS, TutorialId.BOLT_AGS_GMAUL);
	}

	@Test
	public void boltGmaulVoidwakerAndBoltVoidwakerGmaulAreDistinctEntries()
	{
		boolean hasGmaulVw = all.stream().anyMatch(m -> m.getId() == TutorialId.BOLT_GMAUL_VOIDWAKER);
		boolean hasVwGmaul = all.stream().anyMatch(m -> m.getId() == TutorialId.BOLT_VOIDWAKER_GMAUL);
		assertTrue(hasGmaulVw);
		assertTrue(hasVwGmaul);
		assertNotEquals(TutorialId.BOLT_GMAUL_VOIDWAKER, TutorialId.BOLT_VOIDWAKER_GMAUL);
	}
}
