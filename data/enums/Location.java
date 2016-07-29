package scripts.SPXFlaxPicker.data.enums;

import org.tribot.api2007.types.RSTile;

/**
 * Created by Sphiinx on 7/11/2016.
 */
public enum Location {

    SEERS_BANK(new RSTile(2726, 3492, 0)),
    FLAX_FIELD(new RSTile(2743, 3444, 0));

    private final RSTile LOCATION;

    Location(RSTile location) {
        this.LOCATION = location;
    }

    public RSTile getLocation() {
        return LOCATION;
    }

}
