package com.pvptutorials.menu;

import com.pvptutorials.tutorial.TutorialId;
import com.pvptutorials.tutorial.TutorialManager;
import com.pvptutorials.tutorial.TutorialRegistry;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

/**
 * Injects a custom "Additional Training" section into Pete Kayer's tutorial-selection menu.
 * The Pete interface group ID is unknown until Phase 1 widget discovery is complete.
 * See docs/pete-menu.md — update PETE_INTERFACE_GROUP once the ID is found.
 */
@Slf4j
public class PeteTutorialMenu
{
	// Found via Phase 1 widget discovery (widget group 970, 512×334).
	static final int PETE_INTERFACE_GROUP = 970;

	@Inject
	private Client client;

	@Inject
	private TutorialRegistry registry;

	@Inject
	private TutorialManager tutorialManager;

	private boolean injected = false;

	public void onWidgetLoaded(int groupId)
	{
		if (PETE_INTERFACE_GROUP == -1 || groupId != PETE_INTERFACE_GROUP)
		{
			return;
		}
		log.debug("Pete interface loaded (group {}), injecting custom section", groupId);
		inject();
	}

	public void onWidgetClosed(int groupId)
	{
		if (PETE_INTERFACE_GROUP == -1 || groupId != PETE_INTERFACE_GROUP)
		{
			return;
		}
		remove();
	}

	private void inject()
	{
		if (injected)
		{
			return;
		}
		// Auto-select DEVELOPER_TEST when Pete's menu opens.
		// Phase 3: replace with real widget buttons once the interface structure is mapped.
		tutorialManager.requestTutorial(TutorialId.DEVELOPER_TEST);
		log.debug("Auto-selected DEVELOPER_TEST via Pete menu trigger");
		injected = true;
	}

	private void remove()
	{
		if (!injected)
		{
			return;
		}
		// Phase 2: remove custom widget children.
		injected = false;
	}

	public boolean isInjected()
	{
		return injected;
	}
}
