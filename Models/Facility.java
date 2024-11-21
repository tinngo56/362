package Models;

public abstract class Facility extends Mappable<Facility> {
    AccessLevels accessLevel = AccessLevels.BASIC;
    
    public AccessLevels getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevels accessLevel) {
        this.accessLevel = accessLevel;
    }
}
