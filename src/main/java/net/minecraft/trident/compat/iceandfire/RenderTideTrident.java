package net.minecraft.trident.compat.iceandfire;

import com.github.alexthe666.iceandfire.client.model.ModelTideTrident;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.trident.entity.renderer.RenderModelEnchant;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTideTrident extends Render<EntityTideTrident> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/misc/tide_trident.png");
    private final ModelTideTrident modelTideTrident = new ModelTideTrident();

    public RenderTideTrident(RenderManager renderer) {
        super(renderer);
    }

    @Override
    public void doRender(EntityTideTrident trident, double x, double y, double z, float entityYaw, float partialTicks) {
        this.bindEntityTexture(trident);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.rotate(trident.prevRotationYaw + (trident.prevRotationYaw - trident.rotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(trident.prevRotationPitch + (trident.prevRotationPitch - trident.rotationPitch) * partialTicks + 90.0F, 0.0F, 0.0F, 1.0F);
        this.modelTideTrident.render(trident, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        if (trident.effect()) {
            RenderModelEnchant.renderEnchantedGlint(this, trident, this.modelTideTrident, 0.0625F);
        }
        GlStateManager.popMatrix();
        super.doRender(trident, x, y, z, entityYaw, partialTicks);
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTideTrident trident) {
        return TEXTURE;
    }

}