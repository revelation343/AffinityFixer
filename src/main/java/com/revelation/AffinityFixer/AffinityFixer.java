package com.revelation.AffinityFixer;

import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.ItemTemplatesCreatedListener;
import org.gotti.wurmunlimited.modloader.interfaces.ServerStartedListener;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;

import java.io.IOException;
import java.util.Properties;

public class AffinityFixer implements WurmServerMod, Configurable, ItemTemplatesCreatedListener, ServerStartedListener {

    public static int Value = 50000;

    //Properties
    public void configure(Properties properties) {
    Value = Integer.parseInt(properties.getProperty("Value"));
    }

    @Override
    public void onItemTemplatesCreated() {
        try {
            ItemHook.createAffinityFixer();
            } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onServerStarted() {
        try {
            ItemHook.registerAction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
