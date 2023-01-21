package net.minecraft.trident.entity.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.Trident;
import net.minecraft.trident.entity.model.ModelTrident;
import net.minecraft.trident.item.ITrident;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ji_GGO
 * @date 2022/12/12
 */
@SideOnly(Side.CLIENT)
public class RenderItemTrident {

    private static final ModelTrident trident = new ModelTrident();

    public static void render(ItemStack stack, ItemCameraTransforms.TransformType type) {
        if (stack.getItem() instanceof ITrident) {
            ITrident item = (ITrident) stack.getItem();
            if (item.getModel() == Trident.MODEL) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(ModelTrident.TEXTURE);

                GlStateManager.pushMatrix();
                GlStateManager.scale(1.0F, -1.0F, -1.0F);
                trident.renderer();
                if (stack.isItemEnchanted()) {
                    RenderModelEnchant.renderEnchantedGlint(trident, 0.0625F);
                }
                GlStateManager.popMatrix();
            } else {
                item.render(stack, type);
            }
        }
    }

}