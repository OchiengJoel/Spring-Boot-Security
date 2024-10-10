package com.joe.springsec.company.model;

import com.joe.springsec.common.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cms_branch")
public class Branch extends BaseEntity {

    @Column(name = "branch_code")
    private String branchCode;

    @Column(name = "branch_name")
    private String branchName;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

}
