package com.nhan.file;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nhan.model.dto.response.CloudinaryResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Value("${spring.application.name}")
    private String appName;

    @Transactional
    public CloudinaryResponseDTO saveAs(MultipartFile file, String fileName) {
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "public_id", appName + "/" + fileName
            ));

            String url = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");

            return CloudinaryResponseDTO.builder().url(url).publicId(publicId).build();

        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }


}
