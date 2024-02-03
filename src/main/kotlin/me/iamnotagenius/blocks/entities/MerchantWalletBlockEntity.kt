package me.iamnotagenius.blocks.entities

import me.iamnotagenius.MerchantsDelight
import me.iamnotagenius.items.MerchantWalletItem
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos

class MerchantWalletBlockEntity(pos: BlockPos?, state: BlockState?, var amount: Int = 0, var pocket_depth: Int = 0) :
    BlockEntity(MerchantsDelight.MERCHANT_BLOCK_ENTITY, pos, state) {

    override fun writeNbt(nbt: NbtCompound?) {
        nbt?.putInt(MerchantWalletItem.EMERALDS_KEY, amount)
        nbt?.putInt(POCKET_DEPTH_KEY, pocket_depth)
    }

    override fun readNbt(nbt: NbtCompound?) {
        amount = nbt?.getInt(MerchantWalletItem.EMERALDS_KEY)?: 0
        pocket_depth = nbt?.getInt(POCKET_DEPTH_KEY)?: 0
    }

    companion object {
        private val POCKET_DEPTH_KEY = "PocketDepth"
    }
}