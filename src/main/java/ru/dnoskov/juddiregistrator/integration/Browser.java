package ru.dnoskov.juddiregistrator.integration;

import java.util.ArrayList;
import java.util.List;
import org.apache.juddi.v3.client.UDDIConstants;
import org.apache.juddi.v3.client.config.UDDIClient;
import org.apache.juddi.v3.client.transport.Transport;
import org.uddi.api_v3.AuthToken;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.api_v3.BindingTemplates;
import org.uddi.api_v3.BusinessInfos;
import org.uddi.api_v3.BusinessList;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.Description;
import org.uddi.api_v3.DiscardAuthToken;
import org.uddi.api_v3.FindBusiness;
import org.uddi.api_v3.GetAuthToken;
import org.uddi.api_v3.GetServiceDetail;
import org.uddi.api_v3.Name;
import org.uddi.api_v3.ServiceDetail;
import org.uddi.v3_service.UDDIInquiryPortType;
import org.uddi.v3_service.UDDISecurityPortType;


public class Browser {

        private static UDDISecurityPortType security = null;
        private static UDDIInquiryPortType inquiry = null;

        /**
         * This sets up the ws proxies using uddi.xml in META-INF
         */
        public Browser() {
                try {
        	// create a manager and read the config in the archive; 
                        // you can use your config file name
                        UDDIClient client = new UDDIClient("META-INF/uddi.xml");
        	// a UDDIClient can be a client to multiple UDDI nodes, so 
                        // supply the nodeName (defined in your uddi.xml.
                        // The transport can be WS, inVM etc which is defined in the uddi.xml
                        Transport transport = client.getTransport("default");
                        // Now you create a reference to the UDDI API
                        security = transport.getUDDISecurityService();
                        inquiry = transport.getUDDIInquiryService();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        
        
        public String getServiceDescriptionByName(String name) {
        	StringBuilder sb = new StringBuilder();
        	
            try {

                String token = GetAuthKey("uddiadmin", "da_password1");
                BusinessList findBusiness = GetBusinessList(token);
                List<BusinessService> services = getBusinessServices(findBusiness, token); 

                security.discardAuthToken(new DiscardAuthToken(token));
                
                for (BusinessService service : services) {
                	if (ListToString(service.getName()).contains(name)) {
                		sb.append(describeService(service));
                	}
                }
                

	        } catch (Exception e) {
	        		sb.append("Ошибка!").append("\n");
	        		e.printStackTrace();
	        		sb.append(e.getMessage());
	        }
            
            
            if (sb.length() == 0) {
            	sb.append("Сервисов с подобным именем не найдено!");
            }
            
            return sb.toString();
        }
        
        private List<BusinessService> getBusinessServices(BusinessList businesses, String token) throws Exception {
        	List<BusinessService> services = new ArrayList<BusinessService>(); 
        	
        	BusinessInfos businessInfos = businesses.getBusinessInfos();
            for (int i = 0; i < businessInfos.getBusinessInfo().size(); i++) {
                GetServiceDetail gsd = new GetServiceDetail();
                for (int k = 0; k < businessInfos.getBusinessInfo().get(i).getServiceInfos().getServiceInfo().size(); k++) {
                        gsd.getServiceKey().add(businessInfos.getBusinessInfo().get(i).getServiceInfos().getServiceInfo().get(k).getServiceKey());
                }
                gsd.setAuthInfo(token);
                ServiceDetail serviceDetail = inquiry.getServiceDetail(gsd);
               
                services.addAll(serviceDetail.getBusinessService());
            }
            
            return services;
        }
        
        private String describeService(BusinessService service) {
    		StringBuilder sb = new StringBuilder();
    		sb.append("------Service start-------").append("\n");
    		sb.append("Name : ").append(ListToString(service.getName())).append("\n");
    		sb.append("Descr: ").append(ListToDescString(service.getDescription())).append("\n");
    		sb.append("Key  : ").append(service.getServiceKey()).append("\n");
    		
    		
    		BindingTemplates bindingTemplates = service.getBindingTemplates();
    		if (bindingTemplates != null) {
    			sb.append("Binding Templates: ").append("\n");
    			for (BindingTemplate template : bindingTemplates.getBindingTemplate()) {
    				sb.append("   ").append("Access Point: ").append(template.getAccessPoint().getValue()).append("\n");
    			}
    		} else {
    			sb.append("No access points detected!").append("\n");
    		}
    		sb.append("------Service end-------").append("\n");
    		sb.append("\n");
    		
    		return sb.toString();
        }

        /**
         * Find all of the registered businesses. This list may be filtered
         * based on access control rules
         *
         * @param token
         * @return
         * @throws Exception
         */
        private BusinessList GetBusinessList(String token) throws Exception {
                FindBusiness fb = new FindBusiness();
                fb.setAuthInfo(token);
                org.uddi.api_v3.FindQualifiers fq = new org.uddi.api_v3.FindQualifiers();
                fq.getFindQualifier().add(UDDIConstants.APPROXIMATE_MATCH);

                fb.setFindQualifiers(fq);
                Name searchname = new Name();
                searchname.setValue(UDDIConstants.WILDCARD);
                fb.getName().add(searchname);
                BusinessList findBusiness = inquiry.findBusiness(fb);
                return findBusiness;

        }
        

        /**
         * Gets a UDDI style auth token, otherwise, appends credentials to the
         * ws proxies (not yet implemented)
         *
         * @param username
         * @param password
         * @param style
         * @return
         */
        private String GetAuthKey(String username, String password) {
                try {

                        GetAuthToken getAuthTokenRoot = new GetAuthToken();
                        getAuthTokenRoot.setUserID(username);
                        getAuthTokenRoot.setCred(password);

                        // Making API call that retrieves the authentication token for the user.
                        AuthToken rootAuthToken = security.getAuthToken(getAuthTokenRoot);
                        return rootAuthToken.getAuthInfo();
                } catch (Exception ex) {
                        System.out.println("Could not authenticate with the provided credentials " + ex.getMessage());
                }
                return null;
        }

        


        private String ListToString(List<Name> name) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < name.size(); i++) {
                        sb.append(name.get(i).getValue()).append(" ");
                }
                return sb.toString();
        }

        private String ListToDescString(List<Description> name) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < name.size(); i++) {
                        sb.append(name.get(i).getValue()).append(" ");
                }
                return sb.toString();
        }


}
