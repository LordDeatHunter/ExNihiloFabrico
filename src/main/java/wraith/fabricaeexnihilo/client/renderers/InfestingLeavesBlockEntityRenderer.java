package wraith.fabricaeexnihilo.client.renderers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import wraith.fabricaeexnihilo.modules.infested.InfestingLeavesBlockEntity;

public class InfestingLeavesBlockEntityRenderer implements BlockEntityRenderer<InfestingLeavesBlockEntity> {

    public InfestingLeavesBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(InfestingLeavesBlockEntity infesting, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (infesting == null) {
            return;
        }
        var color = infesting.getColor(0) | 0xff000000;
        var state = infesting.getTarget().getLeafBlock().getDefaultState();
        var model = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(state);

        matrices.push();

        var r = (color >> 16 & 255) / 255.0F;
        var g = (color >> 8 & 255) / 255.0F;
        var b = (color & 255) / 255.0F;

        var consumer = vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state, false));
        MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrices.peek(), consumer, state, model, r, g, b, light, overlay);

        matrices.pop();
    }

}