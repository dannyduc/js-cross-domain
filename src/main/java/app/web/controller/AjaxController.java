package app.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AjaxController {

    @RequestMapping(value = "/doPost", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> doPost() {
        Map<String, String> m = new HashMap<String, String>();
        m.put("message", "aok");
        return m;
    }
}
