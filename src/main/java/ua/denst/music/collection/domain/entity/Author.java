package ua.denst.music.collection.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table
@RequiredArgsConstructor
@NoArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column(unique = true)
    @NonNull
    private String name;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "authors")
    @JsonIgnore
    private Set<Track> tracks = new HashSet<>();
}
