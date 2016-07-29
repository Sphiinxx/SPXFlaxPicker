package scripts.SPXFlaxPicker.data;

/**
 * Created by Sphiinx on 7/11/2016.
 */
public class Vars {

    public static Vars vars;

    public static Vars get() {
        return vars == null ? vars = new Vars() : vars;
    }

    public static void reset() {
        vars = null;
    }

    public int flax_picked;

}

