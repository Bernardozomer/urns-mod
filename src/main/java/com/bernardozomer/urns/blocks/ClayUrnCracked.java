package com.bernardozomer.urns.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClayUrnCracked extends ClayUrn {
    public ClayUrnCracked(Settings settings) {
        super(settings);
    }

    @Override
    public void crack(World world, BlockPos pos) {
        world.breakBlock(pos, false);
    }
}
