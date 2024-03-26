package com.fang.database.postgresql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "cg_telemetry_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    public static final String STATUS_ONLINE = "Online";

    public static final String STATUS_OFFLINE = "Offline";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "password")
    private String password;

    @Column(name = "position")
    private String position;

    @Column(name = "role")
    private String role;

    @Column(name = "icon_id")
    private int iconId;

    @Column(name = "dep_name")
    private String departmentName;

    @Column(name = "phone_num")
    private String phoneNum;

    @Column(name = "last_request_time")
    private Date lastRequestTime;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "registration_id")
    private String registrationId;

    @Transient
    private String status;

    @Transient
    private boolean isCacheUpdated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
