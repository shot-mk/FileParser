package com.parser;

/**
 * Created with IntelliJ IDEA.
 * User: nikita
 * Date: 11.11.13
 * Time: 19:48
 * To change this template use File | Settings | File Templates.
 */
public interface FileParser {
    public boolean checkFile(String path);
    public String getFileSignature();
    public FileInfo parse(String path);
}
