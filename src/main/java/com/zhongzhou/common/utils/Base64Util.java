package com.zhongzhou.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName Base64Util
 * @Description file与base64互转工具类
 * @Date 2020/10/11 9:22
 * @Author
 */
public class Base64Util {
    /**
     * 声明Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(Base64Util.class.getName());

    /**
     * 将文件转化为Base64字符串
     *
     * @param file 文件
     * @return base64编码
     */
    public static String getFileBase64(MultipartFile file) {
        InputStream in = null;
        byte[] data = null;
        try {
            in = file.getInputStream();
            data = new byte[in.available()];
            in.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
}
