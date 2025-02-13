package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.AlwaysSerializedOptionalFieldCodec;
import io.github.mattidragon.configloader.api.GenerateMutable;

@GenerateMutable(useFancyMethodNames = true)
public record InfestedConfig(int infestedSpreadAttempts, int infestingSpreadAttempts,
                             double minimumSpreadProgress, double progressPerUpdate, double spreadChance,
                             int updateFrequency) implements MutableInfestedConfig.Source {
    public static final InfestedConfig DEFAULT = new InfestedConfig(4, 1, 0.15, 0.015, 0.5, 10);
    public static final Codec<InfestedConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AlwaysSerializedOptionalFieldCodec.create(Codec.INT, "infestedSpreadAttempts", InfestedConfig.DEFAULT.infestedSpreadAttempts).forGetter(InfestedConfig::infestedSpreadAttempts),
            AlwaysSerializedOptionalFieldCodec.create(Codec.INT, "infestingSpreadAttempts", InfestedConfig.DEFAULT.infestingSpreadAttempts).forGetter(InfestedConfig::infestingSpreadAttempts),
            AlwaysSerializedOptionalFieldCodec.create(Codec.DOUBLE, "minimumSpreadProgress", InfestedConfig.DEFAULT.minimumSpreadProgress).forGetter(InfestedConfig::minimumSpreadProgress),
            AlwaysSerializedOptionalFieldCodec.create(Codec.DOUBLE, "progressPerUpdate", InfestedConfig.DEFAULT.progressPerUpdate).forGetter(InfestedConfig::progressPerUpdate),
            AlwaysSerializedOptionalFieldCodec.create(Codec.DOUBLE, "spreadChance", InfestedConfig.DEFAULT.spreadChance).forGetter(InfestedConfig::spreadChance),
            AlwaysSerializedOptionalFieldCodec.create(Codec.INT, "updateFrequency", InfestedConfig.DEFAULT.updateFrequency).forGetter(InfestedConfig::updateFrequency)
    ).apply(instance, InfestedConfig::new));
}
