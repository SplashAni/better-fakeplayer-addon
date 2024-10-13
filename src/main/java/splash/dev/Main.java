package splash.dev;

import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;
import splash.dev.commands.FakePlayerCommand;
import splash.dev.util.FakePlayerHelper;

public class Main extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Example");
    private static final FakePlayerHelper fakePlayerHelper = new FakePlayerHelper();

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Addon Template");

        Commands.add(new FakePlayerCommand());
        MeteorClient.EVENT_BUS.subscribe(getFakePlayerHelper());

    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "splash.dev";
    }

    public static FakePlayerHelper getFakePlayerHelper() {
        return fakePlayerHelper;
    }
}
