package com.skullking.skullblocks;

import com.skullking.skullblocks.config.SkullBlocksConfig;
import com.skullking.skullblocks.gui.SkullBlocksScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.Identifier;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class SkullBlocksClient implements ClientModInitializer {
    private static KeyBinding openGuiKey;
    private BlockPos lastPlacementBlock;
    private Direction lastPlacementSide;
    private int placementCooldown;
    private BlockPos lastCreativeBreakBlock;
    private boolean wasCreativeBreaking;

    @Override
    public void onInitializeClient() {
        SkullBlocksConfig.load();
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.skullblocks.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, KeyBinding.Category.create(Identifier.of("skullblocks", "main"))));
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(MinecraftClient client) {
        while (openGuiKey.wasPressed()) {
            if (client.currentScreen == null) client.setScreen(new SkullBlocksScreen());
        }
        if (client.player == null || client.world == null || client.interactionManager == null || client.currentScreen != null) {
            resetInputState(client);
            return;
        }
        if (placementCooldown > 0) placementCooldown--;
        tickPlacement(client);
        tickCreativeBreaking(client);
    }

    private void tickPlacement(MinecraftClient client) {
        SkullBlocksConfig cfg = SkullBlocksConfig.INSTANCE;
        if (!cfg.accuratePlacement || !client.options.useKey.isPressed()) {
            lastPlacementBlock = null; lastPlacementSide = null; return;
        }
        if (client.crosshairTarget == null || client.crosshairTarget.getType() != HitResult.Type.BLOCK) {
            lastPlacementBlock = null; lastPlacementSide = null; return;
        }
        BlockHitResult hit = (BlockHitResult) client.crosshairTarget;
        BlockPos pos = hit.getBlockPos().toImmutable();
        Direction side = hit.getSide();
        boolean changedTarget = !pos.equals(lastPlacementBlock) || side != lastPlacementSide;
        if (!changedTarget || placementCooldown > 0) return;
        lastPlacementBlock = pos; lastPlacementSide = side;
        ActionResult result = client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hit);
        if (result.isAccepted()) client.player.swingHand(Hand.MAIN_HAND);
        placementCooldown = Math.max(0, cfg.placementCooldownTicks);
    }

    private void tickCreativeBreaking(MinecraftClient client) {
        SkullBlocksConfig cfg = SkullBlocksConfig.INSTANCE;
        boolean active = cfg.creativeFastBreaking && client.player.isCreative() && client.options.attackKey.isPressed();
        if (!active) {
            if (wasCreativeBreaking) client.interactionManager.cancelBlockBreaking();
            wasCreativeBreaking = false; lastCreativeBreakBlock = null; return;
        }
        wasCreativeBreaking = true;
        if (client.crosshairTarget == null || client.crosshairTarget.getType() != HitResult.Type.BLOCK) {
            lastCreativeBreakBlock = null; return;
        }
        BlockHitResult hit = (BlockHitResult) client.crosshairTarget;
        BlockPos pos = hit.getBlockPos().toImmutable();
        if (pos.equals(lastCreativeBreakBlock)) return;
        lastCreativeBreakBlock = pos;
        client.interactionManager.attackBlock(pos, hit.getSide());
    }

    private void resetInputState(MinecraftClient client) {
        lastPlacementBlock = null; lastPlacementSide = null; lastCreativeBreakBlock = null; placementCooldown = 0;
        if (wasCreativeBreaking && client.interactionManager != null) client.interactionManager.cancelBlockBreaking();
        wasCreativeBreaking = false;
    }
}
