package scripts.SPXFlaxPicker;

import org.tribot.api.General;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

/**
 * Created by Sphiinx on 7/11/2016.
 */
public class SignatureData {

    public static boolean sendSignatureData(long runtimeInSeconds, int flax_count) {
        final String initVector = "E6135CCC2FE8BE8C";
        try {
            final String data = initVector + "," + General.getTRiBotUsername() + "," + runtimeInSeconds + "," + flax_count;
            final String token = Base64.getEncoder().encodeToString(data.getBytes());

            final URL url = new URL("http://www.spxscripts.com/spxflaxpicker/input.php?token="+token);
            final URLConnection conn = url.openConnection();

            conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");

            final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            in.readLine();
            in.close();
            return true;

        } catch (Exception e) {
            General.println(e.getMessage());
        }
        return false;
    }

}

