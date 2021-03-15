package net.minecraft.trident.sound;

import com.google.common.collect.Lists;
import net.minecraft.trident.Trident;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

/**
 * @author ji_GGO
 * @date 2021/03/06
 */
@Mod.EventBusSubscriber(modid = Trident.MODID)
public class TridentSounds {

    public static final ArrayList<SoundEvent> SOUNDS = Lists.newArrayList();

    public static final SoundEvent ITEM_TRIDENT_HIT = register("item.trident.hit");
    public static final SoundEvent ITEM_TRIDENT_HIT_GROUND = register("item.trident.hit_ground");
    public static final SoundEvent ITEM_TRIDENT_RETURN = register("item.trident.return");
    public static final SoundEvent ITEM_TRIDENT_RIPTIDE_1 = register("item.trident.riptide_1");
    public static final SoundEvent ITEM_TRIDENT_RIPTIDE_2 = register("item.trident.riptide_2");
    public static final SoundEvent ITEM_TRIDENT_RIPTIDE_3 = register("item.trident.riptide_3");
    public static final SoundEvent ITEM_TRIDENT_THROW = register("item.trident.throw");
    public static final SoundEvent ITEM_TRIDENT_THUNDER = register("item.trident.thunder");

    public static SoundEvent register(String key) {
        ResourceLocation location = new ResourceLocation(Trident.MODID, key);
        SoundEvent sound = new SoundEvent(location).setRegistryName(location);
        SOUNDS.add(sound);
        return sound;
    }

    @SubscribeEvent
    public static void onSoundEvenrRegistration(RegistryEvent.Register<SoundEvent> event) {
        SOUNDS.forEach(sound -> event.getRegistry().register(sound));
    }

}