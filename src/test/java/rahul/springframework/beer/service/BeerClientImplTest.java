package rahul.springframework.beer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import rahul.springframework.beer.config.WebClientConfig;
import rahul.springframework.beer.domain.Beer;
import rahul.springframework.beer.domain.BeerPagedList;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BeerClientImplTest {

    BeerClientImpl beerClient;

    @BeforeEach
    void setUp() {
        beerClient = new BeerClientImpl(new WebClientConfig().webClient());
    }

    @Test
    void functionalTestGetBeerById() throws InterruptedException {
       // AtomicReference<String> beerName = new AtomicReference<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        beerPagedListMono.map(beerPagedList -> beerPagedList.getContent().get(0).getId())
                .map(id -> beerClient.getBeerById(id, false))
                .flatMap(mono -> mono)
                .subscribe(beer -> {
                    System.out.println("beer name: "+beer.getBeerName());
                   // beerName.set(beer.getBeerName());
                    assertThat(beer.getBeerName()).isEqualTo("Mango Bobs");
                    countDownLatch.countDown();
                });
        countDownLatch.await();

        // Thread.sleep(1000);

    }

    @Test
    void getBeerById() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList beerPagedList = beerPagedListMono.block();
        UUID id = beerPagedList.getContent().get(0).getId();

        Mono<Beer> beerMono = beerClient.getBeerById(id, false);
        Beer beer = beerMono.block();
        assertThat(beer.getId()).isEqualTo(id);
    }

    @Test
    void createBeer() {
        Beer beer = Beer
                    .builder()
                    .beerName("Blue Moon")
                    .beerStyle("WHEAT")
                    .upc("66412389648126")
                    .price(new BigDecimal("10.99"))
                    .build();

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.createBeer(beer);
        ResponseEntity responseEntity = responseEntityMono.block();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void updateBeer() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList beerPagedList = beerPagedListMono.block();
        Beer beer= beerPagedList.getContent().get(0);

        Beer udpatedBeer = Beer.builder()
                            .upc(beer.getUpc())
                            .beerStyle("IPA")
                            .price(beer.getPrice())
                            .beerName(beer.getBeerName())
                            .build();

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.updateBeer(beer.getId(), udpatedBeer);
        ResponseEntity responseEntity = responseEntityMono.block();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteBeerByIdNotFOund() {

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeer(UUID.randomUUID());

        assertThatThrownBy(() -> {
                    ResponseEntity responseEntity = responseEntityMono.block();
                    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                },"RuntimeException", WebClientResponseException.class);
    }

    @Test
    void testDeleteBeerHandException() {

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeer(UUID.randomUUID());

        ResponseEntity<Void> responseEntity = responseEntityMono.onErrorResume(throwable -> {
            if(throwable instanceof WebClientResponseException){
                WebClientResponseException exception = (WebClientResponseException) throwable;
                return Mono.just(ResponseEntity.status(exception.getStatusCode()).build());
            }else{
                throw new RuntimeException(throwable);
            }
        }).block();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteBeer() {

        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList beerPagedList = beerPagedListMono.block();
        Beer beer= beerPagedList.getContent().get(0);

        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeer(beer.getId());
        ResponseEntity responseEntity = responseEntityMono.block();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getBeerByUPC() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList beerPagedList = beerPagedListMono.block();
        String upc = beerPagedList.getContent().get(0).getUpc();

        Mono<Beer> beerMono = beerClient.getBeerByUPC(upc);
        Beer beer = beerMono.block();
        assertThat(beer.getUpc()).isEqualTo(upc);
    }

    @Test
    void listBeers() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);

        BeerPagedList pagedList = beerPagedListMono.block();

        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isGreaterThan(0);
        System.out.println(pagedList.toList());
    }

    @Test
    void listBeersPageSize10() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(1, 10, null, null, null);

        BeerPagedList pagedList = beerPagedListMono.block();

        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isEqualTo(10);
    }

    @Test
    void listBeersNoRecords() {
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(10, 20, null, null, null);

        BeerPagedList pagedList = beerPagedListMono.block();

        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isEqualTo(0);
    }
}