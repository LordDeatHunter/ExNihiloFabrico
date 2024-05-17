package wraith.fabricaeexnihilo.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import wraith.fabricaeexnihilo.client.BlockModelRendererFlags;
import wraith.fabricaeexnihilo.modules.barrels.BarrelBlockEntity;
import wraith.fabricaeexnihilo.util.Color;

public class BarrelBlockEntityRenderer implements BlockEntityRenderer<BarrelBlockEntity> {

    private static final float XZ_SCALE = 12f / 16f;
    private static final float X_MIN = 2f / 16f;
    private static final float X_MAX = 14f / 16f;
    private static final float Z_MIN = 2f / 16f;
    private static final float Z_MAX = 14f / 16f;
    private static final float Y_MIN = 3f / 16f;
    private static final float Y_MAX = 15f / 16f;

    public BarrelBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(BarrelBlockEntity barrel, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlays) {
        var pos = barrel.getPos();
        var world = barrel.getWorld();

        switch (barrel.getState()) {
            case EMPTY -> {}
            case FLUID -> renderFluid(barrel.getFluid(), barrel.getFluidAmount(), matrices, vertexConsumers, light, overlays);
            case ITEM -> renderItem(barrel.getItem(), pos, matrices, vertexConsumers, light, overlays, world);
            case COMPOST -> renderCompost(barrel.getItem(), barrel.getCompostLevel(), barrel.getRecipeProgress(), new Color(0x573f17), pos, matrices, vertexConsumers, light, overlays, world);
        }
    }

    private void renderFluid(FluidVariant fluid, long amount, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlays) {
        var sprite = FluidVariantRendering.getSprite(fluid);
        var color = FluidVariantRendering.getColor(fluid);
        var r = ((color >> 16) & 255) / 256f;
        var g = ((color >> 8) & 255) / 256f;
        var b = (color & 255) / 256f;

        if (sprite == null) return;

        RenderSystem.enableDepthTest();

        // Idea stolen from Modern Industrialisation
        var emitter = RendererAccess.INSTANCE.getRenderer().meshBuilder().getEmitter();
        emitter.square(Direction.UP, X_MIN, Z_MIN, X_MAX, Z_MAX, 1 - MathHelper.lerp(amount / (float) FluidConstants.BUCKET, Y_MIN, Y_MAX));
        emitter.spriteBake(sprite, MutableQuadView.BAKE_LOCK_UV);
        vertexConsumers.getBuffer(RenderLayer.getTranslucent()).quad(matrices.peek(), emitter.toBakedQuad(sprite), r, g, b, 1.0F, light, overlays);
    }

    private void renderItem(ItemStack stack, BlockPos pos, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlays, World world) {
        var yScale = Y_MAX - Y_MIN;

        matrices.push();
        matrices.translate(0.5, Y_MIN + yScale / 2, 0.5);
        matrices.scale(XZ_SCALE, yScale, XZ_SCALE);
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.NONE, light, overlays, matrices, vertexConsumers, world, (int) pos.asLong());
        matrices.pop();
    }

    private void renderCompost(ItemStack result, double amount, double progress, Color color, BlockPos pos, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlays, World world) {
        var finalColor = Color.average(Color.WHITE, color, Math.pow(progress, 4));
        var r = finalColor.r;
        var g = finalColor.g;
        var b = finalColor.b;

        matrices.push();

        var yScale = MathHelper.clamp((float) ((Y_MAX - Y_MIN) * Math.min(amount, 1.0f)), 0, 1);

        if (result.getItem() instanceof BlockItem blockItem) {
            var block = blockItem.getBlock().getDefaultState();
            var model = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(block);
            var consumer = vertexConsumers.getBuffer(TexturedRenderLayers.getEntityCutout());
            matrices.translate(X_MIN, Y_MIN, X_MIN);
            matrices.scale(XZ_SCALE, yScale, XZ_SCALE);
            BlockModelRendererFlags.setColorOverride(true);
            MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(matrices.peek(), consumer, block, model, r, g, b, light, overlays);
            BlockModelRendererFlags.setColorOverride(false);
        } else {
            matrices.translate(X_MIN, Y_MIN, X_MIN);
            matrices.scale(XZ_SCALE, yScale, XZ_SCALE);
            MinecraftClient.getInstance().getItemRenderer().renderItem(result, ModelTransformationMode.NONE, light, overlays, matrices, vertexConsumers, world, (int) pos.asLong());
        }
        matrices.pop();
    }
}
