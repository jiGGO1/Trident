package net.minecraft.trident.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.Trident;
import net.minecraft.trident.entity.model.ModelTrident;
import net.minecraft.trident.entity.renderer.RenderModelEnchant;
import net.minecraft.trident.item.ITrident;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author ji_GGO
 * @date 2022/12/01
 */
@SideOnly(Side.CLIENT)
@Mixin(TileEntityItemStackRenderer.class)
public class MixinTileEntityItemStackRenderer {

    private final ModelTrident trident = new ModelTrident();

    @Inject(method = "renderByItem(Lnet/minecraft/item/ItemStack;)V", cancellable = true,
        at = @At(value = "HEAD"))
    private void render(ItemStack stack, CallbackInfo info) {
        if (stack.getItem() instanceof ITrident) {
            ITrident trident = (ITrident) stack.getItem();
            if (trident.getModel() == Trident.MODEL) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(ModelTrident.TEXTURE);

                GlStateManager.pushMatrix();
                GlStateManager.scale(1.0F, -1.0F, -1.0F);
                this.trident.renderer();
                if (stack.isItemEnchanted()) {
                    RenderModelEnchant.renderEnchantedGlint(this.trident, 0.0625F);
                }
                GlStateManager.popMatrix();
            } else {
                trident.render(stack);
            }

            info.cancel();
        }
    }

}