package wraith.fabricaeexnihilo.compatibility.rei.sieve;

import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import wraith.fabricaeexnihilo.recipe.SieveRecipe;
import wraith.fabricaeexnihilo.util.ItemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SieveRecipeHolder {

    private final List<EntryIngredient> fluids;
    private final List<EntryIngredient> inputs;
    private final EntryIngredient mesh;
    private final HashMap<EntryIngredient, List<Double>> outputs;

    public SieveRecipeHolder(List<EntryIngredient> inputs, List<EntryIngredient> fluids, EntryIngredient mesh, HashMap<EntryIngredient, List<Double>> outputs) {
        this.inputs = inputs;
        this.fluids = fluids;
        this.mesh = mesh;
        this.outputs = outputs;
    }

    public static List<SieveRecipeHolder> fromRecipe(SieveRecipe recipe) {
        var holders = new ArrayList<SieveRecipeHolder>();
    
        var inputs = recipe.getInput().streamEntries().map(EntryIngredients::of).toList();
        var fluids = recipe.getFluid().streamEntries().map(EntryIngredients::of).toList();
        var output = EntryIngredients.of(recipe.getResult());
        var outputs = new HashMap<EntryIngredient, List<Double>>();

        recipe.getRolls().forEach((key, value) -> {
            outputs.clear();
            var mesh = EntryIngredients.of(ItemUtils.getItem(key));
            outputs.put(output, value);
            holders.add(new SieveRecipeHolder(inputs, fluids, mesh, outputs));
        });

        return holders;
    }

    public void add(SieveRecipeHolder recipe) {
        this.outputs.putAll(recipe.outputs);
    }

    public List<EntryIngredient> getFluids() {
        return fluids;
    }

    public List<EntryIngredient> getInputs() {
        return inputs;
    }

    public EntryIngredient getMesh() {
        return mesh;
    }

    public HashMap<EntryIngredient, List<Double>> getOutputs() {
        return outputs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputs, fluids, mesh);
    }

    public List<SieveRecipeHolder> split(int size) {
        var holders = new ArrayList<SieveRecipeHolder>();

        var outputs = new HashMap<EntryIngredient, List<Double>>();

        int i = 0;
        for (var iterator = this.outputs.entrySet().iterator(); iterator.hasNext(); ) {
            var entry = iterator.next();
            var output = entry.getKey();
            var chances = entry.getValue();

            outputs.put(output, chances);
            if (i >= size || !iterator.hasNext()) {
                holders.add(new SieveRecipeHolder(new ArrayList<>(inputs), new ArrayList<>(fluids), mesh, new HashMap<>(outputs)));
                outputs.clear();
                i = 0;
            } else {
                ++i;
            }
        }

        return holders;
    }

}
