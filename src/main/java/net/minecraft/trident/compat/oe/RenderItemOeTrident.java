package net.minecraft.trident.compat.oe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.entity.renderer.RenderModelEnchant;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ji_GGO
 * @date 2023/08/31
 */
@SideOnly(Side.CLIENT)
public class RenderItemOeTrident {

    private static final ModelOeTrident trident = new ModelOeTrident();

    public static void render(ItemStack stack, ItemCameraTransforms.TransformType type) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModelOeTrident.TEXTURE);

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0F, -1.0F, -1.0F);

        trident.renderer();
        if (stack.isItemEnchanted()) {
            RenderModelEnchant.renderEnchantedGlint(trident, 0.0625F);
        }
        GlStateManager.popMatrix();
    }

}