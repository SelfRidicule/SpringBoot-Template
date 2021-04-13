package com.zhongzhou.api.common;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import com.zhongzhou.common.base.BaseController;
import com.zhongzhou.common.bean.ReturnEntity;
import com.zhongzhou.common.bean.ReturnEntityError;
import com.zhongzhou.common.bean.ReturnEntitySuccess;
import com.zhongzhou.common.utils.Constants;
import com.zhongzhou.common.utils.FileUploadUtil;
import io.github.yedaxia.apidocs.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @ClassName: UploadController
 * @Description: 附件上传
 *
 * @Date: 2020-06-28 16:40:06
 **/
@RestController
@RequestMapping("/api")
@Slf4j
@Ignore
public class UploadController extends BaseController {
    private static final long serialVersionUID = 6087390561442587655L;
    private static final String UPLOADFILE_TYPE_IMG = "img";
    private static final String UPLOADFILE_TYPE_VIDEO = "video";
    private static final String UPLOADFILE_TYPE_AUDIO = "audio";
    private static final String UPLOADFILE_TYPE_EXCEL = "excel";
    private static final String UPLOADFILE_TYPE_WORD = "word";
    private static final String UPLOADFILE_TYPE_PDF = "pdf";
    private static final String UPLOADFILE_TYPE_PPT = "ppt";
    private static final String UPLOADFILE_TYPE_TXT = "txt";
    /**
     * 图片后缀
     */
    private static final String IMAGE_SUFFIX = "jpg|png|gif|bmp|jpeg";
    /**
     * 视频后缀
     */
    private static final String VIDEO_SUFFIX = "mp4|mov|webm|ogg";
    /**
     * 音频后缀
     */
    private static final String AUDIO_SUFFIX = "mp3|wav|ogg";
    /**
     * excel后缀
     */
    private static final String EXCEL_SUFFIX = "xls|xlsx|xlsm";
    /**
     * word后缀
     */
    private static final String WORD_SUFFIX = "doc|docx";
    /**
     * ppt后缀
     */
    private static final String PPT_SUFFIX = "ppt|pptx";
    /**
     * pdf后缀
     */
    private static final String PDF_SUFFIX = "pdf";
    /**
     * txt后缀
     */
    private static final String TXT_SUFFIX = "txt";

    @Value("${constants.fileSrc}")
    private String fileSrc;
    @Value("${constants.filePath}")
    private String realPath;

    @Resource
    private TokenController tokenController;

    /**
     * 上传附件，返回URL
     *
     * @param file     附件
     * @param typeCode 类型编码
     * @return ReturnEntity
     */
    @PostMapping("/uploadWithURL")
    public ReturnEntity uploadWithURL(@RequestParam("file") MultipartFile file, @RequestParam(value = "typeCode", required = false) String typeCode) {
        try {
            String filePath = FileUploadUtil.createFileCatalog(realPath, "upload");
            String originalFileName = file.getOriginalFilename();
            String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
            String url = filePath + SecureUtil.simpleUUID() + suffix;
            String fileType = "";
            if (IMAGE_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                fileType = UPLOADFILE_TYPE_IMG;
            } else if (VIDEO_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                fileType = UPLOADFILE_TYPE_VIDEO;
            } else if (AUDIO_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                fileType = UPLOADFILE_TYPE_AUDIO;
            } else if (EXCEL_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                fileType = UPLOADFILE_TYPE_EXCEL;
            } else if (WORD_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                fileType = UPLOADFILE_TYPE_WORD;
            } else if (PDF_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                fileType = UPLOADFILE_TYPE_PDF;
            } else if (PPT_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                fileType = UPLOADFILE_TYPE_PPT;
            } else {
                fileType = "";
            }
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(realPath + url));

            return new ReturnEntitySuccess("上传成功", url);

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ReturnEntityError("上传失败");
        }
    }

    @PostMapping("/upload/replace")
    public ReturnEntity uploadFileWithName(@RequestParam("file") MultipartFile file,
                                           @RequestParam(value = "fileName", required = true) String fileName,
                                           @RequestParam(value = "fileType", required = false, defaultValue = "") String fileType,
                                           @RequestParam(value = "fileLength", required = false, defaultValue = "10") Integer fileLength,
                                           HttpServletRequest request, HttpServletResponse response) {
        try {
            String contextPath = request.getContextPath();
            System.out.println("contextPath=" + contextPath);
            FileUtil.mkdir(new File(realPath));
            String originalFileName = file.getOriginalFilename();
            String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
//            String compressUrl = "ys_" + originalFileName;
            if (StringUtils.isBlank(fileType)) {
                if (IMAGE_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                    fileType = UPLOADFILE_TYPE_IMG;
                } else if (VIDEO_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                    fileType = UPLOADFILE_TYPE_VIDEO;
                } else if (AUDIO_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                    fileType = UPLOADFILE_TYPE_AUDIO;
                } else if (EXCEL_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                    fileType = UPLOADFILE_TYPE_EXCEL;
                } else if (WORD_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                    fileType = UPLOADFILE_TYPE_WORD;
                } else if (PDF_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                    fileType = UPLOADFILE_TYPE_PDF;
                } else if (PPT_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                    fileType = UPLOADFILE_TYPE_PPT;
                } else if (TXT_SUFFIX.contains(suffix.substring(1).toLowerCase())) {
                    fileType = UPLOADFILE_TYPE_TXT;
                } else {
                    fileType = "";
                }
            }

            if (file.isEmpty()) {
                return new ReturnEntityError("附件不能为空");
            } else if (!FileUploadUtil.checkFileSuffix(fileType, originalFileName.substring(originalFileName.lastIndexOf(".") + 1))) {
                return new ReturnEntityError("附件格式错误");
            } else {
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(realPath + fileName));
//                if (UPLOADFILE_TYPE_IMG.equals(fileType)) {
//                    //图片压缩
//                    Thumbnails.of(file.getInputStream()).scale(0.5f).outputQuality(0.5f).toFile(realPath + compressUrl);
//                }
                return new ReturnEntitySuccess(Constants.MSG_UPLOAD_SUCCESS, fileName);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ReturnEntityError(Constants.MSG_UPLOAD_FAILED);
        }
    }

}
