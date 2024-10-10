package com.joe.springsec.email.web;

import com.joe.springsec.email.models.EmailConfiguration;
import com.joe.springsec.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email-configurations")
public class EmailConfigurationController {

    @Autowired
    private EmailService emailConfigService;

    // Create or update email configuration for a company
    @PostMapping("/{companyId}")
    public ResponseEntity<EmailConfiguration> createOrUpdateEmailConfig(
            @PathVariable Long companyId, @RequestBody EmailConfiguration emailConfig) {
        EmailConfiguration updatedConfig = emailConfigService.createOrUpdateEmailConfig(companyId, emailConfig);
        return ResponseEntity.ok(updatedConfig);
    }

    // Get email configuration for a specific company
    @GetMapping("/{companyId}")
    public ResponseEntity<EmailConfiguration> getEmailConfig(@PathVariable Long companyId) {
        EmailConfiguration emailConfig = emailConfigService.getEmailConfigByCompany(companyId);
        return ResponseEntity.ok(emailConfig);
    }

    // Delete email configuration for a company
    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteEmailConfig(@PathVariable Long companyId) {
        emailConfigService.deleteEmailConfig(companyId);
        return ResponseEntity.noContent().build();
    }
}
