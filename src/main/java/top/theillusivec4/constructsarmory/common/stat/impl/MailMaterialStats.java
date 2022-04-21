package top.theillusivec4.constructsarmory.common.stat.impl;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.materials.stats.BaseMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import top.theillusivec4.constructsarmory.ConstructsArmoryMod;
import top.theillusivec4.constructsarmory.api.ArmorMaterialStatsIdentifiers;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@With
public class MailMaterialStats extends BaseMaterialStats {

  public static final MaterialStatsId ID =
      new MaterialStatsId(ConstructsArmoryMod.getResource("mail"));
  public static final MailMaterialStats DEFAULT = new MailMaterialStats();

  private static final String DURABILITY_PREFIX =
      makeTooltipKey(TConstruct.getResource("durability"));
  private static final String ARMOR_PREFIX =
      makeTooltipKey(TConstruct.getResource("armor"));

  private static final ITextComponent DURABILITY_DESCRIPTION =
      makeTooltip(ConstructsArmoryMod.getResource("mail.durability.description"));
  private static final ITextComponent ARMOR_DESCRIPTION =
      makeTooltip(ConstructsArmoryMod.getResource("mail.armor.description"));
  private static final List<ITextComponent> DESCRIPTION =
      ImmutableList.of(DURABILITY_DESCRIPTION, ARMOR_DESCRIPTION,
          ToolStats.ARMOR_TOUGHNESS.getDescription(),
          ToolStats.KNOCKBACK_RESISTANCE.getDescription());

  private float durability = 1.0f;
  private float armor = 1.0f;
  private float armorToughness;
  private float knockbackResistance;

  @Override
  public void encode(PacketBuffer buffer) {
    buffer.writeFloat(this.durability);
    buffer.writeFloat(this.armor);
    buffer.writeFloat(this.armorToughness);
    buffer.writeFloat(this.knockbackResistance);
  }

  @Override
  public void decode(PacketBuffer buffer) {
    this.durability = buffer.readFloat();
    this.armor = buffer.readFloat();
    this.armorToughness = buffer.readFloat();
    this.knockbackResistance = buffer.readFloat();
  }

  @Override
  @Nonnull
  public MaterialStatsId getIdentifier() {
    return ArmorMaterialStatsIdentifiers.MAIL;
  }

  @Override
  @Nonnull
  public List<ITextComponent> getLocalizedInfo() {
    List<ITextComponent> list = new ArrayList<>();
    list.add(formatDurability(this.durability));
    list.add(formatArmor(this.armor));
    list.add(ToolStats.ARMOR_TOUGHNESS.formatValue(this.armorToughness));
    list.add(ToolStats.KNOCKBACK_RESISTANCE.formatValue(this.knockbackResistance));
    return list;
  }

  @Override
  @Nonnull
  public List<ITextComponent> getLocalizedDescriptions() {
    return DESCRIPTION;
  }

  public static ITextComponent formatDurability(float durability) {
    return IToolStat.formatColoredMultiplier(DURABILITY_PREFIX, durability);
  }

  public static ITextComponent formatArmor(float armor) {
    return IToolStat.formatColoredMultiplier(ARMOR_PREFIX, armor);
  }
}
