package com.pvptutorials.menu;

import com.pvptutorials.tutorial.TutorialManager;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.input.MouseAdapter;
import net.runelite.client.input.MouseManager;

@Slf4j
@Singleton
public class PeteTutorialMenu
{
	static final int PETE_INTERFACE_GROUP = 970;
	private static final int CONTENT_PANE_COMP  = 8;
	private static final int SCROLL_PANE_COMP   = 7;
	private static final int INTERACTIVE_COMP   = 9;

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
	private static final int COLOR_TITLE     = 0xFFFFFF;
	private static final int COLOR_DESC      = 0x7F7F7F;
	private static final int COLOR_HEADER    = 0x9F9F9F;
	private static final int COLOR_DIVIDER   = 0x5A4D3E;
	private static final int COLOR_ROW_BG    = 0x3E3529;
	private static final int COLOR_ROW_HOVER = 0x574B3C;

	// Pete's display names used in the detail view (for text override matching).
	// Keys are component 9 dyn indices (Pete's row order).
	private static final Map<Integer, String> PETE_ROW_TITLES = Map.of(
		0, "Prayer Protection",
		1, "Power of Freezes",
		2, "Special Attacks",
		3, "Gear Switching",
		4, "Combo Eating",
		5, "Pete's Penultimate Challenge",
		6, "Pete's Final Challenge"
	);

	@Inject private Client          client;
	@Inject private TutorialManager tutorialManager;
	@Inject private MouseManager    mouseManager;

	// ── state ─────────────────────────────────────────────────────────────────
	private final List<RowEntry> injectedRows       = new ArrayList<>();
	private int     dynCountBeforeInjection         = -1;
	private boolean peteOpen                        = false;
	private boolean treeLogged                      = false;

	private Rectangle               viewportScreenRect     = null;
	private PeteChallengeDefinition pendingDetailChallenge = null;

	// detail view override state
	private boolean detailViewActive      = false;
	private boolean detailOverrideApplied = false;

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
			if (!peteOpen || injectedRows.isEmpty() || viewportScreenRect == null) return event;

