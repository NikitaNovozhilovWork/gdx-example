package io.github.spaceexplorers.lwjgl;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.github.spaceexplorers.SpaceExplorers;

public class LwjglLauncher {
    public static void main(String[] args) {
//        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        createApplication();
    }

    private static LwjglApplication createApplication() {
        return new LwjglApplication(new SpaceExplorers(), getDefaultConfiguration());
    }

    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "SpaceExplorers";
        //// Vsync limits the frames per second to what your hardware can display, and helps eliminate
        //// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
        configuration.vSyncEnabled = true;
        //// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
        //// refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
        configuration.foregroundFPS = LwjglApplicationConfiguration.getDesktopDisplayMode().refreshRate + 1;
        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
        configuration.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
        configuration.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
        //// Adding game icons
        configuration.addIcon("libgdx16.png", Files.FileType.Internal);
        configuration.addIcon("libgdx32.png", Files.FileType.Internal);
        configuration.addIcon("libgdx64.png", Files.FileType.Internal);
        configuration.addIcon("libgdx128.png", Files.FileType.Internal);
        configuration.samples = 4;
        return configuration;
    }
}
