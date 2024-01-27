package me.iamnotagenius

import me.iamnotagenius.blocks.MerchantWalletBlock
import me.iamnotagenius.blocks.entities.MerchantWalletBlockEntity
import me.iamnotagenius.items.MerchantWalletItem
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object MerchantsDelight : ModInitializer {
    val logger: Logger = LoggerFactory.getLogger("merchantsdelight")
    private const val MOD_ID = "merchantsdelight"

    lateinit var MERCHANT_BLOCK_ENTITY: BlockEntityType<MerchantWalletBlockEntity>

    private fun registerWallet(name: String, capacity: Int, builder: FabricBlockEntityTypeBuilder<MerchantWalletBlockEntity>, blockSettings: FabricBlockSettings?, itemSettings: FabricItemSettings?) {
        val blockSettings = blockSettings?: FabricBlockSettings.create()
            .strength(0.8f).nonOpaque().sounds(BlockSoundGroup.WOOL)
        val itemSettings = itemSettings?: FabricItemSettings().maxCount(1)

        val block = Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, name), MerchantWalletBlock(blockSettings))
        val item = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, name),
            MerchantWalletItem(block, itemSettings, capacity))
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register { entries -> entries.add(item) }
        builder.addBlock(block)
    }

	override fun onInitialize() {
        val builder = FabricBlockEntityTypeBuilder.create(::MerchantWalletBlockEntity)

        registerWallet("emerald_wallet",    200, builder, null, null)
        registerWallet("merchants_wallet",  400, builder, null, null)
        registerWallet("gilded_wallet",     650, builder, null, null)
        registerWallet("wanderers_wallet",  900, builder, null, null)

        MERCHANT_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(MOD_ID, "merchants_wallet"),
            builder.build())
	}
}
