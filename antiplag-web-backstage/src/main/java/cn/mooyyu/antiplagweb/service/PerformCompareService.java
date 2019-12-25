package cn.mooyyu.antiplagweb.service;

import org.springframework.stereotype.Service;

@Service
public class PerformCompareService {
    public String jplag(String lang, String simValue, String sessionId) {
        return lang+simValue+sessionId;
    }
}
