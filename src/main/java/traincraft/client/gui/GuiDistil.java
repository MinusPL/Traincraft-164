package traincraft.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import traincraft.api.LiquidManager;
import traincraft.common.containers.ContainerDistil;
import traincraft.common.library.TCBlocksList;
import traincraft.common.library.Info;
import traincraft.common.recipes.DistilRecipes;
import traincraft.common.tile.TileEntityDistil;

public class GuiDistil extends GuiContainer {

	private TileEntityDistil distilInventory;

	public GuiDistil(InventoryPlayer invPlayer, TileEntityDistil tileentitydistil) {
		super(new ContainerDistil(invPlayer, tileentitydistil));
		distilInventory = tileentitydistil;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString("Distillation tower", 8, 6, 0x404040);
		fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
		if (distilInventory.distilItemStacks[0] != null) {
			/**
			 * Stops showing 100% on copper 
			 */
			if(distilInventory.distilItemStacks[0].getItem().itemID == TCBlocksList.oreTC.blockID && (distilInventory.distilItemStacks[0].getItemDamage() != 1 && distilInventory.distilItemStacks[0].getItemDamage() != 2)){
				return;
			}
			double plasticChance = DistilRecipes.smelting().getPlasticChance(distilInventory.distilItemStacks[0].getItem().itemID);
			if(plasticChance!=0){//stops showing 100% for blocks that aren't part of a recipe
				double chanceShown = ((1 / plasticChance) * 100);
				fontRenderer.drawString((int) chanceShown + "%", 79, 70, 0x404040);
			}
		}
	}

	@Override
	public void drawScreen(int t, int g, float par3) {
		//drawGuiContainerBackgroundLayer(par3, t, g);
		//drawGuiContainerForegroundLayer(t, g);
		super.drawScreen(t, g, par3);
		int amount = distilInventory.amount;
		int liqui = (amount * 50) / distilInventory.getTankCapacity();
		if ((LiquidManager.DIESEL != null && distilInventory.getLiquidItemID() == LiquidManager.DIESEL.getID())) {
			if (intersectsWith(t, g)) {
				drawCreativeTabHoveringText("Diesel", t, g);
			}
		}
		if ((LiquidManager.REFINED_FUEL != null && distilInventory.getLiquidItemID() == LiquidManager.REFINED_FUEL.getID())) {
			if (intersectsWith(t, g)) {
				drawCreativeTabHoveringText("Fuel", t, g);
			}
		}
	}

	@Override
	protected void drawCreativeTabHoveringText(String str, int t, int g) {
		int j = (width - xSize) / 2;
		int k = (height - ySize) / 2;

		int liqui = (distilInventory.amount * 50) / distilInventory.getTankCapacity();
		int textWidth = fontRenderer.getStringWidth(distilInventory.amount + "/" + distilInventory.getTankCapacity());
		int startX = t + 14;
		int startY = g - 12;

		int i4 = 0xf0100010;
		int h = 8;
		int w = textWidth;
		drawGradientRect(startX - 3, startY - 4, startX + textWidth + 3, startY + 8 + 4 + 10, i4, i4);
		drawGradientRect(startX - 4, startY - 3, startX + textWidth + 4, startY + 8 + 3 + 10, i4, i4);
		int colour1 = 0x505000ff;
		int colour2 = (colour1 & 0xfefefe) >> 1 | colour1 & 0xff000000;
		drawGradientRect(startX - 3, startY - 3, startX + textWidth + 3, startY + 8 + 3 + 10, colour1, colour2);
		drawGradientRect(startX - 2, startY - 2, startX + textWidth + 2, startY + 8 + 2 + 10, i4, i4);
		fontRenderer.drawStringWithShadow(str, startX, startY, -1);
		fontRenderer.drawStringWithShadow(distilInventory.amount + "/" + distilInventory.getTankCapacity(), startX, startY + 10, -1);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int t, int g) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(new ResourceLocation(Info.resourceLocation,Info.guiPrefix + "gui_distillation_tower2.png"));
		int j = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
		int amount = distilInventory.amount;
		int liqui = (amount * 50) / distilInventory.getTankCapacity();
		if ((LiquidManager.DIESEL != null && distilInventory.getLiquidItemID() == LiquidManager.DIESEL.getID())) {
			drawTexturedModalRect(j + 145, (k + 57) - liqui, 177, 107 - liqui, 18, liqui);
		}
		if ((LiquidManager.REFINED_FUEL != null && distilInventory.getLiquidItemID() == LiquidManager.REFINED_FUEL.getID())) {
			drawTexturedModalRect(j + 145, (k + 57) - liqui, 177, 107 - liqui, 18, liqui);
			drawRect(j + 145, k + 57, 177, 107, 0);
		}
		if (distilInventory.isBurning()) {
			int l = distilInventory.getBurnTimeRemainingScaled(12);
			drawTexturedModalRect(j + 56, (k + 36 + 12) - l, 176, 12 - l, 14, l + 2);
		}
		int i1 = distilInventory.getCookProgressScaled(22);//31
		drawTexturedModalRect(j + 87, k + 36, 184, 15, i1, 41);//41
	}

	public boolean intersectsWith(int mouseX, int mouseY) {
		int j = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		if (mouseX >= j + 143 && mouseX <= j + 164 && mouseY >= k + 5 && mouseY <= k + 57) {
			return true;
		}
		return false;
	}
}
