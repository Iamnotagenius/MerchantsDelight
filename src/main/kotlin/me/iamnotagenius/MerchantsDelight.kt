package me.iamnotagenius

import me.iamnotagenius.blocks.MerchantWalletBlock
import me.iamnotagenius.blocks.entities.MerchantWalletBlockEntity
import me.iamnotagenius.items.MerchantWalletItem
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object MerchantsDelight : ModInitializer {
    public val logger = LoggerFactory.getLogger("merchantsdelight")
    public val MOD_ID = "merchantsdelight"

    public val MERCHANT_WALLET_BLOCK = MerchantWalletBlock(200, FabricBlockSettings.create()
        .strength(2.0f).nonOpaque().sounds(BlockSoundGroup.WOOL))

    public val MERCHANT_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
        Identifier.of(MOD_ID, "merchants_wallet"),
        FabricBlockEntityTypeBuilder.create(::MerchantWalletBlockEntity, MERCHANT_WALLET_BLOCK).build())

    public val MERCHANT_WALLET_ITEM = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "merchants_wallet"),
        MerchantWalletItem(MERCHANT_WALLET_BLOCK, FabricItemSettings().maxCount(1), 200))

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")

        Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, "merchants_wallet"), MERCHANT_WALLET_BLOCK)
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register { entries -> entries.add(MERCHANT_WALLET_ITEM) }
	}
}
