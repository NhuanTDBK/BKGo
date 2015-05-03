/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.XMLFactory;
import java.util.List;
import model.Version;
import model.VersionDAO;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.ext.xml.DomRepresentation;

/**
 *
 * @author nhuan
 */
public class VersionController extends Restlet {

    @Override
    public void handle(Request request, Response response) {
        Method method = request.getMethod();
        String lastSegment = request.getCurrent().getResourceRef().getLastSegment();
        String userIdStr = (String) request.getAttributes().get("userId");
        String path = request.getHeaders().getFirst("X-PATH").getValue().toLowerCase();
        if(method.equals(Method.GET))
        {
            if(path.equals("root"))
            {
                
                List<Version>versions = VersionDAO.getAll();
                DomRepresentation res = XMLFactory.parseVersionToXml(versions);
                response.setEntity(res);
            }
            else
            {
                List<Version>versions = VersionDAO.getVersionByName(path);
                DomRepresentation res = XMLFactory.parseVersionToXml(versions);
                response.setEntity(res);
            }
        }
    }

}
