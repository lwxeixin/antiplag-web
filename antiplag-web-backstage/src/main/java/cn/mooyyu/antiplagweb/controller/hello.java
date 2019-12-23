package cn.mooyyu.antiplagweb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/hello", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
public class hello {
    @RequestMapping(value = "getSessionId")
    @ResponseBody
    public String getSessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }
}
