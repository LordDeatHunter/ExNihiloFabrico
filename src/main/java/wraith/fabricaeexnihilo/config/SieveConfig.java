package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.GenerateMutable;

@GenerateMutable(useFancyMethodNames = true)
public record SieveConfig(double baseProgress, boolean efficiency, double efficiencyScaleFactor,
                          boolean fortune, boolean haste, double hasteScaleFactor, int meshStackSize,
                          int sieveRadius) implements MutableSieveConfig.Source {
    public static final SieveConfig DEFAULT = new SieveConfig(0.1, true, 0.05, true, true, 1.0, 16, 2);
    public static final Codec<SieveConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.optionalFieldOf("baseProgress", SieveConfig.DEFAULT.baseProgress).forGetter(SieveConfig::baseProgress),
            Codec.BOOL.optionalFieldOf("efficiency", SieveConfig.DEFAULT.efficiency).forGetter(SieveConfig::efficiency),
            Codec.DOUBLE.optionalFieldOf("efficiencyScaleFactor", SieveConfig.DEFAULT.efficiencyScaleFactor).forGetter(SieveConfig::efficiencyScaleFactor),
            Codec.BOOL.optionalFieldOf("fortune", SieveConfig.DEFAULT.fortune).forGetter(SieveConfig::fortune),
            Codec.BOOL.optionalFieldOf("haste", SieveConfig.DEFAULT.haste).forGetter(SieveConfig::haste),
            Codec.DOUBLE.optionalFieldOf("hasteScaleFactor", SieveConfig.DEFAULT.hasteScaleFactor).forGetter(SieveConfig::hasteScaleFactor),
            Codec.INT.optionalFieldOf("meshStackSize", SieveConfig.DEFAULT.meshStackSize).forGetter(SieveConfig::meshStackSize),
            Codec.INT.optionalFieldOf("sieveRadius", SieveConfig.DEFAULT.sieveRadius).forGetter(SieveConfig::sieveRadius)
    ).apply(instance, SieveConfig::new));
}
