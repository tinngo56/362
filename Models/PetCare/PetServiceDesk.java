package Models.PetCare;

import java.util.ArrayList;
import java.util.List;

public class PetServiceDesk {

    List<PetService> services;

    public PetServiceDesk() {
        services = new ArrayList<PetService>();
    }

    public PetServiceDesk(List<PetService> services) {
        this.services = services;
    }

}
