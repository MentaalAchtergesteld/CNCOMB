package nl.mentaalachtergesteld.cncomb.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import nl.mentaalachtergesteld.cncomb.CNCOMB;
import nl.mentaalachtergesteld.cncomb.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CNCOMB.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.CIGARETTE);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(
                item.getId().getPath(),
                new ResourceLocation("item/generated")
        ).texture("layer0", new ResourceLocation(CNCOMB.MODID, "item/" + item.getId().getPath()));
    }
}
