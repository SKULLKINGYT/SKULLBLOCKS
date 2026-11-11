package com.skullking.skullblocks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SkullBlocksConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("skullblocks.json");
    public static SkullBlocksConfig INSTANCE = new SkullBlocksConfig();
    public boolean accuratePlacement = true;
    public boolean creativeFastBreaking = false;
    public int placementCooldownTicks = 1;

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                try (Reader reader = Files.newBufferedReader(FILE)) {
                    SkullBlocksConfig loaded = GSON.fromJson(reader, SkullBlocksConfig.class);
                    if (loaded != null) INSTANCE = loaded;
                }
            } else save();
        } catch (Exception exception) {
            System.err.println("[SKULLBLOCKS] Could not load config: " + exception.getMessage());
        }
        INSTANCE.placementCooldownTicks = Math.max(0, Math.min(10, INSTANCE.placementCooldownTicks));
    }

    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            try (Writer writer = Files.newBufferedWriter(FILE)) { GSON.toJson(INSTANCE, writer); }
        } catch (Exception exception) {
            System.err.println("[SKULLBLOCKS] Could not save config: " + exception.getMessage());
        }
    }
}
