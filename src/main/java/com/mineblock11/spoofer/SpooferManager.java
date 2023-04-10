package com.mineblock11.spoofer;

import net.fabricmc.api.ModInitializer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class SpooferManager implements ModInitializer {

    // TARGET, <SPOOF USERNAME, LABEL>
    public static final HashMap<String, Pair<String, Text>> currentlySpoofed = new HashMap<>();

    public static boolean ENABLE_CHAT_SPOOF = false;
    public static final HashMap<String, Identifier> TEXTURE_CACHE = new HashMap<>();

    @Override
    public void onInitialize() {}
}
