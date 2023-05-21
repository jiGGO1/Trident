package net.minecraft.trident.compat.modularwarfare;

import com.google.gson.GsonBuilder;
import com.modularwarfare.ModularWarfare;
import net.minecraft.client.model.ModelBiped;

/**
 * @author ji_GGO
 * @date 2023/03/27
 */
public class ModularWarfareInit {

    public static void init() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ModelBiped.ArmPose.class, new ArmPoseAdapter());
        ModularWarfare.gson = builder.setPrettyPrinting().create();
    }

}