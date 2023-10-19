package net.minecraft.trident.compat.oe;

import com.sirsquidly.oe.client.render.entity.RenderDrowned;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ji_GGO
 * @date 2023/09/01
 */
@SideOnly(Side.CLIENT)
public class RenderOeDrowned extends RenderDrowned {

    public RenderOeDrowned(RenderManager manager) {
        super(manager);
        OceanicExpanse.addVanillaLayerHeldItem(this, this.layerRenderers);
    }

}