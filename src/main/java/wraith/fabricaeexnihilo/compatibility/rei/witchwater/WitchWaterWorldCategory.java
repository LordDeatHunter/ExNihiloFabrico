package wraith.fabricaeexnihilo.compatibility.rei.witchwater;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.rei.GlyphWidget;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import wraith.fabricaeexnihilo.modules.witchwater.WitchWaterFluid;

import java.util.ArrayList;
import java.util.List;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class WitchWaterWorldCategory implements DisplayCategory<WitchWaterWorldDisplay> {

    public static final int GLYPH_ARROW_U = 0;
    public static final int GLYPH_ARROW_V = 0;
    public static final int GLYPH_PLUS_U = 3 * 16;
    public static final int GLYPH_PLUS_V = 0;
    public static final int MARGIN = 6;
    public static final int GLYPH_PLUS_X = MARGIN;
    public static final int GLYPH_ARROW_X = MARGIN + 18;
    public static final int OUTPUT_X = GLYPH_ARROW_X + 18;
    public static final int FLUID_X = MARGIN;
    public static final int OUTPUT_SLOTS_X = 8;
    public static final int OUTPUT_SLOTS_Y = 3;
    public static final int MAX_OUTPUTS = OUTPUT_SLOTS_X * OUTPUT_SLOTS_Y;
    public static final int HEIGHT = OUTPUT_SLOTS_Y * 18 + MARGIN * 2;
    public static final int GLYPH_PLUS_Y = HEIGHT / 2 - 9;
    public static final int GLYPH_ARROW_Y = GLYPH_PLUS_Y;
    public static final int FLUID_Y = GLYPH_PLUS_Y + 18;
    public static final int WITCH_Y = GLYPH_PLUS_Y - 18;
    public static final int OUTPUT_Y = MARGIN;
    public static final int WIDTH = (OUTPUT_SLOTS_X + 2) * 18 + MARGIN * 2;
    public static final int WITCH_X = MARGIN;
    public static final Identifier GLYPHS = id("textures/gui/rei/glyphs.png");

    @Override
    public CategoryIdentifier<? extends WitchWaterWorldDisplay> getCategoryIdentifier() {
        return PluginEntry.WITCH_WATER_WORLD;
    }

    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }

    @Override
    public int getDisplayWidth(WitchWaterWorldDisplay display) {
        return WIDTH;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(WitchWaterFluid.BUCKET);
    }

    @Override
    public Text getTitle() {
        return Text.translatable("fabricaeexnihilo.rei.category.witch_water.world");
    }

    @Override
    public List<Widget> setupDisplay(WitchWaterWorldDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));

        var glyphPlus = new GlyphWidget(bounds, bounds.getMinX() + GLYPH_PLUS_X, bounds.getMinY() + GLYPH_PLUS_Y, 16, 16, GLYPHS, GLYPH_PLUS_U, GLYPH_PLUS_V);
        var glyphArrow = new GlyphWidget(bounds, bounds.getMinX() + GLYPH_ARROW_X, bounds.getMinY() + GLYPH_ARROW_Y, 16, 16, GLYPHS, GLYPH_ARROW_U, GLYPH_ARROW_V);
        widgets.add(glyphPlus);
        widgets.add(glyphArrow);

        var witchWater = EntryIngredients.of(WitchWaterFluid.STILL);
        var fluids = display.input;
        var results = display.outputs;

        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + WITCH_X, bounds.getMinY() + WITCH_Y)).entries(witchWater));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + FLUID_X, bounds.getMinY() + FLUID_Y)).entries(fluids));

        for (int y = 0; y < MAX_OUTPUTS; ++y) {
            var slot = Widgets.createSlot(new Point(bounds.getMinX() + OUTPUT_X + (y % OUTPUT_SLOTS_X) * 18, bounds.getMinY() + OUTPUT_Y + (y / OUTPUT_SLOTS_X) * 18));
            if (y < results.size()) {
                slot.entries(results.get(y));
            }
            widgets.add(slot);
        }

        return widgets;
    }

}