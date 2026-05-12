package com.mindcare.controller;

import com.mindcare.entity.ChatMessage;
import com.mindcare.entity.EmotionRecord;
import com.mindcare.service.AdminService;
import com.mindcare.service.MCPExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final MCPExternalService mcpExternalService;

    @GetMapping("/messages")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ChatMessage> listAllMessages() {
        return adminService.findAllMessages();
    }

    @GetMapping("/messages/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ChatMessage> listMessagesByUser(@PathVariable Long userId) {
        return adminService.findMessagesByUser(userId);
    }

    @GetMapping("/emotions/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<EmotionRecord> listEmotionsByUser(@PathVariable Long userId) {
        return adminService.findEmotionsByUser(userId);
    }

    @GetMapping("/messages/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportMessages(@RequestParam(required = false) Long userId) {
        byte[] bytes = mcpExternalService.exportToExcel(userId);
        String filename = userId == null ? "all-messages.xlsx" : "user-" + userId + "-messages.xlsx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }
}

