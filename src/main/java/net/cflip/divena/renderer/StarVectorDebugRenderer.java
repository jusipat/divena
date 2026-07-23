package net.cflip.divena.renderer;

import net.cflip.divena.celestial.StarList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.Mth;
import net.minecraft.util.debug.DebugValueAccess;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class StarVectorDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Minecraft mc;

    public StarVectorDebugRenderer(Minecraft mc) {
        this.mc = mc;
    }

    @Override
    public void emitGizmos(double camX, double camY, double camZ, @NonNull DebugValueAccess debugValueAccess, @NonNull Frustum frustum, float partialTicks) {
        EnvironmentAttributeProbe attributeProbe = mc.gameRenderer.mainCamera().attributeProbe();
        float skyAngle = attributeProbe.getValue(EnvironmentAttributes.STAR_ANGLE, partialTicks) * ((float) Math.PI / 180F);

        Vec3 start = new Vec3(camX, camY, camZ);

        for (int i = 0; i < StarList.NUM_STARS; i++) {
            Vec3 starAngle = StarList.getStarVector(i).yRot(-Mth.PI / 2.0f).zRot(-skyAngle);
            Vec3 end = start.add(starAngle);
            Gizmos.circle(end, 1.0f, GizmoStyle.stroke(0xff00aaff));
        }
    }
}
