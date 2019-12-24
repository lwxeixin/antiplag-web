package cn.mooyyu.antiplagweb.controller;

import cn.mooyyu.antiplagweb.pojo.Chunk;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
public class Rest {
    private String folder = "static-file-folder/";

    @GetMapping(value = "getSessionId")
    @ResponseBody
    public String getSessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }

    @PostMapping("upload")
    public void singleUpload(Chunk chunk, HttpServletRequest request) {
        String SINGLE_FOLDER = folder + request.getSession().getId();
        MultipartFile file = chunk.getFile();
        String filename = chunk.getFilename();
        try {
            byte[] bytes = file.getBytes();
            if (!Files.isWritable(Paths.get(SINGLE_FOLDER))) {
                Files.createDirectories(Paths.get(SINGLE_FOLDER));
            }
            Path path = Paths.get(SINGLE_FOLDER,filename);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("deleteFile")
    public boolean deleteFile(@RequestParam String fileName, HttpServletRequest request) {
        String SINGLE_FOLDER = folder + request.getSession().getId();
        File file = new File(SINGLE_FOLDER, fileName);
        return file.delete();
    }

    @GetMapping("deleteAllFiles")
    public boolean deleteAllFiles(HttpServletRequest request) {
        String SINGLE_FOLDER = folder + request.getSession().getId();
        File dir = new File(SINGLE_FOLDER);
        if (!dir.exists()) return true;
        for (String file : Objects.requireNonNull(dir.list())) {
            if (!new File(dir, file).delete()) return false;
        }
        return dir.delete();
    }

    @GetMapping("getFilesName")
    public List<String> getFilesName(HttpServletRequest request) {
        String SINGLE_FOLDER = "static-file-folder/" + request.getSession().getId();
        if (!Files.isWritable(Paths.get(SINGLE_FOLDER))) return null;
        File[] fs = new File(SINGLE_FOLDER).listFiles();
        if (fs == null) return null;
        List<String> list = new ArrayList<>();
        for (File f : fs) {
            list.add(f.getName());
        }
        return list;
    }
}
