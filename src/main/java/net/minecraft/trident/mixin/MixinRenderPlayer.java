package net.minecraft.trident.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.trident.Trident;
import net.minecraft.trident.client.ClientProxy;
import net.minecraft.trident.entity.renderer.LayerSpinAttackEffect;
import net.minecraft.trident.util.EntityHelper;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author ji_GGO
 * @date 2021/03/05
 */
@SideOnly(Side.CLIENT)
@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer extends RenderLivingBase<AbstractClientPlayer> {

    private ModelBiped.ArmPose mainHand;

    private ModelBiped.ArmPose offHand;

    public MixinRenderPlayer(RenderManager manager, ModelBase model, float shadowSize) {
        super(manager, model, shadowSize);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/RenderManager;Z)V", at = @At(value = "TAIL"))
    private void init(RenderManager manager, boolean useSmallArms, CallbackInfo info){
        this.addLayer(new LayerSpinAttackEffect(this));
    }

    @Inject(method = "setModelVisibilities(Lnet/minecraft/client/entity/AbstractClientPlayer;)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getItemUseAction()Lnet/minecraft/item/EnumAction;"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void renderTridentMain(AbstractClientPlayer clientPlayer, CallbackInfo info, ModelPlayer modelplayer,
                                  ItemStack itemstack, ItemStack itemstack1,
                                  ModelBiped.ArmPose modelbiped$armpose, ModelBiped.ArmPose modelbiped$armpose1,
                                  EnumAction enumaction){
        if (enumaction == Trident.SPEAR) {
            if (enumaction == itemstack.getItemUseAction()){
                this.mainHand = ClientProxy.THROW_SPEAR;
            } else if (enumaction == itemstack1.getItemUseAction()){
                this.offHand = ClientProxy.THROW_SPEAR;
            }
        }
    }

    @Inject(method = "setModelVisibilities(Lnet/minecraft/client/entity/AbstractClientPlayer;)V",
            at = @At(value = "TAIL"))
    private void renderTrident(AbstractClientPlayer clientPlayer, CallbackInfo info){
        boolean flag = clientPlayer.getPrimaryHand() == EnumHandSide.RIGHT;
        ModelPlayer modelplayer = this.getMainModel();
        if (this.mainHand != null){
            if (flag) {
                modelplayer.rightArmPose = mainHand;
            } else {
                modelplayer.leftArmPose = mainHand;
            }
        }
        if (this.offHand != null){
            if (flag) {
                modelplayer.leftArmPose = offHand;
            } else {
                modelplayer.rightArmPose = offHand;
            }
        }
        this.clear();
    }

    @Redirect(method = "applyRotations(Lnet/minecraft/client/entity/AbstractClientPlayer;FFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V", opcode = 3))
    private void redirectElytraFly(float angle, float x, float y, float z, AbstractClientPlayer player){
        if (!EntityHelper.isSpinAttacking(player)) {
            GlStateManager.rotate(angle, x, y, z);
        }
    }

    @Shadow
    public abstract ModelPlayer getMainModel();

    public void clear(){
        this.mainHand = null;
        this.offHand = null;
    }

}