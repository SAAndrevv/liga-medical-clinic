package liga.medical.medicalmonitoring.core.antisolid;

public class AntiL {

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }

        // comparing another fields
        return true;
    }

    @Override
    public int hashCode() {
        return 31 * 1;
    }

}
