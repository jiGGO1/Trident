package net.minecraft.trident.compat.oe;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.trident.entity.model.IModelTrident;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ji_GGO
 * @date 2023/08/31
 */
@SideOnly(Side.CLIENT)
public class ModelOeTrident extends ModelBase implements IModelTrident {

    public static final ResourceLocation TEXTURE = new ResourceLocation(OceanicExpanse.MODID, "textures/entity/trident.png");
    public final ModelRenderer bone;

    public ModelOeTrident() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.bone = new ModelRenderer(this);
        this.bone.setRotationPoint(8.0F, 11.0F, -8.0F);
        this.bone.cubeList.add(new ModelBox(bone, 0, 14, -8.5F, -1.0F, 7.5F, 1, 17, 1, 0.0F, false));
        this.bone.cubeList.add(new ModelBox(bone, 4, 12, -9.5F, -3.0F, 7.5F, 3, 2, 1, 0.0F, false));
        this.bone.cubeList.add(new ModelBox(bone, 0, 14, -8.5F, -4.0F, 7.5F, 1, 1, 1, 0.0F, false));
        this.bone.cubeList.add(new ModelBox(bone, 4, 10, -9.5F, -5.0F, 7.5F, 3, 1, 1, 0.0F, false));
        this.bone.cubeList.add(new ModelBox(bone, 0, 14, -8.5F, -6.0F, 7.5F, 1, 1, 1, 0.0F, false));
        this.bone.cubeList.add(new ModelBox(bone, 4, 7, -9.5F, -8.0F, 7.5F, 3, 2, 1, 0.0F, false));
        this.bone.cubeList.add(new ModelBox(bone, 0, 14, -8.5F, -9.0F, 7.5F, 1, 1, 1, 0.0F, false));
        this.bone.cubeList.add(new ModelBox(bone, 4, 4, -9.5F, -11.0F, 7.5F, 3, 2, 1, 0.0F, false));
        this.bone.cubeList.add(new ModelBox(bone, 0, 0, -8.5F, -15.0F, 7.5F, 1, 4, 1, 0.0F, false));
        this.bone.cubeList.add(new ModelBox(bone, 12, 0, -10.5F, -14.0F, 7.5F, 1, 4, 1, 0.0F, false));
        this.bone.cubeList.add(new ModelBox(bone, 12, 0, -6.5F, -14.0F, 7.5F, 1, 4, 1, 0.0F, false));
    }

    @Override
    public void renderer() {
        this.bone.render(0.0625F);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.bone.render(scale);
    }

}