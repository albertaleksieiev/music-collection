package ua.denst.music.collection.domain.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "TRACK_CONTENT")
@Data
public class TrackContent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    //    @Column(nullable = false, length = 2147483647)
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] content;
}
