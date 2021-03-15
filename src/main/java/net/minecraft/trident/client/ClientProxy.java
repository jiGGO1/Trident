package net.minecraft.trident.client;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.trident.Trident;
import net.minecraft.trident.common.CommonProxy;
import net.minecraft.trident.entity.EntityTrident;
import net.minecraft.trident.entity.renderer.RendererTrident;
import net.minecraft.trident.network.PacketSpinAttack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ji_GGO
 * @date 2021/03/13
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static final ModelBiped.ArmPose THROW_SPEAR = EnumHelper.addEnum(ModelBiped.ArmPose.class, "THROW_SPEAR", new Class<?>[]{});

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        RenderingRegistry.registerEntityRenderingHandler(EntityTrident.class, RendererTrident::new);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

}
