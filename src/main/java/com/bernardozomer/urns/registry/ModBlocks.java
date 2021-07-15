package com.bernardozomer.urns.registry;

import com.bernardozomer.urns.Urns;
import com.bernardozomer.urns.blocks.ClayUrnCracked;
import com.bernardozomer.urns.blocks.ClayUrnUncracked;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    private static final FabricBlockSettings clayUrnSettings = FabricBlockSettings
            .of(Material.STONE, MapColor.TERRACOTTA_BROWN)
            .sounds(ClayUrnUncracked.getBlockSoundGroup())
            .breakByTool(FabricToolTags.PICKAXES)
            .breakByTool(FabricToolTags.SWORDS)
            .strength(0.3f, 0.3f);

    public static final Block CLAY_URN = new ClayUrnUncracked(clayUrnSettings) {
    };

    public static final Block CLAY_URN_CRACKED = new ClayUrnCracked(clayUrnSettings) {
    };


    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new Identifier(Urns.MOD_ID, "clay_urn"), CLAY_URN);
        Registry.register(Registry.BLOCK, new Identifier(Urns.MOD_ID, "clay_urn_cracked"), CLAY_URN_CRACKED);
    }
}
