package com.bernardozomer.urns.utils;

import net.minecraft.item.ItemStack;

/**
 * Helper functions to deal with item stacks.
 */
public class ItemStackUtils {

    /**
     * Returns if two item stacks are equal, ignoring their item count.
     * Originally by Reece @ The Fabric Project Discord server.
     * @param stack1 The first item stack.
     * @param stack2 The second item stack.
     * @return       true if their items and tags are equal, false otherwise.
     */
    public static boolean equalsIgnoreCount(ItemStack stack1, ItemStack stack2) {
        if (!stack1.isItemEqual(stack2)) {
            return false;
        }

        if (stack1.getTag() == null && stack2.getTag() != null) {
            return false;
        }

        return stack1.getTag() == null || stack1.getTag().equals(stack2.getTag());
    }
}
