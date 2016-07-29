package scripts.SPXFlaxPicker.tasks;

import org.tribot.api.General;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.WebWalking;
import scripts.SPXFlaxPicker.data.enums.Location;
import scripts.TaskFramework.framework.Task;
import scripts.TribotAPI.game.banking.Banking07;
import scripts.TribotAPI.game.timing.Timing07;

/**
 * Created by Sphiinx on 7/11/2016.
 */
public class DepositItems implements Task {


    @Override
    public boolean validate() {
        return Inventory.isFull();
    }

    @Override
    public void execute() {
        if (!Banking.isBankScreenOpen()) {
            if (Banking07.isInBank()) {
                if (Banking.openBank())
                    Timing07.waitCondition(Banking::isBankScreenOpen, General.random(1500, 2000));
            } else {
                WebWalking.walkTo(Location.SEERS_BANK.getLocation(), new Condition() {
                    @Override
                    public boolean active() {
                        return Banking07.isInBank();
                    }
                }, General.random(250, 500));
            }
        } else {
            if (Banking.depositAll() > 0)
                Timing07.waitCondition(() -> Inventory.getAll().length <= 0, General.random(1500, 2000));
        }
    }

    @Override
    public String toString() {
        return "Going to deposit items";
    }
}

