package com.parser.utils;

import com.parser.infos.FileInfo;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: nikita
 * Date: 11.11.13
 * Time: 19:48
 * To change this template use File | Settings | File Templates.
 */
public interface FileParser {
    public boolean checkFile(String path) throws IOException;
    public String getFileSignature() throws IOException;
    public FileInfo parse(String path) throws IOException;
}
