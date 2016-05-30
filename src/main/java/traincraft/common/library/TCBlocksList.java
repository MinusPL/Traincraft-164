/*******************************************************************************
 * Copyright (c) 2012 Mrbrutal. All rights reserved.
 * 
 * @name TrainCraft
 * @author Mrbrutal
 ******************************************************************************/

package traincraft.common.library;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import traincraft.common.items.ItemBlockFluid;
import traincraft.common.items.ItemBlockGeneratorDiesel;
import traincraft.common.items.ItemBlockGeneratorWaterWheel;
import traincraft.common.items.ItemBlockGeneratorWindMill;
import traincraft.common.items.ItemBlockOreTC;

public enum TCBlocksList {

	//assemblyTableI(false, null),
	//assemblyTableII(false, null),
	//assemblyTableIII(false, null),

	//distilIdle(false, null),
	//distilActive(false, null),
	//signal(false, null),
	
	//book(true, ItemBlockBook.class),

	//trainWorkbench(false, null),

	//stopper(false, null),

	//openFurnaceIdle(false, null),
	//openFurnaceActive(false, null),
	oreTC(true, ItemBlockOreTC.class, "ore");
	//lantern(false, null),
	//waterWheel(true, ItemBlockGeneratorWaterWheel.class),
	//windMill(true, ItemBlockGeneratorWindMill.class),
	//generatorDiesel(true, ItemBlockGeneratorDiesel.class),
	
	//Liquids
	//diesel(false, ItemBlockFluid.class),
	//refinedFuel(false, ItemBlockFluid.class),
	
	//tcRailGag(false,null),
	//tcRail(false,null),
	//bridgePillar(false,null);

	public Block block;
	public boolean hasItemBlock;
	public Class itemBlockClass;
	public String block_name;

	TCBlocksList(boolean hasItemBlock, Class<? extends ItemBlock> itemBlockClass, String name) {
		this.hasItemBlock = hasItemBlock;
		this.itemBlockClass = itemBlockClass;
		this.block_name = name;
	}
}
