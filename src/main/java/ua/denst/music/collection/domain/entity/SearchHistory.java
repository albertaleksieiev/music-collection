package ua.denst.music.collection.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ua.denst.music.collection.domain.SearchStatus;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long searchHistoryId;

    @Column
    private String artists;

    @Column
    private String title;

    @Column
    @Enumerated(EnumType.STRING)
    private SearchStatus searchStatus;

    @Column
    private boolean existsInDb = false;

    @OneToOne(fetch = FetchType.LAZY)
    private Track track;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date dateAdded;
}
