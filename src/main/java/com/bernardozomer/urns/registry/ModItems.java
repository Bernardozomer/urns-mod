package com.bernardozomer.urns.registry;

import com.bernardozomer.urns.Urns;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    public static final BlockItem CLAY_URN = new BlockItem(
            ModBlocks.CLAY_URN, new Item.Settings().group(ItemGroup.DECORATIONS));
    public static final BlockItem CLAY_URN_CRACKED = new BlockItem(
            ModBlocks.CLAY_URN_CRACKED, new Item.Settings().group(ItemGroup.DECORATIONS));

    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(Urns.MOD_ID, "clay_urn"), CLAY_URN);
        Registry.register(Registry.ITEM, new Identifier(Urns.MOD_ID, "clay_urn_cracked"), CLAY_URN_CRACKED);
    }
}
