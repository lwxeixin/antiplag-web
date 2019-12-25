package cn.mooyyu.antiplagweb.service;

import cn.mooyyu.antiplagweb.pojo.Chunk;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileManagerService {
    private static final File folder = new File("static-file-folder", "resource");

    public void uploadFile(Chunk chunk, String sessionId) {
        String single_folder = new File(folder, sessionId).getPath();
        MultipartFile file = chunk.getFile();
        String filename = chunk.getFilename();
        try {
            byte[] bytes = file.getBytes();
            if (!Files.isWritable(Paths.get(single_folder))) {
                Files.createDirectories(Paths.get(single_folder));
            }
            Path path = Paths.get(single_folder,filename);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteFile(String fileName, String sessionId) {
        return new File(new File(folder, sessionId), fileName).delete();
    }

    public boolean deleteAllFiles(String sessionId) {
        File dir = new File(folder, sessionId);
        if (!dir.exists()) return true;
        for (String file : Objects.requireNonNull(dir.list())) {
            if (!new File(dir, file).delete()) return false;
        }
        return dir.delete();
    }

    public String[] getFilesName(String sessionId) {
        File dir = new File(folder, sessionId);
        if (!dir.exists()) return null;
        return dir.list();
    }
}
