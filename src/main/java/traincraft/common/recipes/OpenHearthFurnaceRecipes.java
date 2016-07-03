package traincraft.common.recipes;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import traincraft.common.library.ItemIDs;

public class OpenHearthFurnaceRecipes {

	private static final OpenHearthFurnaceRecipes smeltingBase = new OpenHearthFurnaceRecipes();
	private Map smeltingListResult1;
	private Map smeltingListResult2;
	private Map smeltingListCookTime;
	private Map experienceList = new HashMap();
	private Map plasticChanceList = new HashMap();
	private Map plasticList = new HashMap();

	public static final OpenHearthFurnaceRecipes smelting() {
		return smeltingBase;
	}

	private OpenHearthFurnaceRecipes() {
		smeltingListResult1 = new HashMap();
		smeltingListResult2 = new HashMap();
		smeltingListCookTime = new HashMap();

		addSmelting(new ItemStack(Items.iron_ingot), new ItemStack(ItemIDs.graphite.item), new ItemStack(ItemIDs.steel.item), 2F, 1000);
		//addSmelting(ItemIDs.graphite.itemID,Item.ingotIron.shiftedIndex, new ItemStack(ItemIDs.steel.item),2F);
	}

	public void addSmelting(ItemStack i, ItemStack j, ItemStack itemstack, float exp, int cookTime) {
		smeltingListResult1.put(i.getItem(), itemstack);
		smeltingListResult1.put(j.getItem(), itemstack);
		smeltingListResult2.put(i.getItem(), j.getItem());
		smeltingListResult2.put(j.getItem(), i.getItem());
		this.experienceList.put(i.getItem(), Float.valueOf(exp));
		this.experienceList.put(j.getItem(), Float.valueOf(exp));
		smeltingListCookTime.put(i.getItem(), cookTime);
		smeltingListCookTime.put(j.getItem(), cookTime);
	}

	public float getExperience(int i) {
		return this.experienceList.containsKey(Integer.valueOf(i)) ? ((Float) this.experienceList.get(Integer.valueOf(i))).floatValue() : 0.0F;
	}

	public ItemStack getSmeltingResultFromItem1(Item i) {
		return (ItemStack) smeltingListResult1.get(i);
	}

	public int getCookTime(ItemStack i, ItemStack j) {
		if (i != null && j != null) {
			int resultFrom1 = 1000;
			int resultFrom2 = 1000;
			if (smeltingListCookTime.containsKey(i.getItem()))
				resultFrom1 = (Integer) smeltingListCookTime.get(i.getItem());
			if (smeltingListCookTime.containsKey(j.getItem()))
				resultFrom2 = (Integer) smeltingListCookTime.get(j.getItem());
			return resultFrom1 != 0 ? resultFrom1 : resultFrom2;
		}
		return 600;
	}

	public boolean areItemPartOfRecipe(ItemStack i, ItemStack j) {
		ItemStack resultFrom1 = (ItemStack) smeltingListResult1.get(i.getItem());
		ItemStack resultFrom2 = (ItemStack) smeltingListResult1.get(j.getItem());
		if (resultFrom1 == null || resultFrom2 == null) {
			return false;
		}
		if (resultFrom1.areItemStacksEqual(resultFrom1, resultFrom2)) {
			return true;
		}
		return false;
	}

	public Map getSmeltingList() {
		return smeltingListResult1;
	}
	public Map getSmeltingList2() {
		return smeltingListResult2;
	}
}
