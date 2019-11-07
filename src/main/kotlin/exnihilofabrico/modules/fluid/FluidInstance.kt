package exnihilofabrico.modules.fluid

import exnihilofabrico.modules.base.NBTSerializable
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.minecraft.fluid.Fluids
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.ExtendedBlockView

/**
 * This is a "temporary" fluid implementation while the Fabric Fluid API debate rages.
 */
class FluidInstance(var fluid: Identifier, var amount: Int, var data: CompoundTag = CompoundTag()): NBTSerializable {

    fun copy() = FluidInstance(fluid, amount, data)
    fun isEmpty() = this == EMPTY || amount <= 0

    override fun toTag(): CompoundTag {
        val tag = CompoundTag()
        tag.putString("fluid", fluid.toString())
        tag.putInt("amount", amount)
        tag.put("data", data)
        return tag
    }

    override fun fromTag(tag: CompoundTag) {
        fluid = Identifier(tag.getString("fluid"))
        amount = tag.getInt("amount")
        if(tag.containsKey("tag"))
            data = tag.getCompound("data")
    }

    override fun toString(): String {
        return "${amount}x${fluid}@${data}"
    }

    fun asFluid() = Registry.FLUID[fluid]

    @Environment(EnvType.CLIENT)
    fun getSprite(view: ExtendedBlockView, pos: BlockPos) =
        FluidRenderHandlerRegistry.INSTANCE.get(asFluid()).getFluidSprites(view, pos, asFluid().defaultState)

    @Environment(EnvType.CLIENT)
    fun getColor(view: ExtendedBlockView, pos: BlockPos) =
        FluidRenderHandlerRegistry.INSTANCE.get(asFluid()).getFluidColor(view, pos, asFluid().defaultState)

    companion object {
        val EMPTY = FluidInstance(Registry.FLUID.getId(Fluids.EMPTY),0)
        val BUCKET_AMOUNT = 1000

        fun create(tag: CompoundTag?): FluidInstance {
            val instance = create()
            if(tag != null) {
                instance.fromTag(tag)
            }
            return instance
        }

        fun create() = FluidInstance(Registry.FLUID.getId(Fluids.EMPTY),0)
    }
}