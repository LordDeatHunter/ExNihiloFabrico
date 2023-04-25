package wraith.fabricaeexnihilo.client.renderers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.modules.sieves.SieveBlockEntity;

public class SieveBlockEntityRenderer implements BlockEntityRenderer<SieveBlockEntity> {

    private static final float XZ_SCALE = 0.875F;
    private static final float Y_MIN = 0.0625F;
    private static final float Y_MAX = 0.3750F;

    public SieveBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    public void render(@Nullable SieveBlockEntity sieve, float partialTicks, MatrixStack matrixStack, @Nullable VertexConsumerProvider vertexConsumerProvider, int light, int overlays) {
        if (sieve == null || sieve.getWorld() == null) {
            return;
        }
        var mesh = sieve.getMesh();
        var contents = sieve.getContents();
        var progress = sieve.getProgress();
        var world = sieve.getWorld();

        // Render the Mesh
        var pos = sieve.getPos();
        renderMesh(matrixStack, pos, mesh, light, overlays, world, vertexConsumerProvider);
        renderContents(matrixStack, pos, contents, (float) progress, light, overlays, world, vertexConsumerProvider);
    }

    public void renderMesh(MatrixStack matrixStack, BlockPos pos, ItemStack mesh, int light, int overlays, World world, @Nullable VertexConsumerProvider vertexConsumerProvider) {
        if (mesh.isEmpty()) {
            return;
        }
        matrixStack.push();
        matrixStack.translate(0.5, 0.5, 0.5);
        MinecraftClient.getInstance().getItemRenderer().renderItem(mesh, ModelTransformationMode.NONE, light, overlays, matrixStack, vertexConsumerProvider, world, (int) pos.asLong());
        matrixStack.pop();
    }

    public void renderContents(MatrixStack matrixStack, BlockPos pos, ItemStack contents, float progress, int light, int overlays, World world, @Nullable VertexConsumerProvider vertexConsumerProvider) {
        if (contents.isEmpty()) {
            return;
        }
        var yScale = Y_MAX - (Y_MAX - Y_MIN) * progress;

        matrixStack.push();
        matrixStack.translate(0.5, 0.625 + yScale / 2, 0.5);
        matrixStack.scale(XZ_SCALE, yScale, XZ_SCALE);
        MinecraftClient.getInstance().getItemRenderer().renderItem(contents, ModelTransformationMode.NONE, light, overlays, matrixStack, vertexConsumerProvider, world, (int) pos.asLong());
        matrixStack.pop();
    }
}