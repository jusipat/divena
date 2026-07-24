package net.cflip.divena.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

public class CosmicTransceiverRenderState extends BlockEntityRenderState {
    public @Nullable Vector3f targetStarVector = null;
}
