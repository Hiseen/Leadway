package com.leadway.leadway_server.entities;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter @Setter @NoArgsConstructor
public class AutoLoginData {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private LeadwayUser user;

    private Long token;

    private Long expirationTime;
    
    private boolean remember;
}
