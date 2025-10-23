package com.fpt.evplatform.modules.media.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.fpt.evplatform.modules.media.dto.response.MediaResponse;
import com.fpt.evplatform.modules.media.entity.Media;

import com.fpt.evplatform.modules.media.repository.MediaRepository;
import com.fpt.evplatform.modules.salepost.entity.SalePost;

import com.fpt.evplatform.modules.salepost.repository.SalePostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final Cloudinary cloudinary;
    private final MediaRepository mediaRepository;
    private final SalePostRepository salePostRepository;

    @Value("${cloudinary.upload-folder-root:sale-posts}")
    private String rootFolder;

    @Value("${cloudinary.max-file-size-mb:10}")
    private int maxFileSizeMb;

    private static final Set<String> IMAGE_TYPES = Set.of("image/jpeg","image/png","image/webp");
    private static final Set<String> VIDEO_TYPES = Set.of("video/mp4","video/quicktime","video/webm");

    public List<MediaResponse> listByListing(Integer listingId) {
        return mediaRepository.findBySalePost_ListingIdOrderBySortOrderAsc(listingId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public List<MediaResponse> upload(Integer listingId, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) throw new IllegalArgumentException("No files uploaded");

        SalePost post = salePostRepository.findById(listingId)
                .orElseThrow(() -> new NoSuchElementException("SalePost not found"));

        List<Media> existing = mediaRepository.findBySalePost_ListingIdOrderBySortOrderAsc(listingId);
        int nextOrder = existing.isEmpty() ? 1 : existing.get(existing.size() - 1).getSortOrder() + 1;

        List<Media> saved = new ArrayList<>();

        for (MultipartFile f : files) {
            if (f.getSize() > (long) maxFileSizeMb * 1024 * 1024) {
                throw new IllegalArgumentException("File too large (>" + maxFileSizeMb + "MB)");
            }

            String ct = Optional.ofNullable(f.getContentType()).orElse("").toLowerCase(Locale.ROOT);

            String resourceType;
            String type;
            if (IMAGE_TYPES.contains(ct)) { resourceType = "image"; type = "IMAGE"; }
            else if (VIDEO_TYPES.contains(ct)) { resourceType = "video"; type = "VIDEO"; }
            else throw new IllegalArgumentException("Unsupported content type: " + ct);

            String folder = rootFolder + "/" + listingId;

            Map<?,?> res;
            try {
                res = cloudinary.uploader().upload(
                        f.getBytes(),
                        ObjectUtils.asMap(
                                "folder", folder,
                                "use_filename", true,
                                "unique_filename", true,
                                "overwrite", false,
                                "resource_type", resourceType
                        )
                );
            } catch (Exception e) {
                throw new RuntimeException("Upload failed: " + e.getMessage(), e);
            }

            String secureUrl = (String) res.get("secure_url");
            String publicId = (String) res.get("public_id");

            Media media = Media.builder()
                    .salePost(post)
                    .publicId(publicId)
                    .url(secureUrl)
                    .type(type)
                    .sortOrder(nextOrder++)
                    .build();

            saved.add(mediaRepository.save(media));
        }

        return saved.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Integer listingId, Integer mediaId) {
        Media m = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new NoSuchElementException("Media not found"));
        if (!m.getSalePost().getListingId().equals(listingId))
            throw new IllegalArgumentException("Media not in this SalePost");

        try {
            cloudinary.uploader().destroy(
                    m.getPublicId(),
                    ObjectUtils.asMap("resource_type", m.getType().equals("VIDEO") ? "video" : "image")
            );
        } catch (Exception ignored) {}

        mediaRepository.delete(m);
    }

    @Transactional
    public void reorder(Integer listingId, List<Integer> orderedIds) {
        if (orderedIds == null || orderedIds.isEmpty()) return;
        List<Media> all = mediaRepository.findBySalePost_ListingIdOrderBySortOrderAsc(listingId);
        Map<Integer, Media> map = all.stream().collect(Collectors.toMap(Media::getMediaId, x -> x));
        int order = 1;
        for (Integer id : orderedIds) {
            Media m = map.get(id);
            if (m != null) m.setSortOrder(order++);
        }
        mediaRepository.saveAll(all);
    }

    private MediaResponse toDto(Media m) {
        MediaResponse dto = new MediaResponse();
        dto.setMediaId(m.getMediaId());
        dto.setPublicId(m.getPublicId());
        dto.setUrl(m.getUrl());
        dto.setType(m.getType());
        dto.setSortOrder(m.getSortOrder());

        String resource = "VIDEO".equalsIgnoreCase(m.getType()) ? "video" : "image";
        String publicId = m.getPublicId();

        // ---------------- build large ----------------
        Transformation large = new Transformation()
                .width(1280)
                .crop("limit")
                .quality("auto")
                .fetchFormat("auto"); // dùng fetchFormat cho cả ảnh/video

        // ---------------- build thumb ----------------
        Transformation thumb = new Transformation()
                .width(320)
                .height(320)
                .crop("fill")
                .gravity("auto")
                .quality("auto")
                .fetchFormat("auto");

        // ---------------- generate URLs ----------------
        dto.setUrlLarge(
                cloudinary.url()
                        .resourceType(resource)   // "image" hoặc "video"
                        .secure(true)
                        .transformation(large)
                        .generate(publicId)
        );

        dto.setUrlThumb(
                cloudinary.url()
                        .resourceType(resource)
                        .secure(true)
                        .transformation(thumb)
                        .generate(publicId)
        );

        return dto;
    }


}
