package cn.mooyyu.antiplagweb.service;

import jplag.Program;
import jplag.options.CommandLineOptionsExt;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class PerformCompareService {
    private static final File resource = new File("static-file-folder", "resource");
    private static final File result = new File("static-file-folder", "result");

    public String convertMatchesFileToString(File file) {
        byte[] fileContent = new byte[(int) file.length()];
        try {
            FileInputStream in = new FileInputStream(file);
            //noinspection ResultOfMethodCallIgnored
            in.read(fileContent);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(fileContent);
    }

    public String getMatches(File file) {
        String ret = "Matches sorted by average similarity:\n";
        ret += convertMatchesFileToString(new File(file, "matches_avg.csv"));
        ret += "\nMatches sorted by maximum similarity:\n";
        return ret + convertMatchesFileToString(new File(file, "matches_max.csv"));
    }

    public String jplag(String lang, String simValue, String sessionId) {
        try {
            ArrayList<String> args = new ArrayList<>();
            args.add("-l");
            if ("java".equals(lang)) args.add("java19");
            else args.add(lang); //设置语言类型参数，不加此参数，就使用默认值，为java19
            args.add("-r"); //指定结果存放的路径
            args.add(new File(result, sessionId).getPath());
            args.add("-m");  //设置相似度检查门限参数值
            args.add(simValue + "%");
            args.add(new File(resource, sessionId).getPath());
            String[] toPass = new String[args.size()];
            toPass = args.toArray(toPass);
            new Program(new CommandLineOptionsExt(toPass, null)).run();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return getMatches(new File(result, sessionId));
    }
}
