package io.github.haykam821.toolnecessity.block;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.logging.log4j.LogManager;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public final class ToolNecessityCustomTagManager {
	private static final int LAST_NAMED_MINING_LEVEL = MiningLevels.NETHERITE;

	private static final List<Tag<Block>> TAGS = new ArrayList<>();

	private ToolNecessityCustomTagManager() {
		return;
	}

	/**
	 * Gets a tag index from its needed mining level.
	 * @return the index corresponding to its needed mining level
	 */
	private static int getIndex(int miningLevel) {
		return miningLevel - LAST_NAMED_MINING_LEVEL - 1;
	}

	/**
	 * Gets a mining level from the index of a tag.
	 * @return the mining level corresponding to the tag
	 */
	private static int getMiningLevel(int index) {
		return index + LAST_NAMED_MINING_LEVEL + 1;
	}

	/**
	 * Gets an identifier for a block tag based on the given mining level.
	 */
	private static Identifier getTagId(int miningLevel) {
		return new Identifier(ToolNecessityTags.MOD_ID, "needs_mining_level_" + miningLevel + "_tool");
	}

	/**
	 * Creates a block tag with an identifier based on the given mining level.
	 */
	private static Tag<Block> createTag(int miningLevel) {
		return TagRegistry.block(getTagId(miningLevel));
	}

	private static void fillTags(int miningLevel) {
		int index = getIndex(miningLevel);

		for (int addedIndex = TAGS.size(); addedIndex < index; addedIndex++) {
			TAGS.add(createTag(getMiningLevel(addedIndex)));
		}
	}

	public static boolean isSuitableForCustomTags(BlockState state, int miningLevel) {
		// Ensure all tags are available
		fillTags(miningLevel);

		if (miningLevel > TAGS.size()) {
			int index = getIndex(miningLevel);

			for (int addedIndex = TAGS.size(); addedIndex < index; addedIndex++) {
				TAGS.add(createTag(getMiningLevel(addedIndex)));
			}
		}

		// Iterate backwards over all tags
		for (int index = TAGS.size() - 1; index >= 0; index--) {
			int currentMiningLevel = getMiningLevel(index);
			if (miningLevel < currentMiningLevel && state.isIn(TAGS.get(index))) {
				return false;
			}
		}

		return true;
	}

	static {
		try {
			File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), ToolNecessityTags.MOD_ID + ".json");;
			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			JsonObject json = new JsonParser().parse(reader).getAsJsonObject();

			fillTags(Math.max(json.get("ensuredMiningLevel").getAsInt(), 0));
		} catch (IOException exception) {
			LogManager.getLogger("Tool Necessity").warn("Failed to read tool necessity config, defaulting to 10", exception);
			fillTags(10);
		}
	}
}
