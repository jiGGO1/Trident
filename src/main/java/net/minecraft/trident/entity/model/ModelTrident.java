package net.minecraft.trident.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelTrident extends ModelBase implements IModelTrident {

    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/trident.png");
    public final ModelRenderer root;

    public ModelTrident() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.root = new ModelRenderer(this,0, 6);
        this.root.addBox(-0.5F, 2.0F, -0.5F, 1, 25, 1, 0);
        ModelRenderer base = new ModelRenderer(this, 4, 0);
        base.addBox(-1.5F, 0.0F, -0.5F, 3, 2, 1);
        this.root.addChild(base);
        ModelRenderer left_spike = new ModelRenderer(this, 4, 3);
        left_spike.addBox(-2.5F, -3.0F, -0.5F, 1, 4, 1);
        this.root.addChild(left_spike);
        ModelRenderer middle_spike =new ModelRenderer(this, 0, 0);
        middle_spike.addBox(-0.5F, -4.0F, -0.5F, 1, 4, 1, 0);
        this.root.addChild(middle_spike);
        ModelRenderer right_spike = new ModelRenderer(this, 4, 3);
        right_spike.mirror = true;
        right_spike.addBox(1.5F, -3.0F, -0.5F, 1, 4, 1);
        this.root.addChild(right_spike);
    }

    @Override
    public void renderer() {
        this.root.render(0.0625F);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.root.render(scale);
    }

}