package com.du.besttrip.ordersb2c.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@UtilityClass
public final class ResponseUtil {
    public static ResponseEntity<byte[]> toFileResponse(String fileName, byte[] fileContent) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/force-download");
        String urlEncodedFileName = URLEncodeUtil.urlEncode(fileName);
        String contentDisposition = "attachment; filename=%s".formatted(urlEncodedFileName);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }
}
