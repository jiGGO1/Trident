package net.minecraft.trident.entity.model;

import net.minecraft.client.model.ModelBase;

public interface IModelTrident {

    default void renderer() {

    }

    default ModelBase getTridentModel() {
        return this instanceof ModelBase ? (ModelBase) this : null;
    }

}