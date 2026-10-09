package com.pvptutorials.menu;

import com.pvptutorials.tutorial.TutorialId;
import com.pvptutorials.tutorial.TutorialManager;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.Point;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.input.MouseAdapter;
import net.runelite.client.input.MouseManager;
import net.runelite.client.input.MouseWheelListener;

@Slf4j
@Singleton
public class PeteTutorialMenu
{
	static final int PETE_INTERFACE_GROUP = 970;
	private static final int CONTENT_PANE_COMP = 8;
	private static final int SCROLL_PANE_COMP  = 7;

	// ── geometry (cloned from Pete's native rows) ─────────────────────────────
	private static final int ROW_H          = 47;
	private static final int TITLE_X        = 46;
	private static final int TITLE_Y_OFFSET = 5;
	private static final int TITLE_H        = 16;
	private static final int DESC_X         = 48;
	private static final int DESC_Y_OFFSET  = 24;
	private static final int DESC_H         = 16;
	private static final int FONT_ID        = 495;

	// ── colors ────────────────────────────────────────────────────────────────
	private static final int COLOR_TITLE    = 0xFFFFFF;
	private static final int COLOR_DESC     = 0x7F7F7F;
	private static final int COLOR_HEADER   = 0x9F9F9F;
	private static final int COLOR_DIVIDER  = 0x5A4D3E;
	private static final int COLOR_ROW_BG   = 0x3E3529;
	private static final int COLOR_ROW_HOVER = 0x574B3C;

	@Inject private Client client;
	@Inject private TutorialManager tutorialManager;
	@Inject private MouseManager mouseManager;

	// ── state ─────────────────────────────────────────────────────────────────
	private final List<RowEntry> injectedRows = new ArrayList<>();
	private int dynCountBeforeInjection = -1;
	private boolean peteOpen   = false;
	private boolean treeLogged = false;

	@Getter private PeteChallengeDefinition activeDetail = null;
	@Getter private Rectangle viewportScreenRect = null; // screen rect of component 7 (FIXED, not scroll-adjusted)
	private int detailScrollY = 0;

	// ── inner type ────────────────────────────────────────────────────────────

	private static class RowEntry
	{
		Widget rowBg;
		int    rowY;
		PeteChallengeDefinition def;
	}

	// ── mouse listener ────────────────────────────────────────────────────────

	private final MouseAdapter mouseListener = new MouseAdapter()
	{
		@Override
		public MouseEvent mouseClicked(MouseEvent event)
		{
			if (event.getButton() != MouseEvent.BUTTON1) return event;
			if (!peteOpen) return event;
			int mx = event.getX(), my = event.getY();

			if (activeDetail != null)
			{
				Rectangle back  = getBackButtonRect();
				Rectangle start = getStartButtonRect();
				if (back.contains(mx, my))
				{
					activeDetail  = null;
					detailScrollY = 0;
					return event;
				}
				if (start.contains(mx, my) && activeDetail.getId() != null)
				{
					final TutorialId id = activeDetail.getId();
					activeDetail  = null;
					detailScrollY = 0;
					tutorialManager.startTutorial(id);
					return event;
				}
				return event;
			}

			if (injectedRows.isEmpty() || viewportScreenRect == null) return event;
			Widget scrollWidget = client.getWidget(PETE_INTERFACE_GROUP, SCROLL_PANE_COMP);
			int scrollY = scrollWidget != null ? scrollWidget.getScrollY() : 0;
			Widget pane = client.getWidget(PETE_INTERFACE_GROUP, CONTENT_PANE_COMP);
			int paneW = pane != null ? pane.getWidth() : 472;

			for (RowEntry row : injectedRows)
			{
				int rowTop = viewportScreenRect.y + row.rowY - scrollY;
				int rowBot = rowTop + ROW_H;
				if (mx >= viewportScreenRect.x && mx <= viewportScreenRect.x + paneW
					&& my >= rowTop && my <= rowBot)
				{
					activeDetail  = row.def;
					detailScrollY = 0;
					return event;
				}
			}
			return event;
		}
	};

	private final MouseWheelListener wheelListener = e ->
	{
		if (activeDetail != null && viewportScreenRect != null)
		{
			detailScrollY = Math.max(0, detailScrollY + e.getWheelRotation() * 15);
		}
		return e;
	};

