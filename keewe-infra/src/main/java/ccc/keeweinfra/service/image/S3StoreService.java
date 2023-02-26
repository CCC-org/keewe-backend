package ccc.keeweinfra.service.image;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
import java.net.URLDecoder;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3StoreService implements StoreService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            return this.upload(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
        } catch (IOException e) {
            throw new KeeweException(KeeweRtnConsts.ERR600);
        }
    }

    @Override
    public String upload(MultipartFile multipartFile, Integer width, Integer height) {
        if (!checkFileFormat(Objects.requireNonNull(multipartFile.getOriginalFilename())))
            throw new KeeweException(KeeweRtnConsts.ERR449);

        try {
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
            bufferedImage = resizeImage(bufferedImage, width, height);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpeg", out);
            InputStream imageInputStream = new ByteArrayInputStream(out.toByteArray());

            return this.upload(imageInputStream, multipartFile.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

        return URLDecoder.decode(urlObject.getPath().substring(1));
    }

    private String upload(InputStream inputStream, String fileName) {

        String s3FileName = createFileName(fileName);
        try {
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(inputStream.available());
            amazonS3.putObject(bucket, s3FileName, inputStream, objMeta);
        } catch (IOException e) {
            throw new KeeweException(KeeweRtnConsts.ERR600);
        }

        return amazonS3.getUrl(bucket, s3FileName).toString();
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

    private boolean checkFileFormat(String fileName) {
        return fileName.endsWith(".jpeg") || fileName.endsWith(".jpg") || fileName.endsWith(".png");
    }
}
