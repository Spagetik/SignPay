package org.spagetik.signpay.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.MinecraftClient;

public class GuiScreen extends CottonClientScreen {

    public GuiScreen(GuiDescription description) {
        super(description);
    }

    public static void setScreen(GuiDescription description) {
        MinecraftClient.getInstance().setScreen(new GuiScreen(description));
    }
}
