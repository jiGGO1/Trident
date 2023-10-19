package net.minecraft.trident.compat.oe;

import com.sirsquidly.oe.entity.EntityTrident;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.trident.entity.model.IModelTrident;
import net.minecraft.trident.entity.renderer.RenderModelEnchant;
import net.minecraft.trident.entity.renderer.RenderThrownTrident;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderOeTrident extends RenderThrownTrident<EntityTrident> {

    private final ModelOeTrident tridentModel = new ModelOeTrident();

    public RenderOeTrident(RenderManager manager) {
        super(manager);
    }

    @Override
    public void renderEnchanted(EntityTrident entity) {
        if (entity.getItem().isItemEnchanted()) {
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
        return ModelOeTrident.TEXTURE;
    }

}