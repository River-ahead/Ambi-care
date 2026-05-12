package com.mindcare.util;

import com.mindcare.entity.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class ExcelWriter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public byte[] writeChatMessages(List<ChatMessage> messages) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("ChatHistory");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("User ID");
            header.createCell(2).setCellValue("Intent");
            header.createCell(3).setCellValue("Emotion Label");
            header.createCell(4).setCellValue("Emotion Score");
            header.createCell(5).setCellValue("User Content");
            header.createCell(6).setCellValue("Bot Reply");
            header.createCell(7).setCellValue("Created At");

            int rowIdx = 1;
            for (ChatMessage msg : messages) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(msg.getId() == null ? 0 : msg.getId());
                row.createCell(1).setCellValue(msg.getUserId() == null ? 0 : msg.getUserId());
                row.createCell(2).setCellValue(msg.getIntent());
                row.createCell(3).setCellValue(msg.getEmotionLabel());
                row.createCell(4).setCellValue(msg.getEmotionScore() == null ? 0.0 : msg.getEmotionScore());
                row.createCell(5).setCellValue(msg.getUserContent());
                row.createCell(6).setCellValue(msg.getBotReply());
                row.createCell(7).setCellValue(
                        msg.getCreatedAt() == null ? "" : msg.getCreatedAt().format(FORMATTER)
                );
            }

            for (int i = 0; i <= 7; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(bos);
            return bos.toByteArray();
        } catch (IOException e) {
            log.error("导出聊天记录 Excel 失败", e);
            return new byte[0];
        }
    }
}

