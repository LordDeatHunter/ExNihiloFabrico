package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mattidragon.configloader.api.AlwaysSerializedOptionalFieldCodec;

public record FabricaeExNihiloConfig(BarrelConfig barrels,
                                     CrucibleConfig crucibles,
                                     SeedConfig seeds,
                                     SieveConfig sieves,
                                     InfestedConfig infested,
                                     StrainerConfig strainers,
                                     WitchWaterConfig witchwater,
                                     MiscConfig misc) {
    public static final FabricaeExNihiloConfig DEFAULT = new FabricaeExNihiloConfig(BarrelConfig.DEFAULT, CrucibleConfig.DEFAULT, SeedConfig.DEFAULT, SieveConfig.DEFAULT, InfestedConfig.DEFAULT, StrainerConfig.DEFAULT, WitchWaterConfig.DEFAULT, MiscConfig.DEFAULT);
    public static final Codec<FabricaeExNihiloConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    AlwaysSerializedOptionalFieldCodec.create(BarrelConfig.CODEC, "barrels", FabricaeExNihiloConfig.DEFAULT.barrels).forGetter(FabricaeExNihiloConfig::barrels),
                    AlwaysSerializedOptionalFieldCodec.create(CrucibleConfig.CODEC, "crucibles", FabricaeExNihiloConfig.DEFAULT.crucibles).forGetter(FabricaeExNihiloConfig::crucibles),
                    AlwaysSerializedOptionalFieldCodec.create(SeedConfig.CODEC, "seeds", FabricaeExNihiloConfig.DEFAULT.seeds).forGetter(FabricaeExNihiloConfig::seeds),
                    AlwaysSerializedOptionalFieldCodec.create(SieveConfig.CODEC, "sieves", FabricaeExNihiloConfig.DEFAULT.sieves).forGetter(FabricaeExNihiloConfig::sieves),
                    AlwaysSerializedOptionalFieldCodec.create(InfestedConfig.CODEC, "infested", FabricaeExNihiloConfig.DEFAULT.infested).forGetter(FabricaeExNihiloConfig::infested),
                    AlwaysSerializedOptionalFieldCodec.create(StrainerConfig.CODEC, "strainers", FabricaeExNihiloConfig.DEFAULT.strainers).forGetter(FabricaeExNihiloConfig::strainers),
                    AlwaysSerializedOptionalFieldCodec.create(WitchWaterConfig.CODEC, "witchWater", FabricaeExNihiloConfig.DEFAULT.witchwater).forGetter(FabricaeExNihiloConfig::witchwater),
                    MiscConfig.CODEC.forGetter(FabricaeExNihiloConfig::misc)) // Spread misc config props into the main config body
            .apply(instance, FabricaeExNihiloConfig::new));
}
