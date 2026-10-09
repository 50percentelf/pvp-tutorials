package com.pvptutorials;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("pvp-tutorials")
public interface PvpTutorialsConfig extends Config
{
	@ConfigSection(
		name = "Debug",
		description = "Developer instrumentation for reverse-engineering Pete Kayer's system",
		position = 0,
		closedByDefault = true
	)
	String debugSection = "debug";

	@ConfigItem(
		keyName = "showDebugOverlay",
		name = "Show Debug Overlay",
		description = "Render the debug overlay showing region, widget, arena, and tutorial state",
		section = debugSection,
		position = 0
	)
	default boolean showDebugOverlay()
	{
		return false;
	}

	@ConfigItem(
		keyName = "logWidgetLoads",
		name = "Log Widget Loads",
		description = "Log every WidgetLoaded event to help identify Pete Kayer's interface group ID",
		section = debugSection,
		position = 1
	)
	default boolean logWidgetLoads()
	{
		return false;
	}

	@ConfigItem(
		keyName = "logNpcSpawns",
		name = "Log NPC Spawns/Despawns",
		description = "Log NPC spawn and despawn events for arena lifecycle discovery",
		section = debugSection,
		position = 2
	)
	default boolean logNpcSpawns()
	{
		return false;
	}

	@ConfigItem(
		keyName = "logVarbits",
		name = "Log Varbit Changes",
		description = "Log all varbit changes for arena/tutorial state discovery",
		section = debugSection,
		position = 3
	)
	default boolean logVarbits()
	{
		return false;
	}

	@ConfigItem(
		keyName = "logChatMessages",
		name = "Log Game Messages",
		description = "Log GAME and ENGINE chat messages for arena lifecycle discovery",
		section = debugSection,
		position = 4
	)
	default boolean logChatMessages()
	{
		return false;
	}
}
