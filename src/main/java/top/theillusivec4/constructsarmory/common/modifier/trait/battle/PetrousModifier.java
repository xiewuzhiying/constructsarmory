package top.theillusivec4.constructsarmory.common.modifier.trait.battle;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.Tags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IModifierToolStack;
import slimeknights.tconstruct.library.utils.TooltipFlag;
import slimeknights.tconstruct.library.utils.TooltipKey;
import top.theillusivec4.constructsarmory.common.modifier.EquipmentUtil;

public class PetrousModifier extends Modifier {

  private static final float BONUS_PER_BLOCK = 0.1f;

  public PetrousModifier() {
    super(0x999999);
  }

  private static float getBonus(LivingEntity living, int level) {
    BlockPos pos = living.getPosition();
    BlockPos center = pos.down();
    BlockPos[] candidates =
        new BlockPos[] {center, center.north(), center.south(), center.east(), center.west(),
            center.north().east(), center.north().west(), center.south().east(),
            center.south().west()};
    float bonus = 0;

    for (BlockPos candidate : candidates) {

      if (living.world.getBlockState(candidate).getBlock().isIn(Tags.Blocks.STONE)) {
        bonus += BONUS_PER_BLOCK;
      }
    }
    return bonus * level;
  }

  @Override
  public float getProtectionModifier(@Nonnull IModifierToolStack tool, int level,
                                     @Nonnull EquipmentContext context,
                                     @Nonnull EquipmentSlotType slotType, DamageSource source,
                                     float modifierValue) {

    if (!source.isDamageAbsolute() && !source.canHarmInCreative()) {
      modifierValue += getBonus(context.getEntity(), level);
    }
    return modifierValue;
  }

  @Override
  public void addInformation(@Nonnull IModifierToolStack tool, int level,
                             @Nullable PlayerEntity player, @Nonnull List<ITextComponent> tooltip,
                             @Nonnull TooltipKey key, @Nonnull TooltipFlag flag) {
    float bonus;

    if (player != null && key == TooltipKey.SHIFT) {
      bonus = getBonus(player, level);
    } else {
      bonus = level * BONUS_PER_BLOCK;
    }
    EquipmentUtil.addResistanceTooltip(this, tool, bonus, tooltip);
  }
}
