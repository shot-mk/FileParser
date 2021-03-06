package com.parser;

import com.parser.infos.FileInfo;
import com.parser.utils.FileParser;
import com.parser.utils.GifUtil;
import com.parser.utils.JavaClassUtil;
import com.parser.utils.ZipUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nikita
 * Date: 11.11.13
 * Time: 19:46
 * To change this template use File | Settings | File Templates.
 */
public class Parser {

    static List<FileParser> parsers;
    static String path;

    Parser(String path) throws IOException {
        this.path = path;
        parsers = new LinkedList<FileParser>();
        parsers.add(new GifUtil(path));
        parsers.add(new ZipUtil(path));
        parsers.add(new JavaClassUtil(path));
    }

    public FileInfo parse() throws IOException {
        FileInfo info = null ;
        for(FileParser c : parsers) {
            if(c.checkFile(path) == true){
                info = c.parse(path);
                return info;
            }
        }
        throw new IOException();
    }
}
