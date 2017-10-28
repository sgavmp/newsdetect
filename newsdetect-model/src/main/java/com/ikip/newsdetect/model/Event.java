package com.ikip.newsdetect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {
    @Id @GeneratedValue
    private Long id;
    @NotNull
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    @NotNull
    private TypeEventEnum typeEvent;
    @NotNull
    private Long originId;
    private String info;

}
