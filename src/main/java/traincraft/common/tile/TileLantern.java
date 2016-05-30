package traincraft.common.tile;

import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import traincraft.common.Traincraft;
import traincraft.common.core.handlers.PacketHandler;
import traincraft.common.core.handlers.packet.getTEPClient;

public class TileLantern extends TileEntity {
	protected Random rand = new Random();
	public int randomColor = (rand.nextInt()*0xFFFFFF<<0);
	protected Side side;

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		randomColor = nbt.getInteger("randomColor");
		super.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("randomColor", randomColor);
		super.writeToNBT(nbt);
	}
	
	@Override
	public Packet getDescriptionPacket() {
		return Traincraft.network.getPacketFrom(new getTEPClient(this));
	}
	
	public void handlePacketDataFromServer(int color) {
		side = FMLCommonHandler.instance().getEffectiveSide();
		randomColor = color;
		/**
		 * if the color is received server side then send it to the client. This situation happens when the color is set in the GUI
		 * Color has to pass through the server to be registered
		 */
		if(side == Side.SERVER)
		{
			Traincraft.network.sendToAllAround(new getTEPClient(this), new TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 12D));
		}
	}
	
	public String getColor() {
		return String.format("#%06X", (0xFFFFFF & randomColor));
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
	}
}