package com.joe.springsec.email.service;

import com.joe.springsec.auth.models.User;
import com.joe.springsec.auth.services.UserDetailsImpl;
import com.joe.springsec.company.model.Company;
import com.joe.springsec.company.repo.CompanyRepo;
import com.joe.springsec.email.models.EmailConfiguration;
import com.joe.springsec.email.repo.EmailConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Service
@Transactional
public class EmailService {

    @Autowired
    private EmailConfigurationRepository emailConfigRepository;

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private JavaMailSender defaultMailSender;

    @Autowired
    private Environment env; // To access properties in application.properties

    public void sendUserCreationEmail(User user) {
        // Retrieve the email configuration for the first company the user is associated with, or default to system configuration
        EmailConfiguration emailConfig = getEmailConfigurationForCompanyOrDefault(user);

        // Create a JavaMailSender instance based on the company's email configuration
        JavaMailSenderImpl mailSender = createMailSender(emailConfig);

        // Create the email message
        SimpleMailMessage message = createEmailMessage(user, emailConfig);

        // Send the email
        mailSender.send(message);
    }

    private JavaMailSenderImpl createMailSender(EmailConfiguration emailConfig) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfig.getSmtpServer());
        mailSender.setPort(Integer.parseInt(emailConfig.getSmtpPort()));
        mailSender.setUsername(emailConfig.getEmailUsername());
        mailSender.setPassword(emailConfig.getEmailPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }

    private SimpleMailMessage createEmailMessage(User user, EmailConfiguration emailConfig) {
        String companyName = (emailConfig.getCompany() != null) ?
                emailConfig.getCompany().getCompanyName() :
                env.getProperty("default.company.name", "Default Company");

        String subject = "Welcome to the System";
        String content = "Dear " + user.getUsername() + ",\n\n" +
                "You have been successfully registered in the system.\n\n" +
                "Username: " + user.getUsername() + "\n" +
                "Password: " + user.getPassword() + "\n\n" +
                "Best regards,\n" + companyName;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailConfig.getSenderEmail());
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(content);

        return message;
    }

    public void sendLoginNotificationEmail(UserDetailsImpl userDetails) {
        // Get the company IDs from UserDetailsImpl
        List<Long> companyIds = userDetails.getCompanyIds();

        // Retrieve the first company (if available) to get email configuration
        Company company = null;
        if (!companyIds.isEmpty()) {
            company = companyRepo.findById(companyIds.get(0)) // Adjust repository method if necessary
                    .orElse(null); // Handle case where company might not be found
        }

        // Retrieve the email configuration for the company using User's companies
        EmailConfiguration emailConfig;
        if (company != null) {
            emailConfig = emailConfigRepository.findByCompany(company)
                    .orElseGet(this::getDefaultEmailConfiguration); // Fallback to default if none found
        } else {
            emailConfig = getDefaultEmailConfiguration(); // Fallback if no company found
        }

        // Create a JavaMailSender instance based on the company's email configuration
        JavaMailSenderImpl mailSender = createMailSender(emailConfig);

        // Create the email message
        SimpleMailMessage message = createLoginNotificationMessage(userDetails, emailConfig);

        // Send the email
        mailSender.send(message);
    }

    private SimpleMailMessage createLoginNotificationMessage(UserDetailsImpl userDetails, EmailConfiguration emailConfig) {
        String companyName = (emailConfig.getCompany() != null) ?
                emailConfig.getCompany().getCompanyName() :
                env.getProperty("default.company.name", "Default Company");

        String subject = "New Login Notification";
        String content = "Dear " + userDetails.getUsername() + ",\n\n" +
                "We wanted to inform you that your account was just accessed with the following credentials:\n\n" +
                "Username: " + userDetails.getUsername() + "\n" +
                "If this wasn't you, please contact support immediately.\n\n" +
                "Best regards,\n" + companyName;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailConfig.getSenderEmail());
        message.setTo(userDetails.getEmail()); // Ensure getEmail() is available in UserDetailsImpl
        message.setSubject(subject);
        message.setText(content);

        return message;
    }

    // Updated method to get email configuration based on User
    private EmailConfiguration getEmailConfigurationForCompanyOrDefault(User user) {
        // Retrieve the first company associated with the user
        Optional<Company> companyOpt = user.getCompanies().stream().findFirst();
        if (companyOpt.isPresent()) {
            // Use the company's email configuration or fallback to the default configuration
            return emailConfigRepository.findByCompany(companyOpt.get())
                    .orElseGet(this::getDefaultEmailConfiguration); // Fallback to default if none found
        } else {
            return getDefaultEmailConfiguration(); // Fallback if no company found
        }
    }

    // Default system-wide email configuration (with values from application.properties)
    private EmailConfiguration getDefaultEmailConfiguration() {
        EmailConfiguration defaultConfig = new EmailConfiguration();
        defaultConfig.setSmtpServer(env.getProperty("default.smtp.server", "smtp.gmail.com"));
        defaultConfig.setSmtpPort(env.getProperty("default.smtp.port", "587"));
        defaultConfig.setEmailUsername(env.getProperty("default.email.username", "ochiengj2910@gmail.com"));
        defaultConfig.setEmailPassword(env.getProperty("default.email.password", "yzgr hbol tnma eklr"));
        defaultConfig.setSenderEmail(env.getProperty("default.sender.email", "ochiengj2910@gmail.com"));
        return defaultConfig;
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
