package me.iamnotagenius

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.ItemStack

class DeepPocketEnchantment() : Enchantment(Rarity.RARE, MerchantsDelight.WALLET_TARGET, arrayOf()) {
    override fun getMaxLevel(): Int = 2
    override fun isTreasure(): Boolean = true
    override fun isAvailableForEnchantedBookOffer(): Boolean = true

    companion object {
        public fun getCapacityMultiplier(level: Int): Float = multipliers[level] ?: 1f
        public fun getCapacityMultiplier(stack: ItemStack?): Float {
            return multipliers[EnchantmentHelper.getLevel(MerchantsDelight.DEEP_POCKET, stack)] ?: 1f
        }
        private val multipliers = mapOf(
            1 to 2f,
            2 to 3f
        )
    }
}