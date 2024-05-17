package wraith.fabricaeexnihilo.compatibility.recipeviewer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EntityRenderer {
    // Entity rendering voodoo
    // https://github.com/theorbtwo/RoughlyEnoughResources/blob/1f1c028fa20ebdd34df044e6f160e53cc8a1acf7/common/src/main/java/uk/me/desert_island/rer/rei_stuff/EntityLootCategory.java#L50
    // Adapted using vanilla code and brain
    public static void drawEntity(DrawContext context, int mouseX, int mouseY, Entity entity, int startX, int startY, int endX, int endY, float size) {
        var centerX = startX + (endX - startX) / 2;
        var centerY = startY + (endY - startY) / 2;

        float f = (float) Math.atan((centerX - mouseX) / 40.0F);
        float g = (float) Math.atan((centerY - mouseY) / 40.0F);
        if (Math.max(entity.getWidth(), entity.getHeight()) > 1.0) {
            size /= Math.max(entity.getWidth(), entity.getHeight());
        }

        var matrices = context.getMatrices();
        // We apply the current matrix stack to two points to calculate where they appear on screen to apply a scissor mask to cut the entities off.
        var positionMatrix = matrices.peek().getPositionMatrix();
        var scissorStart = new Vector3f(startX, startY, 0);
        var scissorEnd = new Vector3f(endX, endY, 0);
        scissorStart.mulPosition(positionMatrix);
        scissorEnd.mulPosition(positionMatrix);
        context.enableScissor((int) scissorStart.x, (int) scissorStart.y, (int) scissorEnd.x, (int) scissorEnd.y);

        matrices.push();
        matrices.translate(centerX, centerY + 20, 1050.0);
        matrices.multiplyPositionMatrix(new Matrix4f().scaling(1, 1, -1));
        matrices.translate(0.0D, 0.0D, 1000.0D);
        matrices.scale(size, size, size);
        Quaternionf quaternion = RotationAxis.POSITIVE_Z.rotationDegrees(180.0F);
        Quaternionf quaternion2 = RotationAxis.POSITIVE_X.rotationDegrees(g * 20.0F);
        quaternion.dot(quaternion2);
        matrices.multiply(quaternion);

        var yaw = entity.getYaw();
        var pitch = entity.getPitch();
        float bodyYaw = 0, prevHeadYaw = 0, headYaw = 0;
        entity.setYaw(180.0F + f * 40.0F);
        entity.setPitch(-g * 20.0F);

        if (entity instanceof LivingEntity living) {
            bodyYaw = living.bodyYaw;
            prevHeadYaw = living.prevHeadYaw;
            headYaw = living.headYaw;
            living.bodyYaw = 180.0F + f * 20.0F;
            living.headYaw = entity.getYaw();
            living.prevHeadYaw = entity.getYaw();
        }

        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        quaternion2.conjugate();
        entityRenderDispatcher.setRotation(quaternion2);
        entityRenderDispatcher.setRenderShadows(false);
        var vertexConsumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        DiffuseLighting.method_34742();
        entityRenderDispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrices, vertexConsumers, 15728880);
        vertexConsumers.draw();
        DiffuseLighting.enableGuiDepthLighting();
        entityRenderDispatcher.setRenderShadows(true);
        entity.setYaw(yaw);
        entity.setPitch(pitch);

        if (entity instanceof LivingEntity living) {
            living.bodyYaw = bodyYaw;
            living.prevHeadYaw = prevHeadYaw;
            living.headYaw = headYaw;
        }

        matrices.pop();
        context.disableScissor();
    }
}
