package com.pvptutorials.overlay;

import com.pvptutorials.PvpTutorialsConfig;
import com.pvptutorials.arena.ArenaDetector;
import com.pvptutorials.debug.PeteDebugTracker;
import com.pvptutorials.tutorial.TutorialManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class DebugOverlay extends Overlay
{
	private final Client client;
	private final PvpTutorialsConfig config;
	private final PeteDebugTracker debugTracker;
	private final ArenaDetector arenaDetector;
	private final TutorialManager tutorialManager;
	private final PanelComponent panel = new PanelComponent();

	@Inject
	public DebugOverlay(Client client, PvpTutorialsConfig config,
						PeteDebugTracker debugTracker, ArenaDetector arenaDetector,
						TutorialManager tutorialManager)
	{
		this.client          = client;
		this.config          = config;
		this.debugTracker    = debugTracker;
		this.arenaDetector   = arenaDetector;
		this.tutorialManager = tutorialManager;
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPosition(OverlayPosition.TOP_LEFT);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showDebugOverlay())
		{
			return null;
		}

		panel.getChildren().clear();
		panel.getChildren().add(TitleComponent.builder()
			.text("PvP Tutorials Debug")
			.color(Color.ORANGE)
			.build());

		if (client.getLocalPlayer() != null)
		{
			WorldPoint pos = WorldPoint.fromLocalInstance(
				client, client.getLocalPlayer().getLocalLocation());
			panel.getChildren().add(LineComponent.builder()
				.left("Region:")
				.right(String.valueOf(pos.getRegionID()))
				.leftColor(Color.GRAY)
				.rightColor(Color.WHITE)
				.build());
		}

		panel.getChildren().add(LineComponent.builder()
			.left("Arena:")
			.right(arenaDetector.getSession().getArenaState().name())
			.leftColor(Color.GRAY)
			.rightColor(Color.CYAN)
			.build());

		panel.getChildren().add(LineComponent.builder()
			.left("Tutorial:")
			.right(tutorialManager.getActiveState().name())
			.leftColor(Color.GRAY)
			.rightColor(Color.YELLOW)
			.build());

		if (arenaDetector.getSession().getPendingTutorialId() != null)
		{
			panel.getChildren().add(LineComponent.builder()
				.left("Pending:")
				.right(arenaDetector.getSession().getPendingTutorialId().name())
				.leftColor(Color.GRAY)
				.rightColor(Color.GREEN)
				.build());
		}

		List<String> recentWidgets = debugTracker.getRecentWidgetGroupIds();
		if (!recentWidgets.isEmpty())
		{
			panel.getChildren().add(LineComponent.builder()
				.left("Recent widgets:")
				.leftColor(Color.GRAY)
				.build());
			for (String w : recentWidgets)
			{
				panel.getChildren().add(LineComponent.builder()
					.left("  " + w)
					.leftColor(Color.WHITE)
					.build());
			}
		}

		return panel.render(graphics);
	}
}
