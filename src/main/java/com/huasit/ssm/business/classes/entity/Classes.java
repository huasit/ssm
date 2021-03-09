package com.huasit.ssm.business.classes.entity;


import com.huasit.ssm.core.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "classes")
@NoArgsConstructor
@Data
public class Classes {

    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     *
     */
    @Column(nullable = false)
    private boolean del;

    /**
     *
     */
    @Column(nullable = false)
    private String code;

    /**
     *
     */
    @Column(nullable = false)
    private String name;

    /**
     *
     */
    @Column(nullable = false)
    private Long creatorId;

    /**
     *
     */
    @Column(nullable = false)
    private Date createTime;

    /**
     *
     */
    @OneToOne
    private User teacher;
}
