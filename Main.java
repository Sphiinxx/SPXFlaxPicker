package scripts.SPXFlax;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;


/**
 * Created by Sphiinx on 9/3/2015.
 */
@SuppressWarnings("ALL")
@ScriptManifest(authors = "Sphiinx", category = "Money making", name = "SPX FlaxPicker", description = "Created by Sphiinx\nOne of TRiBots Fastest Flax Picker. Start it wherever you want.\n\n\n-FEATURES-\n850+ Flax/Hour\nSexy Paint\nCustom Paths\nWebWalking\nDynamic Clicking\nIntelligent Banking\n\n\n-Supported Locations-\nSeers Village\nMore coming soon\n\n\n-Coming Soon-\nFlax Picking/Spinning then Banking")
public class Main extends Script implements Painting, MessageListening07 {

    private int FLAX_COUNT = 0;
    private final long startTime = System.currentTimeMillis();
    private final Color color1 = new Color(0, 169, 194);
    private final Color color2 = new Color(255, 255, 255);
    private final Font font1 = new Font("Segoe Script", 0, 20);
    private final Font font2 = new Font("Arial", 0, 15);
    private final Image img1 = getImage("http://i.imgur.com/fRrLAWr.png");
    private final RenderingHints antialiasing = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    private final int RANDOM_MOUSE = General.random(100, 120);
    private final int RANDOM_ANGLE = General.random(65, 100);
    private String MODE = "";
    private String STATUS = "";
    private boolean GUI_COMPLETE = false;
    private boolean shouldBank = false;
    private boolean shouldSpin = false;
    RSObject[] flax;
    RSObject nearestFlax;
    private final RSTile FLAX_FIELDS = new RSTile(General.random(2741, 2746), General.random(3442, 3447));
    private final RSTile[] BANK_PATH = new RSTile[]{
            new RSTile(2736, 3445, 0),
            new RSTile(2731, 3456, 0),
            new RSTile(2727, 3468, 0),
            new RSTile(2726, 3480, 0),
            new RSTile(2725, 3492, 0)
    };
    private final RSArea BANK = new RSArea(new RSTile[]{
            new RSTile(2721, 3493, 0),
            new RSTile(2729, 3493, 0),
            new RSTile(2730, 3490, 0),
            new RSTile(2721, 3490, 0)
    });
    private final RSTile[] SPINNING_PATH = new RSTile[] {
            new RSTile(2737, 3444, 0),
            new RSTile(2730, 3454, 0),
            new RSTile(2726, 3466, 0),
            new RSTile(2715, 3472, 0)
    };
    private final RSArea SPINNING_AREA = new RSArea(new RSTile[]{
            new RSTile(2715, 3472, 0),
            new RSTile(2713, 3472, 0),
            new RSTile(2713, 3470, 0),
            new RSTile(2715, 3471, 0)
    });

    // Start of the script run
    @Override
    public void run() {
        Mouse.setSpeed(RANDOM_MOUSE);
        General.useAntiBanCompliance(true);
        // Start of the script loop
        while (true) {
            sleep(100, 110);
            RSTile pos = Player.getPosition();
            if (Inventory.isFull()) {
                bank();
            } else if (pos.distanceTo(FLAX_FIELDS) > 20 && FLAX_FIELDS != null && pos != null) {
                walkToFlax();
            } else {
                pickFlax();
            }
        }
        // End of the script loop
    }
    // End of the script run

    private void emptyInventory() {
        STATUS = "Emptying Inventory";
        if (getInventoryCount() >= 10 && !Banking.isBankScreenOpen()) {
            bank();
        } else if (getInventoryCount() >= 10 && Banking.isBankScreenOpen()) {
            Banking.depositAll();
            Banking.close();
        }
    }

    private void bank() {
        STATUS = "Walking to Bank";
        Walking.walkPath(BANK_PATH);
        if (!BANK.contains(Player.getPosition())) {
            Walking.walkPath(BANK_PATH);
        } else if (BANK.contains(Player.getPosition()) && !Banking.isBankScreenOpen()) {
            STATUS = "Opening Bank";
            Banking.openBank();
            if (Banking.isBankScreenOpen()) {
                STATUS = "Depositing Items";
                Banking.depositAll();
            }
        }
    }

    private void walkToFlax() {
        STATUS = "Walking to Flax";
        WebWalking.walkTo(FLAX_FIELDS);
    }

    private void pickFlax() {
        STATUS = "Picking Flax";
        flax = Objects.findNearest(20, "Flax");
        if (Camera.getCameraAngle() < 50) {
            Camera.setCameraAngle(RANDOM_ANGLE);
        }
        if (flax.length > 0) {
            nearestFlax = flax[0];
            if (Player.getAnimation() == -1 && !Player.isMoving()) {
                if (DynamicClicking.clickRSObject(flax[0], "Pick")) {
                    sleep(400, 500);
                } else if (nearestFlax != null && !nearestFlax.isOnScreen()) {
                    Walking.walkTo(nearestFlax.getPosition());
                    println("No Flax on screen, we're now walking to some.");
                    nearestFlax.getModel().click();
                }
            }
        }
    }

    private int getInventoryCount() {
        return Inventory.getAll().length;
    }

    public void serverMessageReceived(String logs) {
        if (logs.equalsIgnoreCase(new String("You pick some flax."))) {
            FLAX_COUNT++;
        }
    }

    public void clanMessageReceived(String s, String s1) {
    }

    public void tradeRequestReceived(String s) {
    }

    public void personalMessageReceived(String s, String s1) {
    }

    public void duelRequestReceived(String s, String s1) {
    }

    public void playerMessageReceived(String s, String s1) {
    }

    // PAINT START
    // PAINT START
    // PAINT START
    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
    }

    public void onPaint(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;
        g.setRenderingHints(antialiasing);

        if (Login.getLoginState() == Login.STATE.INGAME) {
            long timeRan = System.currentTimeMillis() - startTime;

            g.drawImage(img1, 2, 200, null);
            g.setFont(font1);
            g.setColor(color1);
            g.drawString("- Flax Picker", 68, 226);
            g.setFont(font2);
            g.setColor(color2);
            g.drawString("Runtime: " + Timing.msToString(timeRan), 11, 252);
            g.drawString("Flax: " + FLAX_COUNT, 11, 272);
            g.drawString("Status: " + STATUS, 11, 292);
        }
    }
    // PAINT END
    // PAINT END
    // PAINT END


}
// End of the script.