
package com.rac021.jax.service.discovery ;

import java.util.Map ;
import java.util.List ;
import javax.ws.rs.GET ;
import java.util.HashMap ;
import javax.inject.Inject ;
import java.util.ArrayList ;
import javax.ws.rs.Produces ;
import javax.inject.Singleton ;
import java.lang.reflect.Field ;
import javax.ws.rs.core.UriInfo ;
import javax.ws.rs.core.Context ;
import javax.xml.bind.Marshaller ;
import javax.ws.rs.core.Response ;
import javax.xml.bind.JAXBContext ;
import javax.xml.bind.JAXBException ;
import java.io.ByteArrayOutputStream ;
import com.rac021.jax.api.pojos.Query ;
import javax.annotation.PostConstruct ;
import java.io.UnsupportedEncodingException ;
import com.rac021.jax.api.streamers.EmptyPojo ;
import com.rac021.jax.api.root.ServicesManager ;
import com.rac021.jax.api.qualifiers.ServiceRegistry ;
import com.rac021.jax.api.qualifiers.ResourceRegistry ;
import org.eclipse.persistence.jaxb.MarshallerProperties ;

/**
 *
 * @author R.Yahioaui
 */

@Singleton
@ServiceRegistry("infoServices")
public class InfoServices {

    @Context 
    UriInfo uriInfo ;
            
    @Inject 
    ServicesManager servicesManager ;

    private List<ServiceDescription> services  = null ;
    private ByteArrayOutputStream    baoStream = null ;

    
    @PostConstruct
    public void init() {
    }

    public InfoServices() {
    }
   
    @GET
    @Produces( {"json/plain", "xml/plain" , "json/plain", "json/encrypted"} )
    public Response getResourceJson() throws JAXBException, UnsupportedEncodingException {    
        return produceInfoServices() ;
    }

    private Response produceInfoServices() throws JAXBException, UnsupportedEncodingException {
        
       if( services == null ) {
       
            services = new ArrayList<>() ;

            servicesManager.getMapOfAllSubServices().forEach( ( k, v) -> {

                String serviceName = k ;
                Object service     = v ;
                Field resourceField = servicesManager.getFieldFor( service.getClass(), ResourceRegistry.class) ;

                if( resourceField != null ) {
                    List<Query> queriesByResourceName = servicesManager.getQueriesByResourceName(
                                                                 resourceField.getGenericType().getTypeName()) ;
                    Map<String, String > params = new HashMap<>() ;
                    if( queriesByResourceName != null ) {
                        queriesByResourceName.get(0).getParameters().forEach( (s, t ) -> {
                            params.put(s, t.get("TYPE")) ;
                        }) ;
                    }
                   services.add(new ServiceDescription(k, servicesManager.contains(k).name(), params) ) ;
                }
            });
        
           baoStream = new ByteArrayOutputStream() ;

           Marshaller marshaller = null ;

           marshaller = trySetProperties(marshaller) ;
           marshaller.marshal(services, baoStream )  ;
       }
      
       return  Response.ok( baoStream.toString("UTF8")).build() ; 
       
    }
    
     private javax.xml.bind.Marshaller trySetProperties( Marshaller marshaller) {
        
        Marshaller localMarshaller= marshaller ;
        JAXBContext jc ;
        try {
            jc = JAXBContext.newInstance( ServiceDescription.class, EmptyPojo.class) ;
            localMarshaller = jc.createMarshaller() ;
            localMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json")   ;
            localMarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, Boolean.FALSE) ;
            localMarshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE) ;
        } catch (JAXBException ex) {
            ex.printStackTrace() ;
        }
        return localMarshaller ;
    }
     
}
