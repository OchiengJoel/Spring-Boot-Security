package com.joe.springsec.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "cms_sequence")
    @SequenceGenerator(name = "cms_sequence", sequenceName = "cms_sequence", allocationSize = 100)
    private Long id;

    @Column(name = "created_time", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_time", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "created_by")
    @ToString.Exclude
    private String createdBy;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "updated_by")
    @ToString.Exclude
    private String updatedBy;

}

