package ccc.keewedomain.utils;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DatabaseCleaner {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @PostConstruct
    void init() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .map(e -> e.getJavaType().getAnnotation(Table.class))
                .filter(Objects::nonNull)
                .map(Table::name)
                .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        tableNames.stream()
                .forEach(tableName -> entityManager.createNativeQuery("TRUNCATE TABLE \"" + tableName + "\"").executeUpdate());

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }


}
