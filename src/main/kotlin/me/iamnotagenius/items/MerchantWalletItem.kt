package me.iamnotagenius.items

import me.iamnotagenius.MerchantsDelight
import me.iamnotagenius.blocks.MerchantWalletBlock
import me.iamnotagenius.capacity
import me.iamnotagenius.emeralds
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.enchantment.EnchantmentHelper
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


class MerchantWalletItem(block: Block?, settings: Settings?, val capacity: Int) : BlockItem(block, settings) {
    init {
        (block as? MerchantWalletBlock)?.item = this
    }
    override fun onCraft(stack: ItemStack?, world: World?, player: PlayerEntity?) {
        stack.emeralds = 0
    }

    override fun getItemBarStep(stack: ItemStack?): Int {
        return Math.round(stack.emeralds.toFloat() * 13.0f / stack.capacity.toFloat())
    }

    override fun getItemBarColor(stack: ItemStack?): Int {
        return MathHelper.hsvToRgb(stack.emeralds.toFloat() / stack.capacity / 3.0f, 1.0f, 1.0f)
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

    fun addToWallet(walletStack: ItemStack?, emeraldStack: ItemStack, justOne: Boolean): Boolean {
        if (walletStack?.item !is MerchantWalletItem || !emeraldStack.isOf(Items.EMERALD)) {
            return false
        }

        val amount = if (justOne && emeraldStack.count > 0)
            1 else (walletStack.capacity - walletStack.emeralds).coerceAtMost(emeraldStack.count)

        if (amount > 0) {
            walletStack.emeralds += amount
            emeraldStack.count -= amount
            return true
        }
        return false
    }

    fun removeFromWallet(walletStack: ItemStack?, amount: Int): ItemStack {
        if (walletStack?.item !is MerchantWalletItem) {
            return ItemStack.EMPTY
        }
        val amount = amount.coerceAtMost(walletStack.emeralds)
        walletStack.emeralds -= amount
        return ItemStack(Items.EMERALD, amount)
    }
    fun removeFromWallet(walletStack: ItemStack?, justOne: Boolean): ItemStack {
        return removeFromWallet(
            walletStack,
            if (justOne && walletStack.emeralds > 0) 1 else walletStack.emeralds.coerceAtMost(Items.EMERALD.maxCount)
        )
    }

    companion object {
        public const val EMERALDS_KEY = "Emeralds"
    }
}