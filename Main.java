package scripts.SPXFlaxPicker;

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
import org.tribot.script.interfaces.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;


/**
 * Created by Sphiinx on 9/3/2015.
 */
@SuppressWarnings("ALL")
@ScriptManifest(authors = "Sphiinx", category = "Money making", name = "[SPX] FlaxPicker", version = 1.3)
public class Main extends Script implements Painting, MessageListening07, MousePainting, MouseSplinePainting, Ending{

    private int FLAX_COUNT = 0;
    long timeRan;
    private double version;
    private final long startTime = System.currentTimeMillis();
    public final long START_TIME = System.currentTimeMillis();
    public final Color RED_COLOR = new Color(214, 39, 39, 240);
    public final Color BLACK_COLOR = new Color(0, 0, 0, 100);
    public final Font TITLE_FONT = new Font("Arial Bold", 0, 15);
    public final Font TEXT_FONT = new Font("Arial", 0, 12);
    public final RenderingHints ANTIALIASING = new RenderingHints(
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
        version = getClass().getAnnotation(ScriptManifest.class).version();
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
        g.setRenderingHints(ANTIALIASING);

        if (Login.getLoginState() == Login.STATE.INGAME) {
            timeRan = System.currentTimeMillis() - startTime;

            g.setColor(BLACK_COLOR);
            g.fillRoundRect(11, 220, 200, 110, 8, 8); // SPXMouse07 background
            g.setColor(RED_COLOR);
            g.drawRoundRect(9, 218, 202, 112, 8, 8); // Red outline
            g.fillRoundRect(13, 223, 194, 22, 8, 8); // Title background
            g.setFont(TITLE_FONT);
            g.setColor(Color.WHITE);
            g.drawString("[SPX] Flax Picker", 18, 239);
            g.setFont(TEXT_FONT);
            g.drawString("Runtime: " + Timing.msToString(timeRan), 14, 260);
            g.drawString("Flax: " + FLAX_COUNT, 14, 276);
            g.drawString("Status: " + STATUS, 14, 293);
            g.drawString("v" + version, 185, 326);
        }
    }
    // PAINT END
    // PAINT END
    // PAINT END

    @Override
    public void paintMouse(Graphics g, Point point, Point point1) {
        g.setColor(BLACK_COLOR);
        g.drawRect(Mouse.getPos().x - 13, Mouse.getPos().y - 13, 27, 27); // Square rectangle Stroke
        g.drawRect(Mouse.getPos().x, Mouse.getPos().y - 512, 1, 500); // Top y axis Line Stroke
        g.drawRect(Mouse.getPos().x, Mouse.getPos().y + 13, 1, 500); // Bottom y axis Line Stroke
        g.drawRect(Mouse.getPos().x + 13, Mouse.getPos().y, 800, 1); // Right x axis line Stroke
        g.drawRect(Mouse.getPos().x - 812, Mouse.getPos().y, 800, 1); // left x axis line Stroke
        g.fillOval(Mouse.getPos().x - 3, Mouse.getPos().y - 3, 7, 7); // Center dot stroke
        g.setColor(RED_COLOR);
        g.drawRect(Mouse.getPos().x - 12, Mouse.getPos().y - 12, 25, 25); // Square rectangle
        g.drawRect(Mouse.getPos().x, Mouse.getPos().y - 512, 0, 500); // Top y axis Line
        g.drawRect(Mouse.getPos().x, Mouse.getPos().y + 13, 0, 500); // Bottom y axis Line
        g.drawRect(Mouse.getPos().x + 13, Mouse.getPos().y, 800, 0); // Right x axis line
        g.drawRect(Mouse.getPos().x - 812, Mouse.getPos().y, 800, 0); // left x axis line
        g.fillOval(Mouse.getPos().x - 2, Mouse.getPos().y - 2, 5, 5); // Center dot
    }

    @Override
    public void paintMouseSpline(Graphics graphics, ArrayList<Point> arrayList) {
    }

    public static boolean sendSignatureData(long runtimeInSeconds, int flaxCount) {
        String privateKey = "C5A1B33F62AC2D81";
        String initVector = "E6135CCC2FE8BE8C";
        try {
            String data = initVector + "," + General.getTRiBotUsername() + "," + runtimeInSeconds + "," + flaxCount;
            String token = Base64.getEncoder().encodeToString(data.getBytes());

            URL url = new URL("http://www.spxscripts.com/spxflaxpicker/input.php?token="+token);
            URLConnection conn = url.openConnection();

            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            in.readLine();
            in.close();
            return true;

        } catch (Exception e) {
            General.println(e.getMessage());
        }
        return false;
    }

    @Override
    public void onEnd() {
        sendSignatureData(timeRan / 1000, FLAX_COUNT);
    }

}
// End of the script.