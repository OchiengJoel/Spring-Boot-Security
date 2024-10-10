package com.joe.springsec.email.models;

import com.joe.springsec.company.model.Company;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "cms_email_config")
public class EmailConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String smtpServer;

    @NotBlank
    private String smtpPort;

    @NotBlank
    private String emailUsername;

    @NotBlank
    private String emailPassword;

    @NotBlank
    private String senderEmail;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
