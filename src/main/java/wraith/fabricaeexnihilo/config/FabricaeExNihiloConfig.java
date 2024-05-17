package wraith.fabricaeexnihilo.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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
                    BarrelConfig.CODEC.optionalFieldOf("barrels", FabricaeExNihiloConfig.DEFAULT.barrels).forGetter(FabricaeExNihiloConfig::barrels),
                    CrucibleConfig.CODEC.optionalFieldOf("crucibles", FabricaeExNihiloConfig.DEFAULT.crucibles).forGetter(FabricaeExNihiloConfig::crucibles),
                    SeedConfig.CODEC.optionalFieldOf("seeds", FabricaeExNihiloConfig.DEFAULT.seeds).forGetter(FabricaeExNihiloConfig::seeds),
                    SieveConfig.CODEC.optionalFieldOf("sieves", FabricaeExNihiloConfig.DEFAULT.sieves).forGetter(FabricaeExNihiloConfig::sieves),
                    InfestedConfig.CODEC.optionalFieldOf("infested", FabricaeExNihiloConfig.DEFAULT.infested).forGetter(FabricaeExNihiloConfig::infested),
                    StrainerConfig.CODEC.optionalFieldOf("strainers", FabricaeExNihiloConfig.DEFAULT.strainers).forGetter(FabricaeExNihiloConfig::strainers),
                    WitchWaterConfig.CODEC.optionalFieldOf("witchWater", FabricaeExNihiloConfig.DEFAULT.witchwater).forGetter(FabricaeExNihiloConfig::witchwater),
                    MiscConfig.CODEC.forGetter(FabricaeExNihiloConfig::misc)) // Spread misc config props into the main config body
            .apply(instance, FabricaeExNihiloConfig::new));
}
