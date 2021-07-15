package com.bernardozomer.urns.blocks;

import com.bernardozomer.urns.registry.ModBlocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClayUrnUncracked extends ClayUrn {
    public ClayUrnUncracked(Settings settings) {
        super(settings);
    }

    @Override
    public void crack(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (!(blockEntity instanceof ClayUrnBlockEntity)) {
            return;
        }

        DefaultedList<ItemStack> items = ((ClayUrnBlockEntity) blockEntity).removeItems();
        System.out.println(items);
        world.setBlockState(pos, ModBlocks.CLAY_URN_CRACKED.getDefaultState());
        blockEntity = world.getBlockEntity(pos);

        if (!(blockEntity instanceof ClayUrnBlockEntity)) {
            return;
        }

        ((ClayUrnBlockEntity) blockEntity).setItems(items);
        System.out.println(((ClayUrnBlockEntity) blockEntity).getItems());

        world.playSound(
                null,
                pos,
                getCrackSound(),
                SoundCategory.BLOCKS,
                1f,
                1f
        );
    }
}
