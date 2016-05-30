package traincraft.common.tile;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import traincraft.common.core.handlers.PacketHandler;

public class TileBridgePillar extends TileEntity {

	@SideOnly(Side.CLIENT)
	@Override
	public double getMaxRenderDistanceSquared() {
		/*if(FMLClientHandler.instance()!=null && FMLClientHandler.instance().getClient()!=null && FMLClientHandler.instance().getClient().gameSettings!=null){
			if (FMLClientHandler.instance().getClient().gameSettings.renderDistance == 0) {
				return 30000.0D;
			}
			else if (FMLClientHandler.instance().getClient().gameSettings.renderDistance == 1) {
				return 15900.0D;
			}
			else if (FMLClientHandler.instance().getClient().gameSettings.renderDistance == 2) {
				return 4000.0D;
			} else return 4096.0;
		}else{
			return 4096.0;
		}*/
		return 4096.0D;
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
	}
}