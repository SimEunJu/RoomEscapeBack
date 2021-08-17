package com.sej.escape;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sej.escape.entity.QStore;
import com.sej.escape.entity.file.QStoreFile;
import com.sej.escape.repository.store.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositorySoftDeleteTest {

    @Autowired private StoreRepository storeRepository;
    @Autowired private EntityManager em;

    /*
    @Test
    void testJpqlSoftDelete(){
        storeRepository.findByTheme(1);
        storeRepository.findById(1L);
    }
    */
    @Test
    void testSingleTable(){
        JPAQueryFactory query = new JPAQueryFactory(em);
        QStore store = QStore.store;
        QStoreFile storeFile = QStoreFile.storeFile;
        query.select(store)
                .from(store)
                .leftJoin(storeFile)
                .on(storeFile.ftype.eq("S").and(storeFile.referId.eq(store.id)))
                .fetch();




    }
}
