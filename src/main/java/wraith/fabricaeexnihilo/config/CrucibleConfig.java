package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.AlwaysSerializedOptionalFieldCodec;
import io.github.mattidragon.configloader.api.GenerateMutable;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

@GenerateMutable(useFancyMethodNames = true)
public record CrucibleConfig(int stoneProcessingRate, int woodProcessingRate, int stoneVolume, int woodVolume,
                             boolean efficiency, boolean fireAspect,
                             int tickRate) implements MutableCrucibleConfig.Source {
    public static final CrucibleConfig DEFAULT = new CrucibleConfig((int) (FluidConstants.BUCKET / 100), (int) (FluidConstants.BUCKET / 60), 4, 1, true, true, 20);
    public static final Codec<CrucibleConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AlwaysSerializedOptionalFieldCodec.create(Codec.INT, "fireproofProcessingRate", CrucibleConfig.DEFAULT.stoneProcessingRate).forGetter(CrucibleConfig::stoneProcessingRate),
            AlwaysSerializedOptionalFieldCodec.create(Codec.INT, "woodProcessingRate", CrucibleConfig.DEFAULT.woodProcessingRate).forGetter(CrucibleConfig::woodProcessingRate),
            AlwaysSerializedOptionalFieldCodec.create(Codec.INT, "stoneVolume", CrucibleConfig.DEFAULT.stoneVolume).forGetter(CrucibleConfig::stoneVolume),
            AlwaysSerializedOptionalFieldCodec.create(Codec.INT, "woodVolume", CrucibleConfig.DEFAULT.woodVolume).forGetter(CrucibleConfig::woodVolume),
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL,  "efficiency", CrucibleConfig.DEFAULT.efficiency).forGetter(CrucibleConfig::efficiency),
            AlwaysSerializedOptionalFieldCodec.create(Codec.BOOL,  "fireAspect", CrucibleConfig.DEFAULT.fireAspect).forGetter(CrucibleConfig::fireAspect),
            AlwaysSerializedOptionalFieldCodec.create(Codec.INT, "tickRate", CrucibleConfig.DEFAULT.tickRate).forGetter(CrucibleConfig::tickRate)
    ).apply(instance, CrucibleConfig::new));
}
