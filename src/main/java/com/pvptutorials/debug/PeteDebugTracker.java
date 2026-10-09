package com.pvptutorials.debug;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;

/**
 * Accumulates debug data for reverse-engineering Pete Kayer's tutorial system.
 * All logging is behind config flags — enable in the plugin's Debug config section.
 */
@Slf4j
public class PeteDebugTracker
{
	private static final int RECENT_WIDGET_MAX = 10;

	@Inject
	private Client client;

	private final Deque<String> recentWidgetGroupIds = new ArrayDeque<>(RECENT_WIDGET_MAX);

	public void onWidgetLoaded(WidgetLoaded event, boolean logEnabled)
	{
		int groupId = event.getGroupId();

		if (recentWidgetGroupIds.size() >= RECENT_WIDGET_MAX)
		{
			recentWidgetGroupIds.pollFirst();
		}
		recentWidgetGroupIds.addLast(String.valueOf(groupId));

		if (logEnabled)
		{
			Widget root = client.getWidget(groupId, 0);
			Widget[] roots = root != null ? new Widget[]{root} : null;
			WidgetDebugInfo info = new WidgetDebugInfo(groupId, roots);
			log.debug("[PeteDebug] {}", info.toLogString());
		}
	}

	public void onNpcSpawned(NpcSpawned event, boolean logEnabled)
	{
		if (!logEnabled)
		{
			return;
		}
		NPC npc = event.getNpc();
		log.debug("[PeteDebug] NPC spawned: id={} name=\"{}\" pos={}",
			npc.getId(), npc.getName(), npc.getWorldLocation());
	}

	public void onNpcDespawned(NpcDespawned event, boolean logEnabled)
	{
		if (!logEnabled)
		{
			return;
		}
		NPC npc = event.getNpc();
		log.debug("[PeteDebug] NPC despawned: id={} name=\"{}\"",
			npc.getId(), npc.getName());
	}

	public void onChatMessage(ChatMessage event, boolean logEnabled)
	{
		if (!logEnabled)
		{
			return;
		}
		ChatMessageType type = event.getType();
		if (type == ChatMessageType.GAMEMESSAGE || type == ChatMessageType.ENGINE)
		{
			log.debug("[PeteDebug] Chat [{}]: {}", type, event.getMessage());
		}
	}

	public void onVarbitChanged(VarbitChanged event, boolean logEnabled)
	{
		if (!logEnabled)
		{
			return;
		}
		log.debug("[PeteDebug] Varbit varbitId={} varpId={} value={}",
			event.getVarbitId(), event.getVarpId(), event.getValue());
	}

	public List<String> getRecentWidgetGroupIds()
	{
		return new ArrayList<>(recentWidgetGroupIds);
	}

	public void reset()
	{
		recentWidgetGroupIds.clear();
	}
}
