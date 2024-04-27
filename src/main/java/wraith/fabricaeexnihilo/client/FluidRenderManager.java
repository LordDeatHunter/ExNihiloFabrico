package wraith.fabricaeexnihilo.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.modules.ModFluids;
import wraith.fabricaeexnihilo.modules.base.AbstractFluid;
import wraith.fabricaeexnihilo.modules.base.FluidSettings;

import java.util.function.Function;

import static net.minecraft.screen.PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;

public class FluidRenderManager {

    public static void setupClient() {
        ModFluids.FLUIDS.forEach(FluidRenderManager::setupFluidRenderer);
    }

    private static void setupFluidRenderer(AbstractFluid fluid) {
//        var identifier = Registries.FLUID.getId(fluid);
//        final Identifier listenerId = new Identifier(identifier.getNamespace(), identifier.getPath() + "_reload_listener");
//
//
//        final Sprite[] fluidSprites = {null, null, null};

        var fluidSettings = fluid.getFluidSettings();
        FluidRenderHandlerRegistry.INSTANCE.register(fluid, fluid.getFlowing(), new SimpleFluidRenderHandler(
                fluidSettings.getStillTexture(),
                fluidSettings.getFlowingTexture(),
                fluidSettings.getOverlayTexture()/*,
                fluidSettings.getTint()*/));

//        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
//            @Override
//            public Identifier getFabricId() {
//                return listenerId;
//            }
//
//            @Override
//            public void reload(ResourceManager resourceManager) {
//                final Function<Identifier, Sprite> atlas = MinecraftClient.getInstance().getSpriteAtlas(BLOCK_ATLAS_TEXTURE);
//                fluidSprites[0] = atlas.apply(fluidSettings.getStillTexture());
//                fluidSprites[1] = atlas.apply(fluidSettings.getFlowingTexture());
//                fluidSprites[2] = atlas.apply(fluidSettings.getOverlayTexture());
//            }
//        });
//        FluidRenderHandlerRegistry.INSTANCE.register(fluid, fluid.getFlowing(), (view, pos, state) -> fluidSprites);

        if (fluidSettings.isTranslucent()) {
            BlockRenderLayerMap.INSTANCE.putFluid(fluid, RenderLayer.getTranslucent());
            BlockRenderLayerMap.INSTANCE.putFluid(fluid.getFlowing(), RenderLayer.getTranslucent());
        }
    }

}
