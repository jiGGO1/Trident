package net.minecraft.trident.entity.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.trident.entity.EntityTrident;
import net.minecraft.trident.entity.model.ModelTrident;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderTrident extends Render<EntityTrident> {

    private final ModelTrident tridentModel = new ModelTrident();

    public RenderTrident(RenderManager renderManager) {
        super(renderManager);
    }

    /**
     * https://github.com/thedarkcolour/Future-MC/blob/74022304a9f3731b05ec1794bbd7d8eada270994/src/main/java/thedarkcolour/futuremc/entity/trident/RenderTrident.kt#L12
     * @param entity       Trident
     * @param x            x
     * @param y            y
     * @param z            z
     * @param entityYaw    entityYaw
     * @param partialTicks partialTicks
     */
    @Override
    public void doRender(EntityTrident entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.bindEntityTexture(entity);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(
                entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0f,
                0.0f,
                1.0f,
                0.0f
        );
        GlStateManager.rotate(
                entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks + 90.0f,
                0.0f,
                0.0f,
                1.0f
        );
        this.tridentModel.renderer();

        if (entity.effect()) {
            RenderModelEnchant.renderEnchantedGlint(this, entity, this.tridentModel, 0.0625F);
        }

        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.enableLighting();

        EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
        renderer.disableLightmap();
        GlStateManager.disableLighting();

        bindEntityTexture(entity);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(
                entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0f,
                0.0f,
                1.0f,
                0.0f
        );
        GlStateManager.rotate(
                entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks,
                0.0f,
                0.0f,
                1.0f
        );

        float f10 = 0.05625f;
        GlStateManager.enableRescaleNormal();
        float f11 = (float) entity.arrowShake - partialTicks;

        if (f11 > 0.0f) {
            float f12 = -MathHelper.sin(f11 * 3.0f) * f11;
            GlStateManager.rotate(f12, 0.0f, 0.0f, 1.0f);
        }

        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(f10, f10, f10);
        GlStateManager.translate(-4.0f, 0.0f, 0.0f);

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        GlStateManager.enableLighting();
        renderer.enableLightmap();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityTrident entity) {
        return ModelTrident.TEXTURE;
    }

}