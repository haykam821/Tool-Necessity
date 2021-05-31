package io.github.haykam821.toolnecessity.block;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public final class ToolNecessityTags {
	public static final String MOD_ID = "toolnecessity";

	private static final Identifier NEEDS_NETHERITE_TOOL_ID = new Identifier(MOD_ID, "needs_netherite_tool");
	public static final Tag<Block> NEEDS_NETHERITE_TOOL = TagRegistry.block(NEEDS_NETHERITE_TOOL_ID);

	private ToolNecessityTags() {
		return;
	}
}
