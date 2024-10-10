package com.joe.springsec.email.service;

import com.joe.springsec.auth.models.User;
import com.joe.springsec.company.model.Company;
import com.joe.springsec.company.repo.CompanyRepo;
import com.joe.springsec.email.models.EmailConfiguration;
import com.joe.springsec.email.repo.EmailConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Properties;

@Service
@Transactional
public class EmailService {

    @Autowired
    private EmailConfigurationRepository emailConfigRepository;

    @Autowired
    private CompanyRepo companyRepo;

//    @Autowired
//    private JavaMailSender mailSender;

    //public void sendUserCreationEmail(User user, String systemUrl)
    public void sendUserCreationEmail(User user) {
        // Retrieve the email configuration for the company
        EmailConfiguration emailConfig = getEmailConfigurationForCompany(user);

        // Create a JavaMailSender instance based on the company's email configuration
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfig.getSmtpServer());
        mailSender.setPort(Integer.parseInt(emailConfig.getSmtpPort()));
        mailSender.setUsername(emailConfig.getEmailUsername());
        mailSender.setPassword(emailConfig.getEmailPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Create the email message
        SimpleMailMessage message = createEmailMessage(user, emailConfig);

        // Send the email
        mailSender.send(message);
    }

    // Extracted method to create an email message
    private SimpleMailMessage createEmailMessage(User user, EmailConfiguration emailConfig) {
        String subject = "Welcome to the System";
        String content = "Dear " + user.getUsername() + ",\n\n" +
                "You have been successfully registered in the system.\n\n" +
                "Username: " + user.getUsername() + "\n" +
                "Password: " + user.getPassword() + "\n\n" +
                "Best regards,\n" + emailConfig.getCompany().getCompanyName();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailConfig.getSenderEmail());
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(content);

        return message;
    }

    // Extracted method to get Email Configuration
    private EmailConfiguration getEmailConfigurationForCompany(User user) {
        Company company = user.getCompanies().iterator().next(); // Assuming the user belongs to at least one company
        return emailConfigRepository.findByCompany(company)
                .orElseThrow(() -> new IllegalStateException("Email configuration not found for company: " + company.getCompanyName()));
    }

    // Create or update email configuration
    public EmailConfiguration createOrUpdateEmailConfig(Long companyId, EmailConfiguration emailConfig) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        // Set the company in the email configuration
        emailConfig.setCompany(company);

        return emailConfigRepository.save(emailConfig);
    }

    // Get email configuration by company ID
    public EmailConfiguration getEmailConfigByCompany(Long companyId) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        return emailConfigRepository.findByCompany(company)
                .orElseThrow(() -> new IllegalStateException("Email configuration not found for company: " + company.getCompanyName()));
    }

    // Delete email configuration by company ID
    public void deleteEmailConfig(Long companyId) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        EmailConfiguration emailConfig = emailConfigRepository.findByCompany(company)
                .orElseThrow(() -> new IllegalStateException("Email configuration not found for company: " + company.getCompanyName()));

        emailConfigRepository.delete(emailConfig);
    }
}
