package dev.mockboard.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

    @RequestMapping(value = {"/", "/board/{boardId}"})
    public String forward() {
        return "forward:/index.html";
    }
}
