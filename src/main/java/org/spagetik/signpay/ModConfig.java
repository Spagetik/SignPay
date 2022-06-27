package org.spagetik.signpay;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;

public class ModConfig {
    private final static String CONFIG_FILE_NAME = Signpay.MOD_ID + ".yml";
    private static File file;
    public static String cardId;
    public static String cardToken;

    public ModConfig() {
    }

    public static void registerConfigs() {
        setup();
        readConfig();
    }

    public static void writeConfigs() {
        HashMap<String, String> data = new HashMap<>();
        data.put("cardId", cardId);
        data.put("cardToken", cardToken);
        StringBuilder out = new StringBuilder();
        for (String key : data.keySet()) {
            out.append(key).append("=").append(data.get(key)).append("\n");
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(out.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readConfig() {
        HashMap<String, String> data = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.put(line.split("=")[0], line.split("=")[1]);
            }
            cardId = data.getOrDefault("cardId", "none");
            cardToken = data.getOrDefault("cardToken", "none");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            cardToken = "none";
            cardId = "none";
            writeConfigs();
        }
    }

    private static void setup() {
        Path path = FabricLoader.getInstance().getConfigDir();
        File fileDir = new File(String.valueOf(path.resolve(Signpay.MOD_ID)));
        if (fileDir.exists()) {
            System.out.println("Config dir exists");
        }
        else {
            if (fileDir.mkdir()) {
                System.out.println("Config dir was created");
            }
            else {
                System.out.println("Config dir error");
            }
        }
        file = new File(String.valueOf((path.resolve(Signpay.MOD_ID).resolve(CONFIG_FILE_NAME))));
        if (file.exists()) {
            System.out.println("Config file exists");
        }
        else {
            try {
                if (file.createNewFile()) {
                    writeConfigs();
                    System.out.println("Config file created");
                }
                else {
                    System.out.println("Config file error");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
