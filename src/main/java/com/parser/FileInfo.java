package com.parser;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nikita
 * Date: 11.11.13
 * Time: 19:25
 * To change this template use File | Settings | File Templates.
 */
public interface FileInfo {
    public List<String> getFileData();
    public String getFileSignature();
}
