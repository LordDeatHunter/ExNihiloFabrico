package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.GenerateMutable;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

@GenerateMutable(useFancyMethodNames = true)
public record CrucibleConfig(int stoneProcessingRate, int woodProcessingRate, int stoneVolume, int woodVolume,
                             boolean efficiency, boolean fireAspect,
                             int tickRate) implements MutableCrucibleConfig.Source {
    public static final CrucibleConfig DEFAULT = new CrucibleConfig((int) (FluidConstants.BUCKET / 100), (int) (FluidConstants.BUCKET / 60), 4, 1, true, true, 20);
    public static final Codec<CrucibleConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("fireproofProcessingRate", CrucibleConfig.DEFAULT.stoneProcessingRate).forGetter(CrucibleConfig::stoneProcessingRate),
            Codec.INT.optionalFieldOf("woodProcessingRate", CrucibleConfig.DEFAULT.woodProcessingRate).forGetter(CrucibleConfig::woodProcessingRate),
            Codec.INT.optionalFieldOf("stoneVolume", CrucibleConfig.DEFAULT.stoneVolume).forGetter(CrucibleConfig::stoneVolume),
            Codec.INT.optionalFieldOf("woodVolume", CrucibleConfig.DEFAULT.woodVolume).forGetter(CrucibleConfig::woodVolume),
            Codec.BOOL.optionalFieldOf( "efficiency", CrucibleConfig.DEFAULT.efficiency).forGetter(CrucibleConfig::efficiency),
            Codec.BOOL.optionalFieldOf( "fireAspect", CrucibleConfig.DEFAULT.fireAspect).forGetter(CrucibleConfig::fireAspect),
            Codec.INT.optionalFieldOf("tickRate", CrucibleConfig.DEFAULT.tickRate).forGetter(CrucibleConfig::tickRate)
    ).apply(instance, CrucibleConfig::new));
}
