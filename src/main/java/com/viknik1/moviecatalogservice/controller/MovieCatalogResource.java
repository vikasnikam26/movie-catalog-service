package com.viknik1.moviecatalogservice.controller;

import com.viknik1.moviecatalogservice.model.CatalogItem;
import com.viknik1.moviecatalogservice.model.Movie;
import com.viknik1.moviecatalogservice.model.Rating;
import com.viknik1.moviecatalogservice.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/{userId}")
    List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating userRating =restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/"+userId, UserRating.class);

        return userRating.getUserRating().stream().map(rating -> {
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
            return new CatalogItem(movie.getName(), "Desc", rating.getRating());
        }).collect(Collectors.toList());
    }
}
