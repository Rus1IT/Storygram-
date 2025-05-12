package root.storygram.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import root.storygram.enums.MediaType;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    @Value("${spring.aws.bucketName}")
    private String BUCKET_NAME;

    public void upload(InputStream inputStream, String filename) throws IOException, SdkClientException {
        String mediaType = FileService.getMediaType(filename);
        ObjectMetadata metaData = getObjectMetadata(inputStream, mediaType);

        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME,filename, inputStream, metaData);
        amazonS3.putObject(putObjectRequest);
    }

    public InputStream download(String filename) throws SdkClientException {
        S3Object s3Object = amazonS3.getObject(BUCKET_NAME, filename);
        return s3Object.getObjectContent();
    }

    public void update(InputStream inputStream, String filename) throws IOException, SdkClientException {
        String mediaType =FileService.getMediaType(filename);
        if (!amazonS3.doesObjectExist(BUCKET_NAME, filename)) {
            throw new IllegalArgumentException("Файл " + filename + " не найден в бакете " + BUCKET_NAME);
        }

        ObjectMetadata metaData = getObjectMetadata(inputStream, mediaType);
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, filename, inputStream, metaData);
        amazonS3.putObject(putObjectRequest);
    }

    public void delete(String filename) throws SdkClientException {
        if (!amazonS3.doesObjectExist(BUCKET_NAME, filename)) {
            throw new IllegalArgumentException("Файл " + filename + " не найден в бакете " + BUCKET_NAME);
        }
        amazonS3.deleteObject(BUCKET_NAME, filename);
    }

    private ObjectMetadata getObjectMetadata(InputStream inputStream, String mediaType) throws IOException {
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(inputStream.available());
        metaData.setContentType(mediaType);
        return metaData;
    }

}
