package net.minecraft.trident.entity.renderer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.trident.util.EntityHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerSpinAttackEffect implements LayerRenderer<EntityLivingBase> {

    public static final ResourceLocation RIPTIDE = new ResourceLocation("textures/entity/trident_riptide.png");

    protected final RenderLivingBase<?> renderPlayer;

    private final LayerSpinAttackEffect.Model model = new LayerSpinAttackEffect.Model();

    public LayerSpinAttackEffect(RenderLivingBase<?> renderPlayer) {
        this.renderPlayer = renderPlayer;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (EntityHelper.isSpinAttacking((EntityPlayer)entity)) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.renderPlayer.bindTexture(RIPTIDE);
            for(int i = 0; i < 3; ++i) {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(ageInTicks * (float)(-(45 + i * 5)), 0.0F, 1.0F, 0.0F);
                float factor = 0.75F * (float)i;
                GlStateManager.scale(factor, factor, factor);
                GlStateManager.translate(0.0F, -0.2F + 0.6F * (float)i, 0.0F);
                this.model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public static class Model extends ModelBase {

        private final ModelRenderer root;

        public Model() {
            this.textureWidth = 64;
            this.textureHeight = 64;
            this.root = new ModelRenderer(this, 0, 0);
            this.root.addBox(-8.0F, -16.0F, -8.0F, 16, 32, 16);
        }

        @Override
        public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            this.root.render(scale);
        }

    }

}