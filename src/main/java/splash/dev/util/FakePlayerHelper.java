package splash.dev.util;

import meteordevelopment.meteorclient.events.entity.EntityRemovedEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.PopChams;
import meteordevelopment.meteorclient.utils.entity.fakeplayer.FakePlayerEntity;
import meteordevelopment.meteorclient.utils.entity.fakeplayer.FakePlayerManager;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class FakePlayerHelper {
    List<Recording> recordings = new ArrayList<>();
    FakePlayerEntity fakePlayerEntity;
    int currentPlayTicks = 0;
    private boolean recording = false;
    private boolean playing = false;
    private boolean renderPop = false;

    @EventHandler
    public void onTick(TickEvent.Post event) {

        if (recording) {


            recordings.add(new Recording(
                mc.player.getPos(), mc.player.getYaw(), mc.player.getPitch(), mc.player.getPose()
            ));
        }


        if (playing) {
            if (recordings.isEmpty()) {
                ChatUtils.error("No recorded positions to play.");
                playing = false;
                return;
            }

            Recording recording = recordings.get(currentPlayTicks % recordings.size());

            if (fakePlayerEntity != null) {

                fakePlayerEntity.setPose(fakePlayerEntity.getPose());
                fakePlayerEntity.setPosition(recording.pos());
                fakePlayerEntity.updateLimbs(false);
                fakePlayerEntity.setAngles(recording.yaw(), recording.pitch());

                currentPlayTicks++;
            }

            if (renderPop) {


                mc.particleManager.addEmitter(fakePlayerEntity, ParticleTypes.TOTEM_OF_UNDYING, 30);
                mc.world.playSound(mc.player, fakePlayerEntity.getBlockPos(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);

                PopChams popchams = Modules.get().get(PopChams.class);
                if (popchams.isActive()) {

                }
                renderPop = false;
            }
        }
    }

    public void record() {
        if (recording) {
            ChatUtils.error("Already recording...");
        } else {
            recordings.clear();
            recording = true;
            ChatUtils.info("Started recording...");
        }
    }

    public void stopRecording() {
        if (recording) {
            recording = false;
        }
    }

    public void play() {
        if (playing) {
            ChatUtils.error("Already playing...");
            return;
        }

        if (recordings.isEmpty()) {
            ChatUtils.error("No positions recorded.");
            return;
        }

        FakePlayerManager.clear();
        FakePlayerManager.add("SplashAni_", 36, true);
        fakePlayerEntity = FakePlayerManager.get("SplashAni_");

        currentPlayTicks = 0;
        playing = true;
        ChatUtils.info("Started playing recorded positions.");

    }

    public void stopPlaying() {
        if (playing) {
            playing = false;
            FakePlayerManager.clear();
            ChatUtils.info("Stopped playing.");
        }
    }

    @EventHandler
    public void onEntityRemove(EntityRemovedEvent event) {
        if (event.entity instanceof EndCrystalEntity) {
            //  double dmg = DamageUtils.getAttackDamage()
        }
    }

    public void takeDmg(float health) {
        float newHealth = fakePlayerEntity.getHealth() - health;
        if (newHealth <= 0) {
            fakePlayerEntity.setHealth(36);
            mc.world.addParticle(ParticleTypes.TOTEM_OF_UNDYING, fakePlayerEntity.getX(), fakePlayerEntity.getY(), fakePlayerEntity.getZ(),
                0, 0, 0);
//            mc.world.playSound(fakePlayerEntity.getX(), fakePlayerEntity.getY(), fakePlayerEntity.getZ(), SoundCategory.PLAYERS);
        }
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isRecording() {
        return recording;
    }
}
