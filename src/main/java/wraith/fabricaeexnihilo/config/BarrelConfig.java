package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.GenerateMutable;

@GenerateMutable(useFancyMethodNames = true)
public record BarrelConfig(double compostRate,
                           boolean bleeding,
                           boolean milking,
                           int leakRadius,
                           int tickRate,
                           boolean efficiency,
                           boolean thorns) implements MutableBarrelConfig.Source {
    public static final BarrelConfig DEFAULT = new BarrelConfig(0.01, true, true, 2, 6, true, true);
    public static final Codec<BarrelConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.optionalFieldOf("compostRate", BarrelConfig.DEFAULT.compostRate).forGetter(BarrelConfig::compostRate),
            Codec.BOOL.optionalFieldOf("bleeding", BarrelConfig.DEFAULT.bleeding).forGetter(BarrelConfig::bleeding),
            Codec.BOOL.optionalFieldOf("milking", BarrelConfig.DEFAULT.milking).forGetter(BarrelConfig::milking),
            Codec.INT.optionalFieldOf("leakRadius", BarrelConfig.DEFAULT.leakRadius).forGetter(BarrelConfig::leakRadius),
            Codec.INT.optionalFieldOf("tickRate", BarrelConfig.DEFAULT.tickRate).forGetter(BarrelConfig::tickRate),
            Codec.BOOL.optionalFieldOf("efficiency", BarrelConfig.DEFAULT.efficiency).forGetter(BarrelConfig::efficiency),
            Codec.BOOL.optionalFieldOf("thorns", BarrelConfig.DEFAULT.thorns).forGetter(BarrelConfig::thorns)
    ).apply(instance, BarrelConfig::new));
}
