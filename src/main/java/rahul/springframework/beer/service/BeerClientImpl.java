package rahul.springframework.beer.service;

import org.springframework.http.ResponseEntity;
import rahul.springframework.beer.domain.Beer;
import rahul.springframework.beer.domain.BeerPagedList;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class BeerClientImpl implements BeerClient {
    @Override
    public Mono<Beer> getBeerById(UUID id) {
        return null;
    }

    @Override
    public Mono<ResponseEntity> createBeer(Beer beer) {
        return null;
    }

    @Override
    public Mono<ResponseEntity> updateBeer(Beer beer) {
        return null;
    }

    @Override
    public Mono<ResponseEntity> deleteBeer(Beer beer) {
        return null;
    }

    @Override
    public Mono<Beer> getBeerByUPC(String upc) {
        return null;
    }

    @Override
    public Mono<BeerPagedList> listBeers(Integer pageNumber, Integer pageSize, String beerName, String beerStyle, Boolean showInventoryOnhand) {
        return null;
    }
}
