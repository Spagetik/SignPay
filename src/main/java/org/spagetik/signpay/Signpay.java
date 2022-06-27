package org.spagetik.signpay;

import net.fabricmc.api.ModInitializer;

import java.util.Base64;

public class Signpay implements ModInitializer {
    public static final String MOD_ID = "signpay";
    public static String CARD_ID;
    public static String CARD_TOKEN;

    @Override
    public void onInitialize() {
        ModConfig.registerConfigs();
        updateConfigs();
    }

    public static void updateConfigs() {
        CARD_ID = ModConfig.cardId;
        CARD_TOKEN = ModConfig.cardToken;
    }

    public static String apiToken() {
        return Base64.getEncoder().encodeToString((CARD_ID + ":" + CARD_TOKEN).getBytes());
    }
}