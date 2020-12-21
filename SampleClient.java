import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

import org.apache.commons.lang3.time.StopWatch;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

public class SampleClient {
	 private final static String FORMATTED_STR="First Name=%s,Last Name=%s,Date of Birth=%tD"; 
	 long avg;
    public static void main(String[] theArgs) throws IOException {

        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new LoggingInterceptor(false));
      
        Bundle response = client
                .search()
                .forResource("Patient")
                .where(Patient.FAMILY.matches().value("SMITH"))
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
       
        searchFromFile();
       
    }
        
    
   public static void  searchFromFile() throws IOException
   {
	   for(int i=1;i<=3;i++)
       {System.out.println("loop"+i);
	   	   
	   FhirContext fhirContext = FhirContext.forR4();
       IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
       client.registerInterceptor(new LoggingInterceptor(false));
       client.registerInterceptor(new IClientInterceptor() {
    	  
       	long startTime=System.currentTimeMillis();
       	long totalTime=0;
       
            @Override
			public void interceptResponse(IHttpResponse theResponse) throws IOException {
            	 
				 System.out.println("Response Time=" + (System.currentTimeMillis()- startTime)+ "ms");
				 
				
            }
           
			@Override
			public void interceptRequest(IHttpRequest theRequest) {
			
		       startTime=System.currentTimeMillis();
		      
				
			}
			
			
		}
       );
       
       File file = new File("C:\\Users\\Lalit Goyal\\eclipse-workspace\\playground-basic-master\\playground-basic-master\\src\\main\\java\\LastNames"); 
       
       BufferedReader br = new BufferedReader(new FileReader(file)); 
       long totalTime=0;
       long startTime;
       String st; 
       if(i<3)
       {
       while ((st = br.readLine()) != null) 
       {
    	   totalTime=0;
          startTime=System.currentTimeMillis();
           
    	   Bundle response = client
            .search()
            .forResource("Patient")
            .where(Patient.FAMILY.matches().values(st))
            .returnBundle(Bundle.class).cacheControl(new CacheControlDirective().setNoCache(true))
            .execute();
    	   totalTime=totalTime+(System.currentTimeMillis()- startTime);
   		System.out.println(totalTime);
    
   }
       
	     long avg=totalTime/20;
		System.out.print("Average is"+avg);
       }
       else
       {
    	   System.out.println("caching is disabled");
    	   while ((st = br.readLine()) != null) 
           {
    		   totalTime=0;
               startTime=System.currentTimeMillis();
               

        Bundle response = client
                .search()
                .forResource("Patient")
                .where(Patient.FAMILY.matches().values(st))
                .returnBundle(Bundle.class).cacheControl(new CacheControlDirective().setNoCache(false))
                .execute();
        totalTime=totalTime+(System.currentTimeMillis()- startTime);
		
        
}
    	   
    	     long avg=totalTime/20;
    		System.out.print("Average is"+avg);
    	   
       }
       
       System.out.println("\n\n");
       }
   }
       
}

