package net.minecraft.trident.iceandfire;

import com.github.alexthe666.iceandfire.client.model.ModelTideTrident;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.entity.renderer.RenderModelEnchant;

/**
 * @author ji_GGO
 * @date 2022/12/02
 */
public class RenderItemTideTrident {

    private static ModelTideTrident trident = new ModelTideTrident();

    public static void render(ItemStack stack) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(RenderTideTrident.TEXTURE);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5F, 0.5f, 0.5f);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0.2F, -0.15F);

        int person = Minecraft.getMinecraft().gameSettings.thirdPersonView;
        EntityPlayer player = Minecraft.getMinecraft().player;
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        boolean play = !((screen instanceof GuiInventory) || screen instanceof GuiContainerCreative);
        if (person == 0 && play) {
            boolean flag = player != null && player.getHeldItemMainhand() == stack;
            GlStateManager.translate(flag ? 0.3F : -0.3F, 0.2F, -0.2F);
        } else {
            GlStateManager.translate(0, 0.6F, 0.0F);
        }

        GlStateManager.rotate(160, 1.0F, 0.0F, 0.0F);
        trident.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        if (stack.isItemEnchanted()) {
            RenderModelEnchant.renderEnchantedGlint(trident, 0.0625F);
        }
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

}