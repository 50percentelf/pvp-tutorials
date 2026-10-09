package com.pvptutorials.debug;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import net.runelite.api.widgets.Widget;

/**
 * Snapshot of a widget group's top-level structure, captured when the group loads.
 */
@Getter
public class WidgetDebugInfo
{
	private final int groupId;
	private final int childCount;
	private final List<String> sampleEntries = new ArrayList<>();

	public WidgetDebugInfo(int groupId, Widget[] roots)
	{
		this.groupId    = groupId;
		this.childCount = roots != null ? roots.length : 0;

		if (roots != null)
		{
			int limit = Math.min(roots.length, 20);
			for (int i = 0; i < limit; i++)
			{
				Widget w = roots[i];
				if (w == null)
				{
					continue;
				}
				String text   = w.getText() != null ? w.getText() : "";
				String action = (w.getActions() != null && w.getActions().length > 0)
					? w.getActions()[0] : "";
				sampleEntries.add(String.format(
					"[%d] type=%d text=\"%s\" action=\"%s\" w=%d h=%d hidden=%b",
					i, w.getType(), text, action,
					w.getWidth(), w.getHeight(), w.isHidden()));
			}
		}
	}

	public String toLogString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Widget group %d — %d top-level children", groupId, childCount));
		for (String s : sampleEntries)
		{
			sb.append("\n  ").append(s);
		}
		return sb.toString();
	}
}
