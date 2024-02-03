package me.iamnotagenius

import me.iamnotagenius.items.MerchantWalletItem
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtInt

var ItemStack?.emeralds: Int
    get() = this?.nbt?.getInt(MerchantWalletItem.EMERALDS_KEY)?: 0
    set(value) = this?.setSubNbt(MerchantWalletItem.EMERALDS_KEY, NbtInt.of(value))?: Unit
val ItemStack?.capacity: Int
    get() {
        val itemCapacity = (this?.item as? MerchantWalletItem)?.capacity?: 0
        val pocketDepth = DeepPocketEnchantment.getCapacityMultiplier(this)
        return (itemCapacity * pocketDepth).toInt()
    }