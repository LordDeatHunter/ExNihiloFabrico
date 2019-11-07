package exnihilofabrico.registry

import com.google.gson.reflect.TypeToken
import exnihilofabrico.api.crafting.Lootable
import exnihilofabrico.api.recipes.ToolRecipe
import exnihilofabrico.api.registry.IToolRegistry
import exnihilofabrico.compatibility.modules.MetaModule
import exnihilofabrico.util.isEquivalent
import exnihilofabrico.util.test
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import java.io.File
import java.io.FileReader
import java.util.*
import java.util.function.Predicate

data class ToolRegistry(val registry: MutableList<ToolRecipe> = mutableListOf()):
    AbstractRegistry<MutableList<ToolRecipe>>(), IToolRegistry {

    override fun registerDrops(target: Ingredient, loot: Collection<Lootable>) {
        val match = registry.firstOrNull { target.isEquivalent(it.ingredient) }
        if(match == null)
            registry.add(ToolRecipe(target, loot.toMutableList()))
        else
            match.lootables.addAll(loot)
    }

    override fun isRegistered(target: ItemConvertible) = registry.any { it.ingredient.test(target) }

    override fun getResult(target: ItemConvertible, rand: Random): MutableList<ItemStack> {
        return getAllResults(target)
            .filter { loot-> loot.chance.any {it > rand.nextDouble() }}
            .map{it.stack.copy()}.toMutableList()
    }
    override fun getAllResults(target: ItemConvertible) =
        registry.filter { it.ingredient.test(target) }.map { it.lootables }.flatten()

    override fun registerJson(file: File) {
        val json: MutableList<ToolRecipe> = gson.fromJson(FileReader(file), SERIALIZATION_TYPE)
        json.forEach { registerDrops(it.ingredient, it.lootables) }
    }
    override fun serializable() = registry

    companion object {
        val SERIALIZATION_TYPE = object: TypeToken<MutableList<ToolRecipe>>(){}.type
        fun fromJson(file: File, defaults: (IToolRegistry) -> Unit) = fromJson(file, {ToolRegistry()}, defaults)
    }

}