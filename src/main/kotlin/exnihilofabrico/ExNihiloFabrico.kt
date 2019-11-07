package exnihilofabrico

import com.swordglowsblue.artifice.api.Artifice
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.modules.ModBlocks
import exnihilofabrico.modules.ModFluids
import exnihilofabrico.modules.ModItems
import exnihilofabrico.modules.ModTools
import exnihilofabrico.util.BlockGenerator
import exnihilofabrico.util.ExNihiloItemStack
import io.github.cottonmc.cotton.config.ConfigManager
import io.github.cottonmc.cotton.logging.ModLogger
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.io.File
import java.nio.file.Files


const val MODID: String = "exnihilofabrico"
const val VERSION: String = "0.0a"

// Small helper functions
fun id(name: String) = Identifier(MODID, name)

object ExNihiloFabrico: ModInitializer {
    val ITEM_GROUP = FabricItemGroupBuilder.build(Identifier(MODID, "general")) { ExNihiloItemStack("crook_wood") }
    val LOGGER = ModLogger(MODID)
    val config = ConfigManager.loadConfig(ExNihiloFabricoConfig::class.java)

    override fun onInitialize() {
        // Progmatically generate blocks and items
        LOGGER.info("Generating Blocks/Items")
        BlockGenerator.initRegistryCallBack()

        // Load the early registries that create items/blocks
        ExNihiloRegistries.loadEarlyRegistries()

        /* Register Fluids*/
        LOGGER.info("Registering Fluids")
        ModFluids.registerFluids(Registry.FLUID)
        /* Register Blocks */
        LOGGER.info("Registering Blocks")
        ModBlocks.registerBlocks(Registry.BLOCK)
        /* Register Items */
        LOGGER.info("Registering Items")
        ModBlocks.registerBlockItems(Registry.ITEM)
        ModItems.registerItems(Registry.ITEM)
        ModTools.registerItems(Registry.ITEM)

        /* Register Block Entities */
        LOGGER.info("Registering Block Entities")
        ModBlocks.registerBlockEntities(Registry.BLOCK_ENTITY)

        /* Load the rest of the Ex Nihilo Fabrico registries */
        LOGGER.info("Loading Ex Nihilo Fabrico Registries")
        ExNihiloRegistries.loadRecipeRegistries()

        val dataPack = Artifice.registerData(id("data")) {builder ->
            builder.setDisplayName("Ex Nihilo Fabrico")
            builder.setDescription("Crafting recipes")
            LOGGER.info("Creating Tags")
            generateTags(builder)
            LOGGER.info("Creating Recipes")
            generateRecipes(builder)
        }
    }

    private fun generateRecipes(builder: ArtificeResourcePack.ServerResourcePackBuilder) {
        // Ore Chunk Crafting
        ExNihiloRegistries.ORES.getAll().forEach { ore ->
            builder.addShapedRecipe(id("${ore.getChunkID().path}_crafting")) { ore.generateRecipe(it) }
            if(Registry.ITEM.containsId(ore.getNuggetID())) {
                builder.addSmeltingRecipe(id("${ore.getPieceID().path}_smelting")) { ore.generateNuggetCookingRecipe(it) }
                builder.addBlastingRecipe(id("${ore.getPieceID().path}_blasting")) { ore.generateNuggetCookingRecipe(it) }
            }
            if(Registry.ITEM.containsId(ore.getIngotID())) {
                builder.addSmeltingRecipe(id("${ore.getChunkID().path}_smelting")) { ore.generateIngotCookingRecipe(it) }
                builder.addBlastingRecipe(id("${ore.getChunkID().path}_blasting")) { ore.generateIngotCookingRecipe(it) }
            }
        }
        // Mesh Crafting
        ExNihiloRegistries.MESH.getAll().forEach { mesh -> builder.addShapedRecipe(mesh.identifier) { mesh.generateRecipe(it) } }
        // Mesh Crafting
        ModBlocks.SIEVES.forEach { k, sieve -> builder.addShapedRecipe(k) { sieve.generateRecipe(it) } }
        // Crucible Crafting
        ModBlocks.CRUCIBLES.forEach { k, crucible -> builder.addShapedRecipe(k) { crucible.generateRecipe(it) } }
        // Barrel Crafting
        ModBlocks.BARRELS.forEach { k, barrel -> builder.addShapedRecipe(k) { barrel.generateRecipe(it) } }
    }
    private fun generateTags(builder: ArtificeResourcePack.ServerResourcePackBuilder) {
        // exnihilofabrico:infested_leaves tag
        builder.addBlockTag(id("infested_leaves")) {tag ->
            tag.values(*ModBlocks.INFESTED_LEAVES.keys.toTypedArray())
        }
        builder.addItemTag(id("infested_leaves")) {tag ->
            tag.values(*ModBlocks.INFESTED_LEAVES.keys.toTypedArray())
        }
        ExNihiloRegistries.ORES.getAll().forEach { property ->
            builder.addItemTag(property.getOreID()) {tag ->
                tag.value(property.getChunkID())
            }
        }
    }
}