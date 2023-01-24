package ccc.keeweinfra.service;

import org.springframework.web.multipart.MultipartFile;

public interface StoreService {
    String upload(MultipartFile multipartFile);
    String upload(MultipartFile multipartFil, Integer width, Integer height);

    void delete(String url);
}
