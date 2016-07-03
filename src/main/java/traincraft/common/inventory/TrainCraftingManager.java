package traincraft.common.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import traincraft.common.core.interfaces.ITCRecipe;
import traincraft.common.recipes.ShapedTrainRecipes;
import traincraft.common.recipes.ShapelessTrainRecipe;

public class TrainCraftingManager {
	/** The static instance of this class */
	public static final TrainCraftingManager instance = new TrainCraftingManager();

	/** A list of all the recipes added */
	private List recipes = new ArrayList();
	
	private final ArrayList<ShapedTrainRecipes> shapedRecipes = new ArrayList<ShapedTrainRecipes>();

	public static final TrainCraftingManager getInstance() {
		return instance;
	}

	private TrainCraftingManager() {}

	public void addRecipe(ItemStack par1ItemStack, Object... obj) {
		String var3 = "";
		int var4 = 0;
		int var5 = 0;
		int var6 = 0;

		if (obj[var4] instanceof String[]) {
			String[] var7 = (String[]) ((String[]) obj[var4++]);

			for (int var8 = 0; var8 < var7.length; ++var8) {
				String var9 = var7[var8];
				++var6;
				var5 = var9.length();
				var3 = var3 + var9;
			}
		}
		else {
			while (obj[var4] instanceof String) {
				String var11 = (String) obj[var4++];
				++var6;
				var5 = var11.length();
				var3 = var3 + var11;
			}
		}
		HashMap var12;

		for (var12 = new HashMap(); var4 < obj.length; var4 += 2) {
			Character var13 = (Character) obj[var4];
			ItemStack var14 = null;

			if (obj[var4 + 1] instanceof Item) {
				var14 = new ItemStack((Item) obj[var4 + 1]);
			}
			else if (obj[var4 + 1] instanceof Block) {
				var14 = new ItemStack((Block) obj[var4 + 1], 1, -1);
			}
			else if (obj[var4 + 1] instanceof ItemStack) {
				var14 = (ItemStack) obj[var4 + 1];
			}

			var12.put(var13, var14);
		}

		ItemStack[] var15 = new ItemStack[/* var5 * var6 */9];

		for (int var16 = 0; var16 < var5 * var6; ++var16) {
			char var10 = var3.charAt(var16);

			if (var12.containsKey(Character.valueOf(var10))) {
				var15[var16] = ((ItemStack) var12.get(Character.valueOf(var10))).copy();
			}
			else {
				var15[var16] = null;
			}
		}

		this.recipes.add(new ShapedTrainRecipes(var5, var6, var15, par1ItemStack));
		this.shapedRecipes.add(new ShapedTrainRecipes(var5, var6, var15, par1ItemStack));
	}

	public void addShapelessRecipe(ItemStack par1ItemStack, Object... obj) {
		ArrayList var3 = new ArrayList();
		Object[] var4 = obj;
		int var5 = obj.length;

		for (int var6 = 0; var6 < var5; ++var6) {
			Object var7 = var4[var6];

			if (var7 instanceof ItemStack) {
				var3.add(((ItemStack) var7).copy());
			}
			else if (var7 instanceof Item) {
				var3.add(new ItemStack((Item) var7));
			}
			else {
				if (!(var7 instanceof Block)) {
					throw new RuntimeException("Invalid shapeless recipy!");
				}
				var3.add(new ItemStack((Block) var7));
			}
		}
		this.recipes.add(new ShapelessTrainRecipe(par1ItemStack, var3));
	}

	public ItemStack func_82787_a(IInventory inv, World world) {
		int var2 = 0;
		ItemStack var3 = null;
		ItemStack var4 = null;
		int var5;

		for (var5 = 0; var5 < inv.getSizeInventory(); ++var5) {
			ItemStack var6 = inv.getStackInSlot(var5);

			if (var6 != null) {
				if (var2 == 0) {
					var3 = var6;
				}

				if (var2 == 1) {
					var4 = var6;
				}
				++var2;
			}
		}

		if (var2 == 2 && var3.getItem() == var4.getItem() && var3.stackSize == 1 && var4.stackSize == 1 && var3.getItem().isRepairable() ) {
			Item var11 = var3.getItem();
			int var10 = var11.getMaxDamage() - var3.getItemDamageForDisplay();
			int var7 = var11.getMaxDamage() - var4.getItemDamageForDisplay();
			int var8 = var10 + var7 + var11.getMaxDamage() * 10 / 100;
			int var9 = var11.getMaxDamage() - var8;

			if (var9 < 0) {
				var9 = 0;
			}
			return new ItemStack(var3.getItem(), 1, var9);
		}
		else {
			for (var5 = 0; var5 < this.recipes.size(); ++var5) {
				ITCRecipe var12 = (ITCRecipe) this.recipes.get(var5);
				if (var12.matches(inv, world)) {
					return var12.getCraftingResult(inv);
				}
			}
			return null;
		}
	}

	/**
	 * returns the List<> of all recipes
	 */
	public List getRecipeList() {
		return this.recipes;
	}
	
	public List<ShapedTrainRecipes> getShapedRecipes() {
        return Collections.unmodifiableList(shapedRecipes);
	}
}
