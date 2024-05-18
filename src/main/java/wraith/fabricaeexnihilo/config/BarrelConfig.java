package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.AlwaysSerializedOptionalFieldCodec;
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
            AlwaysSerializedOptionalFieldCodec.create(Codec.DOUBLE, "compostRate", BarrelConfig.DEFAULT.compostRate).forGetter(BarrelConfig::compostRate),
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL, "bleeding", BarrelConfig.DEFAULT.bleeding).forGetter(BarrelConfig::bleeding),
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL, "milking", BarrelConfig.DEFAULT.milking).forGetter(BarrelConfig::milking),
            AlwaysSerializedOptionalFieldCodec.create(Codec.INT, "leakRadius", BarrelConfig.DEFAULT.leakRadius).forGetter(BarrelConfig::leakRadius),
            AlwaysSerializedOptionalFieldCodec.create(Codec.INT, "tickRate", BarrelConfig.DEFAULT.tickRate).forGetter(BarrelConfig::tickRate),
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL, "efficiency", BarrelConfig.DEFAULT.efficiency).forGetter(BarrelConfig::efficiency),
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL, "thorns", BarrelConfig.DEFAULT.thorns).forGetter(BarrelConfig::thorns)
    ).apply(instance, BarrelConfig::new));
}
