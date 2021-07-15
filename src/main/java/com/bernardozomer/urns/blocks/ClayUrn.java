package com.bernardozomer.urns.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class ClayUrn extends FallingBlock implements BlockEntityProvider {
    private static final BlockSoundGroup BLOCK_SOUND_GROUP = new BlockSoundGroup(
            1f, 1f,
            SoundEvents.BLOCK_GLASS_BREAK,
            SoundEvents.BLOCK_BONE_BLOCK_STEP,
            SoundEvents.BLOCK_BONE_BLOCK_PLACE,
            SoundEvents.BLOCK_BONE_BLOCK_HIT,
            SoundEvents.BLOCK_BONE_BLOCK_FALL
    );
    private static final SoundEvent CRACK_SOUND = SoundEvents.BLOCK_NETHER_BRICKS_BREAK;
    private static final int MIN_CRACKING_HEIGHT = 3; // in blocks.
    private static final Block[] PREVENT_CRACKING = new Block[] {
            Blocks.HAY_BLOCK,
            Blocks.SLIME_BLOCK
    };
    private final VoxelShape SHAPE;

    public ClayUrn(Settings settings) {
        super(settings);
        this.SHAPE = this.generateShape();
    }

    /**
     * @return The block sound group.
     */
    public static BlockSoundGroup getBlockSoundGroup() {
        return ClayUrn.BLOCK_SOUND_GROUP;
    }

    /**
     * @return The minimum height for the block to crack.
     */
    public static int getMinCrackingHeight() {
        return ClayUrn.MIN_CRACKING_HEIGHT;
    }

    /**
     * @return An array of every block that prevents cracking when landed on.
     */
    public static Block[] getPreventCracking() {
        return ClayUrn.PREVENT_CRACKING;
    }

    /**
     * Handles cracking.
     * @param world      The world the block is in.
     * @param pos        The block position.
     */
    public abstract void crack(World world, BlockPos pos);

    /**
     * Attempts to transfer exactly one item from the player's hand stack to the block's inventory
     * when they interact with it.
     * @param state  The current block state.
     * @param world  The world the block is in.
     * @param pos    The block position.
     * @param player The player who is interacting with the block.
     * @param hand   The player's hand.
     * @param hit    Information about how the player hit the block.
     * @return       ActionResult.SUCCESS
     */
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
                              Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            ClayUrnBlockEntity blockEntity = (ClayUrnBlockEntity) world.getBlockEntity(pos);
            assert blockEntity != null;

            ItemStack playerStack = player.getMainHandStack();

            if (!playerStack.isEmpty()) {
                if (blockEntity.receive(playerStack, 0, 1) == ActionResult.SUCCESS) {
                    if (!player.isCreative()) {
                        playerStack.decrement(1);
                    }

                    world.playSound(
                            null,
                            pos,
                            ClayUrn.getBlockSoundGroup().getPlaceSound(),
                            SoundCategory.BLOCKS,
                            1f,
                            1f
                    );
                }
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof ClayUrnBlockEntity) {
                ItemScatterer.spawn(world, pos, (ClayUrnBlockEntity) blockEntity);
                world.updateComparators(pos,this);
                world.removeBlockEntity(pos);
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (canFallThrough(world.getBlockState(pos.down())) && pos.getY() >= 0) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (!(blockEntity instanceof ClayUrnBlockEntity)) {
                return;
            }

            // Configure and spawn the falling block entity.
            FallingBlockEntity fallingBlockEntity = new ClayUrnFallingBlockEntity(
                    world,(double)pos.getX() + 0.5D,
                    pos.getY(), (double)pos.getZ() + 0.5D,
                    world.getBlockState(pos),
                    // Set the falling block's inventory to the block entity's.
                    ((ClayUrnBlockEntity) blockEntity).getItems()
            );

            this.configureFallingBlockEntity(fallingBlockEntity);
            world.spawnEntity(fallingBlockEntity);

            world.removeBlockEntity(pos);
        }
    }

    @Override
    public void onLanding(World world, BlockPos pos, BlockState fallingBlockState,
                          BlockState currentStateInPos, FallingBlockEntity fallingBlockEntity) {
        if (!(fallingBlockEntity instanceof ClayUrnFallingBlockEntity fallingClayUrn)) {
            return;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ClayUrnBlockEntity clayUrnBlockEntity) {
            (clayUrnBlockEntity).setItems(fallingClayUrn.getItems());
        }

        if (fallingClayUrn.getOriginY() - pos.getY() >= ClayUrn.getMinCrackingHeight()
                && !Arrays.asList(ClayUrn.getPreventCracking()).contains(world.getBlockState(pos.down()).getBlock())) {
            crack(world, pos);
        }

        world.updateComparators(pos, this);

        if (!fallingBlockEntity.isSilent()) {
            world.playSound(
                    null,
                    pos,
                    soundGroup.getPlaceSound(),
                    SoundCategory.BLOCKS,
                    1f,
                    1f
            );
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ClayUrnBlockEntity(pos, state);
    }

    @Override
    protected void configureFallingBlockEntity(FallingBlockEntity entity) {
        entity.setHurtEntities(2.0f, 40);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    /**
     * @return The sound effect that is played whenever the block is cracked.
     */
    public SoundEvent getCrackSound() {
        return CRACK_SOUND;
    }

    /**
     * Generates the block's custom model.
     * @return The block's model.
     */
    protected VoxelShape generateShape()
    {
        List<VoxelShape> shapes = new ArrayList<>();
        // Base
        shapes.add(VoxelShapes.cuboid(0, 0, 0, 1, 0.75, 1));
        // Top
        shapes.add(VoxelShapes.cuboid(0.125, 0.75, 0.125, 0.875, 1, 0.875));

        VoxelShape result = VoxelShapes.empty();
        for(VoxelShape shape : shapes)
        {
            result = VoxelShapes.combine(result, shape, BooleanBiFunction.OR);
        }
        return result.simplify();
    }
}
