package org.spagetik.signpay;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.LiteralText;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(new LiteralText(Signpay.MOD_ID.toUpperCase()));
            ConfigCategory general = builder.getOrCreateCategory(new LiteralText(Signpay.MOD_ID.toUpperCase()));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            general.addEntry(entryBuilder.startStrField(new LiteralText("Card ID"), ModConfig.cardId)
                    .setDefaultValue("NONE") // Recommended: Used when user click "Reset"
                    .setSaveConsumer(newKey -> ModConfig.cardId = newKey) // Recommended: Called when user save the config
                    .build()); // Builds the option entry for cloth config
            general.addEntry(entryBuilder.startStrField(new LiteralText("Card TOKEN"), ModConfig.cardToken)
                    .setDefaultValue("NONE") // Recommended: Used when user click "Reset"
                    .setSaveConsumer(newKey -> ModConfig.cardToken = newKey) // Recommended: Called when user save the config
                    .build()); // Builds the option entry for cloth config
            builder.setSavingRunnable(() -> {
                if (ModConfig.cardId.equals("")) {
                    ModConfig.cardId = "NONE";
                }
                if (ModConfig.cardToken.equals("")) {
                    ModConfig.cardToken = "NONE";
                }
                ModConfig.writeConfigs();
                ModConfig.registerConfigs();
                Signpay.updateConfigs();
            });
            return builder.build();
        };
    }

}
