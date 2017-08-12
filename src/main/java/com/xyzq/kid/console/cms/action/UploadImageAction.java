package com.xyzq.kid.console.cms.action;

import com.xyzq.simpson.base.etc.Serial;
import com.xyzq.simpson.base.helper.FileHelper;
import com.xyzq.simpson.maggie.access.spring.MaggieAction;
import com.xyzq.simpson.maggie.framework.Context;
import com.xyzq.simpson.maggie.framework.Visitor;
import com.xyzq.kid.console.admin.action.AdminAjaxAction;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

/**
 * 上传图片动作
 */
@MaggieAction(path = "kid/console/uploadImage")
public class UploadImageAction extends AdminAjaxAction {
    /**
     * 图片上传目录
     */
    @Value("${KID.UPLOAD.DIRECTORY.IMAGE}")
    private String imageUploadDirectory;
    /**
     * 图片上传后下载地址
     */
    @Value("${KID.UPLOAD.URL.IMAGE}")
    private String imageUploadUrl;


    /**
     * 动作执行
     *
     * @param visitor 访问者
     * @param context 请求上下文
     * @return 下一步动作，包括后缀名，null表示结束
     */
    @Override
    public String doExecute(Visitor visitor, Context context) throws Exception {
        if(!(new File(imageUploadDirectory)).exists()) {
            (new File(imageUploadDirectory)).mkdirs();
        }
        File file = (File) context.parameter("file");
        if(null == file) {
            context.set("msg", "文件上传失败");
            return "fail.json";
        }
        String fileName = Serial.getFileMD5String(file);
        int i = file.getAbsolutePath().lastIndexOf(".");
        if(-1 != i) {
            fileName = fileName + file.getAbsolutePath().substring(i);
        }
        File targetFile = new File(imageUploadDirectory + File.separator + fileName);
        FileHelper.copy(file, targetFile, true);
        targetFile.setReadable(true, false);
        context.set("data", "\"" + imageUploadUrl + "/" + fileName + "\"");
        return "success.json";
    }
}
