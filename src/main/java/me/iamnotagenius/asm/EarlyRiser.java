package me.iamnotagenius.asm;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;

public class EarlyRiser implements Runnable {
    @Override
    public void run() {
        var resolver = FabricLoader.getInstance().getMappingResolver();
        var className = resolver.mapClassName("intermediary", "net.minecraft.class_1886");
        ClassTinkerers.enumBuilder(className)
                .addEnumSubclass("MD$WALLET", "me.iamnotagenius.asm.WalletTarget")
                .build();
    }
}
