package com.parser.infos;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shot
 * Date: 12.11.13
 * Time: 18:22
 * To change this template use File | Settings | File Templates.
 */
public class ZipInfo implements FileInfo {
    String signature;
    String compressionMethod;
    List<String> files;

    public List<String> getFileData() {
        List<String> result = new LinkedList<String>();
        result.add("File signature : " + signature);
        result.add("File comression method : " + compressionMethod);
        result.add("List of files : ");
        for(String c : this.files) {
            result.add(c);
        }
        return result;
    }

    public String getFileSignature() {
        return signature;
    }


    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCompressionMethod() {
        return compressionMethod;
    }

    public void setCompressionMethod(String compressionMethod) {
        this.compressionMethod = compressionMethod;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = new LinkedList<>();
        this.files.addAll(files);
    }


}
