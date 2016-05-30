package traincraft.common.recipes;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import traincraft.common.core.handlers.ConfigHandler;
import traincraft.common.library.TCBlocksList;
import traincraft.common.library.ItemIDs;

public class DistilRecipes {

	private static final DistilRecipes smeltingBase = new DistilRecipes();
	private Map smeltingList;
	private Map experienceList = new HashMap();
	private Map plasticChanceList = new HashMap();
	private Map plasticList = new HashMap();

	public static final DistilRecipes smelting() {
		return smeltingBase;
	}

	private DistilRecipes() {
		smeltingList = new HashMap();

		//TODO test if copper works also
		if (ConfigHandler.ORE_GEN) {
			addSmelting(/* new ItemStack(BlockIDs.OreTC.blockID, 1, 1) */new ItemStack(Item.getItemFromBlock(Blocks.oreTC.block)), new ItemStack(ItemIDs.diesel.item, 2), 0.5F, 1, new ItemStack(ItemIDs.rawPlastic.item, 1));
			//addSmelting(/*new ItemStack(BlockIDs.OreTC.blockID, 1, 2)*/BlockIDs.OreTC.blockID, new ItemStack(ItemIDs.diesel.item, 2), 0.5F, 1, new ItemStack(ItemIDs.rawPlastic.item, 1));
		}
		addSmelting(new ItemStack(Items.reeds), new ItemStack(ItemIDs.diesel.item), 0.2F, 4, new ItemStack(ItemIDs.rawPlastic.item, 1));
		addSmelting(new ItemStack(Item.getItemFromBlock(Blocks.leaves)), new ItemStack(ItemIDs.diesel.item), 0.2F, 6, new ItemStack(ItemIDs.rawPlastic.item, 2));
		addSmelting(new ItemStack(ItemIDs.diesel.item), new ItemStack(ItemIDs.refinedFuel.item), 1F, 2, new ItemStack(ItemIDs.rawPlastic.item, 1));
		addSmelting(new ItemStack(Items.wheat), new ItemStack(ItemIDs.diesel.item), 0.2F, 4, new ItemStack(ItemIDs.rawPlastic.item, 1));
	}

	/*
	 * public void addSmelting(ItemStack i, ItemStack itemstack, float exp, int plasticChance, ItemStack plasticStack) { smeltingList.put(i, itemstack); plasticList.put(i, plasticStack); this.experienceList.put(Integer.valueOf(plasticStack.getItem().shiftedIndex), Float.valueOf(exp)); this.plasticChanceList.put(i, Float.valueOf(plasticChance)); } */

	/**
	 * 
	 * @param i: Input block id
	 * @param itemstack: Output
	 * @param exp: Experience
	 * @param plasticChance used as follow: Math.random(plasticChance)==0
	 * @param plasticSktack: the plastic output and output size
	 */
	public void addSmelting(ItemStack i, ItemStack itemstack, float exp, int plasticChance, ItemStack plasticStack) {
		smeltingList.put(i.getItem(), itemstack);
		plasticList.put(i.getItem(), plasticStack);
		this.experienceList.put(plasticStack.getItem(), Float.valueOf(exp));
		this.plasticChanceList.put(i.getItem(), Float.valueOf(plasticChance));
	}

	public float getExperience(Item i) {
		return this.experienceList.containsKey(i) ? ((Float) this.experienceList.get(i)).floatValue() : 0.0F;
	}

	public int getPlasticChance(Item i) {
		if (this.plasticChanceList.containsKey(i)) {
			return (int) ((Float) this.plasticChanceList.get(i)).floatValue();
		}
		return 0;
	}

	public ItemStack getSmeltingResult(Item i) {
		return (ItemStack) smeltingList.get(i);
	}

	public ItemStack getPlasticResult(Item i) {
		return (ItemStack) plasticList.get(i);
	}

	public Map getSmeltingList() {
		return smeltingList;
	}
}
