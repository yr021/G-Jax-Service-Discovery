
package com.rac021.jax.service.discovery ;

import java.util.Map ;
import javax.xml.bind.annotation.XmlType ;
import javax.xml.bind.annotation.XmlRootElement ;

/**
 *
 * @author ryahiaoui
 */

 @XmlRootElement
 @XmlType(propOrder = { "serviceName", "security", "params" } )
 public class ServiceDescription {
        
    private  String              serviceName ;
    private  String              security    ;

    private  Map<String, String> params      ;
        
    
    public ServiceDescription(String name, String security, Map<String, String> params ) {
        this.serviceName  = name     ;
        this.security     = security ;
        this.params       = params   ;
    }
    
    public ServiceDescription() {
    }

    public String getServiceName() {
        return serviceName ;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName ;
    }
  
    public void setSecurity(String security) {
        this.security = security ;
    }

    public String getSecurity() {
        return security ;
    }

    public Map<String, String> getParams() {
        return params ;
    }

    public void setParams(Map<String, String> params) {
        this.params = params ;
    }

 }
