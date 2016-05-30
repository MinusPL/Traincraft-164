/*******************************************************************************
 * Copyright (c) 2012 Mrbrutal. All rights reserved.
 * 
 * @name TrainCraft
 * @author Mrbrutal
 ******************************************************************************/

package traincraft.common.blocks;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import traincraft.common.Traincraft;
import traincraft.common.library.BlockIDs;
import traincraft.common.library.GuiIDs;
import traincraft.common.library.Info;
import traincraft.common.tile.TileEntityOpenHearthFurnace;
import traincraft.common.tile.TileHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockOpenHearthFurnace extends BlockContainer {

	private final boolean isActive;
	private static boolean keepFurnaceInventory = false;
	private Random furnaceRand;

	private Icon textureTop_off;
	private Icon textureTop_on;
	private Icon textureBottom;
	private Icon textureFront_off;
	private Icon textureFront_on;
	private Icon textureSide;

	protected BlockOpenHearthFurnace(int par1, int par2, boolean active) {
		super(par1, Material.rock);
		furnaceRand = new Random();
		setCreativeTab(Traincraft.tcTab);
		//setRequiresSelfNotify();
		isActive = active;
		if (isActive) {
			setLightValue(0.8F);
		}
	}

	@Override
	public int idDropped(int i, Random random, int j) {
		return BlockIDs.openFurnaceIdle.blockID;
	}

	@Override
	public Icon getIcon(int i, int j) {
		if (!this.isActive) {
			if (i == 1) {
				return textureTop_off;
			}
			if (i == 0) {
				return textureBottom;
			}
			if (i == 3) {
				return textureFront_off;
			}
			else {
				return textureSide;
			}
		}
		else {
			if (i == 1) {
				return textureTop_on;
			}
			if (i == 0) {
				return textureBottom;
			}
			if (i == 3) {
				return textureFront_on;
			}
			else {
				return textureSide;
			}
		}
	}

	@Override
	public Icon getBlockTexture(IBlockAccess worldAccess, int i, int j, int k, int side) {
		if (((TileEntityOpenHearthFurnace) worldAccess.getBlockTileEntity(i, j, k)).getFacing() != null) {
			side = TileHelper.getOrientationFromSide(((TileEntityOpenHearthFurnace) worldAccess.getBlockTileEntity(i, j, k)).getFacing(), ForgeDirection.getOrientation(side)).ordinal();
		}
		if (!this.isActive) {
			return side == 1 ? textureTop_off : side == 0 ? textureBottom : side == 3 ? textureFront_off : textureSide;
		}
		else {
			return side == 1 ? textureTop_on : side == 0 ? textureBottom : side == 3 ? textureFront_on : textureSide;
		}
	}

	public static void updateHearthFurnaceBlockState(boolean flag, World world, int i, int j, int k, Random random) {
		int l = world.getBlockMetadata(i, j, k);
		TileEntity tileentity = world.getBlockTileEntity(i, j, k);

		keepFurnaceInventory = true;

		if (flag) {
			world.setBlockMetadataWithNotify(i, j, k, BlockIDs.openFurnaceActive.blockID, 0);
		}
		else {
			world.setBlockMetadataWithNotify(i, j, k, BlockIDs.openFurnaceIdle.blockID, 0);
		}
		keepFurnaceInventory = false;
		world.setBlockMetadataWithNotify(i, j, k, l, 0);
		if (tileentity != null) {
			tileentity.validate();
			world.setBlockTileEntity(i, j, k, tileentity);
		}
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9) {
		TileEntity te = world.getBlockTileEntity(i, j, k);
		if (player.isSneaking()) {
			return false;
		}
		if (!world.isRemote) {
			if (te != null && te instanceof TileEntityOpenHearthFurnace) {
				player.openGui(Traincraft.instance, GuiIDs.OPEN_HEARTH_FURNACE, world, i, j, k);
			}
		}
		return true;
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
		world.markBlockForUpdate(i, j, k);
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {
		if (!keepFurnaceInventory) {
			TileEntityOpenHearthFurnace tileentityfurnace = (TileEntityOpenHearthFurnace) world.getBlockTileEntity(i, j, k);
			if (tileentityfurnace != null) {
				label0: for (int l = 0; l < tileentityfurnace.getSizeInventory(); l++) {
					ItemStack itemstack = tileentityfurnace.getStackInSlot(l);
					if (itemstack == null) {
						continue;
					}
					float f = furnaceRand.nextFloat() * 0.8F + 0.1F;
					float f1 = furnaceRand.nextFloat() * 0.8F + 0.1F;
					float f2 = furnaceRand.nextFloat() * 0.8F + 0.1F;
					do {
						if (itemstack.stackSize <= 0) {
							continue label0;
						}
						int i1 = furnaceRand.nextInt(21) + 10;
						if (i1 > itemstack.stackSize) {
							i1 = itemstack.stackSize;
						}
						itemstack.stackSize -= i1;
						EntityItem entityitem = new EntityItem(world, (float) i + f, (float) j + f1, (float) k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = (float) furnaceRand.nextGaussian() * f3;
						entityitem.motionY = (float) furnaceRand.nextGaussian() * f3 + 0.2F;
						entityitem.motionZ = (float) furnaceRand.nextGaussian() * f3;
						world.spawnEntityInWorld(entityitem);
					} while (true);
				}
			}
		}
		super.breakBlock(world, i, j, k, par5, par6);
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack stack) {
		TileEntityOpenHearthFurnace te = (TileEntityOpenHearthFurnace) world.getBlockTileEntity(i, j, k);
		if (te != null) {
			int dir = MathHelper.floor_double((double) ((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
			te.setFacing(ForgeDirection.getOrientation(dir == 0 ? 2 : dir == 1 ? 5 : dir == 2 ? 3 : 4));
			world.markBlockForUpdate(i, j, k);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileEntityOpenHearthFurnace();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		textureTop_off = iconRegister.registerIcon(Info.modID.toLowerCase() + ":furnace_off_top");
		textureTop_on = iconRegister.registerIcon(Info.modID.toLowerCase() + ":furnace_on_top");
		textureBottom = iconRegister.registerIcon(Info.modID.toLowerCase() + ":furnace_bottom");
		textureFront_off = iconRegister.registerIcon(Info.modID.toLowerCase() + ":furnace_off_front");
		textureFront_on = iconRegister.registerIcon(Info.modID.toLowerCase() + ":furnace_on_front");
		textureSide = iconRegister.registerIcon(Info.modID.toLowerCase() + ":furnace_side");
	}
}
