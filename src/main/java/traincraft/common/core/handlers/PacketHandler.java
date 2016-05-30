/*******************************************************************************
 * Copyright (c) 2012 Mrbrutal. All rights reserved.
 * 
 * @name TrainCraft
 * @author Mrbrutal
 ******************************************************************************/

package traincraft.common.core.handlers;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import traincraft.api.AbstractTrains;
import traincraft.api.EntityRollingStock;
import traincraft.api.Locomotive;
import traincraft.common.entity.rollingStock.EntityJukeBoxCart;
import traincraft.common.entity.rollingStock.EntityTracksBuilder;
import traincraft.common.entity.zeppelin.AbstractZeppelin;
import traincraft.common.items.ItemRecipeBook;
import traincraft.common.library.Info;
import traincraft.common.tile.TileBook;
import traincraft.common.tile.TileCrafterTierI;
import traincraft.common.tile.TileCrafterTierII;
import traincraft.common.tile.TileCrafterTierIII;
import traincraft.common.tile.TileEntityDistil;
import traincraft.common.tile.TileEntityOpenHearthFurnace;
import traincraft.common.tile.TileGeneratorDiesel;
import traincraft.common.tile.TileLantern;
import traincraft.common.tile.TileSignal;
import traincraft.common.tile.TileStopper;
import traincraft.common.tile.TileTCRail;
import traincraft.common.tile.TileTCRailGag;
import traincraft.common.tile.TileTrainWbench;
import traincraft.common.tile.TileWaterWheel;
import traincraft.common.tile.TileWindMill;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	protected RollingStockStatsEventHandler statsEventHandler = new RollingStockStatsEventHandler();

	@Override

		if (packetIndex == 1) {
			int x = data.readInt();
			int y = data.readInt();
			int z = data.readInt();
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te instanceof TileEntityDistil) {
				byte orientation = data.readByte();
				short cookTime = data.readShort();
				short burnTime = data.readShort();
				short amount = data.readShort();
				short liquidID = data.readShort();
				((TileEntityDistil) te).handlePacketDataFromServer(orientation, cookTime, burnTime, amount, liquidID);
			}
			if (te instanceof TileEntityOpenHearthFurnace) {
				byte orientation = data.readByte();
				short cookTime = data.readShort();
				short burnTime = data.readShort();
				((TileEntityOpenHearthFurnace) te).handlePacketDataFromServer(orientation, cookTime, burnTime);
			}

		}
		else if (packetIndex == 2) {
			int ID = data.readInt();
			boolean brake = data.readBoolean();
			Entity entity = this.getEntityByID(ID, player);
			if (entity instanceof Locomotive) {
				((Locomotive) entity).setParkingBrakeFromPacket(brake);
				//System.out.println(brake);
			}
			if (player != null && player instanceof EntityPlayer && ((EntityPlayer) player).ridingEntity != null && ((EntityPlayer) player).ridingEntity instanceof Locomotive) {
				Locomotive lo = (Locomotive) ((EntityPlayer) player).ridingEntity;
				if (lo.entityId == ID) {
					lo.setParkingBrakeFromPacket(brake);//parkingBrake=brake;//setParkingBrake(brake);
				}
			}
		}
		else if (packetIndex == 3 || packetIndex == 4) {
			int ID = data.readInt();
			int set = data.readInt();
			AxisAlignedBB box = ((EntityPlayer) player).boundingBox.expand(5, 5, 5);
			List lis3 = ((EntityPlayer) player).worldObj.getEntitiesWithinAABBExcludingEntity((Entity) player, box);
			if (lis3 != null && lis3.size() > 0) {
				for (int j1 = 0; j1 < lis3.size(); j1++) {
					Entity entity = (Entity) lis3.get(j1);
					if (entity instanceof EntityTracksBuilder && ((EntityTracksBuilder) entity).entityId == ID) {
						if (packetIndex == 3) {
							((EntityTracksBuilder) entity).setPlannedHeightFromPacket(set);
						}
						if (packetIndex == 4) {
							((EntityTracksBuilder) entity).setFollowTracksFromPacket(set);
						}
					}
				}
			}
		}
		else if (packetIndex == 5) {
			int x = data.readInt();
			int y = data.readInt();
			int z = data.readInt();
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te instanceof TileEntityDistil) {
				short amount = data.readShort();
				short liquidID = data.readShort();
				((TileEntityDistil) te).handlePacketDataFromServer(amount, liquidID);
			}
		}
		else if (packetIndex == 6) {
			int entityID = data.readInt();
			int page = data.readInt();
			int recipe = data.readInt();

			if (((EntityPlayer) player).entityId == entityID) {
				if (((EntityPlayer) player).getCurrentEquippedItem() != null && (((EntityPlayer) player).getCurrentEquippedItem().getItem() instanceof ItemRecipeBook)) {
					ItemStack stack = ((EntityPlayer) player).getCurrentEquippedItem();
					NBTTagCompound var3 = stack.getTagCompound();
					if (var3 == null) {
						var3 = new NBTTagCompound();
						stack.setTagCompound(var3);
					}
					stack.getTagCompound().setInteger("currPage", page);
					stack.getTagCompound().setInteger("currRecipe", recipe);
				}
			}
		}
		else if (packetIndex == 7) {
			int x = data.readInt();
			int y = data.readInt();
			int z = data.readInt();
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te instanceof TileGeneratorDiesel) {
				boolean producing = data.readBoolean();
				short amount = data.readShort();
				short liquidID = data.readShort();
				((TileGeneratorDiesel) te).handlePacketDataFromServer(producing, amount, liquidID);
			}
		}
		else if (packetIndex == 11) {
			int entityID = data.readInt();
			int rotationServer = data.readInt();
			int realRotation = data.readInt();
			boolean isInReverse = data.readBoolean();
			int anglePitch = data.readInt();
			int posY = data.readInt();
			double posYD = ((double) posY / 1000000);
			boolean shouldSetPosY = data.readBoolean();
			Entity entity = this.getEntityByID(entityID, player);
			if (entity instanceof EntityRollingStock) {
				((EntityRollingStock) entity).rotationYawClient = rotationServer;
				((EntityRollingStock) entity).rotationYawClientReal = realRotation;
				((EntityRollingStock) entity).isClientInReverse = isInReverse;
				((EntityRollingStock) entity).anglePitchClient = anglePitch;
				((EntityRollingStock) entity).setYFromServer(posYD, shouldSetPosY);
			}
		}
		else if (packetIndex == 12) {
			int ID = data.readInt();
			boolean locked = data.readBoolean();
			AxisAlignedBB box = ((EntityPlayer) player).boundingBox.expand(5, 5, 5);
			List lis3 = ((EntityPlayer) player).worldObj.getEntitiesWithinAABBExcludingEntity((Entity) player, box);
			if (lis3 != null && lis3.size() > 0) {
				for (int j1 = 0; j1 < lis3.size(); j1++) {
					Entity entity = (Entity) lis3.get(j1);
					if (entity instanceof AbstractTrains && ((AbstractTrains) entity).entityId == ID) {
						((AbstractTrains) entity).setTrainLockedFromPacket(locked);
					}
				}
			}

		}
		else if (packetIndex == 13) {
			int ID = data.readInt();
			boolean locked = data.readBoolean();
			String theOwner = data.readLine();
			AxisAlignedBB box = ((EntityPlayer) player).boundingBox.expand(5, 5, 5);
			List lis3 = ((EntityPlayer) player).worldObj.getEntitiesWithinAABBExcludingEntity((Entity) player, box);
			if (lis3 != null && lis3.size() > 0) {
				for (int j1 = 0; j1 < lis3.size(); j1++) {
					Entity entity = (Entity) lis3.get(j1);
					if (entity instanceof AbstractTrains && ((AbstractTrains) entity).entityId == ID) {
						((AbstractTrains) entity).setTrainLockedFromPacket(locked, theOwner);
					}
				}
			}
		}
		else if (packetIndex == 14) {
			int entityID = data.readInt();
			int rotationServer = data.readInt();
			int roll = data.readInt();
			Entity entity = this.getEntityByID(entityID, player);
			if (entity instanceof AbstractZeppelin) {
				((AbstractZeppelin) entity).rotationYawClient = rotationServer;
				((AbstractZeppelin) entity).roll = roll;
			}
		}
		else if (packetIndex == 15) {
			int entityID = data.readInt();
			String url = data.readUTF();
			boolean playing = data.readBoolean();
			Entity entity = this.getEntityByID(entityID, player);
			if (entity instanceof EntityJukeBoxCart) {
				((EntityJukeBoxCart) entity).recievePacket(url, playing);
			}
		}
		else if (packetIndex == 16) {
			int entityID = data.readInt();
			int slotsFilled = data.readInt();
			Entity entity = this.getEntityByID(entityID, player);
			if (entity instanceof Locomotive) {
				((Locomotive) entity).recieveSlotsFilled(slotsFilled);
			}
		}
		else if (packetIndex == 17) {
			int entityID = data.readInt();
			boolean isOn = data.readBoolean();
			Entity entity = this.getEntityByID(entityID, player);
			if (entity instanceof Locomotive) {
				((Locomotive) entity).setLocoTurnedOn(isOn, false, false,0);
			}
		}
	}

	private Entity getEntityByID(int par1, Player player) {
		return (Entity) (par1 == ((EntityPlayer) player).entityId ? player : ((EntityPlayer) player).worldObj.getEntityByID(par1));
	}

	public static Packet setDistilLiquid(TileEntity te) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			if (te != null && te instanceof TileEntityDistil) {
				TileEntityDistil tem = (TileEntityDistil) te;
				dos.writeInt(5);
				dos.writeInt(tem.xCoord);
				dos.writeInt(tem.yCoord);
				dos.writeInt(tem.zCoord);
				dos.writeShort(tem.amount);
				dos.writeShort(tem.liquidItemID);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload(Info.channel, bos.toByteArray());
		packet.length = bos.size();
		return packet;
	}

	/**
	 * RollingStock rotation packet sent to client
	 * 
	 * @param entity
	 * @param rotationYawServer
	 * @param realRotation
	 * @param anglePitch
	 * @return
	 */
	public static Packet setRotationPacket(Entity entity, float rotationYawServer, float realRotation, boolean isInReverse, float anglePitch, double posY, boolean shouldSetPosY) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			if (entity != null && entity instanceof EntityRollingStock) {
				dos.writeInt(11);
				dos.writeInt(entity.entityId);
				dos.writeInt((int) rotationYawServer);
				dos.writeInt((int) realRotation);
				dos.writeBoolean(isInReverse);
				dos.writeInt((int) anglePitch);
				dos.writeInt((int) (posY * 1000000));
				dos.writeBoolean(shouldSetPosY);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload(Info.channel, bos.toByteArray());
		packet.length = bos.size();
		return packet;
	}

	/**
	 * Zeppelin rotation packet sent to client
	 * 
	 * @param entity
	 * @param rotationYawServer
	 * @param realRotation
	 * @param anglePitch
	 * @return
	 */
	public static Packet setRotationPacketZeppelin(Entity entity, float rotationYawServer, float roll) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			if (entity != null && entity instanceof AbstractZeppelin) {
				dos.writeInt(14);
				dos.writeInt(entity.entityId);
				dos.writeInt((int) rotationYawServer);
				dos.writeInt((int) roll);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload(Info.channel, bos.toByteArray());
		packet.length = bos.size();
		return packet;
	}

	public static void sendPacketToClients(Packet packet, World worldObj, int x, int y, int z, double range) {
		try {
			PacketDispatcher.sendPacketToAllAround(x, y, z, range, worldObj.provider.dimensionId, packet);
		}
		catch (Exception e) {
			System.out.println("Sending packet to client failed.");
			e.printStackTrace();
		}
	}

	public static Packet setParkingBrake(Entity player, Entity entity, boolean set, boolean toServer) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			if (entity instanceof Locomotive) {
				Locomotive lo = (Locomotive) entity;
				dos.writeInt(2);
				dos.writeInt(lo.entityId);//.getID());
				dos.writeBoolean(set);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload(Info.channel, bos.toByteArray());
		if (toServer) {
			packet.length = bos.size();
			if (player instanceof EntityClientPlayerMP) {
				EntityClientPlayerMP playerMP = (EntityClientPlayerMP) player;
				playerMP.sendQueue.addToSendQueue(packet);
			}
		}
		return packet;
	}

	public static Packet setLocoTurnedOn(Entity player, Entity entity, boolean set, boolean toServer) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			if (entity instanceof Locomotive) {
				Locomotive lo = (Locomotive) entity;
				dos.writeInt(17);
				dos.writeInt(lo.entityId);//.getID());
				dos.writeBoolean(set);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload(Info.channel, bos.toByteArray());
		if (toServer) {
			packet.length = bos.size();
			if (player instanceof EntityClientPlayerMP) {
				EntityClientPlayerMP playerMP = (EntityClientPlayerMP) player;
				playerMP.sendQueue.addToSendQueue(packet);
			}
		}
		return packet;
	}

	public static Packet setTrainLocked(Entity player, Entity entity, boolean set) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			if (entity instanceof AbstractTrains) {
				AbstractTrains lo = (AbstractTrains) entity;
				dos.writeInt(12);
				dos.writeInt(lo.entityId);//.getID());
				dos.writeBoolean(set);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload(Info.channel, bos.toByteArray());
		packet.length = bos.size();
		if (player instanceof EntityClientPlayerMP) {
			EntityClientPlayerMP playerMP = (EntityClientPlayerMP) player;
			playerMP.sendQueue.addToSendQueue(packet);
		}
		return packet;
	}

	public static Packet setTrainLockedToClient(Entity player, Entity entity, boolean set) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			if (entity instanceof AbstractTrains) {
				AbstractTrains lo = (AbstractTrains) entity;
				dos.writeInt(13);
				dos.writeInt(lo.entityId);//.getID());
				dos.writeBoolean(set);
				dos.writeBytes(lo.trainOwner);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload(Info.channel, bos.toByteArray());
		packet.length = bos.size();
		return packet;
	}

	public static Packet setBuilderPlannedHeight(Entity player, Entity entity, int set, int packetID) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			if (entity instanceof EntityTracksBuilder) {
				EntityTracksBuilder lo = (EntityTracksBuilder) entity;
				dos.writeInt(packetID);
				dos.writeInt(lo.entityId);//.getID());
				dos.writeInt(set);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload(Info.channel, bos.toByteArray());
		packet.length = bos.size();
		if (player instanceof EntityClientPlayerMP) {
			EntityClientPlayerMP playerMP = (EntityClientPlayerMP) player;
			playerMP.sendQueue.addToSendQueue(packet);
		}
		return packet;
	}

	public static Packet setBookPage(Entity player, int page, int recipe) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeInt(6);
			dos.writeInt(player.entityId);//.getID());
			dos.writeInt(page);
			dos.writeInt(recipe);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload(Info.channel, bos.toByteArray());
		packet.length = bos.size();
		if (player instanceof EntityClientPlayerMP) {
			EntityClientPlayerMP playerMP = (EntityClientPlayerMP) player;
			playerMP.sendQueue.addToSendQueue(packet);
		}
		return packet;
	}

	public static Packet setJukeBoxStreamingUrl(EntityPlayer player, Entity entity, String url, boolean isPlaying) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		if (url != null && url.length() > 64) url = url.substring(0, 63);
		try {
			if (entity != null && entity instanceof EntityJukeBoxCart) {
				dos.writeInt(15);
				dos.writeInt(entity.entityId);
				dos.writeUTF(url);
				dos.writeBoolean(isPlaying);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload(Info.channel, bos.toByteArray());
		packet.length = bos.size();
		if (player instanceof EntityClientPlayerMP) {
			EntityClientPlayerMP playerMP = (EntityClientPlayerMP) player;
			playerMP.sendQueue.addToSendQueue(packet);
		}
		return packet;
	}

	public static Packet setSlotsFilledPacket(Entity entity, int slotsFilled) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			if (entity instanceof AbstractTrains) {
				AbstractTrains lo = (AbstractTrains) entity;
				dos.writeInt(16);
				dos.writeInt(lo.entityId);
				dos.writeInt(slotsFilled);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload(Info.channel, bos.toByteArray());
		packet.length = bos.size();
		return packet;
	}
}
