package dev.peecer.shieldscale;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ConfigScreen {
    private ConfigScreen() {
    }

    public static Screen create(Screen parent) {
        ShieldScaleConfig config = ShieldScaleConfig.get();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("custom_shield_scale.title"))
                .save(ShieldScaleConfig::save)
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("custom_shield_scale.category.self"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("custom_shield_scale.enabled"))
                                .description(OptionDescription.of(
                                        Component.translatable("custom_shield_scale.enabled.description")))
                                .binding(true, () -> config.enabled, value -> config.enabled = value)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(scaleOption(
                                "custom_shield_scale.self_scale",
                                () -> config.selfScale,
                                value -> config.selfScale = value))
                        .option(offsetOption(
                                "custom_shield_scale.self_offset_x",
                                () -> config.selfOffsetX,
                                value -> config.selfOffsetX = value))
                        .option(offsetOption(
                                "custom_shield_scale.self_offset_y",
                                () -> config.selfOffsetY,
                                value -> config.selfOffsetY = value))
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("custom_shield_scale.category.others"))
                        .option(scaleOption(
                                "custom_shield_scale.others_scale",
                                () -> config.othersScale,
                                value -> config.othersScale = value))
                        .option(offsetOption(
                                "custom_shield_scale.others_offset_x",
                                () -> config.othersOffsetX,
                                value -> config.othersOffsetX = value))
                        .option(offsetOption(
                                "custom_shield_scale.others_offset_y",
                                () -> config.othersOffsetY,
                                value -> config.othersOffsetY = value))
                        .build())
                .build()
                .generateScreen(parent);
    }

    private static Option<Double> scaleOption(
            String key,
            Supplier<Double> getter,
            Consumer<Double> setter) {
        return Option.<Double>createBuilder()
                .name(Component.translatable(key))
                .description(OptionDescription.of(Component.translatable(key + ".description")))
                .binding(1.0, getter, setter)
                .controller(option -> DoubleSliderControllerBuilder.create(option)
                        .range(0.10, 3.00)
                        .step(0.01))
                .build();
    }

    private static Option<Double> offsetOption(
            String key,
            Supplier<Double> getter,
            Consumer<Double> setter) {
        return Option.<Double>createBuilder()
                .name(Component.translatable(key))
                .description(OptionDescription.of(Component.translatable(key + ".description")))
                .binding(0.0, getter, setter)
                .controller(option -> DoubleSliderControllerBuilder.create(option)
                        .range(-1.00, 1.00)
                        .step(0.01))
                .build();
    }
}
