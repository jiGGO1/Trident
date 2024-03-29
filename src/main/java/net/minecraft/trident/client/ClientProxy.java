package net.minecraft.trident.client;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.trident.common.CommonProxy;
import net.minecraft.trident.compat.modularwarfare.ModularWarfareInit;
import net.minecraft.trident.entity.EntityTrident;
import net.minecraft.trident.entity.renderer.RenderTrident;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;
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
    public void preInit(final FMLPreInitializationEvent event) {
        super.preInit(event);
        RenderingRegistry.registerEntityRenderingHandler(EntityTrident.class, RenderTrident::new);
        if (Loader.isModLoaded("modularwarfare")) {
            ModularWarfareInit.init();
        }
    }

    @Override
    public void init(final FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(final FMLPostInitializationEvent event) {
        super.postInit(event);
    }

}