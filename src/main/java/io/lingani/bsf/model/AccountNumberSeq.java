package io.lingani.bsf.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import javax.persistence.*;

@Entity
@SequenceGenerator(name="ACCT_NO_SEQ", initialValue=625130001, allocationSize=50)
public class AccountNumberSeq {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="ACCT_NO_SEQ")
    @Column(nullable=false, updatable=false, unique=true)
    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
}
