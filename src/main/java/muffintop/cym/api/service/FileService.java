package muffintop.cym.api.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.exception.FileUploadFailException;
import muffintop.cym.api.exception.NoFileException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class FileService {

    @Value("${spring.cloud.gcp.storage.url}")
    private String host;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private final Storage storage;

    public String upload(MultipartFile image) throws IOException {

        try {
            File uploadFile = convertFile(image);
            byte[] fileData = FileUtils.readFileToByteArray(uploadFile);
            String uuid = UUID.randomUUID().toString(); // Google Cloud Storage에 저장될 파일 이름
            String imgUrl = host + "/" + bucketName + "/" + uuid;
            String ext = image.getContentType();
            BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, uuid)
                    .setContentType(ext)
                    .build(),
                fileData);
            uploadFile.delete();
            return imgUrl;
        } catch (Exception e) {
            throw new FileUploadFailException();
        }
    }

    public void delete(String objectName) {
        try {
            Blob blob = storage.get(bucketName, objectName);

            Storage.BlobSourceOption precondition =
                Storage.BlobSourceOption.generationMatch(blob.getGeneration());

            storage.delete(bucketName, objectName, precondition);
        } catch (Exception e) {
            throw new NoFileException();
        }
    }

    private File convertFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream outputStream = new FileOutputStream(convertedFile);
        outputStream.write(file.getBytes());
        outputStream.close();

        return convertedFile;
    }

}
