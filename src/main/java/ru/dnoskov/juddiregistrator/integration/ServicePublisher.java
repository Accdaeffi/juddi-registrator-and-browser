package ru.dnoskov.juddiregistrator.integration;

import org.apache.juddi.api_v3.AccessPointType;
import org.apache.juddi.v3.client.config.UDDIClerk;
import org.apache.juddi.v3.client.config.UDDIClient;
import org.uddi.api_v3.AccessPoint;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.api_v3.BindingTemplates;
import org.uddi.api_v3.BusinessEntity;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.Name;

/**
 * This shows you to interact with a UDDI server by publishing a Business,
 * Service and Binding Template. It uses code that is specific to the jUDDI
 * client jar's and represents an easier, simpler way to do things. (UDDIClient
 * and UDDIClerk classes). Credentials and URLs are all set via uddi.xml
 */
public class ServicePublisher {

	private static UDDIClerk clerk = null;

	public ServicePublisher() {
		try {
			UDDIClient uddiClient = new UDDIClient("META-INF/uddi.xml");
			clerk = uddiClient.getClerk("default");
			if (clerk == null)
				throw new Exception("The clerk wasn't found, check the config file!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String publish(String businessKey, String wsdlAddress, String serviceName) {

		String result = "";
		
		try {

			BusinessEntity register = clerk.getBusinessDetail(businessKey);
			if (register == null) {
				result = "No such business!";
			} else {
				BusinessService myService = new BusinessService();
				myService.setBusinessKey(businessKey);
				Name myServName = new Name();
				myServName.setValue(serviceName);
				myService.getName().add(myServName);

				BindingTemplate myBindingTemplate = new BindingTemplate();
				AccessPoint accessPoint = new AccessPoint();
				accessPoint.setUseType(AccessPointType.WSDL_DEPLOYMENT.toString());
				accessPoint.setValue(wsdlAddress);
				myBindingTemplate.setAccessPoint(accessPoint);
				BindingTemplates myBindingTemplates = new BindingTemplates();
				myBindingTemplate = UDDIClient.addSOAPtModels(myBindingTemplate);
				myBindingTemplates.getBindingTemplate().add(myBindingTemplate);
				myService.setBindingTemplates(myBindingTemplates);

				BusinessService svc = clerk.register(myService);
				if (svc == null) {
					result = "Save failed!";
				} else {
					result = "Success!";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = "Exception!";
		} 
		finally {
			clerk.discardAuthToken();
		}
		
		return result;
	}

}
