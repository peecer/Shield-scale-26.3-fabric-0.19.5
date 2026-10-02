package dev.peecer.shieldscale;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CustomShieldScaleClient implements ClientModInitializer {
    public static final String MOD_ID = "custom_shield_scale";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        ShieldScaleConfig.load();
    }
}
