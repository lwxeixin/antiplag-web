package cn.mooyyu.antiplagweb.util;

import cn.mooyyu.antiplagweb.pojo.SimData;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileIO {
    //按相似度由大到小保存结果到文件，type 2 原结果是由小到大，1原结果是由大到小
    public static void saveFile(File outfile, List<SimData> listsd, int type, String laiyuan) {
        if (outfile.exists()) {
            FileWriter fr = null;
            try {
                fr = new FileWriter(outfile);
                if (type == 1) {
                    for (int i = 0; i < listsd.size() - 1; i++) {
                        fr.write((i + 1) + "  " + listsd.get(i).getSimilar() + "%  " + listsd.get(i).getFile1() + "  " + listsd.get(i).getFile2() + "\r\n");
                    }
                } else if (type == 2) {
                    for (int i = listsd.size() - 1; i >= 0; i--) {
                        fr.write((listsd.size() - i) + "  " + listsd.get(i).getSimilar() + "%  " + listsd.get(i).getFile1() + "  " + listsd.get(i).getFile2() + "\r\n");
                    }
                }
                fr.write(laiyuan + "  " + new Date().toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (fr != null) fr.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    //将str以指定编码方式，写入文件
    public static void saveFile(File outfile, String str, String encode) {
        BufferedWriter fr = null;
        try {
            fr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile, true), encode));
            fr.write(str);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fr != null)
                    fr.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static String[] readFile(File infile, String encode) {
        BufferedReader in = null;
        String str;
        ArrayList<String> list = new ArrayList<>();
        String[] res = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(infile), encode));
            while ((str = in.readLine()) != null) {
                list.add(str);
            }
            res = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                res[i] = list.get(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return res;
    }
}