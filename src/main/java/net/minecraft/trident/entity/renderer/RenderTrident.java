package net.minecraft.trident.entity.renderer;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.trident.entity.EntityTrident;
import net.minecraft.trident.entity.model.IModelTrident;
import net.minecraft.trident.entity.model.ModelTrident;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * @author ji_GGO
 * @date 2023/09/01
 */
@SideOnly(Side.CLIENT)
public class RenderTrident extends RenderThrownTrident<EntityTrident> {

    private final ModelTrident tridentModel = new ModelTrident();

    public RenderTrident(RenderManager manager) {
        super(manager);
    }

    @Override
    public void renderEnchanted(EntityTrident entity) {
        if (entity.effect()) {
            RenderModelEnchant.renderEnchantedGlint(this, entity, this.getModel().getTridentModel(), 0.0625F);
        }
    }

    @Override
    public IModelTrident getModel() {
        return this.tridentModel;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityTrident entity) {
        return ModelTrident.TEXTURE;
    }

}