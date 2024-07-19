package com.freshworks.fsa.filespliter;

import com.freshworks.fsa.filespliter.spliter.FileSpliter;

import java.io.IOException;

public class FsaFileSpliterApplication {

    public static void main(String[] args) throws IOException {
        FileSpliter fileSpliter = new FileSpliter();
        fileSpliter.process(args[0]);
    }
}
