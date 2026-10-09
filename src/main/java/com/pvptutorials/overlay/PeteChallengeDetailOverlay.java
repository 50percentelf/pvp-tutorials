package com.pvptutorials.overlay;

import com.pvptutorials.menu.PeteChallengeDefinition;
import com.pvptutorials.menu.PeteTutorialMenu;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class PeteChallengeDetailOverlay extends Overlay
{
	private static final Color BG_COLOR    = new Color(0x3E, 0x35, 0x29, 255);
	private static final Color EDGE_COLOR  = new Color(0x5A, 0x4D, 0x3E);
	private static final Color TITLE_COLOR = Color.WHITE;
	private static final Color HDR_COLOR   = new Color(0xCC, 0xCC, 0xCC);
	private static final Color BODY_COLOR  = new Color(0x9F, 0x9F, 0x9F);
	private static final Color BTN_BG      = new Color(0x57, 0x4B, 0x3C);
	private static final Color BTN_TEXT    = Color.WHITE;
	private static final Color STUB_BG     = new Color(0x48, 0x42, 0x3A);
	private static final Color STUB_TEXT   = new Color(0x70, 0x70, 0x70);

	private final PeteTutorialMenu peteTutorialMenu;

	@Inject
	public PeteChallengeDetailOverlay(PeteTutorialMenu peteTutorialMenu)
	{
		this.peteTutorialMenu = peteTutorialMenu;
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPosition(OverlayPosition.DYNAMIC);
		setMovable(false);
	}

	@Override
	public Dimension render(Graphics2D g)
	{
		PeteChallengeDefinition detail = peteTutorialMenu.getActiveDetail();
		if (detail == null)
		{
			return null;
		}
		Rectangle r = peteTutorialMenu.getViewportScreenRect();
		if (r == null)
		{
			return null;
		}

		int scrollY = peteTutorialMenu.getDetailScrollY();

		// Background fill + border
		g.setColor(BG_COLOR);
		g.fillRect(r.x, r.y, r.width, r.height);
		g.setColor(EDGE_COLOR);
		g.drawRect(r.x, r.y, r.width - 1, r.height - 1);

		// ── Back button (fixed — not scrolled) ───────────────────────────────
		Rectangle backBtn = peteTutorialMenu.getBackButtonRect();
		g.setColor(BTN_BG);
		g.fillRect(backBtn.x, backBtn.y, backBtn.width, backBtn.height);
		g.setColor(EDGE_COLOR);
		g.drawRect(backBtn.x, backBtn.y, backBtn.width - 1, backBtn.height - 1);
		g.setFont(g.getFont().deriveFont(Font.PLAIN, 12f));
		g.setColor(BTN_TEXT);
		g.drawString("< Back", backBtn.x + 8, backBtn.y + 16);

		// ── Start / Coming Soon button (fixed — not scrolled) ─────────────────
		Rectangle startBtn = peteTutorialMenu.getStartButtonRect();
		boolean   isStub   = detail.getId() == null;
		g.setColor(isStub ? STUB_BG : BTN_BG);
		g.fillRect(startBtn.x, startBtn.y, startBtn.width, startBtn.height);
		g.setColor(EDGE_COLOR);
		g.drawRect(startBtn.x, startBtn.y, startBtn.width - 1, startBtn.height - 1);
		g.setFont(g.getFont().deriveFont(Font.BOLD, 12f));
		g.setColor(isStub ? STUB_TEXT : BTN_TEXT);
		String  label = isStub ? "Coming Soon" : "Start Challenge";
		FontMetrics fmBtn = g.getFontMetrics();
		g.drawString(label,
			startBtn.x + (startBtn.width - fmBtn.stringWidth(label)) / 2,
			startBtn.y + startBtn.height - 8);

		// ── Clip to the panel so scrolled content doesn't overflow ────────────
		java.awt.Shape oldClip = g.getClip();
		int contentTop    = r.y + 36;
		int contentBottom = startBtn.y - 4;
		g.setClip(r.x, contentTop, r.width, contentBottom - contentTop);

		int pad = 10;
		int cx  = r.x + pad;
		int cw  = r.width - pad * 2;
		// Scrollable content starts at contentTop; offset by -scrollY
		int cy  = contentTop - scrollY;

		// ── Title (scrollable) ────────────────────────────────────────────────
		g.setFont(g.getFont().deriveFont(Font.BOLD, 15f));
		g.setColor(TITLE_COLOR);
		g.drawString(detail.getTitle(), cx, cy + 15);
		cy += 22;

		// thin divider under title
		g.setColor(EDGE_COLOR);
		g.drawLine(cx, cy, cx + cw, cy);
		cy += 8;

		// ── Overview (scrollable) ─────────────────────────────────────────────
		if (detail.getOverview() != null && !detail.getOverview().isEmpty())
		{
			g.setFont(g.getFont().deriveFont(Font.BOLD, 12f));
			g.setColor(HDR_COLOR);
			g.drawString("Overview", cx, cy + 12);
			cy += 16;
			g.setFont(g.getFont().deriveFont(Font.PLAIN, 12f));
			g.setColor(BODY_COLOR);
			cy = drawWrapped(g, detail.getOverview(), cx, cy, cw) + 8;
		}

		// ── Tips (scrollable) ─────────────────────────────────────────────────
		List<String> tips = detail.getTips();
		if (tips != null && !tips.isEmpty())
		{
			g.setFont(g.getFont().deriveFont(Font.BOLD, 12f));
			g.setColor(HDR_COLOR);
			g.drawString("Tips", cx, cy + 12);
			cy += 16;
			g.setFont(g.getFont().deriveFont(Font.PLAIN, 12f));
			g.setColor(BODY_COLOR);
			for (String tip : tips)
			{
				cy = drawWrapped(g, "• " + tip, cx + 4, cy, cw - 4) + 2;
			}
			cy += 4;
		}

		// ── Key Points (scrollable) ───────────────────────────────────────────
		List<String> keyPoints = detail.getKeyPoints();
		if (keyPoints != null && !keyPoints.isEmpty())
		{
			g.setFont(g.getFont().deriveFont(Font.BOLD, 12f));
			g.setColor(HDR_COLOR);
			g.drawString("Key Points", cx, cy + 12);
			cy += 16;
			g.setFont(g.getFont().deriveFont(Font.PLAIN, 12f));
			g.setColor(BODY_COLOR);
			for (String kp : keyPoints)
			{
				g.drawString("✓ " + kp, cx + 4, cy + 12);
				cy += 15;
			}
		}

		// Restore clip
		g.setClip(oldClip);

		return null; // DYNAMIC position — no dimension handoff needed
	}

	private int drawWrapped(Graphics2D g, String text, int x, int y, int maxW)
	{
		FontMetrics fm    = g.getFontMetrics();
		int         lineH = fm.getHeight();
		String[]    words = text.split(" ");
		StringBuilder line = new StringBuilder();

		for (String word : words)
		{
			String test = line.length() == 0 ? word : line + " " + word;
			if (fm.stringWidth(test) > maxW && line.length() > 0)
			{
				g.drawString(line.toString(), x, y + fm.getAscent());
				y += lineH;
				line = new StringBuilder(word);
			}
			else
			{
				line = new StringBuilder(test);
			}
		}
		if (line.length() > 0)
		{
			g.drawString(line.toString(), x, y + fm.getAscent());
			y += lineH;
		}
		return y;
	}
}
