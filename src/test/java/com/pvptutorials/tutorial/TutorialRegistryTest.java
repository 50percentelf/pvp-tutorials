package com.pvptutorials.tutorial;

import com.pvptutorials.arena.BaseTutorial;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TutorialRegistryTest
{
	private TutorialRegistry registry;

	@Before
	public void setUp()
	{
		registry = new TutorialRegistry();
	}

	// ── TutorialMetadata ──────────────────────────────────────────────────────

	@Test
	public void definedMetadataIsRetrievable()
	{
		TutorialMetadata meta = meta(TutorialId.DEVELOPER_TEST, BaseTutorial.GEAR_SWITCHING, "Developer Test", "Proves architecture");
		registry.define(meta);
		assertSame(meta, registry.getMetadata(TutorialId.DEVELOPER_TEST));
		assertTrue(registry.isDefined(TutorialId.DEVELOPER_TEST));
	}

	@Test
	public void undefinedIdReturnsNull()
	{
		assertNull(registry.getMetadata(TutorialId.RANGE_TO_STAFF));
		assertFalse(registry.isDefined(TutorialId.RANGE_TO_STAFF));
	}

	@Test
	public void getAllMetadataReflectsInsertionOrder()
	{
		TutorialMetadata m1 = meta(TutorialId.DEVELOPER_TEST, BaseTutorial.GEAR_SWITCHING, "Dev", "");
		TutorialMetadata m2 = meta(TutorialId.RANGE_TO_STAFF, BaseTutorial.GEAR_SWITCHING, "R2S", "");
		registry.define(m1);
		registry.define(m2);
		TutorialMetadata[] all = registry.getAllMetadata().toArray(new TutorialMetadata[0]);
		assertEquals(2, all.length);
		assertEquals(TutorialId.DEVELOPER_TEST, all[0].getId());
		assertEquals(TutorialId.RANGE_TO_STAFF, all[1].getId());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void getAllMetadataIsUnmodifiable()
	{
		registry.getAllMetadata().clear();
	}

	// ── Tutorial implementations ───────────────────────────────────────────────

	@Test
	public void registeredImplementationIsRetrievable()
	{
		Tutorial stub = stubTutorial(TutorialId.DEVELOPER_TEST);
		registry.register(stub);
		assertSame(stub, registry.getImplementation(TutorialId.DEVELOPER_TEST));
		assertTrue(registry.isImplemented(TutorialId.DEVELOPER_TEST));
	}

	@Test
	public void unimplementedIdReturnsNull()
	{
		assertNull(registry.getImplementation(TutorialId.RANGE_TO_STAFF));
		assertFalse(registry.isImplemented(TutorialId.RANGE_TO_STAFF));
	}

	@Test
	public void definedButUnimplementedShowsDefined()
	{
		TutorialMetadata m = meta(TutorialId.BOLT_GMAUL_AGS, BaseTutorial.SPECIAL_ATTACKS, "Bolt→Gmaul→AGS", "");
		registry.define(m);
		assertTrue(registry.isDefined(TutorialId.BOLT_GMAUL_AGS));
		assertFalse(registry.isImplemented(TutorialId.BOLT_GMAUL_AGS));
	}

	// ── search delegation ──────────────────────────────────────────────────────

	@Test
	public void searchDelegatesFilterCorrectly()
	{
		registry.define(meta(TutorialId.BOLT_GMAUL_AGS, BaseTutorial.SPECIAL_ATTACKS, "Bolt→Gmaul→AGS", ""));
		registry.define(TutorialMetadata.builder()
			.id(TutorialId.DEVELOPER_TEST)
			.displayName("Developer Test").description("")
			.category(TutorialCategory.FUNDAMENTALS)
			.difficulty(TutorialDifficulty.BEGINNER)
			.tags(List.of())
			.requiredBase(BaseTutorial.GEAR_SWITCHING)
			.status(TutorialStatus.PLACEHOLDER)
			.build());
		List<TutorialMetadata> results = registry.search(TutorialCategory.KO_COMBOS, null, null);
		assertEquals(1, results.size());
		assertEquals(TutorialId.BOLT_GMAUL_AGS, results.get(0).getId());
	}

	// ── Helpers ───────────────────────────────────────────────────────────────

	private static TutorialMetadata meta(TutorialId id, BaseTutorial base, String name, String desc)
	{
		return TutorialMetadata.builder()
			.id(id)
			.displayName(name)
			.description(desc)
			.category(TutorialCategory.KO_COMBOS)
			.difficulty(TutorialDifficulty.BEGINNER)
			.tags(List.of())
			.requiredBase(base)
			.status(TutorialStatus.PLACEHOLDER)
			.build();
	}

	private static Tutorial stubTutorial(TutorialId id)
	{
		return new Tutorial()
		{
			@Override public TutorialId getId()       { return id; }
			@Override public TutorialState getState() { return TutorialState.NONE; }
			@Override public void start()             {}
			@Override public void reset()             {}
			@Override public void stop()              {}
		};
	}
}
