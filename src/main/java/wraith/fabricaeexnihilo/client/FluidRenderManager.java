package wraith.fabricaeexnihilo.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.render.RenderLayer;
import wraith.fabricaeexnihilo.modules.ModFluids;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;

public class FluidRenderManager {
    public static void setupClient() {
        ModFluids.FLUIDS.forEach(FluidRenderManager::setupFluidRenderer);
    }

    private static void setupFluidRenderer(AbstractFluid fluid) {
        var fluidSettings = fluid.getFluidSettings();
        FluidRenderHandlerRegistry.INSTANCE.register(fluid, fluid.getFlowing(), new SimpleFluidRenderHandler(
                fluidSettings.getStillTexture(),
                fluidSettings.getFlowingTexture(),
                fluidSettings.getOverlayTexture()));

        if (fluidSettings.isTranslucent()) {
            BlockRenderLayerMap.INSTANCE.putFluid(fluid, RenderLayer.getTranslucent());
            BlockRenderLayerMap.INSTANCE.putFluid(fluid.getFlowing(), RenderLayer.getTranslucent());
        }
    }
}
