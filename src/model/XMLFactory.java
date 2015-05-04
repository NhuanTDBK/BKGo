package model;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
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
                    
                    child_element.setAttribute("timestamp",format.format(fs.getModifyDate()));
                    
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
    public static DomRepresentation parseFileStorageToXml(List<FileStorage> lstFile) throws IOException {

        DomRepresentation dom = new DomRepresentation();
        Document doc = dom.getDocument();
        rootElement = doc.createElement("Trash");
        doc.appendChild(rootElement);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        if (lstFile != null) {
            Attr root_attr = doc.createAttribute("name");
            rootElement.setAttributeNode(root_attr);
            for (FileStorage fs : lstFile) {
                Element child_element;
                if (fs.getIsFile() == Constants.IS_FILE) {
                    child_element = doc.createElement("File");

                    Attr nameElement = doc.createAttribute("name");
                    nameElement.setValue(fs.getFileName());
                    child_element.setAttributeNode(nameElement);

                    Attr idElement = doc.createAttribute("id");
                    idElement.setValue(Integer.toString(fs.getFileId()));
                    child_element.setAttributeNode(idElement);
                    
                    child_element.setAttribute("timestamp", format.format(fs.getCreateDate()));
                    
                } else {
                    child_element = doc.createElement("Directory");
                    Attr nameElement = doc.createAttribute("name");
                    nameElement.setValue(fs.getFileName());
                    child_element.setAttributeNode(nameElement);

                    Attr idElement = doc.createAttribute("id");
                    idElement.setValue(Integer.toString(fs.getFileId()));
                    child_element.setAttributeNode(idElement);
                    
                    child_element.setAttribute("Date", fs.getCreateDate().toString());

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
    public DomRepresentation getUpperByTransactionId(String transactionId)
	{
		DomRepresentation dom=null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			dom = new DomRepresentation();
			dom.setIndenting(true);
			Document doc = dom.getDocument();
			Element fileNode = doc.createElement("Changes");
			doc.appendChild(fileNode);
			List<FileChange> lstFileChange = FileChangeDAO.getUpperByTransactionId(transactionId);
			dom = getUpperByTransactionId(lstFileChange);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dom;
	}
	public DomRepresentation getUpperByTransactionId(List<FileChange>lst)
	{
		DomRepresentation dom=null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			dom = new DomRepresentation();
			dom.setIndenting(true);
			Document doc = dom.getDocument();
			Element fileNode = doc.createElement("Changes");
			doc.appendChild(fileNode);
			List<FileChange> lstFileChange = lst;
			for(FileChange item : lstFileChange)
			{
				Element changeNode = doc.createElement("Change");
				
				changeNode.setAttribute("fileChangeId",Integer.toString(item.getFileChangeId()));
				
				Attr fileIdAttr = doc.createAttribute("id");
				fileIdAttr.setTextContent(Integer.toString(item.getFileId()));
				changeNode.setAttributeNode(fileIdAttr);
				
				Attr tidAttr = doc.createAttribute("tid");
				tidAttr.setTextContent(Integer.toString(item.getTid()));
				changeNode.setAttributeNode(tidAttr);

				Attr timestampAttr = doc.createAttribute("timestamp");
				timestampAttr.setTextContent(format.format(item.getTimestamp()));
				changeNode.setAttributeNode(timestampAttr);

				int type = item.getType();
				Attr typeAttr = doc.createAttribute("type");
				String typeStr = null;
				switch(type)
				{
				case Constants.CREATE: 
					typeStr = "Create";
					break;
				case Constants.UPDATE:
					typeStr = "Update";
					break;
				case Constants.DELETE:
					typeStr = "Delete";
					break;
				case Constants.RENAME:
					typeStr = "Rename";
					Attr oldName = doc.createAttribute("before");
					oldName.setTextContent(item.getBeforeChange());
					changeNode.setAttributeNode(oldName);

					Attr newName = doc.createAttribute("after");
					newName.setTextContent(item.getBeforeChange());
					changeNode.setAttributeNode(newName);
					break;
				case Constants.MOVE:
					typeStr = "Move";
					Attr oldNameNode = doc.createAttribute("before");
					oldNameNode.setTextContent(item.getBeforeChange());
					changeNode.setAttributeNode(oldNameNode);

					Attr newNameNode = doc.createAttribute("after");
					newNameNode.setTextContent(item.getBeforeChange());
					changeNode.setAttributeNode(newNameNode);
					break;
				}
				typeAttr.setTextContent(typeStr);
				changeNode.setAttributeNode(typeAttr);


				Attr fileName = doc.createAttribute("name");
				fileName.setTextContent(item.getFileName());
				changeNode.setAttributeNode(fileName);

				Attr isFileNode = doc.createAttribute("isFile");
				isFileNode.setTextContent(Integer.toString(item.getIsFile()));
				changeNode.setAttribute("isFile", Integer.toString(item.getIsFile()));

				fileNode.appendChild(changeNode);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dom;
	}
}
