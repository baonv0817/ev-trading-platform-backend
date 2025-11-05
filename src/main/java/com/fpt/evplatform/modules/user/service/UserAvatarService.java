package com.fpt.evplatform.modules.user.service;


import com.fpt.evplatform.common.enums.ErrorCode;
import com.fpt.evplatform.common.exception.AppException;
import com.fpt.evplatform.modules.user.dto.response.AvatarResponse;
import com.fpt.evplatform.modules.user.entity.User;
import com.fpt.evplatform.modules.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserAvatarService {

    private final com.cloudinary.Cloudinary cloudinary;
    private final UserRepository userRepo;

    // Giới hạn 5MB & chỉ ảnh
    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;

    @Transactional
    public AvatarResponse uploadMyAvatar(String username, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new AppException(ErrorCode.FILE_TYPE_NOT_ALLOWED);
        }

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        try {
            // Đặt public_id cố định để thay thế ảnh cũ
            String folder = "ev/users/" + user.getUserId();
            String publicId = folder + "/avatar";

            // Nếu đã có avatar trước đó -> xoá version cũ (không bắt buộc nhưng sạch)
            if (user.getAvatarPublicId() != null) {
                try {
                    cloudinary.uploader().destroy(user.getAvatarPublicId(), Map.of());
                } catch (Exception ignored) {}
            }

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of(
                            "public_id", publicId,
                            "overwrite", true,
                            "folder", folder,              // để Cloudinary quản lý trong thư mục
                            "resource_type", "image",
                            "unique_filename", false,
                            "use_filename", true
                    )
            );

            String secureUrl = (String) uploadResult.get("secure_url");    // URL gốc
            String uploadedPublicId = (String) uploadResult.get("public_id");

            // Lưu vào DB
            user.setAvatarPublicId(uploadedPublicId);
            user.setAvatarUrl(secureUrl);
            userRepo.save(user);

            // Build URL thumb 256x256 crop vào mặt
            String urlThumb = cloudinary.url()
                    .resourceType("image")
                    .transformation(
                            new com.cloudinary.Transformation()
                                    .width(256).height(256)
                                    .crop("thumb").gravity("face")
                                    .quality("auto").fetchFormat("auto")
                    )
                    .secure(true)
                    .generate(uploadedPublicId);

            return AvatarResponse.builder()
                    .publicId(uploadedPublicId)
                    .url(secureUrl)
                    .urlThumb(urlThumb)
                    .build();

        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Transactional
    public void deleteMyAvatar(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (user.getAvatarPublicId() == null) return;

        try {
            cloudinary.uploader().destroy(user.getAvatarPublicId(), Map.of());
        } catch (Exception ignored) {}

        user.setAvatarPublicId(null);
        user.setAvatarUrl(null);
        userRepo.save(user);
    }
}
