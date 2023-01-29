package ccc.keeweinfra.service.image;

import org.springframework.web.multipart.MultipartFile;

public interface StoreService {
    String upload(MultipartFile multipartFile);
    String upload(MultipartFile multipartFile, Integer width, Integer height);

    void delete(String url);
}
