package me.iamnotagenius.items

import me.iamnotagenius.MerchantsDelight
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtInt
import net.minecraft.screen.slot.Slot
import net.minecraft.util.ClickType
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World

var ItemStack?.emeralds: Int
    get() = this?.nbt?.getInt(MerchantWalletItem.EMERALDS_KEY)?: 0
    set(value) = this?.setSubNbt(MerchantWalletItem.EMERALDS_KEY, NbtInt.of(value))?: Unit

class MerchantWalletItem(block: Block?, settings: Settings?, val capacity: Int) : BlockItem(block, settings) {
    override fun onCraft(stack: ItemStack?, world: World?, player: PlayerEntity?) {
        stack.emeralds = 0
}

    override fun getItemBarStep(stack: ItemStack?): Int {
        return Math.round(stack.emeralds.toFloat() * 13.0f / capacity.toFloat())
    }

    override fun getItemBarColor(stack: ItemStack?): Int {
        return MathHelper.hsvToRgb(stack.emeralds.toFloat() / capacity / 3.0f, 1.0f, 1.0f)
    }

    override fun onClicked(
        stack: ItemStack?,
        otherStack: ItemStack?,
        slot: Slot?,
        clickType: ClickType?,
        player: PlayerEntity?,
        cursorStackReference: StackReference?
    ): Boolean {
        MerchantsDelight.logger.info("onClicked stack: {}, other: {}, clickType: {}", stack, otherStack, clickType)
        if (stack == null || otherStack == null || !otherStack.isOf(Items.EMERALD)) {
            return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference)
        }
        return addToWallet(stack, otherStack, clickType == ClickType.RIGHT)
    }

    override fun onStackClicked(stack: ItemStack?, slot: Slot?, clickType: ClickType?, player: PlayerEntity?): Boolean {
        if (clickType != ClickType.RIGHT || stack.emeralds == 0 || slot?.hasStack() == true) {
            return super.onStackClicked(stack, slot, clickType, player)
        }
        slot?.insertStack(removeFromWallet(stack, false))
        return false
    }

    override fun getPlacementState(context: ItemPlacementContext?): BlockState? {
        return super.getPlacementState(context)
    }

    override fun isItemBarVisible(stack: ItemStack?): Boolean = true

    public fun addToWallet(walletStack: ItemStack?, emeraldStack: ItemStack, justOne: Boolean): Boolean {
        if (walletStack?.item !is MerchantWalletItem || !emeraldStack.isOf(Items.EMERALD)) {
            return false
        }

        val amount = if (justOne && emeraldStack.count > 0)
            1 else (capacity - walletStack.emeralds).coerceAtMost(emeraldStack.count)

        if (amount > 0) {
            walletStack.emeralds += amount
            emeraldStack.count -= amount
            return true
        }
        return false
    }

    public fun removeFromWallet(walletStack: ItemStack?, justOne: Boolean): ItemStack {
        if (walletStack?.item !is MerchantWalletItem) {
            return ItemStack.EMPTY
        }
        val amount = if (justOne && walletStack.emeralds > 0) 1 else walletStack.emeralds.coerceAtMost(Items.EMERALD.maxCount)
        walletStack.emeralds -= amount
        return ItemStack(Items.EMERALD, amount)
    }

    companion object {
        public const val EMERALDS_KEY = "Emeralds"
    }
}