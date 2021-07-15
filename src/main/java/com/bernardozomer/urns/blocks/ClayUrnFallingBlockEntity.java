package com.bernardozomer.urns.blocks;

import com.bernardozomer.urns.utils.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class ClayUrnFallingBlockEntity extends FallingBlockEntity implements ImplementedInventory {

    // The inventory the block held before falling.
    // Will be either dropped or passed along to the new block placed on landing.
    private final DefaultedList<ItemStack> items;
    // The height at which the falling block entity was summoned.
    private double originY;

    public ClayUrnFallingBlockEntity(World world, double x, double y, double z, BlockState block, DefaultedList<ItemStack> items) {
        super(world, x, y, z, block);
        this.items = items;
        this.originY = y;
    }

    public double getOriginY() {
        return originY;
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
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound compoundTag) {
        super.writeNbt(compoundTag);
        compoundTag.putDouble("originY", originY);
        return compoundTag;
    }

    @Override
    public void readNbt(NbtCompound compoundTag) {
        super.readNbt(compoundTag);
        originY = compoundTag.getInt("originY");
    }
}
