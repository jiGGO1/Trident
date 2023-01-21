package net.minecraft.trident.mixin;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.item.ITrident;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * @author ji_GGO
 * @date 2021/03/05
 */
@SideOnly(Side.CLIENT)
@Mixin(RenderEntityItem.class)
public class MixinRenderEntityItem {

    @Final
    @Shadow
    private RenderItem itemRenderer;

    @ModifyVariable(method = "doRender", at = @At(value = "STORE", ordinal = 0))
    private IBakedModel replace(IBakedModel model, EntityItem entity) {
        ItemStack stack = entity.getItem();
        if (stack.getItem() instanceof ITrident) {
            ITrident item = (ITrident) stack.getItem();
            return itemRenderer.getItemModelMesher().getModelManager().getModel(item.getModel());
        }
        return model;
    }

}