package org.spagetik.signpay.mixin;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.checkerframework.checker.index.qual.SearchIndexBottom;
import org.spagetik.signpay.Api;
import org.spagetik.signpay.gui.GuiScreen;
import org.spagetik.signpay.gui.SubmitGuiDescription;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSignBlock.class)
public abstract class SignMixin {

    @Inject(method = "onUse", at = @At("HEAD"))
    private void inject(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof SignBlockEntity signBlockEntity) {
            if (world.isClient()) {
                String itemName = signBlockEntity.getTextOnRow(0, false).getString();
                String secondRow = signBlockEntity.getTextOnRow(1, false).getString();
                String price = signBlockEntity.getTextOnRow(2, false).getString();
                String cardNum = signBlockEntity.getTextOnRow(3, false).getString();
                if (!itemName.startsWith("# ")) return;
                itemName = itemName.substring(2);
                String finalItemName = itemName;
                Runnable script = () -> {
                    new Thread(() -> {
                        String pay = Api.sendPayment(price, cardNum, "SignPay: " + finalItemName);
                        player.sendMessage(new LiteralText(pay), true);
                    }).start();
                };
                SubmitGuiDescription guiDescription = new SubmitGuiDescription();
                guiDescription.setScript(script);
                guiDescription.setItemName(itemName);
                guiDescription.setPrice(price);
                guiDescription.setReceiver(cardNum);
                GuiScreen.setScreen(guiDescription);
            }
        }
    }
}
