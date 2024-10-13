package splash.dev.util;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.entity.fakeplayer.FakePlayerEntity;
import meteordevelopment.meteorclient.utils.entity.fakeplayer.FakePlayerManager;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class FakePlayerHelper {
    List<Position> positions = new ArrayList<>();
    FakePlayerEntity fakePlayerEntity;
    private boolean recording = false;
    private boolean playing = false;
    int currentPlayTicks = 0;

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        if (mc.world == null || mc.player == null) return;

        if (recording) {
            positions.add(new Position(mc.player.getPos(), mc.player.getYaw(), mc.player.getPitch()));
        }

        if (playing) {
            if (positions.isEmpty()) {
                ChatUtils.error("No recorded positions to play.");
                playing = false;
                return;
            }

            Position position = positions.get(currentPlayTicks % positions.size());
            if (fakePlayerEntity != null) {
                fakePlayerEntity.setPosition(position.pos());
                fakePlayerEntity.setYaw(position.yaw());
                fakePlayerEntity.setPitch(position.pitch());

            }

            currentPlayTicks++;
        }
    }

    public void record() {
        if (recording) {
            ChatUtils.error("Already recording...");
        } else {
            positions.clear();
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

        if (positions.isEmpty()) {
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

    public boolean isPlaying() {
        return playing;
    }

    public boolean isRecording() {
        return recording;
    }
}
