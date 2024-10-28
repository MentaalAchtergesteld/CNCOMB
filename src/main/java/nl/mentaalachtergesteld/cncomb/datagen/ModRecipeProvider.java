package nl.mentaalachtergesteld.cncomb.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import nl.mentaalachtergesteld.cncomb.CNCOMB;
import nl.mentaalachtergesteld.cncomb.item.ModItems;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {

        simpleCookingRecipe(
                pWriter,
                "smelting",
                RecipeSerializer.SMELTING_RECIPE,
                200,
                ModItems.TOBACCO_LEAF.get(),
                ModItems.DRIED_TOBACCO_LEAF.get(),
                0.1f
        );
        simpleCookingRecipe(
                pWriter,
                "smoking",
                RecipeSerializer.SMOKING_RECIPE,
                100,
                ModItems.TOBACCO_LEAF.get(),
                ModItems.DRIED_TOBACCO_LEAF.get(),
                0.1f
        );

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CIGARETTE.get())
                .requires(ModItems.DRIED_TOBACCO_LEAF.get())
                .requires(Items.PAPER)
                .unlockedBy(getHasName(ModItems.DRIED_TOBACCO_LEAF.get()), has(ModItems.DRIED_TOBACCO_LEAF.get()))
                .save(pWriter);
    }

    protected static void simpleCookingRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, String pCookingMethod, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, int pCookingTime, ItemLike pIngredient, ItemLike pResult, float pExperience) {
        SimpleCookingRecipeBuilder.
                generic(
                        Ingredient.of(pIngredient),
                        RecipeCategory.FOOD,
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
