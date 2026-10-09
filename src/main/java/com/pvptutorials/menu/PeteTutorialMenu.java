package com.pvptutorials.menu;

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
		// Phase 3: add custom widget children to Pete's interface after structure is mapped.
		log.debug("Pete interface ready for custom section injection");
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
