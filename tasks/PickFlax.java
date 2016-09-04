package scripts.spxflaxpicker.tasks;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import scripts.task_framework.framework.Task;
import scripts.tribotapi.game.objects.Objects07;
import scripts.tribotapi.game.timing.Timing07;
import scripts.tribotapi.game.walking.Walking07;

/**
 * Created by Sphiinx on 7/11/2016.
 */
public class PickFlax implements Task {


    @Override
    public boolean validate() {
        final RSObject flax = Objects07.getObject(20, "Flax");
        return flax != null && !Inventory.isFull();
    }

    @Override
    public void execute() {
        if (Player.isMoving())
            return;

        final RSObject flax = Objects07.getObject(20, "Flax");
        if (flax == null)
            return;

        if (!flax.isOnScreen()) {
            Walking07.screenWalkToRSObject(flax);
        } else {
            final RSItem[] inventory_cache = Inventory.getAll();
            if (Clicking.click("Pick", flax))
                Timing07.waitCondition(() -> inventory_cache.length != Inventory.getAll().length, General.random(1500, 2000));
        }
    }

    @Override
    public String toString() {
        return "Picking flax";
    }
}

