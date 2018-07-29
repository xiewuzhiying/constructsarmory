/*
 * Copyright (c) 2018 <C4>
 *
 * This Java class is distributed as a part of the Construct's Armory mod.
 * Construct's Armory is open source and distributed under the GNU Lesser General Public License v3.
 * View the source code and license file on github: https://github.com/TheIllusiveC4/ConstructsArmory
 *
 * Some classes and assets are taken and modified from the parent mod, Tinkers' Construct.
 * Tinkers' Construct is open source and distributed under the MIT License.
 * View the source code on github: https://github.com/SlimeKnights/TinkersConstruct/
 * View the MIT License here: https://tldrlegal.com/license/mit-license
 */

package c4.conarm.integrations.contenttweaker.traits;

import c4.conarm.integrations.contenttweaker.ArmorFunctions;
import c4.conarm.lib.armor.ArmorModifications;
import c4.conarm.lib.modifiers.ArmorModifierTrait;
import c4.conarm.lib.traits.IArmorTrait;
import com.teamacronymcoders.contenttweaker.modules.tinkers.traits.TConTraitRepresentation;
import com.teamacronymcoders.contenttweaker.modules.tinkers.utils.Functions;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.data.NBTConverter;
import crafttweaker.mc1120.enchantments.MCEnchantmentDefinition;
import crafttweaker.mc1120.events.handling.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.modifiers.IToolMod;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/*
 * Base code is from ContentTweaker by The-Acronym-Coders
 * ContentTweaker is open source and distributed under the MIT License
 * View the source code on github: https://github.com/The-Acronym-Coders/ContentTweaker
 * View the MIT License here: https://tldrlegal.com/license/mit-license
 */
public class CoTArmorTrait extends ArmorModifierTrait implements IArmorTrait {

    ArmorFunctions.OnUpdate onUpdate = null;
    ArmorFunctions.OnArmorRepair onArmorRepair = null;
    ArmorFunctions.CanApplyTogetherTrait canApplyTogetherTrait = null;
    ArmorFunctions.CanApplyTogetherEnchantment canApplyTogetherEnchantment = null;
    ArmorFunctions.ExtraInfo extraInfo = null;
    ArmorFunctions.OnArmorTick onArmorTick = null;
    ArmorFunctions.GetModifications getModifications = null;
    ArmorFunctions.OnItemPickup onItemPickup = null;
    ArmorFunctions.OnHeal onHeal = null;
    ArmorFunctions.OnHurt onHurt = null;
    ArmorFunctions.OnDamaged onDamaged = null;
    ArmorFunctions.OnFalling onFalling = null;
    ArmorFunctions.OnJumping onJumping = null;
    ArmorFunctions.OnKnockback onKnockback = null;
    ArmorFunctions.OnArmorDamaged onArmorDamaged = null;
    ArmorFunctions.OnArmorHealed onArmorHealed = null;
    ArmorFunctions.OnArmorChange onArmorChange = null;
    ArmorFunctions.OnAbility onAbility = null;
    ArmorFunctions.GetAbilityLevel getAbilityLevel = null;
    String localizedName = null;
    String localizedDescription = null;
    boolean hidden = false;
    private final ConArmTraitRepresentation thisTrait = new ConArmTraitRepresentation(this);

    public CoTArmorTrait(@Nonnull String identifier, int color, int maxLevel, int countPerLevel) {
        super(identifier, color, maxLevel, countPerLevel);

    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (onUpdate != null) {
            onUpdate.handle(thisTrait, CraftTweakerMC.getIItemStack(tool), CraftTweakerMC.getIWorld(world), CraftTweakerMC.getIEntity(entity), itemSlot, isSelected);
        } else {
            super.onUpdate(tool, world, entity, itemSlot, isSelected);
        }
    }

    @Override
    public void onArmorTick(ItemStack armor, World world, EntityPlayer player) {
        if (onArmorTick != null) {
            onArmorTick.handle(thisTrait, CraftTweakerMC.getIItemStack(armor), CraftTweakerMC.getIWorld(world), CraftTweakerMC.getIPlayer(player));
        } else {
            super.onArmorTick(armor, world, player);
        }
    }

    @Override
    public void onRepair(ItemStack tool, int amount) {
        if (onArmorRepair != null) {
            onArmorRepair.handle(thisTrait, CraftTweakerMC.getIItemStack(tool), amount);
        } else {
            super.onRepair(tool, amount);
        }
    }

    @Override
    public boolean canApplyTogether(IToolMod otherModifier) {
        if (canApplyTogetherTrait != null) {
            return canApplyTogetherTrait.handle(thisTrait, otherModifier.getIdentifier());
        }
        return super.canApplyTogether(otherModifier);
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        if (canApplyTogetherEnchantment != null) {
            return canApplyTogetherEnchantment.handle(thisTrait, new MCEnchantmentDefinition(enchantment));
        }
        return super.canApplyTogether(enchantment);
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        if (extraInfo != null) {
            return Arrays.asList(extraInfo.handle(thisTrait, CraftTweakerMC.getIItemStack(tool), NBTConverter.from(modifierTag, true)));
        }
        return super.getExtraInfo(tool, modifierTag);
    }

    @Override
    public String getLocalizedName() {
        if (localizedName != null) {
            return localizedName;
        }
        return super.getLocalizedName();
    }

    @Override
    public String getLocalizedDesc() {
        if (localizedDescription != null) {
            return localizedDescription;
        }
        return super.getLocalizedDesc();
    }

