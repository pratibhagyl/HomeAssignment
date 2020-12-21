
import java.util.Comparator;

import org.hl7.fhir.r4.model.Patient;

public class PatientNameSorter implements Comparator<Patient> 
{
    @Override
    public int compare(Patient p1, Patient p2) {
    	String p1Name=p1.getName().get(0).getGiven().get(0).asStringValue();
    	String p2Name=p2.getName().get(0).getGiven().get(0).asStringValue();
        return p1Name.compareToIgnoreCase(p2Name);
    }
}