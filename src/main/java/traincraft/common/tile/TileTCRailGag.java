package traincraft.common.tile;

import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import traincraft.common.Traincraft;
import traincraft.common.core.handlers.PacketHandler;
import traincraft.common.core.handlers.packet.getTEPClient;

public class TileTCRailGag extends TileEntity {
	protected Random rand = new Random();
	protected Side side;
	public int originX;
	public int originY;
	public int originZ;
	public String type;
	public float bbHeight = 0.125f;

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		originX = nbt.getInteger("originX");
		originY = nbt.getInteger("originY");
		originZ = nbt.getInteger("originZ");
		bbHeight = nbt.getFloat("bbHeight");
		type = nbt.getString("type");
		super.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("originX", originX);
		nbt.setInteger("originY", originY);
		nbt.setInteger("originZ", originZ);
		nbt.setFloat("bbHeight", bbHeight);
		nbt.setString("type", type);
		super.writeToNBT(nbt);
	}

	@Override
	public Packet getDescriptionPacket() {
		return Traincraft.network.getPacketFrom(new getTEPClient(this));
	}

	public void handlePacketDataFromServer(String type, int bbHeight) {
		this.type = type;
		this.bbHeight = (((float) bbHeight) / 1000);
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
	}
}