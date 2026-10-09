package com.pvptutorials.overlay;

import com.pvptutorials.tutorial.TutorialManager;
import com.pvptutorials.tutorial.TutorialState;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class TutorialOverlay extends Overlay
{
	private final TutorialManager tutorialManager;
	private final PanelComponent panel = new PanelComponent();

	@Inject
	public TutorialOverlay(TutorialManager tutorialManager)
	{
		this.tutorialManager = tutorialManager;
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPosition(OverlayPosition.BOTTOM_LEFT);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		TutorialState state = tutorialManager.getActiveState();
		if (state == TutorialState.NONE)
		{
			return null;
		}

		panel.getChildren().clear();
		panel.getChildren().add(TitleComponent.builder()
			.text("PvP Tutorial")
			.color(Color.YELLOW)
			.build());

		if (tutorialManager.getActiveTutorial() != null)
		{
			panel.getChildren().add(LineComponent.builder()
				.left(tutorialManager.getActiveTutorial().getId().getDisplayName())
				.leftColor(Color.WHITE)
				.build());
			panel.getChildren().add(LineComponent.builder()
				.left("State: " + state)
				.leftColor(Color.CYAN)
				.build());
		}

		return panel.render(graphics);
	}
}
