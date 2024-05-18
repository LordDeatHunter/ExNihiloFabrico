package wraith.fabricaeexnihilo.compatibility.rei.crucible;

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
import wraith.fabricaeexnihilo.compatibility.rei.PluginEntry;
import java.util.ArrayList;
import java.util.List;

public class CrucibleHeatCategory implements DisplayCategory<CrucibleHeatDisplay> {
    private final ItemStack icon;
    private final String name;

    public CrucibleHeatCategory(ItemStack icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    @Override
    public CategoryIdentifier<? extends CrucibleHeatDisplay> getCategoryIdentifier() {
        return PluginEntry.HEATING;
    }

    @Override
    public int getDisplayHeight() {
        return 49;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(icon);
    }

    @Override
    public Text getTitle() {
        return Text.translatable(this.name);
    }

    @Override
    public List<Widget> setupDisplay(CrucibleHeatDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));

        var heat = display.heat;

        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 17);
        widgets.add(
                Widgets.createLabel(new Point(bounds.x + 26, bounds.getCenterY()), Text.translatable("fabricaeexnihilo.rei.category.crucible_heat.speed", heat))
                        .color(0xFF404040, 0xFFBBBBBB)
                        .noShadow()
                        .leftAligned()
        );
        widgets.add(Widgets.createBurningFire(new Point(bounds.x + 6, startPoint.y + 1)).animationDurationTicks(100F / heat));
        widgets.add(Widgets.createSlot(new Point(bounds.x + 6, startPoint.y + 18)).entries(display.source).markInput());
        return widgets;
    }

}