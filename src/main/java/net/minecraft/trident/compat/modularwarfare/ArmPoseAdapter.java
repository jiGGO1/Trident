package net.minecraft.trident.compat.modularwarfare;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.client.model.ModelBiped;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ArmPoseAdapter extends TypeAdapter<ModelBiped.ArmPose> {

    private final Map<String, ModelBiped.ArmPose> nameToConstant = new HashMap<>();
    private final Map<ModelBiped.ArmPose, String> constantToName = new HashMap<>();

    public ArmPoseAdapter() {
        Class<ModelBiped.ArmPose> clazz = ModelBiped.ArmPose.class;
        for (ModelBiped.ArmPose constant : clazz.getEnumConstants()) {
            String name = constant.name();
            try {
                SerializedName annotation = clazz.getDeclaredField(name).getAnnotation(SerializedName.class);
                if (annotation != null) {
                    name = annotation.value();
                    for (String alternate : annotation.alternate()) {
                        this.nameToConstant.put(alternate, constant);
                    }
                }
                this.nameToConstant.put(name, constant);
                this.constantToName.put(constant, name);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void write(JsonWriter writer, ModelBiped.ArmPose value) throws IOException {
        writer.value(value == null ? null : this.constantToName.get(value));
    }

    @Override
    public ModelBiped.ArmPose read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        return this.nameToConstant.get(reader.nextString());
    }

}