/*
 * Copyright (C) 2018-2022 Illusive Soulworks
 *
 * Construct's Armory is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Construct's Armory is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Construct's Armory.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.constructsarmory.common.modifier.trait.battle;

import com.illusivesoulworks.constructsarmory.common.modifier.EquipmentUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.impl.TotalArmorLevelModifier;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import com.illusivesoulworks.constructsarmory.ConstructsArmoryMod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class IrregularModifier extends TotalArmorLevelModifier {

    private static final TinkerDataCapability.TinkerDataKey<Integer> IRREGULAR =
            ConstructsArmoryMod.createKey("irregular");

    public IrregularModifier() {
        super(IRREGULAR);
    }

    private static float getArmorReduction(ItemStack stack, int level) {
        float durabilityPercentage = 1f - (float) stack.getDamageValue() / stack.getMaxDamage();
        return level * durabilityPercentage;
    }

    @Override
    public float getProtectionModifier(@Nonnull IToolStackView tool, int level,
                                       @Nonnull EquipmentContext context,
                                       @Nonnull EquipmentSlot slotType, DamageSource source,
                                       float modifierValue) {
        if (!source.isMagic()) {
            final LivingEntity living = context.getEntity();
            AtomicReference<Float> totalArmorReduction = new AtomicReference<>(0f);
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                    final EquipmentSlot finalSlot = slot;
                    living.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(holder -> {
                        int armorLevel = holder.get(IRREGULAR, 0);
                        if (armorLevel > 0) {
                            totalArmorReduction.updateAndGet(v -> v + getArmorReduction(living.getItemBySlot(finalSlot), armorLevel));
                        }
                    });
                }
            }

            float dodgeChance = totalArmorReduction.get() * 0.01f;
            if (new Random().nextFloat() <= dodgeChance) {
                return -modifierValue;
            } else {
                return -modifierValue + totalArmorReduction.get();
            }
        } else {
            return -modifierValue;
        }
    }

    @Override
    public void addInformation(@Nonnull IToolStackView tool, int level,
                               @Nullable Player player, @Nonnull List<Component> tooltip,
                               @Nonnull TooltipKey key, @Nonnull TooltipFlag flag) {

        float reduction;

        if (player != null && key == TooltipKey.SHIFT) {
            reduction = getArmorReduction(ItemStack.EMPTY, level);
        } else {
            reduction = 2f;
        }
        EquipmentUtil.addResistanceTooltip(this, tool, -reduction, tooltip);
    }
}