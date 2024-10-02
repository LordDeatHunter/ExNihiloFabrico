package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.AlwaysSerializedOptionalFieldCodec;
import io.github.mattidragon.configloader.api.GenerateMutable;
import net.minecraft.util.Identifier;

import java.util.List;

@GenerateMutable(useFancyMethodNames = true)
public record WitchWaterConfig(List<StatusEffectStats> effects) implements MutableWitchWaterConfig.Source {
    public static final WitchWaterConfig DEFAULT = new WitchWaterConfig(List.of(
            new StatusEffectStats(Identifier.of("blindness"), 210, 0),
            new StatusEffectStats(Identifier.of("hunger"), 210, 2),
            new StatusEffectStats(Identifier.of("slowness"), 210, 0),
            new StatusEffectStats(Identifier.of("weakness"), 210, 2),
            new StatusEffectStats(Identifier.of("wither"), 210, 0)
    ));
    public static final Codec<WitchWaterConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AlwaysSerializedOptionalFieldCodec.create(StatusEffectStats.CODEC.listOf(),"effects", WitchWaterConfig.DEFAULT.effects).forGetter(WitchWaterConfig::effects)
    ).apply(instance, WitchWaterConfig::new));

    @GenerateMutable(useFancyMethodNames = true)
    public record StatusEffectStats(Identifier type, int duration, int amplifier) implements MutableWitchWaterConfig.MutableStatusEffectStats.Source {
        public static final Codec<StatusEffectStats> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("type").forGetter(StatusEffectStats::type),
                Codec.INT.fieldOf("duration").forGetter(StatusEffectStats::duration),
                Codec.INT.fieldOf("amplifier").forGetter(StatusEffectStats::amplifier)
        ).apply(instance, StatusEffectStats::new));
    }
}
