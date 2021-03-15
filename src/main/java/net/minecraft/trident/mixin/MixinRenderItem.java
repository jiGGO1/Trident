package net.minecraft.trident.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.Trident;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author ji_GGO
 * @date 2021/02/27
 */
@Mixin(RenderItem.class)
public abstract class MixinRenderItem {

    @Final
    @Shadow
    private ItemModelMesher itemModelMesher;

    @Final
    @Shadow
    private TextureManager textureManager;

    @Inject(method = "renderItemModelIntoGUI(Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/renderer/block/model/IBakedModel;)V",
            at = @At(value = "HEAD"), cancellable = true)
    public void renderTrident(ItemStack stack, int x, int y, IBakedModel bakedmodel, CallbackInfo call){
        if (stack.getItem() == Trident.TRIDENT) {
            IBakedModel trident = this.itemModelMesher.getModelManager().getModel(Trident.MODEL);
            trident.getOverrides().handleItemState(trident, stack, null, Minecraft.getMinecraft().player);
            bakedmodel = trident;
            GlStateManager.pushMatrix();
            this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.setupGuiTransform(x, y, bakedmodel.isGui3d());
            bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
            this.renderItem(stack, bakedmodel);
            GlStateManager.disableAlpha();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
            this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
            call.cancel();
        }
    }

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;)V", cancellable = true,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItemModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V"))
    public void renderItemFrame(ItemStack stack, ItemCameraTransforms.TransformType transformType, CallbackInfo call){
        if (transformType == ItemCameraTransforms.TransformType.FIXED) {
            if (stack.getItem() == Trident.TRIDENT) {
                IBakedModel trident = this.itemModelMesher.getModelManager().getModel(Trident.MODEL);
                this.renderItemModel(stack, trident, transformType, false);
                call.cancel();
            }
        }
    }

    @Shadow
    public abstract void renderItem(ItemStack stack, IBakedModel model);

    @Shadow
    abstract void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d);

    @Shadow
    protected abstract void renderItemModel(ItemStack stack, IBakedModel bakedmodel, ItemCameraTransforms.TransformType transform, boolean leftHanded);

}