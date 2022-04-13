package rahul.springframework.beer.service;

import org.springframework.http.ResponseEntity;
import rahul.springframework.beer.domain.Beer;
import rahul.springframework.beer.domain.BeerPagedList;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BeerClient {

    Mono<Beer> getBeerById(UUID id);

    Mono<ResponseEntity> createBeer(Beer beer);

    Mono<ResponseEntity> updateBeer(Beer beer);

    Mono<ResponseEntity> deleteBeer(Beer beer);

    Mono<Beer> getBeerByUPC(String upc);

    Mono<BeerPagedList> listBeers(Integer pageNumber, Integer pageSize, String beerName,
                                  String beerStyle, Boolean showInventoryOnhand);
}
