package net.minecraft.trident.mixin;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.Trident;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author ji_GGO
 * @date 2021/03/05
 */
@Mixin(RenderEntityItem.class)
public class MixinRenderEntityItem {

    @Redirect(method = "doRender(Lnet/minecraft/entity/item/EntityItem;DDDFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", opcode = 0))
    public void redirect(RenderItem itemRenderer, ItemStack stack, IBakedModel model){
        if (stack.getItem() == Trident.TRIDENT) {
            IBakedModel trident = itemRenderer.getItemModelMesher().getModelManager().getModel(Trident.MODEL);
            itemRenderer.renderItem(stack, trident);
        } else {
            itemRenderer.renderItem(stack, model);
        }
    }

}