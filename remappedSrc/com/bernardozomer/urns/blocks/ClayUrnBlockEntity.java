package com.bernardozomer.urns.blocks;

import com.bernardozomer.urns.registry.ModBlockEntities;
import com.bernardozomer.urns.utils.ImplementedInventory;
import com.bernardozomer.urns.utils.ItemStackUtils;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.collection.DefaultedList;

/**
 * The block entity that is provided by the Clay Urn block.
 * Implements the block's inventory.
 */
public class ClayUrnBlockEntity extends BlockEntity implements ImplementedInventory {
    private DefaultedList<ItemStack> items;
    private static final int INVENTORY_SIZE = 1;

    public ClayUrnBlockEntity() {
        super(ModBlockEntities.CLAY_URN_BLOCK_ENTITY);
        items = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    /**
     * Sets the block's inventory.
     * @param items The new inventory.
     */
    public void setItems(DefaultedList<ItemStack> items) {
        this.items = items;
    }

    /**
     * Evaluates if a stack can be added to an index of the inventory and does it if so.
     * Does not handle decrementing the giver stack.
     * @param giver  The stack that is being transferred.
     * @param index  The inventory index for the items to be added to.
     * @param amount The amount of items to be added.
     * @return       ActionResult.SUCCESS if the item could be added,
     *               ActionResult.FAIL otherwise.
     */
    public ActionResult receive(ItemStack giver, int index, int amount) {
        ItemStack receiver = items.get(index);

        if (receiver.getCount() < receiver.getMaxCount()
                && (receiver.isEmpty() || ItemStackUtils.equalsIgnoreCount(giver, receiver))) {
            ItemStack transfer = giver.copy();
            transfer.setCount(Math.min(receiver.getCount() + amount, receiver.getMaxCount()));

            items.set(index, transfer);

            if (getWorld() != null) {
                this.getWorld().updateComparators(getPos(), getCachedState().getBlock());
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, items);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag, items);
        return super.toTag(tag);
    }
}
