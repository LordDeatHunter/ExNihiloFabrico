package wraith.fabricaeexnihilo.api;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import wraith.fabricaeexnihilo.util.Color;
import wraith.fabricaeexnihilo.util.Lazy;

import java.util.Arrays;

/**
 * Interface that contains methods that allow registering content for FEN internals. All methods are free to be called.
 * Do not extend!
 *
 * @see FENApiModule
 */
@ApiStatus.NonExtendable
public interface FENRegistries {

    /**
     * Register a new type of barrel. It will be registered under {@code fabricaeexnihilo:<name>_barrel}. You will have
     * to manually provide all assets. The model {@code fabricaeexnihilo:block/barrel} can be extended for convenience.
     * If another api module has already registered a barrel with the same name, it will be returned instead.
     * The created block will not be registered with minecraft until after all api modules have run.
     *
     * @param name        The name of the barrel.
     * @param isFireproof Whether it should be fireproof.
     * @param settings    Block settings for the barrel.
     */
    //TODO: enchantability option
    Block registerBarrel(String name, boolean isFireproof, AbstractBlock.Settings settings);

    /**
     * Register a new type of crucible. It will be registered under {@code fabricaeexnihilo:<name>_crucible}. You will
     * have to manually provide all assets. The model {@code fabricaeexnihilo:block/crucible} can be extended for
     * convenience. If another has api module already registered a crucible with the same name, it will be returned instead.
     * The created block will not be registered with minecraft until after all api modules have run.
     *
     * @param name        The name of the crucible.
     * @param isFireproof Whether it should be fireproof.
     * @param settings    Block settings for the crucible.
     */
    //TODO: enchantability option
    Block registerCrucible(String name, boolean isFireproof, AbstractBlock.Settings settings);

    /**
     * Register a new crushed block. It will be registered under {@code fabricaeexnihilo:crushed_<name>}. You will have
     * to manually provide all assets.
     * If another api module has already registered a crushed block with the same name, it will be returned instead.
     * The created block will not be registered with minecraft until after all api modules have run.
     *
     * @param name     The base name of the crushed block.
     * @param settings Block settings for the crushed block.
     */
    Block registerCrushedBlock(String name, AbstractBlock.Settings settings);

    /**
     * Register a new type of infested leaves. It will be registered under
     * {@code fabricaeexnihilo:infested_<name>_leaves}. You will have to manually provide all assets, but you can reuse
     * the standard leaves model. The tint index of 0 is provided as white.
     * If another api module has already registered an infested leaves block with the same name, it will be returned instead.
     * The created block will not be registered with minecraft until after all api modules have run.
     *
     * @param name     The name of the wood type.
     * @param source   The source of the transformation. Used in code.
     * @param settings Block settings for the leaves
     */
    Block registerInfestedLeaves(String name, Identifier source, AbstractBlock.Settings settings);

    /**
     * Register a new type of sieve. It will be registered under {@code fabricaeexnihilo:<name>_sieve}. You will have to
     * manually provide all assets. The model {@code fabricaeexnihilo:block/sieve} can be extended for convenience.
     * If another api module has already registered a sieve with the same name, it will be returned instead.
     * The created block will not be registered with minecraft until after all api modules have run.
     *
     * @param name        The name of the sieve.
     * @param isFireproof Whether it should be fireproof.
     * @param settings    Block settings for the sieve.
     */
    //TODO: enchantability option
    Block registerSieve(String name, boolean isFireproof, AbstractBlock.Settings settings);

    /**
     * Register a new type of strainer. It will be registered under {@code fabricaeexnihilo:<name>_strainer}. You will
     * have to manually provide all assets. The model {@code fabricaeexnihilo:block/strainer} can be extended for
     * convenience.
     * If another api module has already registered a strainer with the same name, it will be returned instead.
     * The created block will not be registered with minecraft until after all api modules have run.
     *
     * @param name        The name of the strainer.
     * @param isFireproof Whether it should be fireproof.
     * @param settings    Block settings for the strainer.
     */
    Block registerStrainer(String name, boolean isFireproof, AbstractBlock.Settings settings);

    /**
     * Register a new type of mesh piece. It will be registered under {@code fabricaeexnihilo:<name>_mesh}. You will
     * have to manually provide all assets. The model {@code fabricaeexnihilo:item/mesh} can be directly extended.
     * (tint index 1 will be tinted based on {@code color}).
     * If another api module has already registered a mesh with the same name, it will be returned instead.
     * The created item will not be registered with minecraft until after all api modules have run.
     *
     * @param name           The name of the mesh.
     * @param color          The color it will be tinted.
     * @param enchantability The enchantability of the mesh. You can use vanilla tool materials as a reference.
     * @param settings       Item settings for the mesh.
     */
    Item registerMesh(String name, Color color, int enchantability, Item.Settings settings);

    /**
     * Register a new type of ore piece. It will be registered under {@code fabricaeexnihilo:<name>_piece}. You will
     * have to manually provide all assets. (layer1 will be tinted based on {@code color}).
     * If another api module has already registered an ore piece with the same name, it will be returned instead.
     * The created item will not be registered with minecraft until after all api modules have run.
     *
     * @param name     The name of the ore.
     * @param settings Item settings for the piece.
     */
    Item registerOrePiece(String name, Item.Settings settings);

