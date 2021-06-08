package com.bernardozomer.urns.blocks;

import com.bernardozomer.urns.utils.ImplementedInventory;

import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

/**
 * The falling block entity that is summoned when a clay urn must fall.
 */
public class ClayUrnFallingBlockEntity extends FallingBlockEntity implements ImplementedInventory {

    // The inventory the block held before falling.
    // Will be either dropped or passed along to the new clay urn block placed on landing.
    private final DefaultedList<ItemStack> items;
    // The height at which the falling block was summoned.
    private double originY;

    public ClayUrnFallingBlockEntity(World world, double x, double y, double z, BlockState block,
                                     DefaultedList<ItemStack> items) {
        super(world, x, y, z, block);
        this.items = items;
        originY = y;
    }

    /**
     * Drops both the block and its inventory when it can't find a valid landing space.
     * @param item The block.
     * @return     The block as an item.
     */
    @Override
    public ItemEntity dropItem(ItemConvertible item) {
        ItemScatterer.spawn(world, getBlockPos(), this);
        return this.dropItem(item, 0);
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        super.toTag(compoundTag);
        compoundTag.putDouble("originY", originY);
        return compoundTag;
    }

    @Override
    public void fromTag(CompoundTag compoundTag) {
        super.fromTag(compoundTag);
        originY = compoundTag.getInt("originY");
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    public double getOriginY() {
        return originY;
    }
}
