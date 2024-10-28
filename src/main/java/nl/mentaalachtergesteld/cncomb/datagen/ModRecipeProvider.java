package nl.mentaalachtergesteld.cncomb.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import nl.mentaalachtergesteld.cncomb.CNCOMB;
import nl.mentaalachtergesteld.cncomb.item.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> pWriter) {

        simpleCookingRecipe(
                pWriter,
                "smelting",
                RecipeSerializer.SMELTING_RECIPE,
                200,
                ModItems.TOBACCO_LEAF.get(),
                ModItems.DRIED_TOBACCO_LEAF.get(),
                RecipeCategory.MISC,
                0.1f
        );
        simpleCookingRecipe(
                pWriter,
                "smoking",
                RecipeSerializer.SMOKING_RECIPE,
                100,
                ModItems.TOBACCO_LEAF.get(),
                ModItems.DRIED_TOBACCO_LEAF.get(),
                RecipeCategory.MISC,
                0.1f
        );

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FILTER.get(), 20)
                .pattern("PPP")
                .pattern("PWP")
                .pattern("PPP")
                .define('P', Items.PAPER)
                .define('W', ItemTags.WOOL)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.CIGARETTE.get())
                .pattern("PTF")
                .define('P', Items.PAPER)
                .define('T', ModItems.DRIED_TOBACCO_LEAF.get())
                .define('F', ModItems.FILTER.get())
                .unlockedBy(getHasName(ModItems.DRIED_TOBACCO_LEAF.get()), has(ModItems.DRIED_TOBACCO_LEAF.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CIGARETTE_PACK.get())
                .pattern("P P")
                .pattern("P P")
                .pattern("PPP")
                .define('P', Items.PAPER)
                .unlockedBy(getHasName(ModItems.CIGARETTE.get()), has(ModItems.CIGARETTE.get()))
                .save(pWriter);
    }

    protected static void simpleCookingRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, String pCookingMethod, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, int pCookingTime, ItemLike pIngredient, ItemLike pResult, RecipeCategory category, float pExperience) {
        SimpleCookingRecipeBuilder.
                generic(
                        Ingredient.of(pIngredient),
                        category,
                        pResult,
                        pExperience,
                        pCookingTime,
                        pCookingSerializer
                ).unlockedBy(
                        getHasName(pIngredient),
                        has(pIngredient)
                )
                .save(
                        pFinishedRecipeConsumer,
                        CNCOMB.MODID + ":" + getItemName(pResult) + "_from_" + pCookingMethod
                );
    }
}
