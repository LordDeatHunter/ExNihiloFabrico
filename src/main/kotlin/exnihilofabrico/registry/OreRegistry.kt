package exnihilofabrico.registry

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import exnihilofabrico.ExNihiloFabrico
import exnihilofabrico.api.registry.IOreRegistry
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.modules.ore.OreChunkItem
import exnihilofabrico.modules.ore.OrePieceItem
import exnihilofabrico.modules.ore.OreProperties
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import java.io.File
import java.io.FileReader
import java.io.FileWriter

data class OreRegistry(val registry: MutableList<OreProperties> = mutableListOf()): AbstractRegistry<MutableList<OreProperties>>(), IOreRegistry {

    val item_settings: Item.Settings = Item.Settings().group(ExNihiloFabrico.ITEM_GROUP).maxCount(64)

    override fun clear() = registry.clear()
    override fun getAll() = registry
    override fun register(vararg properties: OreProperties) = registry.addAll(properties)

    override fun registerPieceItems(itemRegistry: Registry<Item>) =
        registry.forEach { Registry.register(itemRegistry, it.getPieceID(), OrePieceItem(it, item_settings)) }

    override fun registerChunkItems(itemRegistry: Registry<Item>) =
        registry.forEach { Registry.register(itemRegistry, it.getChunkID(), OreChunkItem(it, item_settings)) }

    override fun getPropertiesForModel(identifier: ModelIdentifier): OreProperties? =
        registry.firstOrNull { it.getChunkID().path == identifier.path || it.getPieceID().path == identifier.path }

    override fun serializable() = registry
    override fun registerJson(file: File) {
        if(file.exists()){
            val json: MutableList<OreProperties> = gson.fromJson(FileReader(file), SERIALIZATION_TYPE)
            json.forEach { register(it) }
        }
    }

    companion object {
        val SERIALIZATION_TYPE = object : TypeToken<MutableList<OreProperties>>() {}.type
        fun fromJson(file: File) = fromJson(file, {OreRegistry()}, MetaModule::registerOres)
    }
}