package com.pvptutorials;

import com.google.inject.Provides;
import com.pvptutorials.arena.ArenaDetector;
import com.pvptutorials.arena.BaseTutorial;
import com.pvptutorials.debug.PeteDebugTracker;
import com.pvptutorials.menu.PeteTutorialMenu;
import com.pvptutorials.overlay.DebugOverlay;
import com.pvptutorials.overlay.TutorialOverlay;
import com.pvptutorials.simulation.SimulationEngine;
import com.pvptutorials.tutorial.DeveloperTestTutorial;
import com.pvptutorials.tutorial.TutorialDefinition;
import com.pvptutorials.tutorial.TutorialId;
import com.pvptutorials.tutorial.TutorialManager;
import com.pvptutorials.tutorial.TutorialRegistry;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "PvP Tutorials",
	description = "Client-side PvP tutorials extending Pete Kayer's training system",
	tags = {"pvp", "tutorial", "combat", "training"}
)
public class PvpTutorialsPlugin extends Plugin
{
	@Inject private Client                 client;
	@Inject private PvpTutorialsConfig     config;
	@Inject private OverlayManager         overlayManager;
	@Inject private PeteDebugTracker       debugTracker;
	@Inject private ArenaDetector          arenaDetector;
	@Inject private TutorialManager        tutorialManager;
	@Inject private TutorialRegistry       tutorialRegistry;
	@Inject private PeteTutorialMenu       peteTutorialMenu;
	@Inject private SimulationEngine       simulationEngine;
	@Inject private DebugOverlay           debugOverlay;
	@Inject private TutorialOverlay        tutorialOverlay;
	@Inject private DeveloperTestTutorial  developerTestTutorial;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(debugOverlay);
		overlayManager.add(tutorialOverlay);
		registerTutorials();
		log.info("PvP Tutorials started");
	}

	private void registerTutorials()
	{
		tutorialRegistry.define(new TutorialDefinition(
			TutorialId.DEVELOPER_TEST, BaseTutorial.GEAR_SWITCHING,
			"Developer Test", "Proves the full plugin architecture end-to-end"));
		tutorialRegistry.register(developerTestTutorial);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(debugOverlay);
		overlayManager.remove(tutorialOverlay);
		tutorialManager.stopActiveTutorial();
		arenaDetector.reset();
		simulationEngine.reset();
		debugTracker.reset();
		log.info("PvP Tutorials stopped");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		arenaDetector.onGameStateChanged(event.getGameState());
		if (event.getGameState() == GameState.LOGGING_IN
			|| event.getGameState() == GameState.HOPPING)
		{
			tutorialManager.stopActiveTutorial();
			simulationEngine.reset();
			debugTracker.reset();
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		arenaDetector.onGameTick();

		if (arenaDetector.getSession().isActive()
			&& arenaDetector.getSession().getActiveTutorialId() != null
			&& tutorialManager.getActiveTutorial() == null)
		{
			tutorialManager.onArenaActivated();
		}

		if (!arenaDetector.getSession().isInside()
			&& tutorialManager.getActiveTutorial() != null)
		{
			tutorialManager.stopActiveTutorial();
			simulationEngine.reset();
		}

		tutorialManager.onGameTick();
		simulationEngine.onGameTick();
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
		debugTracker.onWidgetLoaded(event, config.logWidgetLoads());
		peteTutorialMenu.onWidgetLoaded(event.getGroupId());
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		debugTracker.onNpcSpawned(event, config.logNpcSpawns());
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
	{
		debugTracker.onNpcDespawned(event, config.logNpcSpawns());
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		debugTracker.onChatMessage(event, config.logChatMessages());
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		debugTracker.onVarbitChanged(event, config.logVarbits());
		arenaDetector.onVarbitChanged(event);
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		tutorialManager.onMenuOptionClicked(event);
	}

	@Provides
	PvpTutorialsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PvpTutorialsConfig.class);
	}
}
