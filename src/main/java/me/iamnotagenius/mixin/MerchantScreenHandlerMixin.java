package me.iamnotagenius.mixin;

import me.iamnotagenius.MerchantsDelight;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantScreenHandler.class)
public abstract class MerchantScreenHandlerMixin extends ScreenHandler {
    protected MerchantScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Inject(method = "switchTo", at = @At("HEAD"))
    private void merchantsdelight$switchTo(int recipeIndex, CallbackInfo ci) {
        MerchantsDelight.INSTANCE.getLogger().info("Switch to {}", recipeIndex);
    }
}
