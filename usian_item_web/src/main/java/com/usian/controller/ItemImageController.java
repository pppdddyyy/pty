package com.usian.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Title: ItemImageController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/8/10 15:07
 */
@RestController
@RequestMapping("file")
public class ItemImageController {

    @Autowired
    private FastFileStorageClient storageClient;

    @RequestMapping("upload")
    public Result  upload(MultipartFile file){



        try {
            //保存到服务器本地
//            file.transferTo(new File("D://imge//" + file.getOriginalFilename()));

            //保存到fastdfs服务器上
            //获取上传文件的名字
            String fileName = file.getOriginalFilename();
            // 获取上次文件的后缀名
            String suffix = fileName.substring(fileName.lastIndexOf(".")+1);//.png


            StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
                    file.getInputStream(), file.getSize(), suffix, null);
            // 获取上次之后的文件ID
            String path = "http://image.usian.com/"+storePath.getFullPath();
            System.out.println("上次的路径："+path);
            return Result.ok(path);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.ok();

    }
}
