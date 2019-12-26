package cn.mooyyu.antiplagweb.controller;

import cn.mooyyu.antiplagweb.service.PerformCompareService;
import it.zielke.moji.MossException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
    public String[] jplag(@RequestParam String lang, @RequestParam int simValue, HttpServletRequest request) {
        return performCompareService.jplag(lang, simValue, request.getSession().getId());
    }

    @GetMapping("MOSS")
    @ResponseBody
    public String MOSS(@RequestParam String lang, @RequestParam String id, HttpServletRequest request) throws IOException, MossException {
        return performCompareService.MOSS(lang, id, request.getSession().getId());
    }
}
