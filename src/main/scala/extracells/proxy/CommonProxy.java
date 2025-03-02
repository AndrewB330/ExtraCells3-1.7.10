package extracells.proxy;

import appeng.api.AEApi;
import appeng.api.IAppEngApi;
import appeng.api.recipes.IRecipeHandler;
import appeng.api.recipes.IRecipeLoader;
import cpw.mods.fml.common.registry.GameRegistry;
import extracells.registries.BlockEnum;
import extracells.registries.ItemEnum;
import extracells.tileentity.*;

import java.io.*;

public class CommonProxy {

    private class ExternalRecipeLoader implements IRecipeLoader {

        @Override
        public BufferedReader getFile(String path) throws Exception {
            return new BufferedReader(new FileReader(new File(path)));
        }
    }

    private class InternalRecipeLoader implements IRecipeLoader {

        @Override
        public BufferedReader getFile(String path) throws Exception {
            InputStream resourceAsStream = getClass().getResourceAsStream("/assets/extracells/recipes/" + path);
            InputStreamReader reader = new InputStreamReader(resourceAsStream, "UTF-8");
            return new BufferedReader(reader);
        }
    }

    public void addRecipes(File configFolder) {
        IRecipeHandler recipeHandler = AEApi.instance().registries().recipes().createNewRecipehandler();
        File externalRecipe = new File(configFolder.getPath() + File.separator + "AppliedEnergistics2" + File.separator + "extracells.recipe");
        if (externalRecipe.exists()) {
            recipeHandler.parseRecipes(new ExternalRecipeLoader(), externalRecipe.getPath());
        } else {
            recipeHandler.parseRecipes(new InternalRecipeLoader(), "main.recipe");
        }
        recipeHandler.injectRecipes();
    }

    public void registerBlocks() {
        for (BlockEnum current : BlockEnum.values()) {
            GameRegistry.registerBlock(current.getBlock(), current.getItemBlockClass(), current.getInternalName());
        }
    }

    public void registerItems() {
        for (ItemEnum current : ItemEnum.values()) {
            GameRegistry.registerItem(current.getItem(), current.getInternalName());
        }
    }

    public void registerMovables() {
        IAppEngApi api = AEApi.instance();
        api.registries().movable().whiteListTileEntity(TileEntityCertusTank.class);
        api.registries().movable().whiteListTileEntity(TileEntityFluidInterface.class);
    }

    public void registerRenderers() {
        // Only Clientside
    }

    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityCertusTank.class, "tileEntityCertusTank");
        GameRegistry.registerTileEntity(TileEntityFluidInterface.class, "tileEntityFluidInterface");
    }

    public boolean isClient() {
        return false;
    }

    public boolean isServer() {
        return true;
    }
}
