package com.skullking.skullblocks.gui;

import com.skullking.skullblocks.config.SkullBlocksConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import java.util.List;

public final class SkullBlocksScreen extends Screen {
    private static final int RED = 0xFFFF2020;
    private static final int PANEL = 0xF0121019;
    private static final List<String> SECTIONS = List.of("HOME", "BLOCK PLACEMENT", "FAST BREAKING", "KEYBINDS", "CONFIGURATION", "ABOUT");
    private String section = "HOME";

    public SkullBlocksScreen() { super(Text.literal("SKULLBLOCKS")); }

    @Override
    protected void init() {
        clearChildren();
        int left = Math.max(12, width / 2 - 210);
        int top = Math.max(16, height / 2 - 145);
        int navY = top + 58;
        for (String name : SECTIONS) {
            addDrawableChild(ButtonWidget.builder(Text.literal(name), button -> { section = name; init(); })
                    .dimensions(left, navY, 112, 22).build());
            navY += 27;
        }
        int x = left + 128, y = top + 60;
        switch (section) {
            case "HOME", "BLOCK PLACEMENT" -> {
                addAction(x, y, "ACCURATE PLACEMENT", () -> { SkullBlocksConfig.INSTANCE.accuratePlacement = !SkullBlocksConfig.INSTANCE.accuratePlacement; SkullBlocksConfig.save(); init(); }, SkullBlocksConfig.INSTANCE.accuratePlacement);
                addAction(x, y + 32, "COOLDOWN: " + SkullBlocksConfig.INSTANCE.placementCooldownTicks + " TICKS", () -> { SkullBlocksConfig.INSTANCE.placementCooldownTicks = (SkullBlocksConfig.INSTANCE.placementCooldownTicks + 1) % 11; SkullBlocksConfig.save(); init(); }, true);
            }
            case "FAST BREAKING" -> addAction(x, y, "CREATIVE FAST BREAK", () -> { SkullBlocksConfig.INSTANCE.creativeFastBreaking = !SkullBlocksConfig.INSTANCE.creativeFastBreaking; SkullBlocksConfig.save(); init(); }, SkullBlocksConfig.INSTANCE.creativeFastBreaking);
            case "CONFIGURATION" -> addDrawableChild(ButtonWidget.builder(Text.literal("RESET ALL SETTINGS"), button -> { SkullBlocksConfig.INSTANCE = new SkullBlocksConfig(); SkullBlocksConfig.save(); init(); }).dimensions(x, y, 246, 25).build());
            default -> { }
        }
    }

    private void addAction(int x, int y, String label, Runnable action, boolean enabled) {
        if (label.equals("ACCURATE PLACEMENT") || label.equals("CREATIVE FAST BREAK")) label += enabled ? "  [ON]" : "  [OFF]";
        addDrawableChild(ButtonWidget.builder(Text.literal(label), button -> action.run()).dimensions(x, y, 246, 25).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        int left = Math.max(12, width / 2 - 210), top = Math.max(16, height / 2 - 145);
        context.fill(left - 8, top - 8, left + 390, top + 298, PANEL);
        context.fill(left - 8, top - 8, left + 390, top - 5, RED);
        context.drawTextWithShadow(textRenderer, Text.literal("SKULLBLOCKS"), left + 2, top + 8, RED);
        context.drawTextWithShadow(textRenderer, Text.literal("BY SKULLKING  •  CLIENT MODULE"), left + 128, top + 10, 0xFFB8AFC4);
        context.fill(left + 120, top + 42, left + 122, top + 280, 0xFF5A1828);
        int x = left + 128, y = top + 42;
        context.drawTextWithShadow(textRenderer, Text.literal(section), x, y, 0xFFFFFFFF);
        if (section.equals("HOME")) {
            context.drawTextWithShadow(textRenderer, Text.literal("Build faster. Place precisely."), x, y + 18, 0xFFB8AFC4);
            context.drawTextWithShadow(textRenderer, Text.literal("Hold RMB and move your crosshair"), x, y + 43, 0xFF8F879A);
            context.drawTextWithShadow(textRenderer, Text.literal("to attempt the next vanilla placement."), x, y + 55, 0xFF8F879A);
        } else if (section.equals("KEYBINDS")) {
            context.drawTextWithShadow(textRenderer, Text.literal("OPEN MENU  •  P (rebind in Minecraft Controls)"), x, y + 22, 0xFFB8AFC4);
            context.drawTextWithShadow(textRenderer, Text.literal("PLACE  •  Hold RIGHT MOUSE BUTTON"), x, y + 46, 0xFFB8AFC4);
            context.drawTextWithShadow(textRenderer, Text.literal("FAST BREAK  •  Hold LEFT MOUSE BUTTON"), x, y + 70, 0xFFB8AFC4);
        } else if (section.equals("CONFIGURATION")) {
            context.drawTextWithShadow(textRenderer, Text.literal("Settings are saved to skullblocks.json"), x, y + 22, 0xFFB8AFC4);
        } else if (section.equals("ABOUT")) {
            context.drawTextWithShadow(textRenderer, Text.literal("SKULLBLOCKS 1.0.0"), x, y + 22, RED);
            context.drawTextWithShadow(textRenderer, Text.literal("Created by SKULLKING"), x, y + 42, 0xFFFFFFFF);
            context.drawTextWithShadow(textRenderer, Text.literal("Independent client-side implementation."), x, y + 62, 0xFFB8AFC4);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}
