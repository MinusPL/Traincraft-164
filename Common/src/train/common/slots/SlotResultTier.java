package src.train.common.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import src.train.common.core.interfaces.ITier;
import src.train.common.core.managers.TierRecipeManager;
import cpw.mods.fml.common.registry.GameRegistry;

public class SlotResultTier extends Slot {

	private EntityPlayer thePlayer;
	private ITier tier2;
	/** The craft matrix inventory linked to this result slot. */
	private final IInventory craftMatrix;

	public SlotResultTier(EntityPlayer entityplayer, IInventory iinventory, int i, int j, int k) {
		super(iinventory, i, j, k);
		thePlayer = entityplayer;
		tier2 = (ITier) iinventory;
		craftMatrix = iinventory;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return false;
	}

	@Override
	public void onSlotChanged() {
		this.inventory.onInventoryChanged();
	}

	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack itemstack) {
		for (int i = 0; i < 10; i++) {
			if (inventory.getStackInSlot(i) != null) {
				inventory.decrStackSize(i, TierRecipeManager.getInstance().getTierRecipe(tier2.Tier(), itemstack).toDecrease(i));
			}
		}
		this.onCrafting(itemstack);
		super.onPickupFromSlot(player, itemstack);
		GameRegistry.onItemCrafted(player, itemstack, craftMatrix);
	}
}