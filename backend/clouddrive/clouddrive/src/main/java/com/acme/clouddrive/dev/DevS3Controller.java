package com.acme.clouddrive.dev;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dev/s3")
public class DevS3Controller {


    @Value("${app.s3.bucket}")
private String bucketName;

    private final S3Client s3;
    private final String bucket;

    public DevS3Controller(S3Client s3, @Value("${app.s3.bucket}") String bucket) {
        this.s3 = s3;
        this.bucket = bucket;
    }

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        ListBucketsResponse resp = s3.listBuckets();
        List<String> names = resp.buckets().stream().map(b -> b.name()).toList();
        return Map.of(
                "configuredBucket", bucket,
                "allBucketsVisibleToYou", names
        );
    }

    @GetMapping("/head")
public Map<String, Object> head(@RequestParam String key) {
    HeadObjectResponse r = s3.headObject(
            HeadObjectRequest.builder().bucket(bucketName).key(key).build()
    );
    return Map.of(
            "bucket", bucketName,
            "key", key,
            "size", r.contentLength(),
            "contentType", r.contentType(),
            "etag", r.eTag(),
            "lastModified", r.lastModified().toString()
    );
}
}
