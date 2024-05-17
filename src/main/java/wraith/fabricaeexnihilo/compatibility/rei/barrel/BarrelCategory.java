package wraith.fabricaeexnihilo.compatibility.rei.barrel;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.DefaultApiModule;
import wraith.fabricaeexnihilo.compatibility.rei.GlyphWidget;
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;

import java.util.ArrayList;
import java.util.List;

import static wraith.fabricaeexnihilo.FabricaeExNihilo.id;


public class BarrelCategory implements DisplayCategory<BarrelDisplay> {
    public static final int MARGIN = 6;
    public static final Identifier GLYPHS = id("textures/gui/rei/glyphs.png");

    @Override
    public CategoryIdentifier<? extends BarrelDisplay> getCategoryIdentifier() {
        return PluginEntry.BARREL;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("fabricaeexnihilo.rei.category.barrel");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(DefaultApiModule.INSTANCE.oakBlocks.barrel());
    }

    @Override
    public int getDisplayWidth(BarrelDisplay display) {
        return 156;
    }

    @Override
    public List<Widget> setupDisplay(BarrelDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));

        var genericOutputs = display.outputs;
        var rowCount = (genericOutputs.size() - 1) / 2;
        for (int i = 0; i < genericOutputs.size(); i++) {
            var col = i % 2;
            var row = i / 2;
            widgets.add(Widgets.createSlot(getPoint(col + 6, row + 1 - rowCount / 2.0, bounds))
                    .entries(genericOutputs.get(i))
                    .markOutput());
        }
        if (!genericOutputs.isEmpty()) {
            var arrowPos = getPoint(4, 1, bounds);
            arrowPos.translate(-6, -1);
            widgets.add(Widgets.createArrow(arrowPos));
        }

        if (display.above != null) widgets.add(Widgets.createSlot(getPoint(2, 0, bounds)).entries(display.above).markInput());
        if (display.below != null) widgets.add(Widgets.createSlot(getPoint(2, 2, bounds)).entries(display.below).markInput());
        if (display.triggerItem != null) widgets.add(Widgets.createSlot(getPoint(0, display.inputFluid == null ? 1 : 0.5, bounds)).entries(display.triggerItem).markInput());
        if (display.inputFluid != null) widgets.add(Widgets.createSlot(getPoint(0, display.triggerItem == null ? 1 : 1.5, bounds)).entries(display.inputFluid).markInput());
        widgets.add(new GlyphWidget(bounds, bounds.getMinX() + MARGIN + 18 + 1, bounds.getMinY() + MARGIN + 18, 16, 16, GLYPHS, 0, 0));
        widgets.add(Widgets.createSlot(getPoint(2, 1, bounds)).entries(PluginEntry.BARRELS.get()).disableBackground().disableHighlight().disableTooltips());

        if (display.nearby != null && display.conversionOutput != null) {
            widgets.add(Widgets.createLabel(getPoint(3.25, 1.35, bounds), Text.translatable("fabricaeexnihilo.rei.category.barrel.nearby")).leftAligned());
            widgets.add(Widgets.createSlot(getPoint(3.25, 2, bounds)).entries(display.nearby).markInput());
            widgets.add(new GlyphWidget(bounds, bounds.getMinX() + MARGIN + 18 * 4 - 1, bounds.getMinY() + MARGIN + 18 * 2, 16, 16, GLYPHS, 0, 0));
            widgets.add(Widgets.createSlot(getPoint(4.75, 2, bounds)).entries(display.conversionOutput).markOutput());
        }

        if (display.compostResult != null) {
            widgets.add(Widgets.createSlot(getPoint(3.25, 0, bounds)).entries(display.compostResult).markOutput());
            widgets.add(Widgets.createLabel(getPoint(4.25, 0.25, bounds), Text.translatable("fabricaeexnihilo.rei.category.barrel.compost", display.compostAmount * 100)).leftAligned());
        }

        if (display.duration != 0) {
            widgets.add(Widgets.createLabel(getPoint(0, 0, bounds), Text.translatable("fabricaeexnihilo.rei.category.barrel.duration", display.duration)).leftAligned());
        }

        return widgets;
    }

    private Point getPoint(double x, double y, Rectangle bounds) {
        return new Point(bounds.getMinX() + MARGIN + 18 * x, bounds.getMinY() + MARGIN + 18 * y);
    }
}
