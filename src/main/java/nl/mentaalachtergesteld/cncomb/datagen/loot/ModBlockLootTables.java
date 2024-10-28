package nl.mentaalachtergesteld.cncomb.datagen.loot;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.RegistryObject;
import nl.mentaalachtergesteld.cncomb.block.ModBlocks;
import nl.mentaalachtergesteld.cncomb.block.custom.TobaccoCropBlock;
import nl.mentaalachtergesteld.cncomb.item.ModItems;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        LootItemCondition.Builder lootItemConditionBuilder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks.TOBACCO_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TobaccoCropBlock.AGE, 7));

        this.add(ModBlocks.TOBACCO_CROP.get(), createCropDrops(
                ModBlocks.TOBACCO_CROP.get(),
                ModItems.TOBACCO_LEAF.get(),
                ModItems.TOBACCO_SEEDS.get(),
                lootItemConditionBuilder
        ));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
