package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/admin/common")
public class CommonController {

    // 本地存储目录（你建好的文件夹，结尾带 /）
    private static final String UPLOAD_DIR = "D:/Develop/JetBrains/java_learn/sky-take-out-upload/";
    // 访问前缀，端口和后端一致
    private static final String URL_PREFIX = "http://localhost:8080/upload/";

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}", file.getOriginalFilename());
        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新的文件名
            String objectName = UUID.randomUUID().toString() + extension;

            //确保目录存在
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            //保存到本地
            file.transferTo(new File(dir, objectName));

            //文件的请求路径
            String filePath = URL_PREFIX + objectName;
            log.info("文件上传成功：{}", filePath);
            return Result.success(filePath);
        } catch (Exception e) {
            log.error("文件上传失败", e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}