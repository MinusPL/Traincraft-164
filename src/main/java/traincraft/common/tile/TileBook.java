/*******************************************************************************
 * Copyright (c) 2013 Mrbrutal. All rights reserved.
 * 
 * @name Traincraft
 * @author Mrbrutal
 ******************************************************************************/

package traincraft.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import traincraft.common.Traincraft;
import traincraft.common.core.handlers.PacketHandler;
import traincraft.common.core.handlers.packet.getTEPClient;

public class TileBook extends TileEntity {
	
	private int facingMeta;
	
	public TileBook() {
		facingMeta = this.blockMetadata;
	}
	
	public int getFacing() {
		return facingMeta;
	}

	public void setFacing(int facing) {
		this.facingMeta = facing;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTag) {
		super.readFromNBT(nbtTag);
		facingMeta = nbtTag.getByte("Orientation");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTag) {
		super.writeToNBT(nbtTag);
		nbtTag.setByte("Orientation", (byte) facingMeta);
	}

	@Override
	public Packet getDescriptionPacket() {
		return Traincraft.network.getPacketFrom(new getTEPClient(this));
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
	}

	public void handlePacketDataFromServer(int orientation) {
		facingMeta = orientation;
	}
}