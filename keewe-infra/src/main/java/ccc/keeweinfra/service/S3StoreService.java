package ccc.keeweinfra.service;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        String s3FileName = createFileName(multipartFile.getOriginalFilename());

        ObjectMetadata objMeta = new ObjectMetadata();
        try {
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
            bufferedImage = resizeImage(bufferedImage, 450, 450);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpeg", out);
            byte[] bytes = out.toByteArray();
            InputStream imageInputStream = new ByteArrayInputStream(bytes);

            objMeta.setContentLength(bytes.length);
            amazonS3.putObject(bucket, s3FileName, imageInputStream, objMeta);
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

    private String createFileName(String originalFileName) {
        return UUID.randomUUID() + "-" + originalFileName;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, Integer targetWidth, Integer targetHeight) {
        Integer imageLength = Math.min(originalImage.getHeight(), originalImage.getWidth());
        BufferedImage scaledImage =
                Scalr.crop(originalImage, (originalImage.getWidth() - imageLength) / 2,
                        (originalImage.getHeight() - imageLength) / 2, imageLength, imageLength);
        return Scalr.resize(scaledImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_EXACT, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }
}
