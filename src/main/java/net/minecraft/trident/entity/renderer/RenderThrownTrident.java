package net.minecraft.trident.entity.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.trident.entity.model.IModelTrident;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ji_GGO
 * @date 2023/09/01
 */
@SideOnly(Side.CLIENT)
public abstract class RenderThrownTrident<T extends EntityArrow> extends Render<T> {

    public RenderThrownTrident(RenderManager manager) {
        super(manager);
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
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
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

        this.renderer();

        this.renderEnchanted(entity);

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

        float scale = 0.05625f;
        GlStateManager.enableRescaleNormal();
        float shake = (float) entity.arrowShake - partialTicks;

        if (shake > 0.0f) {
            float rotation = -MathHelper.sin(shake * 3.0f) * shake;
            GlStateManager.rotate(rotation, 0.0f, 0.0f, 1.0f);
        }

        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.translate(-4.0f, 0.0f, 0.0f);

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        GlStateManager.enableLighting();
        renderer.enableLightmap();
    }

    public void renderer() {
        this.getModel().renderer();
    }

    public abstract void renderEnchanted(T entity);

    public abstract IModelTrident getModel();

}