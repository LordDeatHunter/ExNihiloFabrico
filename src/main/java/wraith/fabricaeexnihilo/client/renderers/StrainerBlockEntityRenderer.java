package wraith.fabricaeexnihilo.client.renderers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import wraith.fabricaeexnihilo.modules.strainer.StrainerBlockEntity;
import java.util.List;
import java.util.Objects;

public class StrainerBlockEntityRenderer implements BlockEntityRenderer<StrainerBlockEntity> {

    public StrainerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(StrainerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        var items = entity.getInventory().stream().filter(stack -> !stack.isEmpty()).toList();
        var count = items.size();
        if (items.isEmpty()) return;

        matrices.push();
        matrices.translate(0.5, -0.1, 0.5);

        block:
        {
            // First layer
            matrices.translate(count == 1 ? 0 : 0.25, count < 5 ? 0.5 : 0.25, count == 1 ? 0 : 0.25);
            renderItem(items.get(0), entity, tickDelta, matrices, vertexConsumers, light, overlay, itemRenderer);

            if (count < 2) break block;
            matrices.translate(-0.5, 0, -0.5);
            renderItem(items.get(1), entity, tickDelta, matrices, vertexConsumers, light, overlay, itemRenderer);

            if (count < 3) break block;
            matrices.translate(0, 0, 0.5);
            renderItem(items.get(2), entity, tickDelta, matrices, vertexConsumers, light, overlay, itemRenderer);

            if (count < 4) break block;
            matrices.translate(0.5, 0, -0.5);
            renderItem(items.get(3), entity, tickDelta, matrices, vertexConsumers, light, overlay, itemRenderer);

            // Second layer
            if (count < 5) break block;
            matrices.translate(count == 5 ? -0.25 : 0, 0.5, count == 5 ? 0.25 : 0.5);
            renderItem(items.get(4), entity, tickDelta, matrices, vertexConsumers, light, overlay, itemRenderer);

            if (count < 6) break block;
            matrices.translate(-0.5, 0, -0.5);
            renderItem(items.get(5), entity, tickDelta, matrices, vertexConsumers, light, overlay, itemRenderer);

            if (count < 7) break block;
            matrices.translate(0, 0, 0.5);
            renderItem(items.get(6), entity, tickDelta, matrices, vertexConsumers, light, overlay, itemRenderer);

            if (count < 8) break block;
            matrices.translate(0.5, 0, -0.5);
            renderItem(items.get(7), entity, tickDelta, matrices, vertexConsumers, light, overlay, itemRenderer);
        }
        matrices.pop();
    }

    private void renderItem(ItemStack stack, StrainerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ItemRenderer itemRenderer) {
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((Objects.requireNonNull(entity.getWorld()).getTime() + tickDelta) * 4));
        itemRenderer.renderItem(stack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, entity.getWorld(), entity.getPos().hashCode());
        matrices.pop();
    }

}
