package com.sparta.scheduleapp.controller;

import com.sparta.scheduleapp.exception.message.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/uploads/";

    @BeforeEach
    public void setup() throws Exception {
        // Create the upload directory if it doesn't exist
        Files.createDirectories(Paths.get(UPLOAD_DIR));
    }

    @Test
    public void testUploadFileSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "Test File Content".getBytes());

        mockMvc.perform(multipart("/api/files/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("File uploaded successfully: test.jpg"));

        Path path = Paths.get(UPLOAD_DIR + "test.jpg");
        assertThat(Files.exists(path)).isTrue();
    }

    @Test
    public void testUploadFileDisallowedFileType() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Test File Content".getBytes());

        mockMvc.perform(multipart("/api/files/upload")
                        .file(file))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(ErrorMessage.INVALID_FILETYPE.getMessage()));
    }

    @Test
    public void testDownloadFileSuccess() throws Exception {
        // First, upload a file to ensure it exists for download
        Path path = Paths.get(UPLOAD_DIR + "test.jpg");
        Files.write(path, "Test File Content".getBytes());

        MvcResult result = mockMvc.perform(get("/api/files/download/test.jpg"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test.jpg"))
                .andReturn();

        byte[] content = result.getResponse().getContentAsByteArray();
        assertThat(content).isEqualTo("Test File Content".getBytes());
    }

    @Test
    public void testDownloadFileNotFound() throws Exception {
        mockMvc.perform(get("/api/files/download/nonexistent.jpg"))
                .andExpect(status().isNotFound());
    }
}
