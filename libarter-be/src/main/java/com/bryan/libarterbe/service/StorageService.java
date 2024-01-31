package com.bryan.libarterbe.service;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StorageService {

    BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(System.getenv("azure_conn_str")).buildClient();
    BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("test");

    public String generateFilename(int uid, int index)
    {
        LocalDateTime now = LocalDateTime.now();
        return uid + "_" + index + "_" + now;
    }
    public void writeResource(String filename, String image)
    {
        BlobClient blobClient = containerClient.getBlobClient(filename);
        blobClient.upload(BinaryData.fromString(image));
    }

    public String readResource(String filename)
    {
        try {
            BlobClient blobClient = containerClient.getBlobClient(filename);
            return blobClient.downloadContent().toString();
        }catch (Exception e)
        {
            return null;
        }
    }

    public boolean deleteResource(String filename)
    {
        try {
            BlobClient blobClient = containerClient.getBlobClient(filename);
            blobClient.delete();
            return true;
        }catch (BlobStorageException e)
        {
            if(e.getStatusCode() == 404)
            {
                return true;
            }
            else return false;
        }
    }
}
