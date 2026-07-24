package net.cflip.divena.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.cflip.divena.block.blockentity.CosmicTransceiverBlockEntity;
import net.cflip.divena.celestial.StarList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3fc;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CosmicTransceiverRenderer implements BlockEntityRenderer<CosmicTransceiverBlockEntity, CosmicTransceiverRenderState> {
    @Override
    public @NonNull CosmicTransceiverRenderState createRenderState() {
        return new CosmicTransceiverRenderState();
    }

    @Override
    public void extractRenderState(
            @NonNull CosmicTransceiverBlockEntity blockEntity, @NonNull CosmicTransceiverRenderState state, float partialTicks,
            @NonNull Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        int targetStar = blockEntity.getTargetStar();
        if (targetStar == -1) {
            state.targetStarVector = null;
            return;
        }

        EnvironmentAttributeProbe attributeProbe = Minecraft.getInstance().gameRenderer.mainCamera().attributeProbe();
        float skyAngle = attributeProbe.getValue(EnvironmentAttributes.STAR_ANGLE, partialTicks) * ((float) Math.PI / 180F);
        state.targetStarVector = StarList.getStarVector(targetStar).zRot(-skyAngle).toVector3f();
    }

    @Override
    public void submit(@NonNull CosmicTransceiverRenderState state, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector collector, @NonNull CameraRenderState cameraState) {
        final float beamLength = 128;
        if (state.targetStarVector == null)
            return;
        Vector3fc targetVec = state.targetStarVector.normalize(beamLength);
        EnderDragonRenderer.submitCrystalBeams(targetVec.x(), targetVec.y(), targetVec.z(), 0, poseStack, collector, state.lightCoords);
    }

    @Override
    public boolean shouldRender(@NonNull CosmicTransceiverBlockEntity blockEntity, @NonNull Vec3 cameraPosition) {
        return true;
    }
}
