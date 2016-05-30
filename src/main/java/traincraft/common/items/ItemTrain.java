package traincraft.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import traincraft.common.Traincraft;
import traincraft.common.library.Info;
import traincraft.common.library.ItemIDs;

public class ItemTrain extends Item {
	
	public String texture_string;
	public ItemTrain(String iName)
	{
		super();
		maxStackSize = 64;
		texture_string = iName;
		setCreativeTab(Traincraft.tcTab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(Info.modID.toLowerCase() + ":parts/" + texture_string);
	}
}