package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.GenerateMutable;

//TODO: Probably should remove many of these
@GenerateMutable(useFancyMethodNames = true)
public record SeedConfig(boolean enabled, boolean cactus, boolean chorus, boolean flowerSeeds, boolean grass,
                         boolean kelp, boolean mycelium, boolean netherSpores, boolean seaPickle,
                         boolean sugarCane) implements MutableSeedConfig.Source {
    public static final SeedConfig DEFAULT = new SeedConfig(true, true, true, true, true, true, true, true, true, true);
    public static final Codec<SeedConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("enabled", SeedConfig.DEFAULT.enabled).forGetter(SeedConfig::enabled),
            Codec.BOOL.optionalFieldOf("cactus", SeedConfig.DEFAULT.cactus).forGetter(SeedConfig::cactus),
            Codec.BOOL.optionalFieldOf("chorus", SeedConfig.DEFAULT.chorus).forGetter(SeedConfig::chorus),
            Codec.BOOL.optionalFieldOf("flowerSeeds", SeedConfig.DEFAULT.flowerSeeds).forGetter(SeedConfig::flowerSeeds),
            Codec.BOOL.optionalFieldOf("grass", SeedConfig.DEFAULT.grass).forGetter(SeedConfig::grass),
            Codec.BOOL.optionalFieldOf("kelp", SeedConfig.DEFAULT.kelp).forGetter(SeedConfig::kelp),
            Codec.BOOL.optionalFieldOf("mycelium", SeedConfig.DEFAULT.mycelium).forGetter(SeedConfig::mycelium),
            Codec.BOOL.optionalFieldOf("netherSpores", SeedConfig.DEFAULT.netherSpores).forGetter(SeedConfig::netherSpores),
            Codec.BOOL.optionalFieldOf("seaPickle", SeedConfig.DEFAULT.seaPickle).forGetter(SeedConfig::seaPickle),
            Codec.BOOL.optionalFieldOf("sugarCane", SeedConfig.DEFAULT.sugarCane).forGetter(SeedConfig::sugarCane)
    ).apply(instance, SeedConfig::new));
}
