package com.parser.infos;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shot
 * Date: 12.11.13
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */
public class JavaClassInfo implements FileInfo {
    String signature;
    String version;
    String thisClassName;
    String[] Fields;
    String[] Methods;
    int numberOfStrings, numberOfMethods;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getFileData() {
        List<String> result = new LinkedList<String>();
        result.add("File signature : " + signature);
        result.add("Version : " + version);
        result.add("This class name : " + thisClassName);
        result.add("Fields");
        for (int i = 0; i < Fields.length; i++) {
            result.add("field : " + Fields[i]);
        }
        result.add("Methods");
        for (int i = 0; i < Fields.length; i++) {
            result.add("method : " + Methods[i]);
        }
        return result;
    }

    public String getFileSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String[] getFields() {
        return Fields;
    }

    public void setFields(String[] fields) {
        Fields = fields;
    }

    public String[] getMethods() {
        return Methods;
    }

    public void setMethods(String[] methods) {
        Methods = methods;
    }

    public int getNumberOfStrings() {
        return numberOfStrings;
    }

    public void setNumberOfStrings(int numberOfStrings) {
        this.numberOfStrings = numberOfStrings;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int numberOfMethods) {
        this.numberOfMethods = numberOfMethods;
    }

    public String getThisClassName() {
        return thisClassName;
    }

    public void setThisClassName(String thisClassName) {
        this.thisClassName = thisClassName;
    }
}
