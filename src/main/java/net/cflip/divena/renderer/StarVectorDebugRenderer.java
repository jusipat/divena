package net.cflip.divena.renderer;

import net.cflip.divena.celestial.StarList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
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

        Vec3 search = mc.getCameraEntity().getLookAngle();
        int foundStar = StarList.findStar(search, skyAngle);
        Gizmos.circle(start.add(search), 0.1f, GizmoStyle.stroke(0xff5555aa));

        GizmoStyle normalStyle = GizmoStyle.stroke(0xff00aaff);
        GizmoStyle foundStyle = GizmoStyle.stroke(0xffff55aa);

        for (int i = 0; i < StarList.NUM_STARS; i++) {
            Vec3 starAngle = StarList.getStarVector(i).zRot(-skyAngle);
            Vec3 end = start.add(starAngle);

            Gizmos.circle(end, 0.01f, i == foundStar ? foundStyle : normalStyle);
        }
    }
}
