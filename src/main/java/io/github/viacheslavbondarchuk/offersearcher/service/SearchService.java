package io.github.viacheslavbondarchuk.offersearcher.service;

import io.github.viacheslavbondarchuk.offersearcher.consumer.impl.EventKafkaConsumer;
import io.github.viacheslavbondarchuk.offersearcher.consumer.impl.MarketKafkaConsumer;
import io.github.viacheslavbondarchuk.offersearcher.consumer.impl.SelectionKafkaConsumer;
import io.github.viacheslavbondarchuk.offersearcher.domain.SearchRequest;
import io.github.viacheslavbondarchuk.offersearcher.domain.SearchResponse;
import io.github.viacheslavbondarchuk.offersearcher.domain.SearchResult;
import io.github.viacheslavbondarchuk.offersearcher.exception.SearchServiceNotReadyException;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final EventDocumentStorage eventDocumentStorage;
    private final MarketDocumentStorage marketDocumentStorage;
    private final SelectionDocumentStorage selectionDocumentStorage;

    private final EventKafkaConsumer eventKafkaConsumer;
    private final MarketKafkaConsumer marketKafkaConsumer;
    private final SelectionKafkaConsumer selectionKafkaConsumer;

    private void checkReady() {
        if (!(eventKafkaConsumer.isReady() && marketKafkaConsumer.isReady() && selectionKafkaConsumer.isReady())) {
            throw new SearchServiceNotReadyException("Search service is not ready. For detail info call status endpoint");
        }
    }

    public SearchResponse<List<Document>> search(SearchRequest request) {
        checkReady();
        SearchResult<List<Document>> searchResult = switch (request.getType()) {
            case EVENT -> eventDocumentStorage.find(request);
            case MARKET -> marketDocumentStorage.find(request);
            case SELECTION -> selectionDocumentStorage.find(request);
        };
        return SearchResponse.<List<Document>>builder()
                .data(searchResult.getResult())
                .hits(searchResult.getHits())
                .skip(request.getSkip())
                .limit(request.getLimit())
                .build();
    }

}
