package com.zw.erp.ws.crmorderservice.proxy;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.5.2
 * 2016-12-15T09:40:23.722+08:00
 * Generated source version: 2.5.2
 * 
 */
@WebServiceClient(name = "CrmOrderServiceImplService", 
                  wsdlLocation = "http://116.236.220.212:18081/erpv3_service/crmOrderService.ws?wsdl",
                  targetNamespace = "http://crm.impl.service.webapp.apt.com/") 
public class CrmOrderServiceImplService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://crm.impl.service.webapp.apt.com/", "CrmOrderServiceImplService");
    public final static QName CrmOrderServiceImplPort = new QName("http://crm.impl.service.webapp.apt.com/", "CrmOrderServiceImplPort");
    static {
        URL url = null;
        try {
            url = new URL("http://116.236.220.212:18081/erpv3_service/crmOrderService.ws?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(CrmOrderServiceImplService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://116.236.220.212:18081/erpv3_service/crmOrderService.ws?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public CrmOrderServiceImplService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public CrmOrderServiceImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CrmOrderServiceImplService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public CrmOrderServiceImplService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public CrmOrderServiceImplService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public CrmOrderServiceImplService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns ICrmOrderService
     */
    @WebEndpoint(name = "CrmOrderServiceImplPort")
    public ICrmOrderService getCrmOrderServiceImplPort() {
        return super.getPort(CrmOrderServiceImplPort, ICrmOrderService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ICrmOrderService
     */
    @WebEndpoint(name = "CrmOrderServiceImplPort")
    public ICrmOrderService getCrmOrderServiceImplPort(WebServiceFeature... features) {
        return super.getPort(CrmOrderServiceImplPort, ICrmOrderService.class, features);
    }

}
