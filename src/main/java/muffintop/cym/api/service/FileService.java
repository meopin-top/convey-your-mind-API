package muffintop.cym.api.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public void upload(MultipartFile image, String uuid) {
        try {
            File uploadFile = convertFile(image);
            File webpFile = covertToWebp(uploadFile);
            byte[] fileData = FileUtils.readFileToByteArray(webpFile);
            uploadToStorage(uuid, fileData);
            uploadFile.delete();
            webpFile.delete();
        } catch (Exception e) {
            throw new FileUploadFailException();
        }
    }

    private File covertToWebp(File target) throws IOException {
        ImmutableImage immutableImage = ImmutableImage.loader().fromFile(target);
        return immutableImage.output(WebpWriter.MAX_LOSSLESS_COMPRESSION, new File("temp.webp"));
    }

    private void uploadToStorage(String uuid, byte[] fileData) {
        BlobInfo blobInfo = storage.create(
            BlobInfo.newBuilder(bucketName, uuid)
                .setContentType("webp")
                .build(),
            fileData);
    }

    public String makeUrl(String uuid) {
        String imgUrl = host + "/" + bucketName + "/" + uuid;
        return imgUrl;
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
