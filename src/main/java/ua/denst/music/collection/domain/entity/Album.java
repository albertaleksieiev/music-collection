package ua.denst.music.collection.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
public class Album {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    private Long id;

    @Column
    private String name;

//    private Set<Author> authors = new HashSet<>();

//    private Set<Track> tracks = new HashSet<>();
}
