package net.minecraft.trident.compat.modularwarfare;

import com.google.gson.GsonBuilder;
import com.modularwarfare.ModularWarfare;
import net.minecraft.client.model.ModelBiped;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ji_GGO
 * @date 2023/03/27
 */
@SideOnly(Side.CLIENT)
public class ModularWarfareInit {

    public static final String MODID = "modularwarfare";

    @Method(modid = ModularWarfareInit.MODID)
    public static void init() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ModelBiped.ArmPose.class, new ArmPoseAdapter());
        ModularWarfare.gson = builder.setPrettyPrinting().create();
    }

}