package ua.denst.music.collection.domain.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
@RequiredArgsConstructor
@NoArgsConstructor
public class MusicCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long collectionId;

    @Column(unique = true)
    @NonNull
    private String collectionName;
}
