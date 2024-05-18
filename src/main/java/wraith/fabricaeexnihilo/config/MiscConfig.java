package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.AlwaysSerializedOptionalFieldCodec;
import io.github.mattidragon.configloader.api.GenerateMutable;

@GenerateMutable(useFancyMethodNames = true)
public record MiscConfig(boolean enableSaltCollection) implements MutableMiscConfig.Source {
    public static final MiscConfig DEFAULT = new MiscConfig(true);
    public static final MapCodec<MiscConfig> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL, "enableSaltCollection", MiscConfig.DEFAULT.enableSaltCollection).forGetter(MiscConfig::enableSaltCollection)
    ).apply(instance, MiscConfig::new));
}
