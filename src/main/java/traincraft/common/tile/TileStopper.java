/*******************************************************************************
 * Copyright (c) 2012 Mrbrutal. All rights reserved.
 * 
 * @name TrainCraft
 * @author Mrbrutal
 ******************************************************************************/

package traincraft.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import traincraft.common.Traincraft;
import traincraft.common.core.handlers.PacketHandler;
import traincraft.common.core.handlers.packet.getTEPClient;

public class TileStopper extends TileEntity {

	private int facingMeta;

	public TileStopper() {
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

	public void handlePacketDataFromServer(int orientation) {
		facingMeta = orientation;
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
	}
}