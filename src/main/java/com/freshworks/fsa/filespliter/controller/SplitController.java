package com.freshworks.fsa.filespliter.controller;

import com.freshworks.fsa.filespliter.spliter.FileSpliter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@AllArgsConstructor
@RequestMapping("/crm/sales/_api")
public class SplitController {
    private FileSpliter fileSpliter;

    @GetMapping("/split")
    public void split() throws IOException {
        fileSpliter.process();
    }

}
