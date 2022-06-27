package org.spagetik.signpay.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

public class SubmitGuiDescription extends LightweightGuiDescription {

    private Runnable script;
    public WGridPanel root = new WGridPanel()
            .setInsets(Insets.ROOT_PANEL);

    public WButton submitButton = new WButton()
            .setLabel(new LiteralText("ПОДТВЕРДИТЬ"))
            .setOnClick(() -> {
                MinecraftClient.getInstance().setScreen(null);
                script.run();
            });

    public WButton cancelButton = new WButton()
            .setLabel(new LiteralText("ОТМЕНА"))
            .setOnClick(() -> {
                MinecraftClient.getInstance().setScreen(null);
            });

    public WLabel label = new WLabel(new LiteralText("Подтвердите операцию"))
            .setHorizontalAlignment(HorizontalAlignment.CENTER);

    public SubmitGuiDescription() {
        setRootPanel(root);
        root.validate(this);
        root.setSize(100, 120);
        root.add(submitButton, 0, 5, 5, 1);
        root.add(cancelButton, 6, 5, 5, 1);
        root.add(label, 0, 0, 11, 1);
    }

    public void setScript(Runnable script) {
        this.script = script;
    }

    public void setItemName(String itemName) {
        WLabel label2 = new WLabel(new LiteralText("Оплата: " + itemName))
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(label2, 0, 1, 11, 1);
    }

    public void setPrice(String price) {
        WLabel label3 = new WLabel(new LiteralText("Сумма: " + price + " АР"))
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(label3, 0, 3, 11, 1);
    }

    public void setReceiver(String receiver) {
        WLabel label4 = new WLabel(new LiteralText("Карта получателя: " + receiver))
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(label4, 0, 4, 11, 1);
    }
}
