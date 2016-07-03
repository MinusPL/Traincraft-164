package traincraft.common.core.handlers.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import traincraft.common.core.handlers.ServerTickHandler;
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

public class getTEPClient implements IMessage
{
	int par1, x, y, z, facing, cook_time, burn_time, amount, lid, random_color,
	srv_tick, id_drop, has_model, switch_state, bb_height;
	String type;
	
	public getTEPClient(TileEntity tileEntity)
	{
		if (tileEntity != null && tileEntity instanceof TileTrainWbench)
		{
			TileTrainWbench tem = (TileTrainWbench) tileEntity;
			par1 = 0;
			x = tem.xCoord;
			y = tem.yCoord;
			z = tem.zCoord;
			facing = tem.getFacing().ordinal();
		}
		if (tileEntity != null && tileEntity instanceof TileCrafterTierIII)
		{
			TileCrafterTierIII tem = (TileCrafterTierIII) tileEntity;
			par1 = 0;
			x = tem.xCoord;
			y = tem.yCoord;
			z = tem.zCoord;
			facing = tem.getFacing().ordinal();
		}
		if (tileEntity != null && tileEntity instanceof TileCrafterTierII)
		{
			TileCrafterTierII tem = (TileCrafterTierII) tileEntity;
			par1 = 0;
			x = tem.xCoord;
			y = tem.yCoord;
			z = tem.zCoord;
			facing = tem.getFacing().ordinal();
		}
		if (tileEntity != null && tileEntity instanceof TileCrafterTierI)
		{
			TileCrafterTierI tem = (TileCrafterTierI) tileEntity;
			par1 = 0;
			x = tem.xCoord;
			y = tem.yCoord;
			z = tem.zCoord;
			facing = tem.getFacing().ordinal();
		}
		if (tileEntity != null && tileEntity instanceof TileEntityDistil)
		{
			TileEntityDistil tem = (TileEntityDistil) tileEntity;
			par1 = 1;
			x = tem.xCoord;
			y = tem.yCoord;
			z = tem.zCoord;
			facing = tem.getFacing().ordinal();
			cook_time = tem.distilCookTime;
			burn_time = tem.distilBurnTime;
			amount = tem.amount;
			lid = tem.liquidItemID;
		}
		if (tileEntity != null && tileEntity instanceof TileEntityOpenHearthFurnace)
		{
			TileEntityOpenHearthFurnace tem = (TileEntityOpenHearthFurnace) tileEntity;
			par1 = 1;
			x = tem.xCoord;
			y = tem.yCoord;
			z = tem.zCoord;
			facing = tem.getFacing().ordinal();
			cook_time = tem.furnaceCookTime;
			burn_time = tem.furnaceBurnTime;
		}
		if (tileEntity != null && tileEntity instanceof TileStopper) 
		{
			TileStopper tem = (TileStopper) tileEntity;
			par1 = 0;
			x = tem.xCoord;
			y = tem.yCoord;
			z = tem.zCoord;
			facing = tem.getFacing();
		}
		if (tileEntity != null && tileEntity instanceof TileBook)
		{
			TileBook tem = (TileBook) tileEntity;
			par1 = 0;
			x = tem.xCoord;
			y = tem.yCoord;
			z = tem.zCoord;
			facing = tem.getFacing();
		}
		if (tileEntity != null && tileEntity instanceof TileSignal)
		{
			TileSignal tem = (TileSignal) tileEntity;
			par1 = 0;
			x = tem.xCoord;
			y = tem.yCoord;
			z = tem.zCoord;
			facing = tem.getFacing();
		}
		if (tileEntity != null && tileEntity instanceof TileLantern)
		{
			TileLantern tem = (TileLantern) tileEntity;
			par1 = 0;
			x = tem.xCoord;
			y = tem.yCoord;
			z = tem.zCoord;
			random_color = tem.randomColor;
		}
		if (tileEntity != null && tileEntity instanceof TileWaterWheel)
		{
			TileWaterWheel tem = (TileWaterWheel) tileEntity;
			par1 = 0;
			x = tem.xCoord;
			y = tem.yCoord;
			z = tem.zCoord;
			facing = tem.getFacing();
		}
		if (tileEntity != null && tileEntity instanceof TileWindMill)
		{
			TileWindMill tem = (TileWindMill) tileEntity;
			par1 = 0;
			x = tem.xCoord;
			y = tem.yCoord;
			z = tem.zCoord;
			facing = tem.getFacing();
			srv_tick = ServerTickHandler.windStrength;
		}
		if (tileEntity != null && tileEntity instanceof TileGeneratorDiesel)
		{
			TileGeneratorDiesel tem = (TileGeneratorDiesel) tileEntity;
			par1 = 0;
			x = tem.xCoord;
			y = tem.yCoord;
			z = tem.zCoord;
			facing = tem.getFacing();
		}
		if (tileEntity != null && tileEntity instanceof TileTCRail)
		{
			TileTCRail tem = (TileTCRail) tileEntity;
			par1 = 0;
			x = tem.xCoord;
			y = tem.yCoord;
			z = tem.zCoord;
			facing = tem.getFacing();
			type = tem.getType();
			has_model = tem.hasModel ? 1 : 0;
			switch_state = tem.getSwitchState() ? 1 : 0;
			id_drop = tem.idDrop;
		}
		if (tileEntity != null && tileEntity instanceof TileTCRailGag)
		{
			TileTCRailGag tem = (TileTCRailGag) tileEntity;
			par1 = 0;
			x = tem.xCoord;
			y = tem.yCoord;
			z = tem.zCoord;
			type = tem.type;
			bb_height = (int)(tem.bbHeight * 1000);
		}	
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		par1 = ByteBufUtils.readVarInt(buf, 5);
		x = ByteBufUtils.readVarInt(buf, 5);
		y = ByteBufUtils.readVarInt(buf, 5);
		z = ByteBufUtils.readVarInt(buf, 5);
		facing = ByteBufUtils.readVarInt(buf, 5);
		cook_time = ByteBufUtils.readVarInt(buf, 5);
		burn_time = ByteBufUtils.readVarInt(buf, 5);
		amount = ByteBufUtils.readVarInt(buf, 5);
		lid = ByteBufUtils.readVarInt(buf, 5);
		random_color = ByteBufUtils.readVarInt(buf, 5);
		srv_tick = ByteBufUtils.readVarInt(buf, 5);
		has_model = ByteBufUtils.readVarInt(buf, 5);
		switch_state = ByteBufUtils.readVarInt(buf, 5);
		id_drop = ByteBufUtils.readVarInt(buf, 5);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, par1, 5);
		ByteBufUtils.writeVarInt(buf, x, 5);
		ByteBufUtils.writeVarInt(buf, y, 5);
		ByteBufUtils.writeVarInt(buf, z, 5);
		ByteBufUtils.writeVarInt(buf, facing, 5);
		ByteBufUtils.writeVarInt(buf, cook_time, 5);
		ByteBufUtils.writeVarInt(buf, burn_time, 5);
		ByteBufUtils.writeVarInt(buf, amount, 5);
		ByteBufUtils.writeVarInt(buf, lid, 5);
		ByteBufUtils.writeVarInt(buf, random_color, 5);
		ByteBufUtils.writeVarInt(buf, srv_tick, 5);
	}
	
	public static class Handler implements IMessageHandler<getTEPClient, IMessage>
	{
		@Override
		public IMessage onMessage(getTEPClient message, MessageContext ctx)
		{
			int packetIndex = message.par1;
			World world;
			switch(ctx.side)
			{
				case CLIENT:
				{
					world = Minecraft.getMinecraft().thePlayer.worldObj;
				}
				case SERVER:
				{
					world = ctx.getServerHandler().playerEntity.worldObj;
				}
				default:
				{
					assert false : "Invalid side in TestMsgHandler: " + ctx.side;
				}
			}
			if(packetIndex == 0)
			{
				if (packetIndex == 0)
				{
					int x = message.x;
					int y = message.y;
					int z = message.z;
					TileEntity te = world.getTileEntity(x, y, z);
					if (te instanceof TileTrainWbench)
					{
						int orientation = message.facing;
						((TileTrainWbench) te).handlePacketDataFromServer(orientation);
					}
					if (te instanceof TileCrafterTierIII)
					{
						int orientation = message.facing;
						((TileCrafterTierIII) te).handlePacketDataFromServer(orientation);
					}
					if (te instanceof TileCrafterTierII)
					{
						int orientation = message.facing;
						((TileCrafterTierII) te).handlePacketDataFromServer(orientation);
					}
					if (te instanceof TileCrafterTierI)
					{
						int orientation = message.facing;
						((TileCrafterTierI) te).handlePacketDataFromServer(orientation);
					}
					if (te instanceof TileStopper)
					{
						int orientation = message.facing;
						((TileStopper) te).handlePacketDataFromServer(orientation);
					}
					if (te instanceof TileBook)
					{
						int orientation = message.facing;
						((TileBook) te).handlePacketDataFromServer(orientation);
					}
					if (te instanceof TileSignal)
					{
						int orientation = message.facing;
						((TileSignal) te).handlePacketDataFromServer(orientation);
					}
					if (te instanceof TileLantern)
					{
						int color = message.random_color;
						((TileLantern) te).handlePacketDataFromServer(color);
					}
					if (te instanceof TileWaterWheel)
					{
						int orientation = message.facing;
						((TileWaterWheel) te).handlePacketDataFromServer(orientation);
					}
					if (te instanceof TileWindMill)
					{
						int orientation = message.facing;
						int wind = message.srv_tick;
						((TileWindMill) te).handlePacketDataFromServer(orientation, wind);
					}
					if (te instanceof TileGeneratorDiesel)
					{
						int orientation = message.facing;
						((TileGeneratorDiesel) te).handlePacketDataFromServer(orientation);
					}
					if (te instanceof TileTCRail)
					{
						int orientation = message.facing;
						String type = data.readUTF();
						boolean hasModel = data.readBoolean();
						boolean switchActive = data.readBoolean();
						int idDrop = data.readInt();
						((TileTCRail) te).handlePacketDataFromServer(orientation, type, hasModel, switchActive, idDrop);
					}
					if (te instanceof TileTCRailGag)
					{
						String type = data.readUTF();
						int bbHeight = data.readInt();
						((TileTCRailGag) te).handlePacketDataFromServer(type, bbHeight);
					}
				}				
			}
			else if(packetIndex == 1)
			{
				int x = message.x;
				int y = message.y;
				int z = message.z;
				TileEntity te = world.getTileEntity(x, y, z);
				if(te instanceof TileEntityOpenHearthFurnace)
				{
					int orientation = message.facing;
					int cookTime = message.cook_time;
					int burnTime = message.burn_time;
					((TileEntityOpenHearthFurnace) te).handlePacketDataFromServer(orientation, cookTime, burnTime);					
				}
			}
			else if(packetIndex == 1)
			{
				
			}
			else if(packetIndex == 1)
			{
				
			}
			else if(packetIndex == 1)
			{
				
			}
			else if(packetIndex == 1)
			{
				
			}
			else if(packetIndex == 1)
			{
				
			}
			else if(packetIndex == 1)
			{
				
			}
			else if(packetIndex == 1)
			{
				
			}
			else if(packetIndex == 1)
			{
				
			}
			else if(packetIndex == 1)
			{
				
			}
			else if(packetIndex == 1)
			{
				
			}
			else if(packetIndex == 1)
			{
				
			}
			else if(packetIndex == 1)
			{
				
			}
			else if(packetIndex == 1)
			{
				
			}
			else if(packetIndex == 1)
			{
				
			}		
			return null;
		}
		
	}
}
