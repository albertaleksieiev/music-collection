package ua.denst.music.collection.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
public class TrackListPattern {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long patternId;

    @Column
    private String value;

    @Column
    private String example;
}
