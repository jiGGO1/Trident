package net.minecraft.trident.compat.weather;

import net.minecraft.trident.Trident;
import weather2.api.WeatherDataHelper;

/**
 * @author ji_GGO
 * @date 2023/01/21
 */
public class WeatherRiptide {

    public static void init() {
        Trident.riptide = (player -> WeatherDataHelper.isPrecipitatingAt(player.getEntityWorld(), player.getPosition()));
    }

}