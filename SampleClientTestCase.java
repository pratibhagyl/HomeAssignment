import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class SampleClientTestCase {
	@Autowired
	SampleClient s;


	@Test()
	public void addStudentTest()
	{ FhirContext fhirContext = FhirContext.forR4();
    IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
    client.registerInterceptor(new LoggingInterceptor(false));
    
		Exception e=assertThrows(NameNotmatchException.class, ()->{
			 Bundle response = client
		                .search()
		                .forResource("Patient")
		                .where(Patient.FAMILY.matches().value("Goyal"))
		                .returnBundle(Bundle.class)
		                .execute();
			 System.out.println("Responses: " + response.getTotal());
        });
		assertEquals("name should not be empty",e.getMessage());
	}
	@Test
	public void testCreateAndRead() {
		FhirContext fhirContext = FhirContext.forR4();
		String methodName = "testCreateResourceConditional";
		 IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
		    client.registerInterceptor(new LoggingInterceptor(false));

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		IIdType id = client.create().resource(pt).execute().getId();

		Patient pt2 = client.read().resource(Patient.class).withId(id).execute();
		assertEquals(methodName, pt2.getName().get(0).getFamily());
	}
	@Test
	void test() {
		fail("Not yet implemented");
	}

}
