package scripts.SPXFlaxPicker;

import com.allatori.annotations.DoNotRename;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.*;
import scripts.SPXFlaxPicker.data.Vars;
import scripts.SPXFlaxPicker.tasks.DepositItems;
import scripts.SPXFlaxPicker.tasks.PickFlax;
import scripts.SPXFlaxPicker.tasks.WalkToFlax;
import scripts.TaskFramework.framework.Task;
import scripts.TribotAPI.AbstractScript;
import scripts.TribotAPI.game.utiity.Utility07;
import scripts.TribotAPI.gui.GUI;
import scripts.TribotAPI.painting.paint.Calculations;
import scripts.TribotAPI.painting.paint.enums.DataPosition;

import java.awt.*;

/**
 * Created by Sphiinx on 9/3/2015.
 * Re-written by Sphiinx on 7/28/2016.
 */
@ScriptManifest(authors = "Sphiinx", category = "Money making", name = "[SPX] FlaxPicker", version = 1.5)
@DoNotRename
public class Main extends AbstractScript implements Painting, MessageListening07, EventBlockingOverride, Ending {

    @Override
    protected GUI getGUI() {
        return null;
    }

    @Override
    public void run() {
        Vars.reset();
        super.run();
    }

    @Override
    public void addTasks(Task... tasks) {
        super.addTasks(new DepositItems(), new WalkToFlax(), new PickFlax());
    }

    @Override
    public void onPaint(Graphics g) {
        super.onPaint(g);
        paint_manager.drawGeneralData("Flax picked: ", Vars.get().flax_picked + Calculations.getPerHour(Vars.get().flax_picked, this.getRunningTime()), DataPosition.TWO, g);
        paint_manager.drawGeneralData("Status: ", task_manager.getStatus() + Utility07.getLoadingPeriods(), DataPosition.THREE, g);
    }

    @Override
    public void serverMessageReceived(String s) {
        if (s.contains("You pick some flax."))
            Vars.get().flax_picked++;
    }

    @Override
    public void clanMessageReceived(String s, String s1) {
    }

    @Override
    public void tradeRequestReceived(String s) {
    }

    @Override
    public void personalMessageReceived(String s, String s1) {
    }

    @Override
    public void duelRequestReceived(String s, String s1) {
    }

    @Override
    public void playerMessageReceived(String s, String s1) {
    }

    @Override
    public void onEnd() {
        SignatureData.sendSignatureData(this.getRunningTime() / 1000, Vars.get().flax_picked);
        super.onEnd();
    }

}