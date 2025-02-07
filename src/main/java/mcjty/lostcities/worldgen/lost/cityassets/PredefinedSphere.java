package mcjty.lostcities.worldgen.lost.cityassets;

import mcjty.lostcities.api.ILostCityAsset;
import mcjty.lostcities.worldgen.lost.regassets.PredefinedSphereRE;
import mcjty.lostcities.worldgen.lost.regassets.data.DataTools;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class PredefinedSphere implements ILostCityAsset {

    private ResourceLocation name;
    private ResourceKey<Level> dimension;
    private int chunkX;
    private int chunkZ;
    private int centerX;
    private int centerZ;
    private int radius;

    public PredefinedSphere(PredefinedSphereRE object) {
        name = object.getRegistryName();
        dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(object.getDimension()));
        chunkX = object.getChunkX();
        chunkZ = object.getChunkZ();
        centerX = object.getCenterX();
        centerZ = object.getCenterZ();
        radius = object.getRadius();
    }

    @Override
    public String getName() {
        return DataTools.toName(name);
    }

    @Override
    public ResourceLocation getId() {
        return name;
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterZ() {
        return centerZ;
    }

    public int getRadius() {
        return radius;
    }
}