package com.kaltsit;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 简易补丁打包工具
 * @Author wangcy
 * @Date 2021/6/10 10:22
 */
public class FileCopier {

    static final File dir = new File("C:\\_git_repo\\3.2_Gemini_slszyzz");
    static final File targetDir = new File("C:\\Users\\EDZ\\Desktop\\3.2_Gemini_slszyzz" + getTimestamp());

    public static void main(String[] args) throws Exception {
        InputStream is = ClassLoader.getSystemResourceAsStream("files.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while((line = reader.readLine()) != null) {
            if(isSourceFile(line)) {
                String path = line.replace("src/main/app", "").replace("src/main/business", "")
                        .replace("src/main/core", "").replace("src/main/resourcelibrary", "");
                File source = new File(dir, "/target/classes/" + path.replace(".java", ".class"));
                File target = new File(targetDir, "/WEB-INF/classes/" + path.replace(".java", ".class"));
                if(!target.getParentFile().exists()) {
                    target.getParentFile().mkdirs();
                }
                Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }else if(isMapperFile(line)) {
                String path = line.replace("src/main/resources", "");
                File source = new File(dir, "/target/classes" + path);
                File target = new File(targetDir, "/WEB-INF/classes/" + path);
                if(!target.getParentFile().exists()) {
                    target.getParentFile().mkdirs();
                }
                Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }else if(isWebappFile(line)) {
                File source = new File(dir, line);
                File target = new File(targetDir, line.replace("src/main/webapp", ""));
                if(!target.getParentFile().exists()) {
                    target.getParentFile().mkdirs();
                }
                Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }else {
                System.out.println("不识别的路径：" + line);
            }
        }
        System.out.println("打包完成：" + targetDir.getPath());
    }

    static boolean isSourceFile(String str) {
        return str.contains("src/main/app") || str.contains("src/main/business") ||
                str.contains("src/main/core") || str.contains("src/main/resourcelibrary");
    }

    static boolean isMapperFile(String str) {
        return str.contains("src/main/resources/mapper");
    }

    static boolean isWebappFile(String str) {
        return str.contains("src/main/webapp");
    }

    static String getTimestamp() {
        DateFormat df = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");
        return df.format(new Date());
    }

}
