import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;

import java.util.ArrayList;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

public class SampleClient {

	private final static String FORMATTED_STR="First Name=%s,Last Name=%s,Date of Birth=%tD"; 
	
    public static void main(String[] theArgs) {

        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new LoggingInterceptor(false));

        // Search for Patient resources
        Bundle response1 = client
                .search()
                .forResource("Patient")
                .where(Patient.FAMILY.matches().value("SMITH"))
                .returnBundle(Bundle.class)
                .execute();
        Bundle response = client
                .search()
                .forResource("Patient")
               
                .returnBundle(Bundle.class)
                .execute();

        ArrayList<Patient> sortedPatientList=new ArrayList<Patient>();
        
        //Task 1 Print 
        if(response.hasEntry()) {
        	response.getEntry().forEach(entry->{
        		if(entry.getResource() instanceof Patient) {        			
		        		Patient p=(Patient) entry.getResource();
		        		String firstName=p.getName().get(0).getGiven().get(0).asStringValue();
		        		String lastName=p.getName().get(0).getFamily();
		        		String output=String.format(FORMATTED_STR, p.getName().get(0).getGiven().get(0),lastName,p.getBirthDate());
		        		System.out.println(output);
		        		
		        		//for task 2
		        		sortedPatientList.add(p);
        		}
        	});
        }	
        
        
        System.out.println("Sorted by first name");
        //Task 2 Sort
        sortedPatientList.sort(new PatientNameSorter());
        //print sortedList
        sortedPatientList.forEach(p->{
    		String firstName=p.getName().get(0).getGiven().get(0).asStringValue();
    		String lastName=p.getName().get(0).getFamily();
    		String output=String.format(FORMATTED_STR, p.getName().get(0).getGiven().get(0),lastName,p.getBirthDate());
    		System.out.println(output);
        });
        
        
    }

}
