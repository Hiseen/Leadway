package com.leadway.leadway_server.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class AutoLoginData {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private LeadwayUser user;

    private Long token;

    private Long expirationTime;
    
    private boolean remember;

    public boolean isRemember() {
		return remember;
	}

	public void setRemember(boolean remember) {
		this.remember = remember;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeadwayUser getUser() {
        return user;
    }

    public void setUser(LeadwayUser user) {
        this.user = user;
    }

    public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
        this.token = token;
    }


    public Long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Long expirationTime) {
        this.expirationTime = expirationTime;
    }
}