			int mx = event.getX(), my = event.getY();
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
					pendingDetailChallenge = row.def;
					detailOverrideApplied  = false;
					if (row.def.getId() != null)
					{
						tutorialManager.requestTutorial(row.def.getId());
					}
					// Propagate event so the duplicated comp 9 widget also fires.
					return event;
				}
			}
			return event;
		}
	};

	// ── public lifecycle ──────────────────────────────────────────────────────

	public void startListening()
	{
		mouseManager.registerMouseListener(mouseListener);
	}

	public void stopListening()
	{
		mouseManager.unregisterMouseListener(mouseListener);
	}

	// ── events ────────────────────────────────────────────────────────────────

	public void onWidgetLoaded(int groupId)
	{
		if (groupId == PETE_INTERFACE_GROUP)
		{
			peteOpen = true;
			clearInjectedState();
			treeLogged = false;
			return;
		}
		if (peteOpen && pendingDetailChallenge != null)
		{
			log.debug("[PvpTutorials] Widget loaded while Pete open: group={} challenge='{}'",
				groupId, pendingDetailChallenge.getTitle());
			// If Pete's detail view is a separate group, try to override its text.
			tryOverrideGroup(groupId);
		}
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

		// After injection check, try to override Pete's detail view if needed.
		if (pendingDetailChallenge != null)
		{
			tryOverrideDetailView();
		}

		if (!injectedRows.isEmpty()) return;
		if (group.length <= 1) return;

		if (!treeLogged)
		{
			logFlatGroup(group);
			treeLogged = true;
		}

		injectInto(group);
	}

	public void onMenuOpened(MenuOpened event)
	{
		// Pete's native comp 9 widgets provide right-click options — no custom entries needed.
	}

	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		// Handled via RUNELITE onClick lambdas where applicable.
	}

	// ── hover highlighting ────────────────────────────────────────────────────

	private void checkHover()
	{
		if (viewportScreenRect == null) return;

		Widget pane = client.getWidget(PETE_INTERFACE_GROUP, CONTENT_PANE_COMP);
		if (pane == null) return;

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

	// ── detail view override ──────────────────────────────────────────────────

	private void tryOverrideDetailView()
	{
		Widget listContent = client.getWidget(PETE_INTERFACE_GROUP, CONTENT_PANE_COMP);
		boolean nowDetailView = listContent == null || listContent.isHidden();

		if (nowDetailView != detailViewActive)
		{
			detailViewActive      = nowDetailView;
			detailOverrideApplied = false;
			if (nowDetailView)
			{
				log.debug("[PvpTutorials] Pete detail view opened (challenge='{}')",
					pendingDetailChallenge.getTitle());
				logDetailViewWidgets();
			}
		}

		if (!detailViewActive || detailOverrideApplied) return;

		if (overrideDetailViewText())
		{
			detailOverrideApplied = true;
		}
	}

	// Scan group 970 flat components for Pete's arena title and replace with custom text.
	private boolean overrideDetailViewText()
	{
		if (pendingDetailChallenge == null) return false;

		String peteTitle = PETE_ROW_TITLES.get(pendingDetailChallenge.getPeteRowIndex());
		if (peteTitle == null) return false;

		boolean found = false;
		for (int i = 0; i < 512; i++)
		{
			Widget w = client.getWidget(PETE_INTERFACE_GROUP, i);
			if (w == null) break;
			if (replaceText(w, peteTitle, pendingDetailChallenge)) found = true;

			Widget[] dc = w.getDynamicChildren();
			if (dc != null)
			{
				for (Widget dw : dc)
				{
					if (replaceText(dw, peteTitle, pendingDetailChallenge)) found = true;
				}
			}
			Widget[] sc = w.getStaticChildren();
			if (sc != null)
			{
				for (Widget sw : sc)
				{
					if (replaceText(sw, peteTitle, pendingDetailChallenge)) found = true;
				}
			}
		}
		return found;
	}

	private boolean replaceText(Widget w, String peteTitle, PeteChallengeDefinition def)
	{
		if (w == null || w.isHidden() || w.getType() != WidgetType.TEXT) return false;
		String text = w.getText();
		if (text == null || !text.contains(peteTitle)) return false;

		w.setText(def.getTitle());
		w.revalidate();
		log.debug("[PvpTutorials] Replaced '{}' → '{}' on widget id=0x{}", text, def.getTitle(),
			Integer.toHexString(w.getId()));
		return true;
	}

	// Attempt to override text in a group that just loaded (in case Pete's detail view is a separate group).
	private void tryOverrideGroup(int groupId)
	{
		if (pendingDetailChallenge == null) return;
		String peteTitle = PETE_ROW_TITLES.get(pendingDetailChallenge.getPeteRowIndex());
		if (peteTitle == null) return;

		boolean found = false;
		for (int i = 0; i < 512; i++)
		{
			Widget w = client.getWidget(groupId, i);
			if (w == null) break;
			if (replaceText(w, peteTitle, pendingDetailChallenge)) found = true;

			Widget[] dc = w.getDynamicChildren();
			if (dc != null) for (Widget dw : dc) if (replaceText(dw, peteTitle, pendingDetailChallenge)) found = true;
			Widget[] sc = w.getStaticChildren();
			if (sc != null) for (Widget sw : sc) if (replaceText(sw, peteTitle, pendingDetailChallenge)) found = true;
		}

		if (!found)
		{
			logGroupWidgets(groupId);
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

		// ── Visual rows into component 8 ──────────────────────────────────────
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

		// ── Interactive overlays into component 9 (duplicated Pete rows) ─────
		Widget interactiveLayer = client.getWidget(PETE_INTERFACE_GROUP, INTERACTIVE_COMP);
		if (interactiveLayer != null)
		{
			Widget[] interactiveDyn = interactiveLayer.getDynamicChildren();
			for (int i = 0; i < challenges.size(); i++)
			{
				PeteChallengeDefinition def = challenges.get(i);
				int peteRowIdx = def.getPeteRowIndex();
				if (interactiveDyn == null || peteRowIdx < 0 || peteRowIdx >= interactiveDyn.length)
				{
					log.warn("[PvpTutorials] Cannot duplicate comp9 dyn[{}] for '{}'",
						peteRowIdx, def.getTitle());
					continue;
				}
				Widget src = interactiveDyn[peteRowIdx];
				if (src == null) continue;

				int rowY = firstRowY + i * ROW_H;

				Widget overlay = interactiveLayer.createChild(-1, src.getType());
				overlay.setOriginalX(src.getOriginalX());
				overlay.setOriginalY(rowY);
				overlay.setOriginalWidth(src.getOriginalWidth() > 0 ? src.getOriginalWidth() : paneW);
				overlay.setOriginalHeight(ROW_H);
				overlay.setClickMask(src.getClickMask());

				String[] srcActions = src.getActions();
				if (srcActions != null)
				{
					for (int a = 0; a < srcActions.length; a++)
					{
						if (srcActions[a] != null) overlay.setAction(a, srcActions[a]);
					}
				}

				Object[] opListener = src.getOnOpListener();
				if (opListener != null) overlay.setOnOpListener(opListener);

				overlay.setHasListener(true);
				overlay.revalidate();

				log.debug("[PvpTutorials] Duplicated comp9 dyn[{}] (clickMask={} opListener={}) -> rowY={} for '{}'",
					peteRowIdx, src.getClickMask(), opListener != null ? opListener[0] : "none",
					rowY, def.getTitle());
			}
		}
		else
		{
			log.warn("[PvpTutorials] Component 9 not found — interactive overlays not injected");
		}

		// ── Activate scrollbar ────────────────────────────────────────────────
		// Set scrollHeight on the scroll viewport (comp 7) directly so the
		// scrollbar activates when content exceeds the 555px visible area.
		int totalNeeded = firstRowY + challenges.size() * ROW_H + 20;
		Widget scrollViewport = client.getWidget(PETE_INTERFACE_GROUP, SCROLL_PANE_COMP);
		if (scrollViewport != null)
		{
			scrollViewport.setScrollHeight(totalNeeded);
			scrollViewport.revalidateScroll();
		}
		contentPane.setScrollHeight(totalNeeded);
		contentPane.revalidateScroll();

		updateViewportScreenRect();

		log.debug("[PvpTutorials] Injected {} rows — dynBefore={} listBottom={} firstRowY={} totalNeeded={}",
			challenges.size(), dynCountBeforeInjection, listBottom, firstRowY, totalNeeded);
	}

	// ── helpers ───────────────────────────────────────────────────────────────

	private void clearInjectedState()
	{
		injectedRows.clear();
		dynCountBeforeInjection  = -1;
		viewportScreenRect       = null;
		pendingDetailChallenge   = null;
		detailViewActive         = false;
		detailOverrideApplied    = false;
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
			if (pid == -1) break;
			int pg = pid >>> 16;
			int pc = pid & 0xFFFF;
			if (pg != PETE_INTERFACE_GROUP) break;
			Widget p = client.getWidget(pg, pc);
			if (p == w) break;
			w = p;
		}
		viewportScreenRect = new Rectangle(sx, sy, viewport.getWidth(), viewport.getHeight());
	}

	private Widget findContentPane(Widget[] group)
	{
		Widget best     = null;
		int    bestDyn  = 0;
		Widget fallback = null;

		for (int i = 1; i < group.length; i++)
		{
			Widget w = group[i];
			if (w == null || w.isHidden() || w.getType() != WidgetType.LAYER) continue;
			if (w.getWidth() < 200 || w.getHeight() < 200) continue;
			if (fallback == null) fallback = w;
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
		if (widgets == null) return 0;
		int max = 0;
		for (Widget w : widgets)
		{
			if (w == null) continue;
			int bottom = w.getOriginalY() + w.getOriginalHeight();
			if (bottom > max) max = bottom;
		}
		return max;
	}

	private Widget[] getPeteGroup()
	{
		Widget root = client.getWidget(PETE_INTERFACE_GROUP, 0);
		if (root == null) return null;
		List<Widget> list = new ArrayList<>();
		list.add(root);
		for (int i = 1; i < 512; i++)
		{
			Widget w = client.getWidget(PETE_INTERFACE_GROUP, i);
			if (w == null) break;
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
					Widget   dw   = dc[d];
					if (dw == null) continue;
					String[] acts = dw.getActions();
					String   actStr = acts != null ? String.join("|", acts) : "";
					Object[] opl  = dw.getOnOpListener();
					sb.append(String.format(
						"%n    dyn[%d] type=%d origX=%d origY=%d origW=%d origH=%d clickMask=%d hasListener=%b actions='%s' opListener=%s hidden=%b text='%s'",
						d, dw.getType(),
						dw.getOriginalX(), dw.getOriginalY(), dw.getOriginalWidth(), dw.getOriginalHeight(),
						dw.getClickMask(), dw.hasListener(),
						actStr,
						opl != null ? String.valueOf(opl[0]) : "null",
						dw.isHidden(),
						dw.getText() != null ? dw.getText() : ""));
				}
			}
		}
		log.debug("{}", sb);
	}

	private void logDetailViewWidgets()
	{
		StringBuilder sb = new StringBuilder("[PvpTutorials] Detail view widget scan (group 970):");
		for (int i = 0; i < 512; i++)
		{
			Widget w = client.getWidget(PETE_INTERFACE_GROUP, i);
			if (w == null) break;
			appendTextWidgets(sb, w, i, -1);
			Widget[] dc = w.getDynamicChildren();
			if (dc != null) for (int d = 0; d < dc.length; d++) appendTextWidgets(sb, dc[d], i, d);
			Widget[] sc = w.getStaticChildren();
			if (sc != null) for (int d = 0; d < sc.length; d++) appendTextWidgets(sb, sc[d], i, -2 - d);
		}
		log.debug("{}", sb);
	}

	private void logGroupWidgets(int groupId)
	{
		StringBuilder sb = new StringBuilder(
			String.format("[PvpTutorials] Candidate detail group %d widget scan:", groupId));
		for (int i = 0; i < 512; i++)
		{
			Widget w = client.getWidget(groupId, i);
			if (w == null) break;
			appendTextWidgets(sb, w, i, -1);
			Widget[] dc = w.getDynamicChildren();
			if (dc != null) for (int d = 0; d < dc.length; d++) appendTextWidgets(sb, dc[d], i, d);
		}
		log.debug("{}", sb);
	}

	private void appendTextWidgets(StringBuilder sb, Widget w, int comp, int dyn)
	{
		if (w == null || w.getType() != WidgetType.TEXT) return;
		String text = w.getText();
		if (text == null || text.isEmpty()) return;
		String loc = dyn == -1 ? String.valueOf(comp) : (comp + ".dyn[" + dyn + "]");
		sb.append(String.format("%n  [%s] hidden=%b text='%s' origX=%d origY=%d w=%d h=%d",
			loc, w.isHidden(), text, w.getOriginalX(), w.getOriginalY(), w.getWidth(), w.getHeight()));
	}

	public boolean isInjected()
	{
		return !injectedRows.isEmpty();
	}
}
