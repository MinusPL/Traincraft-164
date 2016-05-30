/*******************************************************************************
 * Copyright (c) 2012 Mrbrutal. All rights reserved.
 * 
 * @name TrainCraft
 * @author Mrbrutal
 ******************************************************************************/

package traincraft.client.core.handlers;

import java.util.EnumSet;

import net.minecraft.client.settings.KeyBinding;
import traincraft.client.core.helpers.KeyBindingHelper;
import traincraft.common.library.Info;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.TickType;

public class KeyBindingHandler extends KeyHandler {

	public KeyBindingHandler() {
		super(KeyBindingHelper.gatherKeyBindings(), KeyBindingHelper.gatherIsRepeating());
	}

	@Override
	public String getLabel() {
		return Info.modName + ": " + this.getClass().getSimpleName();
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	private static String getLocalizedKey(String key) {
		return /* LanguageRegistry.instance().getStringLocalization(key) */"test";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {}

}
