package model;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.restlet.ext.xml.DomRepresentation;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mysql.fabric.Response;

import utils.Constants;

public class FileChangeFactory {
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
