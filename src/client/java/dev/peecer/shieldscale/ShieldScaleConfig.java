package dev.peecer.shieldscale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ShieldScaleConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("custom_shield_scale.json");

    private static ShieldScaleConfig instance = new ShieldScaleConfig();

    public boolean enabled = true;

    public double selfScale = 1.0;
    public double selfOffsetX = 0.0;
    public double selfOffsetY = 0.0;

    public double othersScale = 1.0;
    public double othersOffsetX = 0.0;
    public double othersOffsetY = 0.0;

    private ShieldScaleConfig() {
    }

    public static ShieldScaleConfig get() {
        return instance;
    }

    public static void load() {
        if (Files.exists(PATH)) {
            try (Reader reader = Files.newBufferedReader(PATH)) {
                ShieldScaleConfig loaded = GSON.fromJson(reader, ShieldScaleConfig.class);
                if (loaded != null) {
                    instance = loaded;
                }
            } catch (Exception exception) {
                CustomShieldScaleClient.LOGGER.warn("Could not read {}", PATH, exception);
            }
        }

        instance.sanitize();
        save();
    }

    public static void save() {
        instance.sanitize();

        try {
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH)) {
                GSON.toJson(instance, writer);
            }
        } catch (IOException exception) {
            CustomShieldScaleClient.LOGGER.warn("Could not write {}", PATH, exception);
        }
    }

    private void sanitize() {
        selfScale = clamp(selfScale, 0.10, 3.00, 1.0);
        othersScale = clamp(othersScale, 0.10, 3.00, 1.0);

        selfOffsetX = clamp(selfOffsetX, -1.00, 1.00, 0.0);
        selfOffsetY = clamp(selfOffsetY, -1.00, 1.00, 0.0);
        othersOffsetX = clamp(othersOffsetX, -1.00, 1.00, 0.0);
        othersOffsetY = clamp(othersOffsetY, -1.00, 1.00, 0.0);
    }

    private static double clamp(double value, double min, double max, double fallback) {
        if (!Double.isFinite(value)) {
            return fallback;
        }
        return Math.max(min, Math.min(max, value));
    }
}
