package com.lmeng.api.project.controller;

import com.lmeng.api.project.common.BaseResponse;
import com.lmeng.api.project.common.ResultUtils;
import com.lmeng.api.project.manager.OosManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/8/5
 * @des 文件上传控制类
 */

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    private static final String FILE_DELIMITER = ",";

    @Resource
    private OosManager cosManager;

    /**
     * 上传多个文件
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public BaseResponse<Map<String, Object>> uploadFile(@RequestBody MultipartFile file, HttpServletRequest request) throws IOException {
        Map<String, Object> result = new HashMap<>(2);
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        //拼接新的文件名字
        String fileName = uuid + "-" + file.getOriginalFilename();
        //上传文件
        String imageUrl = cosManager.uploadFileOSS(file.getInputStream(), fileName);
        //返回map记恨
        result.put("url", imageUrl);
        return ResultUtils.success(result);
    }

    @PostMapping("/uploads")
    public BaseResponse<Map<String, Object>> uploadFiles(List<MultipartFile> files) throws Exception {
        try {
            // 上传文件路径
//            String filePath = RuoYiConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files) {
                // 上传并返回新文件名称
                String uuid = RandomStringUtils.randomAlphanumeric(8);
                String fileName = uuid + "-" + file.getOriginalFilename();
                String imageUrl = cosManager.uploadFileOSS(file.getInputStream(), fileName);
                urls.add(imageUrl);
                fileNames.add(fileName);
//                newFileNames.add(FileUtils.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());
            }
            Map<String, Object> result = new HashMap<>();
            result.put("urls", StringUtils.join(urls, FILE_DELIMITER));
            result.put("fileNames", StringUtils.join(fileNames, FILE_DELIMITER));
//            result.put("newFileNames" , StringUtils.join(newFileNames, FILE_DELIMITER));
            result.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMITER));
            return ResultUtils.success(result);
        } catch (Exception e) {
            throw new Exception("上传文件失败");
        }
    }

}
