package com.clothes.recognition.image.utilities;

import com.amazonaws.util.IOUtils;
import com.clothes.recognition.image.exception.ConvertImageException;
import com.clothes.recognition.image.model.dto.ImageDto;
import com.clothes.recognition.image.model.enums.Extension;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.EnumUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;

@Slf4j
@Component
public class FileHelper {

    @NotNull
    public static String getFileName(ImageDto imageDto) {
        return imageDto.getFile().getName().concat(String.valueOf(imageDto.getReceiveDateTime()));
    }

    public static File convert(MultipartFile multipartFile) {
        File file = new File(multipartFile.getOriginalFilename());
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();
        } catch (IOException e) {
            log.error("filed to convert MultipartFile to File ".concat(e.getMessage()));
        }
        return file;
    }

    @NotNull
    public static ByteBuffer getByteBufferFromImage(ImageDto imageDto) {
        try {
            return ByteBuffer.wrap(IOUtils.toByteArray(new FileInputStream(FileHelper.convert(imageDto.getFile()))));
        } catch (IOException e) {
            log.error("filed to getByteBufferFromImage ".concat(e.getMessage()));
        }
        throw new ConvertImageException("getByteBufferFromImage failed");
    }

    public static String getMD5Sum(MultipartFile file) {
        try {
            return org.apache.commons.codec.digest.DigestUtils.md5Hex(file.getInputStream());
        } catch (IOException e) {
            log.error("filed to getMD5Sum ".concat(e.getMessage()));
        }
        throw new ConvertImageException("getMD5Sum failed");
    }

    public static void checkExtension(String originalFileName) {
        final String extension = FilenameUtils.getExtension(originalFileName);
        if (extension != null && EnumUtils.isValidEnum(Extension.class, extension.toUpperCase())) {
            throw new IllegalStateException("file type invalid, correct types: " + Extension.values());
        }
    }
}
