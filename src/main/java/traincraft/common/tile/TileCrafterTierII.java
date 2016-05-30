package traincraft.common.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import traincraft.common.core.handlers.PacketHandler;
import traincraft.common.core.interfaces.ITier;
import traincraft.common.core.managers.TierRecipe;
import traincraft.common.core.managers.TierRecipeManager;
import traincraft.common.library.Info;

public class TileCrafterTierII extends TileEntity implements IInventory, ITier {
	private Random rand;
	private ItemStack[] crafterInventory;

	private ForgeDirection facing;
	private final int Tier = 2;
	private List<ItemStack> resultList;
	private static List<ItemStack> knownRecipes = new ArrayList<ItemStack>();
	private static int[] slotSelected;

	public TileCrafterTierII() {
		crafterInventory = new ItemStack[26];
		this.rand = new Random();
		this.resultList = new ArrayList<ItemStack>();
		slotSelected = new int[8];
	}

	@Override
	public int getSizeInventory() {
		return crafterInventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return crafterInventory[i];
	}

	public List<ItemStack> getResultList() {
		return resultList;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (crafterInventory[i] != null) {
			if (crafterInventory[i].stackSize <= j) {
				ItemStack itemstack = crafterInventory[i];
				crafterInventory[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = crafterInventory[i].splitStack(j);
			if (crafterInventory[i].stackSize == 0) {
				crafterInventory[i] = null;
			}
			return itemstack1;
		}
		else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (crafterInventory[par1] != null) {
			ItemStack var2 = crafterInventory[par1];
			crafterInventory[par1] = null;
			return var2;
		}
		else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		crafterInventory[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName() {
		return "TierII";
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTag) {
		super.readFromNBT(nbtTag);
		facing = ForgeDirection.getOrientation(nbtTag.getByte("Orientation"));
		slotSelected = nbtTag.getIntArray("Selected");
		NBTTagList nbttaglist = nbtTag.getTagList("Items");
		this.crafterInventory = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < crafterInventory.length) {
				this.crafterInventory[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		NBTTagList nbttaglist2 = nbtTag.getTagList("Known");
		for (int i = 0; i < nbttaglist2.tagCount(); i++) {
			NBTTagCompound nbttagcompound2 = (NBTTagCompound) nbttaglist2.tagAt(i);
			byte byte1 = nbttagcompound2.getByte("Recipe");
			if (byte1 >= 0) {
				if (!listContains(knownRecipes, ItemStack.loadItemStackFromNBT(nbttagcompound2))) {
					knownRecipes.add(ItemStack.loadItemStackFromNBT(nbttagcompound2));
				}
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTag) {
		super.writeToNBT(nbtTag);
		if(facing!=null){
			nbtTag.setByte("Orientation", (byte) facing.ordinal());
		}else{
			nbtTag.setByte("Orientation", (byte) ForgeDirection.NORTH.ordinal());
		}
		nbtTag.setIntArray("Selected", slotSelected);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < this.crafterInventory.length; i++) {
			if (this.crafterInventory[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.crafterInventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbtTag.setTag("Items", nbttaglist);

		NBTTagList nbttaglist2 = new NBTTagList();
		if (knownRecipes != null) {
			for (int i = 0; i < knownRecipes.size(); i++) {
				NBTTagCompound nbttagcompound2 = new NBTTagCompound();
				nbttagcompound2.setByte("Recipe", (byte) i);
				knownRecipes.get(i).writeToNBT(nbttagcompound2);
				nbttaglist2.appendTag(nbttagcompound2);
			}
			nbtTag.setTag("Known", nbttaglist2);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void onInventoryChanged() {
		resultList.clear();
		for (int i = 10; i < crafterInventory.length - 8; i++) {
			crafterInventory[i] = null;
		}

		List<TierRecipe> recipes = TierRecipeManager.getInstance().getTierRecipeList(2);
		int count = 0;
		for (int j = 0; j < recipes.size(); j++) {
			ItemStack stack = recipes.get(j).hasComponents(crafterInventory[0], crafterInventory[1], crafterInventory[2], crafterInventory[3], crafterInventory[4], crafterInventory[5], crafterInventory[6], crafterInventory[7], crafterInventory[8], crafterInventory[9]);
			if (stack != null) {
				if((count+10)<crafterInventory.length) {
					resultList.add(stack);
					crafterInventory[count + 10] = new ItemStack(stack.itemID, 1, 0);
				}
				count++;
			}
		}

		for (int i = 0; i < resultList.size(); i++) {
			if (!listContains(knownRecipes, resultList.get(i))) {
				knownRecipes.add(resultList.get(i));
			}
		}
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj == null) {
			return true;
		}
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
			return false;
		}
		return entityplayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
	}

	public ForgeDirection getFacing() {
		if(facing!=null)return this.facing;
		return ForgeDirection.NORTH;
	}

	public void setFacing(ForgeDirection face) {
		this.facing = face;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public Packet getDescriptionPacket() {
		return PacketHandler.getTEPClient(this);
	}

	public void handlePacketDataFromServer(byte orientation) {
		facing = ForgeDirection.getOrientation(orientation);
	}

	private boolean listContains(List<ItemStack> list, ItemStack stack) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).itemID == stack.itemID) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int Tier() {
		return 2;
	}

	@Override
	public List<ItemStack> knownRecipes() {
		return knownRecipes;
	}

	@Override
	public int[] getSlotSelected() {
		return slotSelected;
	}

	@Override
	public void setSlotSelected(int[] selected) {
		this.slotSelected = selected;
	}

	@Override
	public String getGUIName() {
		return "Steel age";
	}

	@Override
	public String getGUITexture() {
		return Info.guiPrefix + "gui_tierII_steelAge.png";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
	}
}