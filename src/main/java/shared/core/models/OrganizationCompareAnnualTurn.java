package shared.core.models;
import java.util.Comparator;

/**
 * a class that sorts a collection in descending order
 */
public class OrganizationCompareAnnualTurn implements Comparator<Organization> {
    @Override
    public int compare(Organization organization1, Organization organization2) {
        if (organization1.getAnnualTurnover() == organization2.getAnnualTurnover()) {
            return 0;
        } else if (organization1.getAnnualTurnover() > organization2.getAnnualTurnover()) {
            return -1;
        } else {
            return 1;
        }

    }
}