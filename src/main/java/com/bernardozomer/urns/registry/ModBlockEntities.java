package com.bernardozomer.urns.registry;

import static com.bernardozomer.urns.registry.ModBlocks.CLAY_URN;

import com.bernardozomer.urns.Urns;
import com.bernardozomer.urns.blocks.ClayUrnBlockEntity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {

    public static BlockEntityType<ClayUrnBlockEntity> CLAY_URN_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(ClayUrnBlockEntity::new, CLAY_URN).build(null);

    public static void registerBlockEntities() {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, Urns.MOD_ID + ":clay_urn", CLAY_URN_BLOCK_ENTITY);
    }
}