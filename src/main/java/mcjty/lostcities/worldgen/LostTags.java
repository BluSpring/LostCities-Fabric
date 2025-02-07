package mcjty.lostcities.worldgen;

import mcjty.lostcities.LostCities;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class LostTags {

    public static final ResourceLocation FOLIAGE = new ResourceLocation(LostCities.MODID, "foliage");
    public static final TagKey<Block> FOLIAGE_TAG = TagKey.create(Registry.BLOCK_REGISTRY, FOLIAGE);

    public static final ResourceLocation ROTATABLE = new ResourceLocation(LostCities.MODID, "rotatable");
    public static final TagKey<Block> ROTATABLE_TAG = TagKey.create(Registry.BLOCK_REGISTRY, ROTATABLE);

    public static final ResourceLocation EASY_BREAKABLE = new ResourceLocation(LostCities.MODID, "easybreakable");
    public static final TagKey<Block> EASY_BREAKABLE_TAG = TagKey.create(Registry.BLOCK_REGISTRY, EASY_BREAKABLE);

    public static final ResourceLocation NOT_BREAKABLE = new ResourceLocation(LostCities.MODID, "notbreakable");
    public static final TagKey<Block> NOT_BREAKABLE_TAG = TagKey.create(Registry.BLOCK_REGISTRY, NOT_BREAKABLE);

    public static final ResourceLocation LIGHTS = new ResourceLocation(LostCities.MODID, "lights");
    public static final TagKey<Block> LIGHTS_TAG = TagKey.create(Registry.BLOCK_REGISTRY, LIGHTS);
}
