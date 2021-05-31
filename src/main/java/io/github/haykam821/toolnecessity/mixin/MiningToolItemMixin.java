package io.github.haykam821.toolnecessity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.toolnecessity.block.ToolNecessityCustomTagManager;
import io.github.haykam821.toolnecessity.block.ToolNecessityTags;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;

@Mixin(MiningToolItem.class)
public class MiningToolItemMixin extends ToolItem {
	private MiningToolItemMixin(ToolMaterial material, Item.Settings settings) {
		super(material, settings);
	}

	@Inject(method = "isSuitableFor", at = @At("HEAD"), cancellable = true)
	public void modifyIsSuitableFor(BlockState state, CallbackInfoReturnable<Boolean> ci) {
		int miningLevel = this.getMaterial().getMiningLevel();

		if (!ToolNecessityCustomTagManager.isSuitableForCustomTags(state, miningLevel)) {
			ci.setReturnValue(false);
		} else if (miningLevel < MiningLevels.NETHERITE && state.isIn(ToolNecessityTags.NEEDS_NETHERITE_TOOL)) {
			ci.setReturnValue(false);
		}
	}
}
