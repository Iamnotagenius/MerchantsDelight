package me.iamnotagenius.blocks

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FacingBlock
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.Direction
import net.minecraft.state.property.Properties.HORIZONTAL_FACING as HORIZONTAL_FACING

class LootableWalletBlock(settings: Settings?) : FacingBlock(settings) {
    init {
        defaultState = defaultState.with(HORIZONTAL_FACING, Direction.NORTH)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        builder?.add(HORIZONTAL_FACING)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(HORIZONTAL_FACING, rotation.rotate(state.get(HORIZONTAL_FACING)))
    }

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state.get(HORIZONTAL_FACING)))
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState.with(HORIZONTAL_FACING, ctx.horizontalPlayerFacing)
    }
}