package rahul.springframework.beer.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beer {

    private UUID id;

    @NonNull
    private String beerName;

    @NonNull
    private String beerStyle;

    private String upc;

    private String quantityOnHand;

    private BigDecimal price;

}
