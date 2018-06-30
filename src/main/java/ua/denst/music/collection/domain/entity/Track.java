package ua.denst.music.collection.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ua.denst.music.collection.domain.TrackSource;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static ua.denst.music.collection.domain.TrackSource.FILESYSTEM;

@Getter
@Setter
@Entity
@Table(name = "TRACK")
@NoArgsConstructor
@EqualsAndHashCode(of = {"title", "authorsStr"})
public class Track implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long trackId;

    @Column
    private String fileName;

    @Column
    private String title;

    @Column
    private Integer duration;

    @Column
    private Short bpm;

    @Column
    private Short bitRate;

    @Column
    private String key;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date dateAdded;

    @JsonIgnore
    @OneToOne(cascade = ALL, fetch = LAZY)
    private TrackContent content;

    @Column(columnDefinition = "Decimal(10,2)")
    private Double sizeMb;

    @Column
    @Enumerated(EnumType.STRING)
    private TrackSource trackSource = FILESYSTEM;

    @Column
    private String extension;

//    private Album album;

    @Column
    private String authorsStr;

    @Column
    private Short year;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "AUTHOR_TRACK", joinColumns = @JoinColumn(name = "TRACK_ID"), inverseJoinColumns = @JoinColumn(name = "AUTHOR_ID"))
    private Set<Author> authors = new HashSet<>();

    @ManyToMany(cascade = ALL)
    @JoinTable(name = "TRACK_GENRE", joinColumns = @JoinColumn(name = "TRACK_ID"), inverseJoinColumns = @JoinColumn(name = "GENRE_ID"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "TRACK_COLLECTION", joinColumns = @JoinColumn(name = "TRACK_ID"), inverseJoinColumns = @JoinColumn(name = "COLLECTION_ID"))
    @JsonIgnore
    private Set<MusicCollection> collections = new HashSet<>();
}
