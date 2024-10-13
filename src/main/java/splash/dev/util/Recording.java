package splash.dev.util;

import net.minecraft.entity.EntityPose;
import net.minecraft.util.math.Vec3d;

public record Recording(Vec3d pos, float yaw, float pitch, EntityPose pose) {
}
