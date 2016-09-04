package scripts.spxflaxpicker;

import com.allatori.annotations.DoNotRename;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.*;
import scripts.spxflaxpicker.data.Vars;
import scripts.spxflaxpicker.tasks.DepositItems;
import scripts.spxflaxpicker.tasks.PickFlax;
import scripts.spxflaxpicker.tasks.WalkToFlax;
import scripts.task_framework.framework.Task;
import scripts.tribotapi.AbstractScript;
import scripts.tribotapi.game.utiity.Utility07;
import scripts.tribotapi.gui.GUI;
import scripts.tribotapi.painting.paint.Calculations;
import scripts.tribotapi.painting.paint.enums.DataPosition;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Sphiinx on 9/3/2015.
 * Re-written by Sphiinx on 7/28/2016.
 */
@ScriptManifest(authors = "Sphiinx", category = "Money making", name = "[SPX] FlaxPicker", version = 1.5)
@DoNotRename
public class Main extends AbstractScript implements Painting, MessageListening07, EventBlockingOverride, Ending {

    @Override
    protected GUI getGUI() {
        try {
            return new GUI(new URL("http://spxscripts.com/spxflaxpicker/GUI.fxml"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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