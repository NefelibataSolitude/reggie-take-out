/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/28 15:37
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.controller;

import lombok.extern.slf4j.Slf4j;
import net.tecoy.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 文件上传/下载处理
 * @author Tecoy
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${upload.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        if (basePath.equals("")) {
            // 获取当前项目路径
            basePath = System.getProperty("user.dir") + "/upload";
        }
        File realPath = new File(basePath);
        if (!realPath.exists()) {
            realPath.mkdirs();
        }
        // 获取文件后缀
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 文件名
        String fileName = UUID.randomUUID() + suffix;

        try {
            file.transferTo(new File(basePath + "\\" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param response
     * @param name
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response, String name) {
        if (basePath.equals("")) {
            // 获取当前项目路径
            basePath = System.getProperty("user.dir") + "/upload";
        }
        FileInputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = new FileInputStream(basePath + "\\" + name);
            outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            response.setCharacterEncoding("utf-8");
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
