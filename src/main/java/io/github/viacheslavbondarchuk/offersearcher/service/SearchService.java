package io.github.viacheslavbondarchuk.offersearcher.service;

import io.github.viacheslavbondarchuk.offersearcher.consumer.impl.EventKafkaConsumer;
import io.github.viacheslavbondarchuk.offersearcher.consumer.impl.MarketKafkaConsumer;
import io.github.viacheslavbondarchuk.offersearcher.consumer.impl.SelectionKafkaConsumer;
import io.github.viacheslavbondarchuk.offersearcher.domain.SearchRequest;
import io.github.viacheslavbondarchuk.offersearcher.domain.SearchResponse;
import io.github.viacheslavbondarchuk.offersearcher.domain.SearchResult;
import io.github.viacheslavbondarchuk.offersearcher.domain.SearchServiceStatus;
import io.github.viacheslavbondarchuk.offersearcher.exception.SearchServiceNotReadyException;
import io.github.viacheslavbondarchuk.offersearcher.util.Checking;
import io.github.viacheslavbondarchuk.offersearcher.util.KeyValuePair;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService implements StatusAwareService<SearchServiceStatus> {
    private final EventResettableDocumentStorage eventDocumentStorage;
    private final MarketResettableDocumentStorage marketDocumentStorage;
    private final SelectionResettableDocumentStorage selectionDocumentStorage;

    private final EventKafkaConsumer eventKafkaConsumer;
    private final MarketKafkaConsumer marketKafkaConsumer;
    private final SelectionKafkaConsumer selectionKafkaConsumer;


    private boolean isReady() {
        return eventKafkaConsumer.isReady() && marketKafkaConsumer.isReady() && selectionKafkaConsumer.isReady();
    }

    public SearchResponse<List<Document>> search(SearchRequest request) {
        Checking.check(!isReady(), () -> new SearchServiceNotReadyException("Search service is not ready. For detail info call status endpoint"));
        SearchResult<List<Document>> searchResult = switch (request.getType()) {
            case EVENT -> eventDocumentStorage.find(request);
            case MARKET -> marketDocumentStorage.find(request);
            case SELECTION -> selectionDocumentStorage.find(request);
        };
        return new SearchResponse<>(searchResult.getResult(), searchResult.getHits(), request.getSkip(), request.getLimit());
    }

    @Override
    public KeyValuePair<String, SearchServiceStatus> getStatus() {
        return KeyValuePair.of(getServiceName(), new SearchServiceStatus(isReady()));
    }

    @Override
    public String getServiceName() {
        return "Search service";
    }
}
