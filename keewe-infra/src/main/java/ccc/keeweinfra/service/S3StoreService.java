package ccc.keeweinfra.service;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3StoreService implements StoreService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Override
    public String upload(MultipartFile multipartFile) {
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        try {
            objMeta.setContentLength(multipartFile.getInputStream().available());
            amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);
        } catch (IOException e) {
            throw new KeeweException(KeeweRtnConsts.ERR600);
        }

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }

    @Override
    public void delete(String url) {
        amazonS3.deleteObject(bucket, urlToKey(url));
    }

    private String urlToKey(String url) {
        URL urlObject = null;
        try {
            urlObject = new URL(url);
        } catch (MalformedURLException e) {
            throw new KeeweException(KeeweRtnConsts.ERR424);
        }
        return urlObject.getPath();
    }
}