	// ── public lifecycle ──────────────────────────────────────────────────────

	public void startListening()
	{
		mouseManager.registerMouseListener(mouseListener);
		mouseManager.registerMouseWheelListener(wheelListener);
	}

	public void stopListening()
	{
		mouseManager.unregisterMouseListener(mouseListener);
		mouseManager.unregisterMouseWheelListener(wheelListener);
	}

	// ── events ────────────────────────────────────────────────────────────────

	public void onWidgetLoaded(int groupId)
	{
		if (groupId != PETE_INTERFACE_GROUP)
		{
			return;
		}
		peteOpen = true;
		clearInjectedState();
		treeLogged = false;
	}

	public void onClientTick()
	{
		if (!peteOpen || injectedRows.isEmpty()) return;
		updateViewportScreenRect();
		checkHover();
	}

	public void onGameTick()
	{
		Widget[] group = getPeteGroup();

		if (group == null)
		{
			if (peteOpen)
			{
				peteOpen = false;
				clearInjectedState();
				treeLogged = false;
			}
			return;
		}

		peteOpen = true;

		if (!injectedRows.isEmpty())
		{
			// Detect if Pete reloaded his list (back button pressed, etc.)
			Widget pane = client.getWidget(PETE_INTERFACE_GROUP, CONTENT_PANE_COMP);
			if (pane != null)
			{
				Widget[] dyn = pane.getDynamicChildren();
				int cur = dyn != null ? dyn.length : 0;
				if (cur <= dynCountBeforeInjection)
				{
					clearInjectedState();
				}
			}
		}

		if (!injectedRows.isEmpty())
		{
			return;
		}

		if (group.length <= 1)
		{
			return;
		}

		if (!treeLogged)
		{
			logFlatGroup(group);
			treeLogged = true;
		}

		injectInto(group);
	}

	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		// handled via RUNELITE onClick lambdas in onMenuOpened
	}

	public void onMenuOpened(MenuOpened event)
	{
		if (!peteOpen)
		{
			return;
		}

		Point mouse = client.getMouseCanvasPosition();

		if (activeDetail != null)
		{
			handleDetailViewMenu(mouse);
		}
		else if (!injectedRows.isEmpty())
		{
			handleListViewMenu(mouse);
		}
	}

	public int getDetailScrollY()
	{
		return detailScrollY;
	}

	// ── detail view menu ──────────────────────────────────────────────────────

	private void handleDetailViewMenu(Point mouse)
	{
		if (viewportScreenRect == null)
		{
			return;
		}

		Rectangle backBtn = getBackButtonRect();
		if (backBtn.contains(mouse.getX(), mouse.getY()))
		{
			client.createMenuEntry(0)
				.setOption("Back")
				.setTarget("")
				.setType(MenuAction.RUNELITE)
				.onClick(e ->
				{
					activeDetail  = null;
					detailScrollY = 0;
				});
			return;
		}

		if (activeDetail.getId() != null)
		{
			Rectangle startBtn = getStartButtonRect();
			if (startBtn.contains(mouse.getX(), mouse.getY()))
			{
				final TutorialId id    = activeDetail.getId();
				final String    title  = activeDetail.getTitle();
				client.createMenuEntry(0)
					.setOption("Start")
					.setTarget("<col=ff9040>" + title + "</col>")
					.setType(MenuAction.RUNELITE)
					.onClick(e ->
					{
						activeDetail  = null;
						detailScrollY = 0;
						tutorialManager.startTutorial(id);
					});
			}
		}
	}

	// ── list view menu ────────────────────────────────────────────────────────

	private void handleListViewMenu(Point mouse)
	{
		if (viewportScreenRect == null)
		{
			return;
		}

		Widget scrollWidget = client.getWidget(PETE_INTERFACE_GROUP, SCROLL_PANE_COMP);
		int scrollY = scrollWidget != null ? scrollWidget.getScrollY() : 0;
		Widget pane = client.getWidget(PETE_INTERFACE_GROUP, CONTENT_PANE_COMP);
		if (pane == null)
		{
			return;
		}

		for (RowEntry row : injectedRows)
		{
			int rowTop = viewportScreenRect.y + row.rowY - scrollY;
			int rowBot = rowTop + ROW_H;

			if (mouse.getX() < viewportScreenRect.x
				|| mouse.getX() > viewportScreenRect.x + pane.getWidth()
				|| mouse.getY() < rowTop
				|| mouse.getY() > rowBot)
			{
				continue;
			}

			final PeteChallengeDefinition def = row.def;

			// "View" is index 0 — becomes the default left-click action
			client.createMenuEntry(0)
				.setOption("View")
				.setTarget("<col=ff9040>" + def.getTitle() + "</col>")
				.setType(MenuAction.RUNELITE)
				.onClick(e ->
				{
					activeDetail  = def;
					detailScrollY = 0;
				});

			// "Start" (or nothing for unimplemented stubs)
			if (def.getId() != null)
			{
				client.createMenuEntry(-1)
					.setOption("Start")
					.setTarget("<col=ff9040>" + def.getTitle() + "</col>")
					.setType(MenuAction.RUNELITE)
					.onClick(e -> tutorialManager.startTutorial(def.getId()));
			}

			break;
		}
	}

	// ── hover highlighting ────────────────────────────────────────────────────

	private void checkHover()
	{
		if (viewportScreenRect == null || activeDetail != null)
		{
			return;
		}

		Widget pane = client.getWidget(PETE_INTERFACE_GROUP, CONTENT_PANE_COMP);
		if (pane == null)
		{
			return;
		}

		Widget scrollWidget = client.getWidget(PETE_INTERFACE_GROUP, SCROLL_PANE_COMP);
		int scrollY = scrollWidget != null ? scrollWidget.getScrollY() : 0;

		Point mouse = client.getMouseCanvasPosition();

		for (RowEntry row : injectedRows)
		{
			int rowTop = viewportScreenRect.y + row.rowY - scrollY;
			int rowBot = rowTop + ROW_H;
			boolean hovered = mouse.getX() >= viewportScreenRect.x
				&& mouse.getX() <= viewportScreenRect.x + pane.getWidth()
				&& mouse.getY() >= rowTop
				&& mouse.getY() <= rowBot;

			int target = hovered ? COLOR_ROW_HOVER : COLOR_ROW_BG;
			if (row.rowBg.getTextColor() != target)
			{
				row.rowBg.setTextColor(target);
				row.rowBg.revalidate();
			}
		}
	}

	// ── injection ─────────────────────────────────────────────────────────────

	private void injectInto(Widget[] group)
	{
		Widget contentPane = findContentPane(group);
		if (contentPane == null)
		{
			log.warn("[PvpTutorials] No content pane found in Pete group 970");
			return;
		}

		Widget[] dyn = contentPane.getDynamicChildren();
		dynCountBeforeInjection = dyn != null ? dyn.length : 0;

		Widget scrollContent = findParent(group, contentPane);
		int paneW      = contentPane.getWidth();
		int listBottom = computeInsertY(contentPane);

		int sepY      = listBottom;
		int headY     = sepY + 3;
		int firstRowY = sepY + 22;

		// 1px horizontal divider
		Widget divider = contentPane.createChild(-1, WidgetType.RECTANGLE);
		divider.setFilled(true);
		divider.setTextColor(COLOR_DIVIDER);
		divider.setOriginalX(0);
		divider.setOriginalY(sepY);
		divider.setOriginalWidth(paneW);
		divider.setOriginalHeight(1);
		divider.revalidate();

		// "Additional Training" section heading
		Widget heading = contentPane.createChild(-1, WidgetType.TEXT);
		heading.setText("Additional Training");
		heading.setTextColor(COLOR_HEADER);
		heading.setFontId(FONT_ID);
		heading.setXTextAlignment(1);
		heading.setOriginalX(0);
		heading.setOriginalY(headY);
		heading.setOriginalWidth(paneW);
		heading.setOriginalHeight(TITLE_H);
		heading.revalidate();

		List<PeteChallengeDefinition> challenges = PeteChallengeManifest.ALL;
		for (int i = 0; i < challenges.size(); i++)
		{
			PeteChallengeDefinition def = challenges.get(i);
			int rowY = firstRowY + i * ROW_H;

			RowEntry entry = new RowEntry();
			entry.def  = def;
			entry.rowY = rowY;

			Widget rowBg = contentPane.createChild(-1, WidgetType.RECTANGLE);
			rowBg.setFilled(true);
			rowBg.setTextColor(COLOR_ROW_BG);
			rowBg.setOriginalX(0);
			rowBg.setOriginalY(rowY);
			rowBg.setOriginalWidth(paneW);
			rowBg.setOriginalHeight(ROW_H);
			rowBg.revalidate();
			entry.rowBg = rowBg;

			Widget title = contentPane.createChild(-1, WidgetType.TEXT);
			title.setText(def.getTitle());
			title.setTextColor(COLOR_TITLE);
			title.setFontId(FONT_ID);
			title.setOriginalX(TITLE_X);
			title.setOriginalY(rowY + TITLE_Y_OFFSET);
			title.setOriginalWidth(paneW - TITLE_X - 25);
			title.setOriginalHeight(TITLE_H);
			title.revalidate();

			Widget desc = contentPane.createChild(-1, WidgetType.TEXT);
			desc.setText(def.getRowDesc());
			desc.setTextColor(COLOR_DESC);
			desc.setFontId(FONT_ID);
			desc.setOriginalX(DESC_X);
			desc.setOriginalY(rowY + DESC_Y_OFFSET);
			desc.setOriginalWidth(paneW - DESC_X - 25);
			desc.setOriginalHeight(DESC_H);
			desc.revalidate();

			Widget arrow = contentPane.createChild(-1, WidgetType.TEXT);
			arrow.setText(">");
			arrow.setTextColor(COLOR_DESC);
			arrow.setFontId(FONT_ID);
			arrow.setXTextAlignment(1);
			arrow.setOriginalX(paneW - 25);
			arrow.setOriginalY(rowY + TITLE_Y_OFFSET);
			arrow.setOriginalWidth(20);
			arrow.setOriginalHeight(TITLE_H);
			arrow.revalidate();

			injectedRows.add(entry);
		}

		// Ensure scrollbar activates (totalNeeded must exceed component height)
		int compHeight = contentPane.getHeight() > 0 ? contentPane.getHeight() : 555;
		int totalNeeded = Math.max(firstRowY + challenges.size() * ROW_H + 5, compHeight + 1);
		if (scrollContent != null)
		{
			scrollContent.setScrollHeight(totalNeeded);
			scrollContent.revalidate();
		}
		contentPane.setScrollHeight(totalNeeded);
		contentPane.revalidate();

		updateViewportScreenRect();

		log.debug("[PvpTutorials] Injected {} rows — dynBefore={} listBottom={} firstRowY={}",
			challenges.size(), dynCountBeforeInjection, listBottom, firstRowY);
	}

	// ── button rectangles (used by overlay and menu handler) ──────────────────

	public Rectangle getBackButtonRect()
	{
		if (viewportScreenRect == null)
		{
			return new Rectangle(0, 0, 0, 0);
		}
		return new Rectangle(viewportScreenRect.x + 6, viewportScreenRect.y + 6, 72, 22);
	}

	public Rectangle getStartButtonRect()
	{
		if (viewportScreenRect == null)
		{
			return new Rectangle(0, 0, 0, 0);
		}
		int btnW = 150, btnH = 28;
		return new Rectangle(
			viewportScreenRect.x + (viewportScreenRect.width - btnW) / 2,
			viewportScreenRect.y + viewportScreenRect.height - btnH - 10,
			btnW, btnH);
	}

	// ── helpers ───────────────────────────────────────────────────────────────

	private void clearInjectedState()
	{
		injectedRows.clear();
		dynCountBeforeInjection = -1;
		viewportScreenRect = null;
		activeDetail = null;
		detailScrollY = 0;
	}

	private void updateViewportScreenRect()
	{
		// Walk from component 7 (SCROLL_PANE_COMP) — its position is fixed and
		// does not shift with scroll, unlike component 8 (content pane).
		Widget viewport = client.getWidget(PETE_INTERFACE_GROUP, SCROLL_PANE_COMP);
		if (viewport == null)
		{
			viewportScreenRect = null;
			return;
		}
		int sx = 0, sy = 0;
		Widget w = viewport;
		while (w != null)
		{
			sx += w.getRelativeX();
			sy += w.getRelativeY();
			int pid = w.getParentId();
			if (pid == -1)
			{
				break;
			}
			int pg = pid >>> 16;
			int pc = pid & 0xFFFF;
			if (pg != PETE_INTERFACE_GROUP)
			{
				break;
			}
			Widget p = client.getWidget(pg, pc);
			if (p == w)
			{
				break;
			}
			w = p;
		}
		viewportScreenRect = new Rectangle(sx, sy, viewport.getWidth(), viewport.getHeight());
	}

	private Widget findParent(Widget[] group, Widget child)
	{
		int parentComp = child.getParentId() & 0xFFFF;
		if (parentComp > 0 && parentComp < group.length && group[parentComp] != null)
		{
			return group[parentComp];
		}
		return null;
	}

	private Widget findContentPane(Widget[] group)
	{
		Widget best     = null;
		int    bestDyn  = 0;
		Widget fallback = null;

		for (int i = 1; i < group.length; i++)
		{
			Widget w = group[i];
			if (w == null || w.isHidden() || w.getType() != WidgetType.LAYER)
			{
				continue;
			}
			if (w.getWidth() < 200 || w.getHeight() < 200)
			{
				continue;
			}
			if (fallback == null)
			{
				fallback = w;
			}
			Widget[] dc    = w.getDynamicChildren();
			int      count = dc != null ? dc.length : 0;
			if (count > bestDyn)
			{
				bestDyn = count;
				best    = w;
			}
		}
		return best != null ? best : fallback;
	}

	private int computeInsertY(Widget container)
	{
		// Only use dynamic children — Pete's rows are all dynamic, and
		// static children may have large template Y values that would push our rows off-screen.
		int max = maxBottom(container.getDynamicChildren());
		return max > 0 ? max + 5 : 10;
	}

	private int maxBottom(Widget[] widgets)
	{
		if (widgets == null)
		{
			return 0;
		}
		int max = 0;
		for (Widget w : widgets)
		{
			if (w == null)
			{
				continue;
			}
			int bottom = w.getOriginalY() + w.getOriginalHeight();
			if (bottom > max)
			{
				max = bottom;
			}
		}
		return max;
	}

	private Widget[] getPeteGroup()
	{
		Widget root = client.getWidget(PETE_INTERFACE_GROUP, 0);
		if (root == null)
		{
			return null;
		}
		List<Widget> list = new ArrayList<>();
		list.add(root);
		for (int i = 1; i < 512; i++)
		{
			Widget w = client.getWidget(PETE_INTERFACE_GROUP, i);
			if (w == null)
			{
				break;
			}
			list.add(w);
		}
		return list.toArray(new Widget[0]);
	}

	private void logFlatGroup(Widget[] group)
	{
		StringBuilder sb = new StringBuilder(
			"[PvpTutorials] Pete group 970 flat (" + group.length + " components):");
		for (int i = 0; i < group.length; i++)
		{
			Widget w = group[i];
			if (w == null)
			{
				sb.append(String.format("%n  [%d] null", i));
				continue;
			}
			Widget[] dc       = w.getDynamicChildren();
			int      dynCount = dc != null ? dc.length : 0;
			sb.append(String.format(
				"%n  [%d] type=%d parent=%d origX=%d origY=%d origW=%d origH=%d x=%d y=%d w=%d h=%d scrollH=%d dyn=%d hidden=%b text='%s'",
				i, w.getType(),
				w.getParentId() & 0xFFFF,
				w.getOriginalX(), w.getOriginalY(), w.getOriginalWidth(), w.getOriginalHeight(),
				w.getRelativeX(), w.getRelativeY(), w.getWidth(), w.getHeight(),
				w.getScrollHeight(), dynCount,
				w.isHidden(),
				w.getText() != null ? w.getText() : ""));
			if (dynCount > 0 && !w.isHidden())
			{
				for (int d = 0; d < dc.length; d++)
				{
					Widget dw = dc[d];
					if (dw == null)
					{
						continue;
					}
					sb.append(String.format(
						"%n    dyn[%d] type=%d origX=%d origY=%d origW=%d origH=%d hidden=%b text='%s'",
						d, dw.getType(),
						dw.getOriginalX(), dw.getOriginalY(), dw.getOriginalWidth(), dw.getOriginalHeight(),
						dw.isHidden(),
						dw.getText() != null ? dw.getText() : ""));
				}
			}
		}
		log.debug("{}", sb);
	}

	public boolean isInjected()
	{
		return !injectedRows.isEmpty();
	}
}
