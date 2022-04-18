package rahul.springframework.beer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import rahul.springframework.beer.config.WebClientProperties;
import rahul.springframework.beer.domain.Beer;
import rahul.springframework.beer.domain.BeerPagedList;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeerClientImpl implements BeerClient {

    private final WebClient webClient;

    @Override
    public Mono<Beer> getBeerById(UUID id, Boolean showInventoryOnHand) {
       return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_V1_GET_PATH)
                            .queryParamIfPresent("showInventoryOnHand", Optional.ofNullable(showInventoryOnHand))
                            .build(id)
                )
                .retrieve()
                .bodyToMono(Beer.class);
    }

    @Override
    public Mono<ResponseEntity<Void>> createBeer(Beer beer) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_V1_PATH).build())
                .body(BodyInserters.fromValue(beer))
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public Mono<ResponseEntity<Void>> updateBeer(UUID id, Beer beer) {
        return webClient.put()
                .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_V1_GET_PATH).build(id))
                .body(BodyInserters.fromValue(beer))
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteBeer(UUID id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_V1_PATH)
                        .build(id))
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public Mono<Beer> getBeerByUPC(String upc) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_UPC_PATH)
                                    .build(upc)
                ).retrieve().bodyToMono(Beer.class);
    }

    @Override
    public Mono<BeerPagedList> listBeers(Integer pageNumber, Integer pageSize, String beerName, String beerStyle, Boolean showInventoryOnhand) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(WebClientProperties.BEER_V1_PATH)
                            .queryParamIfPresent("pageNumber", Optional.ofNullable(pageNumber))
                            .queryParamIfPresent("pageSize", Optional.ofNullable(pageSize))
                            .queryParamIfPresent("beerName", Optional.ofNullable(beerName))
                            .queryParamIfPresent("beerStyle", Optional.ofNullable(beerStyle))
                            .queryParamIfPresent("showInventoryOnhand", Optional.ofNullable(showInventoryOnhand))
                            .build()
                )
                .retrieve()
                .bodyToMono(BeerPagedList.class);
    }
}
