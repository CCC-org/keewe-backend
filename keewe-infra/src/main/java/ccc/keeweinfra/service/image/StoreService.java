package ccc.keeweinfra.service.image;

import org.springframework.web.multipart.MultipartFile;

public interface StoreService {
    String upload(MultipartFile multipartFile);

    void delete(String url);
}
