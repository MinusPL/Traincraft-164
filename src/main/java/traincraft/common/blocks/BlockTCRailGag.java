package traincraft.common.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import traincraft.common.Traincraft;
import traincraft.common.items.ItemTCRail.TrackTypes;
import traincraft.common.library.TCBlocksList;
import traincraft.common.library.Info;
import traincraft.common.tile.TileTCRailGag;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTCRailGag extends Block {
	private Icon texture;
	float f = 0.125F;

	public BlockTCRailGag(int id) {
		super(id, Material.iron);
		setCreativeTab(Traincraft.tcTab);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		int l = par1World.getBlockId(par2, par3, par4);
		Block block = Block.blocksList[l];
		return false;
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int par5, int par6) {
		TileTCRailGag tileEntity = (TileTCRailGag) world.getBlockTileEntity(i, j, k);
		if (tileEntity != null) {
			world.destroyBlock(tileEntity.originX, tileEntity.originY, tileEntity.originZ, false);
			world.removeBlockTileEntity(tileEntity.originX, tileEntity.originY, tileEntity.originZ);
		}
		world.removeBlockTileEntity(i, j, k);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random par1Random) {
		return 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int idPicked(World world, int i, int j, int k) {
		return 0;
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int par5) {
		TileTCRailGag tileEntity = (TileTCRailGag) world.getBlockTileEntity(i, j, k);
		if (tileEntity != null) {
			if (world.getBlockId(tileEntity.originX, tileEntity.originY, tileEntity.originZ) < 1) {
				world.destroyBlock(i, j, k, false);
				world.removeBlockTileEntity(i, j, k);
			}
			if (!world.doesBlockHaveSolidTopSurface(i, j - 1, k) && world.getBlockId(i, j-1, k) != BlockIDs.TCBlocksList.blockID) {
				world.destroyBlock(i, j, k, false);
				world.removeBlockTileEntity(i, j, k);
			}
		}
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int i, int j, int k) {
		TileTCRailGag tileEntity = (TileTCRailGag) par1IBlockAccess.getBlockTileEntity(i, j, k);
		if (tileEntity != null) {
			//System.out.println(tileEntity.type+" "+tileEntity.bbHeight);
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, tileEntity.bbHeight, 1.0F);
		}
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileTCRailGag();
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		texture = iconRegister.registerIcon(Info.modID.toLowerCase() + ":tracks/rail_normal_turned");
	}

	@Override
	public Icon getIcon(int i, int j) {
		return texture;
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		TileTCRailGag tileEntity = (TileTCRailGag) world.getBlockTileEntity(i, j, k);
		if (tileEntity != null && tileEntity.type != null && (tileEntity.type.equals(TrackTypes.LARGE_SLOPE_WOOD.getLabel()) || tileEntity.type.equals(TrackTypes.LARGE_SLOPE_GRAVEL.getLabel()) || tileEntity.type.equals(TrackTypes.LARGE_SLOPE_BALLAST.getLabel()))) {
			List list = world.getEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getAABBPool().getAABB((double) ((float) i - 1.5), (double) j, (double) ((float) k - 1.5), (double) ((float) (i + 1.5)), (double) ((float) (j + 1.5)), (double) ((float) (k + 1.5))));
			if (!list.isEmpty()) {
				return null;
			}
			return AxisAlignedBB.getAABBPool().getAABB((double) i + this.minX, (double) j + this.minY, (double) k + this.minZ, (double) i + this.maxX, (double) j + tileEntity.bbHeight - 0.2, (double) k + this.maxZ);
		}
		return null;
	}
}