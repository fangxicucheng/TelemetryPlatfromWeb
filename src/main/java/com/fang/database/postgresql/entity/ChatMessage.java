package com.fang.database.postgresql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cg_chat_message")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Column(name = "room_id")
    private int roomId;

    @Column(name = "user_id")
    private int userId;

    @Transient
    private String userDisplayName;

    @Column(name = "message")
    private String message;

    @Column(name = "chat_time")
    private Date chatTime;

    @Column(name = "is_sys_msg")
    private boolean isSystemMessage;
}
