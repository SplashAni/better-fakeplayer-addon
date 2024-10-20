package splash.dev.mixin;

import meteordevelopment.meteorclient.systems.modules.render.PopChams;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import splash.dev.util.GhostEntityInterface;

import java.util.List;

@Mixin(value = PopChams.class,remap = false)
public class PopChamsMixin implements GhostEntityInterface {


    @Override
    public void add(Entity entity) {

    }
}
