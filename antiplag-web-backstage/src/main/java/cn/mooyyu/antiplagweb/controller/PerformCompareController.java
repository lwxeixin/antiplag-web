package cn.mooyyu.antiplagweb.controller;

import cn.mooyyu.antiplagweb.service.PerformCompareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "performCompare", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
public class PerformCompareController {
    private PerformCompareService performCompareService;
    @Autowired
    public PerformCompareController(PerformCompareService performCompareService) {
        this.performCompareService = performCompareService;
    }

    @GetMapping("jplag")
    @ResponseBody
    public String jplag(@RequestParam String lang, @RequestParam String simValue, HttpServletRequest request) {
        return performCompareService.jplag(lang, simValue, request.getSession().getId());
    }
}
