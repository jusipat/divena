package net.cflip.divena.mixin;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import net.cflip.divena.celestial.CelestialManager;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.util.RandomSource;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SkyRenderer.class)
public class SkyRenderMixin {
    @Shadow
    private int starIndexCount;

    /**
     * @author cflip
     * @reason Need to make the sky renderer use star positions from CelestialManager instead of building them itself.
     */
    @Overwrite
    private GpuBuffer buildStars() {
        RandomSource random = RandomSource.createThreadLocalInstance(10842L);
        GpuBuffer vertexBuffer;

        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION.getVertexSize() * CelestialManager.NUM_STARS * 4)) {
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);

            for (int i = 0; i < CelestialManager.NUM_STARS; ++i) {
                Vector3f starCenter = CelestialManager.getSkyRenderVector(i);

                float starSize = 0.15F + random.nextFloat() * 0.1F;
                float zRot = (float) (random.nextDouble() * (double) (float) Math.PI * (double) 2.0F);
                Matrix3f rotation = (new Matrix3f()).rotateTowards((new Vector3f(starCenter)).negate(), new Vector3f(0.0F, 1.0F, 0.0F)).rotateZ(-zRot);

                bufferBuilder.addVertex((new Vector3f(starSize, -starSize, 0.0F)).mul(rotation).add(starCenter));
                bufferBuilder.addVertex((new Vector3f(starSize, starSize, 0.0F)).mul(rotation).add(starCenter));
                bufferBuilder.addVertex((new Vector3f(-starSize, starSize, 0.0F)).mul(rotation).add(starCenter));
                bufferBuilder.addVertex((new Vector3f(-starSize, -starSize, 0.0F)).mul(rotation).add(starCenter));
            }

            try (MeshData mesh = bufferBuilder.buildOrThrow()) {
                this.starIndexCount = mesh.drawState().indexCount();
                vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "Stars vertex buffer", 40, mesh.vertexBuffer());
            }
        }

        return vertexBuffer;
    }
}
