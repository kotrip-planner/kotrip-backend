package com.example.kotrip.repository.city;

import com.example.kotrip.entity.tourlist.City;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CityRepository {
    private final EntityManager em;

    // city 목록들 전부 findAll
    public List<City> findAll() {

        return em.createQuery("select c from City c", City.class)
                .getResultList();
    }

    public Optional<City> findById(final int id){
        return Optional.of(em.createQuery("select c from City c where cityId = :id", City.class)
                .setParameter("id",id)
                .getSingleResult());
    }
}
