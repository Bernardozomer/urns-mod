package com.bernardozomer.urns;

import com.bernardozomer.urns.registry.ModBlockEntities;
import com.bernardozomer.urns.registry.ModBlocks;
import com.bernardozomer.urns.registry.ModItems;
import net.fabricmc.api.ModInitializer;

/**
 * Class that initializes the mod.
 */
public class Urns implements ModInitializer {

    public static final String MOD_ID = "urns";

    /**
     * Registers stuff upon initialization.
     */
    @Override
    public void onInitialize() {
        ModBlocks.registerBlocks();
        ModBlockEntities.registerBlockEntities();
        ModItems.registerItems();
    }
}
