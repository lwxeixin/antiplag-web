package cn.mooyyu.antiplagweb.controller;

import cn.mooyyu.antiplagweb.pojo.Chunk;
import cn.mooyyu.antiplagweb.service.FileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/fileManager", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
public class FileManagerController {
    private FileManagerService fileManagerService;
    @Autowired
    public FileManagerController(FileManagerService fileManagerService) {
        this.fileManagerService = fileManagerService;
    }

    @PostMapping("uploadFile")
    public void uploadFile(Chunk chunk, HttpServletRequest request) {
        fileManagerService.uploadFile(chunk, request.getSession().getId());
    }

    @GetMapping("deleteFile")
    public boolean deleteFile(@RequestParam String fileName, HttpServletRequest request) {
        return fileManagerService.deleteFile(fileName, request.getSession().getId());
    }

    @GetMapping("deleteAllFiles")
    public boolean deleteAllFiles(HttpServletRequest request) {
        return fileManagerService.deleteAllFiles(request.getSession().getId());
    }

    @GetMapping("getFilesName")
    public String[] getFilesName(HttpServletRequest request) {
        return fileManagerService.getFilesName(request.getSession().getId());
    }
}
