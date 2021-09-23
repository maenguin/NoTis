package com.maenguin.notis.repository;

import com.maenguin.notis.model.account.NotisItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotisItemRepository extends JpaRepository<NotisItem, Long> {
    @Query("select ni from NotisItem ni " +
            " join ni.account a" +
            " where a.seq = :accountSeq")
    List<NotisItem> findByAccountSeq(@Param("accountSeq") Long accountSeq);
}
