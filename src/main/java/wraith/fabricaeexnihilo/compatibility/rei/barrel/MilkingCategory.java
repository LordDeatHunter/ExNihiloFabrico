package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.rei.GlyphWidget;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;

import java.util.ArrayList;
import java.util.List;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;

public class MilkingCategory implements DisplayCategory<MilkingDisplay> {

    public static final Identifier ARROW = id("textures/gui/rei/glyphs.png");
    public static final int MARGIN = 6;
    public static final int HEIGHT = 3 * 18 + MARGIN * 2;
    public static final int ARROW_Y = HEIGHT / 2;
    public static final int BARRELS_Y = ARROW_Y;
    public static final int ABOVE_Y = BARRELS_Y - 18;
    public static final int OUTPUT_Y = ARROW_Y;
    public static final int WIDTH = 8 * 18 + MARGIN * 2;
    public static final int ARROW_X = WIDTH / 2 - 9;
    public static final int BARRELS_X = ARROW_X - 18;
    public static final int ABOVE_X = BARRELS_X;
    public static final int OUTPUT_X = ARROW_X + 18;
    private final ItemStack icon;
    private final String name;

    public MilkingCategory(ItemStack icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    @Override
    public CategoryIdentifier<? extends MilkingDisplay> getCategoryIdentifier() {
        return PluginEntry.MILKING;
    }

    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }

    @Override
    public int getDisplayWidth(MilkingDisplay display) {
        return WIDTH;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(icon);
    }

    @Override
    public Text getTitle() {
        return Text.translatable(name);
    }

    @Override
    public List<Widget> setupDisplay(MilkingDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + ARROW_X, bounds.getMinY() + ARROW_Y, 16, 16, ARROW, 0, 0));

        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + ABOVE_X, bounds.getMinY() + ABOVE_Y)).entries(display.entity).markInput());
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + BARRELS_X, bounds.getMinY() + BARRELS_Y)).entries(PluginEntry.BARRELS.get()));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + OUTPUT_X, bounds.getMinY() + OUTPUT_Y)).entries(display.result).markOutput());

        //TODO: some day figure out how to render a little entity over the barrel instead of an egg :/ (steal code from witchwater entity recipe?)
//        (display.recipe.entity.flatten().firstOrNull())?.let{
//            widgets.add(EntityWidget(bounds.getMinX(), bounds.getMinY(), 36, 36, it))
//        }

        var text = Widgets.createLabel(new Point(0, 0), Text.literal(String.valueOf(display.amount)));
        text.setPoint(new Point(bounds.getMaxX() - MARGIN - text.getBounds().getMaxX(), bounds.getMinY() + MARGIN));
        widgets.add(text);

        return widgets;
    }

}