package com.pvptutorials;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class PvpTutorialsTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(PvpTutorialsPlugin.class);
		RuneLite.main(args);
	}
}
