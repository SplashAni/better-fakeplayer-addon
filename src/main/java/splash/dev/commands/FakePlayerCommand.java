package splash.dev.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import splash.dev.Main;
import splash.dev.util.FakePlayerHelper;

public class FakePlayerCommand extends Command {

    public FakePlayerCommand() {
        super("fake-player", "Controls the fake player recording and playback.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        FakePlayerHelper helper = Main.getFakePlayerHelper();

        builder.then(literal("record")
            .executes(context -> {
                if (helper.isRecording()) {
                    helper.stopRecording();
                    info("Stopped recording.");
                } else {
                    helper.record();
                    info("Started recording.");
                }
                return SINGLE_SUCCESS;
            })
        );

        builder.then(literal("play")
            .executes(context -> {
                if (helper.isPlaying()) {
                    helper.stopPlaying();
                } else {
                    helper.play();
                }
                return SINGLE_SUCCESS;
            })
        );
    }
}
