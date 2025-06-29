/**
 *   This Source Code Form is subject to the terms of the Mozilla Public
 *   License, v. 2.0. If a copy of the MPL was not distributed with this
 *   file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * If it is not possible or desirable to put the notice in a particular
 * file, then You may include the notice in a location (such as a LICENSE
 * file in a relevant directory) where a recipient would be likely to look
 * for such a notice.
 *
 * You may add additional accurate notices of copyright ownership.
 */

package com.dulkirfabric.mixin.render;

import com.dulkirfabric.features.InventoryScale;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;


@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Shadow public int width;

    @Shadow public int height;

    @Inject(
            method = "init(Lnet/minecraft/client/MinecraftClient;II)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screen/Screen;height:I",
                    shift = At.Shift.AFTER
            )
    )
    private void dulkir$onInitAfterViewportSizeSet(MinecraftClient client, int width, int height, CallbackInfo ci) {
        this.width = (int) ceil(width / InventoryScale.INSTANCE.getScale());
        this.height = (int) ceil(height /  InventoryScale.INSTANCE.getScale());
    }

    @Inject(
            method = "resize",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screen/Screen;height:I",
                    shift = At.Shift.AFTER
            )
    )
    private void dulkir$onResizeAfterViewportSizeSet(MinecraftClient client, int width, int height, CallbackInfo ci) {
        this.width = (int) ceil(width / InventoryScale.INSTANCE.getScale());
        this.height = (int) ceil(height / InventoryScale.INSTANCE.getScale());
    }

    @WrapMethod(
            method = "render"
    )
    private void dulkir$modifyMouse(DrawContext context, int mouseX, int mouseY, float deltaTicks, Operation<Void> original) {
        original.call(
                context,
                (int) floor(mouseX * InventoryScale.INSTANCE.getScale()),
                (int) floor(mouseY * InventoryScale.INSTANCE.getScale()),
                deltaTicks
        );
    }

}