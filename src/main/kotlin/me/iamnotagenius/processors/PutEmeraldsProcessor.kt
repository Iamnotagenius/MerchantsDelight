package me.iamnotagenius.processors

import com.mojang.serialization.Codec
import me.iamnotagenius.blocks.entities.MerchantWalletBlockEntity
import net.minecraft.structure.StructurePlacementData
import net.minecraft.structure.StructureTemplate
import net.minecraft.structure.processor.StructureProcessor
import net.minecraft.structure.processor.StructureProcessorType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.intprovider.IntProvider
import net.minecraft.world.WorldView

class PutEmeraldsProcessor(private val amountProvider: IntProvider) : StructureProcessor() {
    override fun getType(): StructureProcessorType<PutEmeraldsProcessor> = TYPE

    override fun process(
        world: WorldView?,
        pos: BlockPos?,
        pivot: BlockPos?,
        originalBlockInfo: StructureTemplate.StructureBlockInfo?,
        currentBlockInfo: StructureTemplate.StructureBlockInfo?,
        data: StructurePlacementData?
    ): StructureTemplate.StructureBlockInfo? {

        val blockEntity = (world?.getBlockEntity(currentBlockInfo?.pos) as? MerchantWalletBlockEntity)?:
            return currentBlockInfo
        val random = data?.getRandom(currentBlockInfo?.pos)
        blockEntity.amount = amountProvider.get(random)
        return currentBlockInfo
    }

    companion object {
        val CODEC: Codec<PutEmeraldsProcessor> =
                IntProvider.NON_NEGATIVE_CODEC.fieldOf("amount")
                    .xmap(::PutEmeraldsProcessor) { it.amountProvider }
                    .codec()
        val TYPE = StructureProcessorType { CODEC }
    }
}