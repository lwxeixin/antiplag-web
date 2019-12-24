package cn.mooyyu.antiplagweb.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

@Component
public class CleanStaticFolder implements ApplicationRunner {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String folder = "static-file-folder/";
        for (String dir : Objects.requireNonNull(new File(folder).list())) {
            String url = folder + '/' + dir;
            for (String file : Objects.requireNonNull(new File(folder, dir).list())) {
                new File(url, file).delete();
            }
            new File(folder, dir).delete();
        }
    }
}
