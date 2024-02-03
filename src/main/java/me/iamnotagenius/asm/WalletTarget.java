package me.iamnotagenius.asm;

import me.iamnotagenius.items.MerchantWalletItem;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

public class WalletTarget extends WalletTargetMixin {
    @Override
    public boolean isAcceptableItem(Item other) {
        return other instanceof MerchantWalletItem;
    }
}

@Mixin(EnchantmentTarget.class)
abstract class WalletTargetMixin {
    @Shadow
    abstract boolean isAcceptableItem(Item other);
}
