package model;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.restlet.ext.xml.DomRepresentation;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.Constants;

public class XMLFactory {

    public static Element rootElement;

    public static DomRepresentation parseFileVersionToXml(List<FileVersion> lstFile) throws IOException {

        DomRepresentation dom = new DomRepresentation();
        Document doc = dom.getDocument();
        rootElement = doc.createElement("Directories");
        doc.appendChild(rootElement);
        if (lstFile != null) {
            Attr root_attr = doc.createAttribute("name");
            rootElement.setAttributeNode(root_attr);
            for (FileVersion fs : lstFile) {
                Element child_element;
                if (fs.getIsFile() == Constants.IS_FILE) {
                    child_element = doc.createElement("File");

                    Attr nameElement = doc.createAttribute("name");
                    nameElement.setValue(fs.getFileName());
                    child_element.setAttributeNode(nameElement);

                    Attr idElement = doc.createAttribute("id");
                    idElement.setValue(Integer.toString(fs.getFileId()));
                    child_element.setAttributeNode(idElement);
                } else {
                    child_element = doc.createElement("Directory");
                    Attr nameElement = doc.createAttribute("name");
                    nameElement.setValue(fs.getFileName());
                    child_element.setAttributeNode(nameElement);

                    Attr idElement = doc.createAttribute("id");
                    idElement.setValue(Integer.toString(fs.getFileId()));
                    child_element.setAttributeNode(idElement);
                }
                rootElement.appendChild(child_element);
            }
        }
        Date begin = new Date();
        System.out.println("Time begin: " + begin.toString());
        Date end = new Date();
        System.out.println("Time end: " + end.toString());
        // --------------------
        dom.setIndenting(true);
        System.out.println("File saved!");
        return dom;
    }

    public static DomRepresentation parseVersionToXml(List<Version> versions) {
        DomRepresentation dom;
        try {
            dom = new DomRepresentation();
            Document doc = dom.getDocument();
            rootElement = doc.createElement("Version");
            doc.appendChild(rootElement);
            if (versions != null) {
                Attr root_attr = doc.createAttribute("name");
                rootElement.setAttributeNode(root_attr);
                for (Version fs : versions) {
                    Element child_element;

                    child_element = doc.createElement("File");

                    Attr nameElement = doc.createAttribute("name");
                    nameElement.setValue(fs.getFileName());
                    child_element.setAttributeNode(nameElement);

                    Attr idElement = doc.createAttribute("id");
                    idElement.setValue(Integer.toString(fs.getFileId()));
                    child_element.setAttributeNode(idElement);
                    
                    Attr versionElement = doc.createAttribute("version");
                    versionElement.setValue(Integer.toString(fs.getFileVersion()));
                    child_element.setAttributeNode(versionElement);
                    
                    child_element.setAttribute("modifyDate",fs.getModifyDate().toString());
                    
                    rootElement.appendChild(child_element);
                }
            }
            
            dom.setIndenting(true);
            System.out.println("File saved!");
            return dom;
        } catch (IOException ex) {
            Logger.getLogger(XMLFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
}
