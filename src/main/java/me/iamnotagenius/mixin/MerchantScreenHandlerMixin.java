package me.iamnotagenius.mixin;

import me.iamnotagenius.MerchantsDelight;
import me.iamnotagenius.items.MerchantWalletItem;
import net.minecraft.item.Items;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.village.MerchantInventory;
import net.minecraft.village.TradeOfferList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantScreenHandler.class)
public abstract class MerchantScreenHandlerMixin extends ScreenHandler {
    @Shadow @Final
    private MerchantInventory merchantInventory;

    @Shadow public abstract TradeOfferList getRecipes();

    protected MerchantScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Inject(method = "switchTo", at = @At("RETURN"))
    private void merchantsdelight$switchTo(int recipeIndex, CallbackInfo ci) {
        MerchantsDelight.INSTANCE.getLogger().info("Switch to {}", recipeIndex);
        var offer = getRecipes().get(recipeIndex);
        if (offer.getAdjustedFirstBuyItem().isOf(Items.EMERALD)) {
            merchantsdelight$fillFromWallets(0);
        }
        if (offer.getSecondBuyItem().isOf(Items.EMERALD)) {
            merchantsdelight$fillFromWallets(1);
        }
    }

    @Unique
    private void merchantsdelight$fillFromWallets(int slot) {
        var itemStack = merchantInventory.getStack(slot);
        if (!itemStack.isEmpty() && !itemStack.isOf(Items.EMERALD)) {
            return;
        }
        for (int i = 3; i < 39; ++i) {
            var walletStack = slots.get(i).getStack();
            if (walletStack.getItem() instanceof MerchantWalletItem walletItem) {
                var remainder = walletItem.removeFromWallet(walletStack, Items.EMERALD.getMaxCount() - itemStack.getCount());
                if (remainder.getCount() > 0) {
                    if (itemStack.isEmpty()) {
                        merchantInventory.setStack(slot, remainder);
                        itemStack = remainder;
                    }
                    else if (itemStack.isOf(Items.EMERALD)) {
                        itemStack.setCount(itemStack.getCount() + remainder.getCount());
                    }
                }
                if (merchantInventory.getStack(slot).getCount() >= Items.EMERALD.getMaxCount()) {
                    break;
                }
            }
        }
    }
}
