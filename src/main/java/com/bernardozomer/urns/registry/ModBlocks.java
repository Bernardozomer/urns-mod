package com.bernardozomer.urns.registry;

import com.bernardozomer.urns.Urns;
import com.bernardozomer.urns.blocks.ClayUrn;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block CLAY_URN = new ClayUrn(FabricBlockSettings
            .of(Material.STONE, MaterialColor.BROWN_TERRACOTTA)
            .sounds(ClayUrn.getBlockSoundGroup())
            .breakByTool(FabricToolTags.PICKAXES)
            .breakByTool(FabricToolTags.SWORDS)
            .strength(0.3f, 0.3f)) {
    };

    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(Urns.MOD_ID, "clay_urn"), CLAY_URN);
    }
}