    @Override
    public ArmorModifications getModifications(EntityPlayer player, ArmorModifications mods, ItemStack armor, DamageSource source, double damage, int slot) {
        if (getModifications != null) {
            return getModifications.handle(thisTrait, CraftTweakerMC.getIPlayer(player), mods, CraftTweakerMC.getIItemStack(armor), CraftTweakerMC.getIDamageSource(source), damage, slot);
        }
        return super.getModifications(player, mods, armor, source, damage, slot);
    }

    @Override
    public void onItemPickup(ItemStack armor, EntityItem item, EntityItemPickupEvent evt) {
        if (onItemPickup != null) {
            onItemPickup.handle(thisTrait, CraftTweakerMC.getIItemStack(armor), CraftTweakerMC.getIEntityItem(item), new MCPlayerPickupItemEvent(evt));
        } else {
            super.onItemPickup(armor, item, evt);
        }
    }

    @Override
    public float onHeal(ItemStack armor, EntityPlayer player, float amount, float newAmount, LivingHealEvent evt) {
        if (onHeal != null) {
            return onHeal.handle(thisTrait, CraftTweakerMC.getIItemStack(armor), CraftTweakerMC.getIPlayer(player), amount, newAmount, evt);
        }
        return super.onHeal(armor, player, amount, newAmount, evt);
    }

    @Override
    public float onHurt(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingHurtEvent evt) {
        if (onHurt != null) {
            return onHurt.handle(thisTrait, CraftTweakerMC.getIItemStack(armor), CraftTweakerMC.getIPlayer(player), CraftTweakerMC.getIDamageSource(source), damage, newDamage, new MCEntityLivingHurtEvent(evt));
        }
        return super.onHurt(armor, player, source, damage, newDamage, evt);
    }

    @Override
    public float onDamaged(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingDamageEvent evt) {
        if (onDamaged != null) {
            return onDamaged.handle(thisTrait, CraftTweakerMC.getIItemStack(armor), CraftTweakerMC.getIPlayer(player), CraftTweakerMC.getIDamageSource(source), damage, newDamage, evt);
        }
        return super.onDamaged(armor, player, source, damage, newDamage, evt);
    }

    @Override
    public void onKnockback(ItemStack armor, EntityPlayer player, LivingKnockBackEvent evt) {
        if (onKnockback != null) {
            onKnockback.handle(thisTrait, CraftTweakerMC.getIItemStack(armor), CraftTweakerMC.getIPlayer(player), evt);
        } else {
            super.onKnockback(armor, player, evt);
        }
    }

    @Override
    public void onFalling(ItemStack armor, EntityPlayer player, LivingFallEvent evt) {
        if (onFalling != null) {
            onFalling.handle(thisTrait, CraftTweakerMC.getIItemStack(armor), CraftTweakerMC.getIPlayer(player), new MCEntityLivingFallEvent(evt));
        } else {
            super.onFalling(armor, player, evt);
        }
    }

    @Override
    public void onJumping(ItemStack armor, EntityPlayer player, LivingEvent.LivingJumpEvent evt) {
        if (onJumping != null) {
            onJumping.handle(thisTrait, CraftTweakerMC.getIItemStack(armor), CraftTweakerMC.getIPlayer(player), new MCEntityLivingJumpEvent(evt));
        } else {
            super.onJumping(armor, player, evt);
        }
    }

    @Override
    public void onAbilityTick(int level, World world, EntityPlayer player) {
        if (onAbility != null) {
            onAbility.handle(thisTrait, level, CraftTweakerMC.getIWorld(world), CraftTweakerMC.getIPlayer(player));
        } else {
            super.onAbilityTick(level, world, player);
        }
    }

    @Override
    public void onArmorChanged(ItemStack armor, EntityPlayer player, int slot) {
        if (onArmorChange != null) {
            onArmorChange.handle(thisTrait, CraftTweakerMC.getIItemStack(armor), CraftTweakerMC.getIPlayer(player), slot);
        } else {
            super.onArmorChanged(armor, player, slot);
        }
    }

    @Override
    public int onArmorDamage(ItemStack armor, DamageSource source, int damage, int newDamage, EntityPlayer player, int slot) {
        if (onArmorDamaged != null) {
            return onArmorDamaged.handle(thisTrait, CraftTweakerMC.getIItemStack(armor), CraftTweakerMC.getIDamageSource(source), damage, newDamage, CraftTweakerMC.getIPlayer(player), slot);
        }
        return super.onArmorDamage(armor, source, damage, newDamage, player, slot);
    }

    @Override
    public int onArmorHeal(ItemStack armor, DamageSource source, int amount, int newAmount, EntityPlayer player, int slot) {
        if (onArmorHealed != null) {
            return onArmorHealed.handle(thisTrait, CraftTweakerMC.getIItemStack(armor), CraftTweakerMC.getIDamageSource(source), amount, newAmount, CraftTweakerMC.getIPlayer(player), slot);
        }
        return super.onArmorHeal(armor, source, amount, newAmount, player, slot);
    }

    @Override
    public int getAbilityLevel(ModifierNBT data) {
        if (getAbilityLevel != null) {
            return getAbilityLevel.handle(thisTrait, data);
        }
        return super.getAbilityLevel(data);
    }

    public void addItem(RecipeMatch recipeMatch) {
        this.items.add(recipeMatch);
    }
}
