package com.sky.controller.admin;

import cn.hutool.core.util.IdUtil;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: CommonController
 * USER: SHINIAN
 * DATE: 2023/5/17
 * DESCRIPTION : 全局通用的表现层
 */
@Slf4j
@Api(tags = "B端-通用表现层接口")
@RequestMapping("/admin/common")
@RestController
public class CommonController {

    //注入容器中的AliOss上传工具类的对象
    @Resource
    private AliOssUtil aliOssUtil;

    /**
     * 上传文件到阿里云Oss对象存储
     *
     * @param file 接收到要上传的文件
     * @return 文件在阿里云Oss对象存储中的访问URL路径(响应给前台 : 前台基于路径就可以访问图片并且展示出来)
     */
    @ApiOperation("B端-上传文件到阿里云")
    @PostMapping("/upload")
    public Result<String> uploadImage(MultipartFile file) throws IOException {
        //生成一个在桶中不重复的文件名称
        String originalFilename = file.getOriginalFilename();
        String suffixFileName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String bucketFileName = IdUtil.simpleUUID().concat(suffixFileName);
        //基于阿里云上传工具上传原始文件并接收访问URL路径
        String imageURL = aliOssUtil.upload(file.getBytes(), bucketFileName);
        log.info("文件上传到阿里云Oss对象的URL访问路径 : {}", imageURL);
        return Result.success(imageURL);
    }
}
