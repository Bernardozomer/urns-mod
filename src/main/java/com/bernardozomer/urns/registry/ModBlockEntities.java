package com.bernardozomer.urns.registry;

import static com.bernardozomer.urns.registry.ModBlocks.CLAY_URN;
import com.bernardozomer.urns.blocks.ClayUrnBlockEntity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {

    public static BlockEntityType<ClayUrnBlockEntity> CLAY_URN_BLOCK_ENTITY;

    public static void registerBlockEntities() {
        CLAY_URN_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "urns:clay_urn",
                BlockEntityType.Builder.create(ClayUrnBlockEntity::new, CLAY_URN).build(null));
    }
}
