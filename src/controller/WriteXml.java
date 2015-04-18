/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author hoàng
 */
public class WriteXml {
	public Element rootElement;
	public File dropBoxPath;
	public WriteXml(String filePath)
	{
		this.dropBoxPath = new File(filePath);
	}
    public void lietkeDir(File file, Element element, Document doc) throws DOMException, NoSuchAlgorithmException, FileNotFoundException, IOException{
        	
            if(!file.isDirectory()){
                Element child_element = doc.createElement("File");
  
             	Attr nameElement = doc.createAttribute("name");
             	String fileName = file.getAbsolutePath().replaceAll(dropBoxPath.getPath()+"/","");
                nameElement.setValue(fileName);
                child_element.setAttributeNode(nameElement);
                
                Attr sizeElement = doc.createAttribute("size");
                sizeElement.setValue(String.valueOf(file.length() / 1024.0));
                child_element.setAttributeNode(sizeElement);
                
                Attr modifyElement = doc.createAttribute("lastModified");
                modifyElement.setValue(new java.sql.Date(file.lastModified()).toString());
                child_element.setAttributeNode(modifyElement);
                
                Attr signElement = doc.createAttribute("signature");
                String fileHash = ServerUtil.encryptFile(new FileInputStream(file));
                signElement.setValue(fileHash);
                child_element.setAttributeNode(signElement);
                
                element.appendChild(child_element);
                UploadHTTP.uploadFile(file, fileName, fileHash);
                
            }else{
                File[] arr = file.listFiles();
                for(int i = 0; i< file.listFiles().length; i++){    
                    File child_file = arr[i];
                    Element child_element;
                    Attr child_attr;
                    if(child_file.isDirectory()){
                        child_element = doc.createElement("Directory");
                        element.appendChild(child_element);
                        child_attr = doc.createAttribute("name");
                        String directoryName = arr[i].getAbsolutePath().replaceAll(dropBoxPath.getPath()+"/","");
                        child_attr.setValue(directoryName);
                        child_element.setAttributeNode(child_attr);
//                        child_attr = doc.createAttribute("type");
//                        child_attr.setValue("Directory");
                        child_element.setAttributeNode(child_attr);
                        UploadHTTP.uploadDirectory(directoryName);
                    }
              lietkeDir(child_file, rootElement, doc);
                
                }
                
            }
    }
    
    public void writexml() throws IOException, DOMException, NoSuchAlgorithmException{
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            rootElement = doc.createElement("Directories");
            doc.appendChild(rootElement);
            Attr root_attr = doc.createAttribute("name");
            root_attr.setValue(dropBoxPath.getName());
            rootElement.setAttributeNode(root_attr);
            Date begin = new Date();
            System.out.println("Time begin: "+begin.toString());
            lietkeDir(dropBoxPath, rootElement, doc);
            Date end = new Date();
            System.out.println("Time end: "+end.toString());
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File out_put = new File("xml_demo.xml");
            out_put.createNewFile();
            StreamResult result = new StreamResult(out_put);
 
            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);
 
            transformer.transform(source, result);
            
            System.out.println("File saved!");
 
	} catch (ParserConfigurationException pce) {
            pce.printStackTrace();
	} catch (TransformerException tfe) {
            tfe.printStackTrace();
	}
    }
}
