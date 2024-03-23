package com.example.kotrip.repository.city;

import com.example.kotrip.entity.tourlist.City;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CityRepository {
    private final EntityManager em;

    // city 목록들 전부 findAll
    public List<City> findAll() {

        return em.createQuery("select c from City c", City.class)
                .getResultList();
    }
}
