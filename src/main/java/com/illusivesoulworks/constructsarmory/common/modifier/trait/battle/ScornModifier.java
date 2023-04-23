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

import com.illusivesoulworks.constructsarmory.ConstructsArmoryMod;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import slimeknights.tconstruct.library.modifiers.impl.TotalArmorLevelModifier;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nonnull;

public class ScornModifier extends TotalArmorLevelModifier {

    private static final TinkerDataCapability.TinkerDataKey<Integer> SCORN =
            ConstructsArmoryMod.createKey("scorn");

    public ScornModifier() {
        super(SCORN);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, ScornModifier::onHurt);
    }

    private static void onHurt(final LivingHurtEvent evt) {
        LivingEntity living = evt.getEntityLiving();
        Entity sourceEntity = evt.getSource().getDirectEntity();
        if (sourceEntity instanceof LivingEntity attacker) {

            if (attacker.getMainHandItem().isEmpty()) {
                living.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(holder -> {
                    int totalLevel = holder.get(SCORN, 0);

                    if (totalLevel > 0) {
                        float damageReduction = calculateDamageReduction(totalLevel);
                        evt.setAmount(evt.getAmount() * (1 - damageReduction));
                    }
                });
            }
        }
    }

    private static float calculateDamageReduction(int level) {
        float reduction = 0.2f;
        float currentReduction = reduction;

        for (int i = 1; i < level; i++) {
            currentReduction *= 0.6f;
            reduction += currentReduction;
        }

        return reduction;
    }
    @Override
    public float getProtectionModifier(@Nonnull IToolStackView tool, int level,
                                       @Nonnull EquipmentContext context,
                                       @Nonnull EquipmentSlot slotType, DamageSource source,
                                       float modifierValue) {

        if (!source.isBypassMagic() && !source.isBypassInvul()) {
            modifierValue += calculateDamageReduction(level);
        }
        return modifierValue;
    }
}