    /**
     * Register a new type of seed that creates normal plants. It will be registered under
     * {@code fabricaeexnihilo:<name>_seeds}. You will have to manually provide all assets.
     * If another api module has already registered a seed with the same name, it will be returned instead.
     * The created item will not be registered with minecraft until after all api modules have run.
     *
     * @param name   The name of the seeds.
     * @param plants A lazy that resolves to the possible results.
     * @see #registerSeed(String, Identifier...)
     */
    Item registerSeed(String name, Lazy<Block[]> plants);

    /**
     * Register a new type of seed that creates normal plants. It will be registered under
     * {@code fabricaeexnihilo:<name>_seeds}. You will have to manually provide all assets.
     * If another api module has already registered a seed with the same name, it will be returned instead.
     * The created item will not be registered with minecraft until after all api modules have run.
     *
     * @param name   The name of the seeds.
     * @param plants Ids that resolve to possible results.
     * @see #registerSeed(String, Identifier...)
     */
    default Item registerSeed(String name, Identifier... plants) {
        return registerSeed(name, new Lazy<>(() -> Arrays.stream(plants)
                .map(Registries.BLOCK::get)
                .toArray(Block[]::new)));
    }

    /**
     * Register a new type of seed that creates tall plants. It will be registered under
     * {@code fabricaeexnihilo:<name>_seeds}. You will have to manually provide all assets.
     * If another api module has already registered a seed with the same name, it will be returned instead.
     * The created item will not be registered with minecraft until after all api modules have run.
     *
     * @param name   The name of the seeds.
     * @param plants A lazy that resolves to the possible results.
     * @see #registerTallPlantSeed(String, Identifier...)
     */
    Item registerTallPlantSeed(String name, Lazy<TallPlantBlock[]> plants);

    /**
     * Register a new type of seed that creates tall plants. It will be registered under
     * {@code fabricaeexnihilo:<name>_seeds}. You will have to manually provide all assets.
     * If another api module has already registered a seed with the same name, it will be returned instead.
     * The created item will not be registered with minecraft until after all api modules have run.
     *
     * @param name   The name of the seeds.
     * @param plants Ids that resolve to possible results.
     * @see #registerTallPlantSeed(String, Identifier...)
     */
    default Item registerTallPlantSeed(String name, Identifier... plants) {
        return registerTallPlantSeed(name, new Lazy<>(() -> Arrays.stream(plants)
                .map(Registries.BLOCK::get)
                .map(block -> (TallPlantBlock) block)
                .toArray(TallPlantBlock[]::new)));
    }

    /**
     * Register a new type of seed that transforms one block into another. It will be registered under
     * {@code fabricaeexnihilo:<name>_seeds}. You will have to manually provide all assets.
     * If another api module has already registered a seed with the same name, it will be returned instead.
     * The created item will not be registered with minecraft until after all api modules have run.
     *
     * @param name The name of the seeds.
     * @param from A lazy that resolves to the source block.
     * @param to   A lazy that resolves to the result block.
     * @see #registerTransformingSeed(String, Identifier, Identifier)
     */
    Item registerTransformingSeed(String name, Lazy<Block> from, Lazy<Block> to);

    /**
     * Register a new type of seed that transforms one block into another. It will be registered under
     * {@code fabricaeexnihilo:<name>_seeds}. You will have to manually provide all assets.
     * If another api module has already registered a seed with the same name, it will be returned instead.
     * The created item will not be registered with minecraft until after all api modules have run.
     *
     * @param name The name of the seeds.
     * @param from An id that points to the source block.
     * @param to   An id that points to the result block.
     * @see #registerTransformingSeed(String, Lazy, Lazy)
     */
    default Item registerTransformingSeed(String name, Identifier from, Identifier to) {
        return registerTransformingSeed(name, new Lazy<>(() -> Registries.BLOCK.get(from)), new Lazy<>(() -> Registries.BLOCK.get(to)));
    }

    /**
     * Registers a new type of wood block by calling all the relevant methods. You will need an additional call to
     * {@link #registerInfestedLeaves} and {@link #registerSeed} to register the corresponding leaves and seed
     * respectively.
     * If another api module has already registered a seed with the same name, it will be returned instead.
     * The created item will not be registered with minecraft until after all api modules have run.
     *
     * @param name      The name of the wood type. Will be directly passed into the called methods.
     * @param fireproof Whether the wood is fireproof or not. Will be directly passed into the called methods.
     * @see #registerBarrel
     * @see #registerCrucible
     * @see #registerSieve
     * @see #registerStrainer
     * @return An object with methods to access all the return values of the called methods.
     */
    WoodenBlockBundle registerWood(String name, boolean fireproof, AbstractBlock.Settings settings);

    /**
     * Provides default item settings that all FEN items are based on.
     */
    Item.Settings defaultItemSettings();

    /**
     * Provides wooden block settings that all wooden FEN blocks are based on.
     */
    AbstractBlock.Settings woodenBlockSettings();

    /**
     * Provides stone block settings that all stone FEN blocks are based on.
     */
    AbstractBlock.Settings stoneBlockSettings();

    /**
     * Provides sandy block settings that all sand-like FEN blocks use.
     */
    AbstractBlock.Settings sandyBlockSettings();

    /**
     * Provides gravely block settings that all gravel-like FEN blocks use.
     */
    AbstractBlock.Settings gravelyBlockSettings();

    /**
     * Provides infested leaves block settings that all infested leaves are based on.
     */
    AbstractBlock.Settings infestedLeavesBlockSettings();

    /**
     * A container for blocks of a wood type. Used as return value of {@link #registerWood}
     */
    @ApiStatus.NonExtendable
    interface WoodenBlockBundle {
        Block sieve();
        Block strainer();
        Block barrel();
        Block crucible();
    }
}
