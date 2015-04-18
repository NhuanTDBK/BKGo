/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author tbug
 */
public class FileAttr {
    public String fileName;
    public String fileKey;
    public String fileParent;
    
    public FileAttr(String fileName, String fileKey, String fileParent){
        this.fileName = fileName;
        this.fileKey = fileKey;
        this.fileParent = fileParent;
    }
}
