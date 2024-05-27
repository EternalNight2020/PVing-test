package org.example.pvingtest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @package: org.example.pvingtest.demos.web.controller
 * @className: PVingController
 * @author: alexwang
 * @description: TODO
 * @date: 2024/5/26 16:25
 */
@Controller
public class PVingController {

    @GetMapping("/test.html")
    public String index() {
        return "pving-test";
    }
}
