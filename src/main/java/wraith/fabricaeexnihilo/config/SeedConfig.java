package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.AlwaysSerializedOptionalFieldCodec;
import io.github.mattidragon.configloader.api.GenerateMutable;

//TODO: Probably should remove many of these
@GenerateMutable(useFancyMethodNames = true)
public record SeedConfig(boolean enabled, boolean cactus, boolean chorus, boolean flowerSeeds, boolean grass,
                         boolean kelp, boolean mycelium, boolean netherSpores, boolean seaPickle,
                         boolean sugarCane) implements MutableSeedConfig.Source {
    public static final SeedConfig DEFAULT = new SeedConfig(true, true, true, true, true, true, true, true, true, true);
    public static final Codec<SeedConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL, "enabled", SeedConfig.DEFAULT.enabled).forGetter(SeedConfig::enabled),
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL, "cactus", SeedConfig.DEFAULT.cactus).forGetter(SeedConfig::cactus),
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL, "chorus", SeedConfig.DEFAULT.chorus).forGetter(SeedConfig::chorus),
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL, "flowerSeeds", SeedConfig.DEFAULT.flowerSeeds).forGetter(SeedConfig::flowerSeeds),
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL, "grass", SeedConfig.DEFAULT.grass).forGetter(SeedConfig::grass),
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL, "kelp", SeedConfig.DEFAULT.kelp).forGetter(SeedConfig::kelp),
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL, "mycelium", SeedConfig.DEFAULT.mycelium).forGetter(SeedConfig::mycelium),
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL, "netherSpores", SeedConfig.DEFAULT.netherSpores).forGetter(SeedConfig::netherSpores),
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL, "seaPickle", SeedConfig.DEFAULT.seaPickle).forGetter(SeedConfig::seaPickle),
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL, "sugarCane", SeedConfig.DEFAULT.sugarCane).forGetter(SeedConfig::sugarCane)
    ).apply(instance, SeedConfig::new));
}
