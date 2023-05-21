package net.minecraft.trident.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelTrident extends ModelBase {

    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/trident.png");
    public final ModelRenderer modelRenderer;

    public ModelTrident() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.modelRenderer = new ModelRenderer(this,0, 6);
        this.modelRenderer.addBox(-0.5F, 2.0F, -0.5F, 1, 25, 1, 0);
        ModelRenderer modelrenderer = new ModelRenderer(this, 4, 0);
        modelrenderer.addBox(-1.5F, 0.0F, -0.5F, 3, 2, 1);
        this.modelRenderer.addChild(modelrenderer);
        ModelRenderer modelrenderer1 = new ModelRenderer(this, 4, 3);
        modelrenderer1.addBox(-2.5F, -3.0F, -0.5F, 1, 4, 1);
        this.modelRenderer.addChild(modelrenderer1);
        ModelRenderer modelrenderer2 =new ModelRenderer(this, 0, 0);
        modelrenderer2.addBox(-0.5F, -4.0F, -0.5F, 1, 4, 1, 0);
        this.modelRenderer.addChild(modelrenderer2);
        ModelRenderer modelrenderer3 = new ModelRenderer(this, 4, 3);
        modelrenderer3.mirror = true;
        modelrenderer3.addBox(1.5F, -3.0F, -0.5F, 1, 4, 1);
        this.modelRenderer.addChild(modelrenderer3);
    }

    public void renderer() {
        this.modelRenderer.render(0.0625F);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.modelRenderer.render(scale);
    }

}
