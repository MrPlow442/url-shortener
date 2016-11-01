package hr.mlovrekov.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelpController {

    /**
     * Shows static help html page
     * @return
     */
    @RequestMapping(path = "/help", method = RequestMethod.GET)
    public String forwardToIndexHtml() {
        return "forward:index.html";
    }

}
