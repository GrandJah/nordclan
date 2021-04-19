package com.nordclan.test.auth.controller;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nordclan.test.auth.service.ImageGenerator;

@Controller
public class ImageController {
  private final ImageGenerator imageGenerator;

  public ImageController(ImageGenerator imageGenerator) {
    this.imageGenerator = imageGenerator;
  }

  @RequestMapping("/pictures/{text}")
  @ResponseBody
  public HttpEntity<byte[]> getArticleImage(@PathVariable String text) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.IMAGE_JPEG);
    byte[] image = this.imageGenerator.generateFromText(text);
    headers.setContentLength(image.length);
    return new HttpEntity<>(image, headers);
  }
}
