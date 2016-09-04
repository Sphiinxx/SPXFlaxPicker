package scripts.spxflaxpicker.tasks;

import org.tribot.api.General;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSObject;
import scripts.spxflaxpicker.data.enums.Location;
import scripts.task_framework.framework.Task;
import scripts.tribotapi.game.objects.Objects07;

/**
 * Created by Sphiinx on 7/11/2016.
 */
public class WalkToFlax implements Task {


    @Override
    public boolean validate() {
        final RSObject flax = Objects07.getObject(20, "Flax");
        return flax == null && !Inventory.isFull();
    }

    @Override
    public void execute() {
        WebWalking.walkTo(Location.FLAX_FIELD.getLocation(), new Condition() {
            @Override
            public boolean active() {
                final RSObject flax = Objects07.getObject(20, "Flax");
                return flax != null && flax.isOnScreen();
            }
        }, General.random(250, 500));
    }

    @Override
    public String toString() {
        return "Walking to flax fields";
    }
}

