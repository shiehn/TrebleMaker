package com.treblemaker.services;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static java.lang.System.out;

@Service
public class FeedBackFileSync {

    @Value("${api.user}")
    String apiUser;

    @Value("${api.password}")
    String apiPassword;

    @Value("${api.upload.endpoint}")
    String apiUploadEndpoint;

    public void sync(File file) throws IOException {
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", new FileSystemResource(file));
        HttpHeaders headers = new HttpHeaders();

        String auth = apiUser + ":" + apiPassword;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );
        headers.set( "Authorization", authHeader);

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);

        ResponseEntity<String> response = restTemplate.exchange(apiUploadEndpoint,
                HttpMethod.POST, requestEntity, String.class);

        System.out.println("request : " + requestEntity.getHeaders());
        System.out.println("response status: " + response.getStatusCode());
        System.out.println("response body: " + response.getBody());
    }
}
