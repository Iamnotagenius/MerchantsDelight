package me.iamnotagenius.mixin;

import me.iamnotagenius.MerchantsDelight;
import me.iamnotagenius.items.MerchantWalletItem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

	@Shadow @Final
	public DefaultedList<ItemStack> main;
	@Shadow @Final
	public DefaultedList<ItemStack> offHand;

	@Inject(at = @At("HEAD"), method = "insertStack(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
	private void init(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
		if (!itemStack.isOf(Items.EMERALD)) {
			return;
		}
		MerchantsDelight.INSTANCE.getLogger().info("Picked up");
		var walletStackOptional = Optional.of(offHand.get(0))
				.filter(PlayerInventoryMixin::isWalletStack)
				.or(() -> main.stream().filter(PlayerInventoryMixin::isWalletStack).findFirst());
		if (walletStackOptional.isEmpty()) {
			return;
		}
		var walletStack = walletStackOptional.get();
		var item = (MerchantWalletItem)walletStack.getItem();
		if (item.addToWallet(walletStack, itemStack, false)) {
			cir.setReturnValue(true);
		}
	}
	
	@Unique
	private static boolean isWalletStack(ItemStack itemStack) {
		return itemStack.getItem() instanceof MerchantWalletItem;
	}
}