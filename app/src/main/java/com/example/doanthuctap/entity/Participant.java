package com.example.doanthuctap.entity;

public class Participant {
    public int participantId;
    public int meetingId;
    public String participantName;
    public String email;
    public int userId;
    public Role role;

    public enum Role {
        MANAGER,
        MEMBER
    }
}
