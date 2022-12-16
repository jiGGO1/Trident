package net.minecraft.trident.mixin;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.item.ITrident;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author ji_GGO
 * @date 2021/03/05
 */
@SideOnly(Side.CLIENT)
@Mixin(RenderEntityItem.class)
public class MixinRenderEntityItem {

    @Redirect(method = "doRender(Lnet/minecraft/entity/item/EntityItem;DDDFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;getItemModelWithOverrides(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/client/renderer/block/model/IBakedModel;"))
    public IBakedModel redirect(RenderItem itemRenderer, ItemStack stack, World world, EntityLivingBase entity) {
        if (stack.getItem() instanceof ITrident) {
            ITrident item = (ITrident) stack.getItem();
            return itemRenderer.getItemModelMesher().getModelManager().getModel(item.getModel());
        } else {
            return itemRenderer.getItemModelWithOverrides(stack, world, entity);
        }
    }

}