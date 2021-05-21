package net.kunmc.lab.superhot;

import org.bukkit.attribute.AttributeModifier;

import java.util.UUID;

public class Const {
    public static AttributeModifier DECELERATION = new AttributeModifier(UUID.randomUUID(), "SuperhotDecelerated", -0.50, AttributeModifier.Operation.MULTIPLY_SCALAR_1);
}
