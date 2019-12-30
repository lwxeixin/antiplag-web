package cn.mooyyu.antiplagweb.service;

import cn.mooyyu.antiplagweb.util.FileIO;
import cn.mooyyu.antiplagweb.util.TextExtractor;
import it.zielke.moji.MossException;
import it.zielke.moji.SocketClient;
import jplag.Program;
import jplag.options.CommandLineOptions;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class PerformCompareService {
    private static final File resource = new File("static-file-folder", "resource");
    private static final File result = new File("static-file-folder", "result");

    private String convertMatchesFileToString(File file) {
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

    private void cleanResult(File dir) {
        File[] files = dir.listFiles();
        if (files != null) for (File file : files) { //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }

    private void jplagDocConvert(String sessionId) {
        File convertDir = new File(result, sessionId+"-converted");
        if (convertDir.exists()) cleanResult(convertDir);
        else { //noinspection ResultOfMethodCallIgnored
            convertDir.mkdir();
        }
        File resourceDir = new File(resource, sessionId);
        String[] files = resourceDir.list();
        if (files != null) for (String file : files) {
            FileIO.saveFile(new File(convertDir, file + ".txt"), TextExtractor.convertToText(new File(resourceDir, file)), "utf-8");
        }
    }

    public String[] jplag(String lang, int simValue, String sessionId) {
        cleanResult(new File(result, sessionId));
        if ("doc".equals(lang)) jplagDocConvert(sessionId);
        String[] ret;
        try {
            ArrayList<String> args = new ArrayList<>();
            args.add("-l");
            if ("java".equals(lang)) args.add("java19");
            else if ("doc".equals(lang)) args.add("text");
            else args.add(lang);
            args.add("-r"); //指定结果存放的路径
            args.add(new File(result, sessionId).getPath());
            args.add("-m");  //设置相似度检查门限参数值
            args.add(simValue + "%");
            if ("doc".equals(lang)) args.add(new File(result, sessionId+"-converted").getPath());
            else args.add(new File(resource, sessionId).getPath());
            String[] toPass = new String[args.size()];
            toPass = args.toArray(toPass);
            new Program(new CommandLineOptions(toPass)).run();
        } catch (Exception e) {
            System.out.println(e.toString());
            ret = new String[1];
            ret[0] = "error";
            return ret;
        }
        ret = new String[2];
        File file = new File(result, sessionId);
        ret[0] = convertMatchesFileToString(new File(file, "matches_avg.csv"));
        ret[1] = convertMatchesFileToString(new File(file, "matches_max.csv"));
        return ret;
    }

    public String MOSS(String lang, String id, String sessionId) {
        SocketClient socketClient = new SocketClient();
        socketClient.setUserID(id);
        try {
            socketClient.setLanguage(lang);
            socketClient.run();
            File[] resources = new File(resource, sessionId).listFiles();
            if (resources != null) for (File file : resources) {
                socketClient.uploadFile(file);
            }
            socketClient.sendQuery();
        } catch (MossException e) {
            System.out.println(e.toString());
            return "请确保您从网站获得有效的MOSS帐户，然后输入用户ID。\n这也可能是MOSS服务器的暂时问题或网络故障。";
        } catch (IOException e) {
            System.out.println(e.toString());
            return "网络连接失败，请检查网络。\n这也可能是MOSS服务器的暂时问题。";
        }
        return socketClient.getResultURL().toString();
    }
}
