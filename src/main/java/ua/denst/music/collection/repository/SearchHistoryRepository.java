package ua.denst.music.collection.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.denst.music.collection.domain.SearchStatus;
import ua.denst.music.collection.domain.entity.SearchHistory;

@Repository
public interface SearchHistoryRepository extends CrudRepository<SearchHistory, Long> {
    Iterable<SearchHistory> findBySearchStatus(SearchStatus searchStatus);
}
