package traincraft.api;

import mods.railcraft.api.fuel.FuelManager;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import traincraft.common.Traincraft;
import traincraft.common.blocks.BlockTraincraftFluid;
import traincraft.common.items.ItemBlockFluid;
import traincraft.common.library.TCBlocksList;
import traincraft.common.library.ItemIDs;
import buildcraft.api.fuels.IronEngineFuel;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LiquidManager {

	public static final int BUCKET_VOLUME = 1000;
	private static LiquidManager instance;
	public static FluidStack WATER_FILTER = new FluidStack(FluidRegistry.WATER, 1);
	public static FluidStack LAVA_FILTER = new FluidStack(FluidRegistry.LAVA, 1);
	public static Fluid oil;
	public static Fluid steam;
	public static Fluid fuel;
	public static Fluid creosoteOil;
	public static Fluid biomass;
	public static Fluid biofuel;
	public static Fluid seedoil;
	public static Fluid honey;
	public static Fluid juice;

	public static final Fluid DIESEL = new Fluid("Diesel").setUnlocalizedName("diesel.name").setDensity(860);
	public static final Fluid REFINED_FUEL = new Fluid("RefinedFuel").setDensity(820).setUnlocalizedName("refinedfuel.name");

	public static LiquidManager getInstance() {
		if (instance == null) {
			instance = new LiquidManager();
		}
		return instance;
	}

	public void registerLiquids() {
		FluidRegistry.registerFluid(DIESEL);
		FluidRegistry.registerFluid(REFINED_FUEL);
		BlockIDs.TCBlocksList.block = new BlockTraincraftFluid(BlockIDs.TCBlocksList.blockID, DIESEL, Material.water).setFlammable(true).setFlammability(5);
		DIESEL.setBlockID(BlockIDs.TCBlocksList.block);
		BlockIDs.TCBlocksList.block = new BlockTraincraftFluid(BlockIDs.TCBlocksList.blockID, REFINED_FUEL, Material.water).setFlammable(true).setFlammability(4);
		REFINED_FUEL.setBlockID(BlockIDs.TCBlocksList.block);
		FluidContainerRegistry.registerFluidContainer(DIESEL, new ItemStack(ItemIDs.diesel.item), new ItemStack(ItemIDs.emptyCanister.item));
		FluidContainerRegistry.registerFluidContainer(REFINED_FUEL, new ItemStack(ItemIDs.refinedFuel.item), new ItemStack(ItemIDs.emptyCanister.item));
		dieselFilter();
		FuelManager.addBoilerFuel(DIESEL, 60000);
		FuelManager.addBoilerFuel(REFINED_FUEL, 96000);
		IronEngineFuel.addFuel(DIESEL, 3, 200000);
		IronEngineFuel.addFuel(REFINED_FUEL, 6, 100000);
		MinecraftForge.EVENT_BUS.register(this);

		Traincraft.proxy.registerBlock(BlockIDs.TCBlocksList.block, ItemBlockFluid.class);
		Traincraft.proxy.registerBlock(BlockIDs.TCBlocksList.block, ItemBlockFluid.class);
	}

	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void textureHook(TextureStitchEvent.Post event) {
		if (event.map.textureType == 0) {
			DIESEL.setIcons(BlockIDs.TCBlocksList.block.getBlockTextureFromSide(1), BlockIDs.TCBlocksList.block.getBlockTextureFromSide(2));
			REFINED_FUEL.setIcons(BlockIDs.TCBlocksList.block.getBlockTextureFromSide(1), BlockIDs.TCBlocksList.block.getBlockTextureFromSide(2));
		}
	}

	public static void getLiquidsFromDictionnary() {
		oil = FluidRegistry.getFluid("oil");
		steam = FluidRegistry.getFluid("steam");
		fuel = FluidRegistry.getFluid("fuel");
		creosoteOil = FluidRegistry.getFluid("creosote oil");
		biomass = FluidRegistry.getFluid("biomass");
		biofuel = FluidRegistry.getFluid("bioethanol");
		seedoil = FluidRegistry.getFluid("seedoil");
		honey = FluidRegistry.getFluid("honey");
		juice = FluidRegistry.getFluid("juice");
	}

	public boolean isDieselLocoFuel(ItemStack stack) {
		FluidStack[] multiFilter;
		FluidStack bucketLiquid = getFluidInContainer(stack);
		multiFilter = LiquidManager.getInstance().dieselFilter();
		if (multiFilter != null) {
			for (int i = 0; i < multiFilter.length; i++) {
				if (multiFilter[i] != null && bucketLiquid != null && multiFilter[i].isFluidEqual(bucketLiquid))
					return true;
				if (isEmptyContainer(stack))
					return true;
			}
		}
		return false;
	}

	public static FluidStack[] dieselFilter() {
		FluidStack[] fuels = new FluidStack[4];
		if (DIESEL != null)
			fuels[0] = new FluidStack(DIESEL, 1);
		if (REFINED_FUEL != null)
			fuels[1] = new FluidStack(REFINED_FUEL, 1);
		if (biofuel != null)
			fuels[2] = new FluidStack(biofuel, 1);
		if (fuel != null)
			fuels[3] = new FluidStack(fuel, 1);
		return fuels;
	}

	public boolean isBucket(ItemStack stack) {
		return FluidContainerRegistry.isBucket(stack);
	}

	public boolean isContainer(ItemStack stack) {
		return FluidContainerRegistry.isContainer(stack);
	}

	public boolean isFilledContainer(ItemStack stack) {
		return FluidContainerRegistry.isFilledContainer(stack);
	}

	public boolean isEmptyContainer(ItemStack stack) {
		return FluidContainerRegistry.isEmptyContainer(stack);
	}

	public ItemStack fillFluidContainer(FluidStack liquid, ItemStack empty) {
		if ((liquid == null) || (empty == null))
			return null;
		return FluidContainerRegistry.fillFluidContainer(liquid, empty);
	}

	public FluidStack getFluidInContainer(ItemStack stack) {
		return FluidContainerRegistry.getFluidForFilledItem(stack);
	}

	public boolean containsFluid(ItemStack stack, FluidStack liquid) {
		return FluidContainerRegistry.containsFluid(stack, liquid);
	}

	public boolean isFluidEqual(FluidStack L1, FluidStack L2) {
		if ((L1 == null) || (L2 == null)) {
			return false;
		}
		return L1.isFluidEqual(L2);
	}

	public ItemStack processContainer(IInventory inventory, IFluidTank tank, ItemStack itemstack, int tankIndex) {
		FluidStack bucketLiquid = getFluidInContainer(itemstack);
		ItemStack emptyItem = itemstack.getItem().getContainerItemStack(itemstack);

		if ((bucketLiquid != null) && (emptyItem == null)) {
			int used = tank.fill(bucketLiquid, false);
			if (used >= bucketLiquid.amount) {
				tank.fill(bucketLiquid, true);
				inventory.decrStackSize(0, 1);
				return emptyItem;
			}
		}
		else if ((getInstance().isEmptyContainer(itemstack))) {
			ItemStack filled = getInstance().fillFluidContainer(tank.getFluid(), itemstack);
			if ((filled != null)) {
				FluidStack liquid = getFluidInContainer(filled);
				FluidStack drain = tank.drain(liquid.amount, false);
				if ((drain != null) && (drain.amount > 0)) {
					tank.drain(liquid.amount, true);
					inventory.decrStackSize(0, 1);
					return filled;
				}
			}
		}
		return null;
	}

	public ItemStack processContainer(IInventory inventory, int inventoryIndex, StandardTank tank, ItemStack itemstack, int tankIndex) {
		FluidStack bucketLiquid = getFluidInContainer(itemstack);
		ItemStack emptyItem = itemstack.getItem().getContainerItemStack(itemstack);
		if ((bucketLiquid != null)) {
			int used = tank.fill(bucketLiquid, false);
			if (used >= bucketLiquid.amount) {
				tank.fill(bucketLiquid, true);
				inventory.decrStackSize(inventoryIndex, 1);
				return emptyItem;
			}
		}
		else if ((getInstance().isEmptyContainer(itemstack))) {
			ItemStack filled = getInstance().fillFluidContainer(tank.getFluid(), itemstack);
			if ((filled != null)) {
				FluidStack liquid = getFluidInContainer(filled);
				FluidStack drain = tank.drain(liquid.amount, false);
				if ((drain != null) && (drain.amount > 0)) {
					tank.drain(liquid.amount, true);
					inventory.decrStackSize(inventoryIndex, 1);
					return filled;
				}
			}
		}
		return null;
	}

	public class StandardTank extends FluidTank {
		private int tankIndex;

		public StandardTank(int capacity) {
			super(capacity);
		}

		public void setTankIndex(int index) {
			this.tankIndex = index;
		}

		public int getTankIndex() {
			return this.tankIndex;
		}

		public boolean isEmpty() {
			return (getFluid() == null) || (getFluid().amount <= 0);
		}
	}

	public class FilteredTank extends StandardTank {
		private final FluidStack filter;
		private final FluidStack[] multiFilter;

		public FilteredTank(int capacity, FluidStack filter) {
			super(capacity);
			this.filter = filter;
			this.multiFilter = null;
		}

		public FilteredTank(int capacity, FluidStack filter, int pressure) {
			this(capacity, filter);
		}

		public FilteredTank(int capacity, FluidStack[] filter) {
			super(capacity);
			this.multiFilter = filter;
			this.filter = null;
		}

		public FilteredTank(int capacity, FluidStack[] filter, int pressure) {
			this(capacity, filter);
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			if (multiFilter != null) {
				for (int i = 0; i < multiFilter.length; i++) {
					if (multiFilter[i] != null && isFluidEqual(this.multiFilter[i], resource)) {
						return super.fill(resource, doFill);
					}
				}
			}
			else
			if (this.filter.isFluidEqual(resource)) {
				return super.fill(resource, doFill);
			}
			return 0;
		}

		public FluidStack getFilter() {
			return this.filter.copy();
		}

		public boolean liquidMatchesFilter(FluidStack resource) {
			if ((resource == null) || (this.filter == null)) {
				return false;
			}
			return this.filter.isFluidEqual(resource);
		}
	}

	public class ReverseFilteredTank extends StandardTank {
		private final FluidStack filter;
		private final FluidStack[] multiFilter;

		public ReverseFilteredTank(int capacity, FluidStack filter) {
			super(capacity);
			this.filter = filter;
			this.multiFilter = null;
		}

		public ReverseFilteredTank(int capacity, FluidStack filter, int pressure) {
			this(capacity, filter);
		}

		public ReverseFilteredTank(int capacity, FluidStack[] filter) {
			super(capacity);
			this.multiFilter = filter;
			this.filter = null;
		}

		public ReverseFilteredTank(int capacity, FluidStack[] filter, int pressure) {
			this(capacity, filter);
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			if (multiFilter != null) {
				for (int i = 0; i < multiFilter.length; i++) {
					if (multiFilter[i] != null && (resource.fluidID != multiFilter[i].fluidID)) {
						return super.fill(resource, doFill);
					}
				}
			}
			else if (filter.fluidID != resource.fluidID) {
				return super.fill(resource, doFill);
			}
			return 0;
		}

		public FluidStack getFilter() {
			return filter.copy();
		}

		public boolean liquidMatchesFilter(FluidStack resource) {
			if ((resource == null) || (filter == null)) {
				return false;
			}
			return filter.isFluidEqual(resource);
		}
	}
}
