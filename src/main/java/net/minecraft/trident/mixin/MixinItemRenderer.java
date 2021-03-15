package net.minecraft.trident.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.Trident;
import net.minecraft.trident.util.EntityHelper;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author ji_GGO
 * @date 2021/03/04
 */
@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @Final
    @Shadow
    private Minecraft mc;

    @Inject(method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getItemUseAction()Lnet/minecraft/item/EnumAction;"))
    public void renderTrident(AbstractClientPlayer player, float partialTicks, float pitch, EnumHand hand, float swingProgress, ItemStack stack, float equippedProgress, CallbackInfo call){
        if (stack.getItemUseAction() == Trident.SPEAR) {
            boolean flag = hand == EnumHand.MAIN_HAND;
            EnumHandSide enumhandside = flag ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
            int k = enumhandside == EnumHandSide.RIGHT ? 1 : -1;
            this.transformSideFirstPerson(enumhandside, equippedProgress);
            GlStateManager.translate((float)k * -0.5F, 0.7F, 0.1F);
            GlStateManager.rotate(-55.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate((float)k * 35.3F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((float)k * -9.785F, 0.0F, 0.0F, 1.0F);
            float f5 = (float)stack.getMaxItemUseDuration() - ((float)this.mc.player.getItemInUseCount() - partialTicks + 1.0F);
            float f7 = f5 / 10.0F;
            if (f7 > 1.0F) {
                f7 = 1.0F;
            }
            if (f7 > 0.1F) {
                float f9 = MathHelper.sin((f5 - 0.1F) * 1.3F);
                float f2 = f7 - 0.1F;
                float f3 = f9 * f2;
                GlStateManager.translate(f3 * 0.0F, f3 * 0.004F, f3 * 0.0F);
            }
            GlStateManager.translate(0.0F, 0.0F, f7 * 0.2F);
            GlStateManager.scale(1.0F, 1.0F, 1.0F + f7 * 0.2F);
            GlStateManager.rotate((float)k * 45.0F, 0.0F, -1.0F, 0.0F);
        }
    }

    @Inject(method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemSide(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V"))
    public void renderAttack(AbstractClientPlayer player, float partialTicks, float pitch, EnumHand hand, float swingProgress, ItemStack stack, float equippedProgress, CallbackInfo call) {
        if (EntityHelper.isSpinAttacking(player)) {
            boolean flag = hand == EnumHand.MAIN_HAND;
            EnumHandSide enumhandside = flag ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
            this.transformSideFirstPerson(enumhandside, equippedProgress);
            int j = enumhandside == EnumHandSide.RIGHT ? 1 : -1;
            //GlStateManager.translate((float) j * -0.4F, 0.8F, 0.3F);
            //GlStateManager.rotate((float) j * 65.0F, 0.0F, 1.0F, 0.0F);
            //GlStateManager.rotate((float) j * -85.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate((float) j * 1.3F, 2.7F, -0.5F);
            GlStateManager.rotate((float) j * 65.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((float) j * -85.0F, -0.0F, 0.0F, 1.0F);
            GlStateManager.scale(2, 2, 2);
        }
    }

    @Shadow
    abstract void transformSideFirstPerson(EnumHandSide hand, float equippedProgress);

}