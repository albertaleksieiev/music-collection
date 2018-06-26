package ua.denst.music.collection.domain.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
@RequiredArgsConstructor
@NoArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long genreId;

    @Column(unique = true)
    @NonNull
    private String name;
}
