package me.iamnotagenius.blocks

import me.iamnotagenius.DeepPocketEnchantment
import me.iamnotagenius.MerchantsDelight
import me.iamnotagenius.blocks.entities.MerchantWalletBlockEntity
import me.iamnotagenius.emeralds
import me.iamnotagenius.items.MerchantWalletItem
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.state.StateManager
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.state.property.Properties.HORIZONTAL_FACING as HORIZONTAL_FACING

class MerchantWalletBlock(settings: Settings) : BlockWithEntity(settings), BlockEntityProvider {
    lateinit var item: MerchantWalletItem
    init {
        defaultState = defaultState.with(HORIZONTAL_FACING, Direction.NORTH)
    }


    fun getCapacity(blockEntity: MerchantWalletBlockEntity): Int {
        return (item.capacity * DeepPocketEnchantment.getCapacityMultiplier(blockEntity.pocket_depth).toInt())
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        builder?.add(HORIZONTAL_FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
        return super.getPlacementState(ctx)?.with(HORIZONTAL_FACING, ctx?.horizontalPlayerFacing?.opposite)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        val blockEntity = world.getBlockEntity(pos) as? MerchantWalletBlockEntity ?: return ActionResult.FAIL
        val activeStack = player.inventory.mainHandStack
        if (activeStack.isEmpty) {
            val amount = blockEntity.amount.coerceAtMost(Items.EMERALD.maxCount)
            blockEntity.amount -= amount
            player.inventory.setStack(player.inventory.selectedSlot, ItemStack(Items.EMERALD, amount))
            return ActionResult.SUCCESS
        }
        else if (activeStack.isOf(Items.EMERALD)) {
            val amount = (getCapacity(blockEntity) - blockEntity.amount).coerceAtMost(activeStack.count)
            if (amount > 0) {
                blockEntity.amount += amount
                activeStack.count -= amount
                return ActionResult.SUCCESS
            }
        }

        return ActionResult.PASS
    }

    override fun getRenderType(state: BlockState?): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return VoxelShapes.cuboid(0.25, 0.0, 0.25, 0.75, 1.0, 0.75)
    }

    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity {
        return MerchantWalletBlockEntity(pos, state)
    }

    override fun onPlaced(
        world: World?,
        pos: BlockPos?,
        state: BlockState?,
        placer: LivingEntity?,
        itemStack: ItemStack?
    ) {
        val blockEntity = world?.getBlockEntity(pos) as? MerchantWalletBlockEntity
        if (blockEntity == null || pos == null) {
            super.onPlaced(world, pos, state, placer, itemStack)
            return
        }
        blockEntity.amount = itemStack.emeralds
        blockEntity.pocket_depth = EnchantmentHelper.getLevel(MerchantsDelight.DEEP_POCKET, itemStack)
        super.onPlaced(world, pos, state, placer, itemStack)
    }

    override fun onBreak(world: World?, pos: BlockPos?, state: BlockState?, player: PlayerEntity?) {
        val blockEntity = world?.getBlockEntity(pos) as? MerchantWalletBlockEntity
        if (blockEntity == null || world.isClient || player?.isCreative() == true || pos == null) {
            super.onBreak(world, pos, state, player)
            return
        }
        val itemStack = ItemStack(item)
        itemStack.emeralds = blockEntity.amount
        if (blockEntity.pocket_depth > 0) {
            EnchantmentHelper.set(mapOf(MerchantsDelight.DEEP_POCKET to blockEntity.pocket_depth), itemStack)
        }
        val itemEntity = ItemEntity(
            world,
            pos.x.toDouble() + 0.5,
            pos.y.toDouble() + 0.5,
            pos.z.toDouble() + 0.5,
            itemStack
        )
        itemEntity.setToDefaultPickupDelay()
        world.spawnEntity(itemEntity)
        super.onBreak(world, pos, state, player)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(HORIZONTAL_FACING, rotation.rotate(state.get(HORIZONTAL_FACING)))
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state.get(HORIZONTAL_FACING)))
    }
}