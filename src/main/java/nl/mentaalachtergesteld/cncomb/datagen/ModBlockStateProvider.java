package nl.mentaalachtergesteld.cncomb.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import nl.mentaalachtergesteld.cncomb.CNCOMB;
import nl.mentaalachtergesteld.cncomb.block.ModBlocks;
import nl.mentaalachtergesteld.cncomb.block.custom.TobaccoCropBlock;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CNCOMB.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        makeCropStates((CropBlock) ModBlocks.TOBACCO_CROP.get(), "tobacco_crop_stage", "tobacco_plant_stage");


        simpleBlock(
                ModBlocks.WILD_TOBACCO.get(),
                new ConfiguredModel(models()
                                .crop("wild_tobacco", new ResourceLocation(CNCOMB.MODID, "block/tobacco_plant_stage7"))
                                .renderType("cutout"))
        );
    }

    private void makeCropStates(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> cropStates(state, block, modelName, textureName);
        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] cropStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + block.getAge(state),
                new ResourceLocation(CNCOMB.MODID, "block/" + textureName + state.getValue(((TobaccoCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return  models;
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
