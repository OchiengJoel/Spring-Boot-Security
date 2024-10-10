package com.joe.springsec.company.model;

import com.joe.springsec.auth.models.User;
import com.joe.springsec.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Table(name = "cms_company")
@Entity
public class Company extends BaseEntity {

    @Column(name = "company_name")
    private String companyName;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Branch> branches;

    @ManyToMany(mappedBy = "companies")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> users;
}
