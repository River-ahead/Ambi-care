package com.mindcare.service;

import com.mindcare.entity.ChatMessage;
import com.mindcare.repository.ChatHistoryRepository;
import com.mindcare.util.ExcelWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MCP 风格外部工具封装：
 * - 发送高风险邮件预警；
 * - 导出聊天记录 Excel。
 * 当前为本地工具调用风格，未来可替换为真正 MCP client/server。
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MCPExternalService {

    private final JavaMailSender mailSender;
    private final ChatHistoryRepository chatHistoryRepository;
    private final ExcelWriter excelWriter;

    public void sendEmailAlert(String toEmail, String userName, double riskScore, String reason) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(toEmail);
            msg.setSubject("【心理预警】疑似高风险个案提醒");
            msg.setText("系统检测到用户【" + userName + "】存在较高心理风险。\n"
                    + "风险评分：" + riskScore + "\n"
                    + "触发原因：" + reason + "\n\n"
                    + "请尽快根据校园/医院相关流程进行跟进和评估。");
            mailSender.send(msg);
            log.info("已发送高风险预警邮件至 {}", toEmail);
        } catch (Exception e) {
            log.error("发送预警邮件失败（可能未正确配置 SMTP），to={}", toEmail, e);
        }
    }

    public byte[] exportToExcel(Long userId) {
        List<ChatMessage> messages =
                userId == null ? chatHistoryRepository.findAll()
                        : chatHistoryRepository.findByUserIdOrderByCreatedAtAsc(userId);
        return excelWriter.writeChatMessages(messages);
    }
}

